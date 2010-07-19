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

import com.serotonin.messaging.MessageControl;
import com.serotonin.messaging.TestableTransport;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpMessageParser;
import com.serotonin.modbus4j.ip.IpRequestHandler;

public class TcpSlave extends ModbusSlaveSet {
    // Configuration fields
    private int port = ModbusUtils.TCP_PORT;

    // Runtime fields.
    private ServerSocket serverSocket;
    final ExecutorService executorService;

    public TcpSlave() {
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
            getExceptionHandler().receivedException(e);
        }

        // Now close the executor service.
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            getExceptionHandler().receivedException(e);
        }
    }

    class TcpConnectionHandler implements Runnable {
        private final Socket socket;
        private TestableTransport transport;
        private MessageControl conn;

        TcpConnectionHandler(Socket socket) throws ModbusInitException {
            this.socket = socket;
            try {
                transport = new TestableTransport(socket.getInputStream(), socket.getOutputStream());
            }
            catch (IOException e) {
                throw new ModbusInitException(e);
            }
        }

        public void run() {
            IpMessageParser ipMessageParser = new IpMessageParser(false);
            IpRequestHandler ipRequestHandler = new IpRequestHandler(TcpSlave.this);

            conn = new MessageControl();
            conn.setExceptionHandler(getExceptionHandler());

            try {
                conn.start(transport, ipMessageParser, ipRequestHandler);
                executorService.execute(transport);
            }
            catch (IOException e) {
                getExceptionHandler().receivedException(new ModbusInitException(e));
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
                catch (InterruptedException e) {
                    // no op
                }
            }

            conn.close();
            try {
                socket.close();
            }
            catch (IOException e) {
                getExceptionHandler().receivedException(new ModbusInitException(e));
            }
        }
    }
}
