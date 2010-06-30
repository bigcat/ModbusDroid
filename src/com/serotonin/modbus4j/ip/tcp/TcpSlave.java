/*
 * Created on 28-Sep-2006
 */
package com.serotonin.modbus4j.ip.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.serotonin.io.messaging.ListenerConnection;
import com.serotonin.io.messaging.TestableTransport;
import com.serotonin.modbus4j.ModbusSlave;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpMessageParser;
import com.serotonin.modbus4j.ip.IpRequestHandler;

public class TcpSlave extends ModbusSlave {
    // Configuration fields
    private int port = ModbusUtils.TCP_PORT;
    
    // Runtime fields.
    private ServerSocket serverSocket;
    private final ExecutorService executorService;
    
    public TcpSlave(ProcessImage processImage, int slaveId) {
        super(processImage, slaveId);
        executorService = Executors.newCachedThreadPool();
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() throws ModbusInitException {
        try {
            serverSocket = new ServerSocket(port);
            
            Socket socket; 
            while (true) {
                socket = serverSocket.accept();
                TcpConnectionHandler handler = new TcpConnectionHandler(socket);
                executorService.execute(handler);
            }
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    @Override
    public void stop() {
        // Close the socket first to prevent new messages.
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            getExceptionListener().receivedException(e);
        }
        
        // Now close the executor service.
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            getExceptionListener().receivedException(e);
        }
    }
    
    class TcpConnectionHandler implements Runnable {
        private final Socket socket;
        private TestableTransport transport;
        private ListenerConnection conn;
        
        TcpConnectionHandler(Socket socket) throws ModbusInitException {
            this.socket = socket;
            try {
                transport = new TestableTransport(socket.getInputStream(), socket.getOutputStream(),
                        "Modbus4J TcpSlave");
            }
            catch (IOException e) {
                throw new ModbusInitException(e);
            }
        }
        
        public void run() {
            IpMessageParser ipMessageParser = new IpMessageParser();
            IpRequestHandler ipRequestHandler = new IpRequestHandler(slaveId, processImage);
            
            conn = new ListenerConnection(ipRequestHandler);
            conn.addListener(getExceptionListener());
            
            try {
                conn.start(transport, ipMessageParser);
            }
            catch (IOException e) {
                getExceptionListener().receivedException(new ModbusInitException(e));
            }
            
            // Monitor the socket to detect when it gets closed.
            while (true) {
                try {
                    transport.testInputStream();
                }
                catch (IOException e) {
                    break;
                }
                
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {}
            }
            
            conn.close();
            try {
                socket.close();
            }
            catch (IOException e) {
                getExceptionListener().receivedException(new ModbusInitException(e));
            }
        }
    }
}
