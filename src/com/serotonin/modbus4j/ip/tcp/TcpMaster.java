package com.serotonin.modbus4j.ip.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

import com.serotonin.io.messaging.SenderConnection;
import com.serotonin.io.messaging.StreamTransport;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpMessageParser;
import com.serotonin.modbus4j.ip.IpMessageRequest;
import com.serotonin.modbus4j.ip.IpMessageResponse;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;

public class TcpMaster extends ModbusMaster {
    // Configuration fields.
    private short nextTransactionId = 0;
    private final IpParameters ipParameters;
    private boolean keepAlive;
    
    // Runtime fields.
    private IpMessageParser ipMessageParser;
    private Socket socket;
    private StreamTransport transport;
    private SenderConnection conn;
    
    public TcpMaster(IpParameters params, boolean keepAlive) {
        ipParameters = params;
        this.keepAlive = keepAlive;
    }
    
    protected short getNextTransactionId() {
        return nextTransactionId++;
    }

    @Override
    synchronized public void init() throws ModbusInitException {
        ipMessageParser = new IpMessageParser();
        try {
            if (keepAlive)
                openConnection();
        }
        catch (Exception e) {
            throw new ModbusInitException(e);
        }
        initialized = true;
    }

    @Override
    synchronized public void destroy() {
        closeConnection();
        initialized = false;
    }

    @Override
    synchronized public ModbusResponse send(ModbusRequest request) throws ModbusTransportException {
        try {
            // Check if we need to open the connection.
            if (!keepAlive)
                openConnection();
        }
        catch (Exception e) {
        	Log.e(getClass().getSimpleName(), e.getMessage());
            closeConnection();
            throw new ModbusTransportException(e);
        }            
        
        // Wrap the modbus request in a ip request.
        IpMessageRequest ipRequest = new IpMessageRequest(request, getNextTransactionId());
        
        // Send the request to get the response.
        IpMessageResponse ipResponse;
        
        try {
        	Log.i(getClass().getSimpleName(), "Trying to send an ipRequest over open connection");
            ipResponse = (IpMessageResponse)conn.send(ipRequest);
            //Log.i(getClass().getSimpleName(), "Got response!");
            return ipResponse.getModbusResponse();
        }
        catch (Exception e) {
        	Log.e(getClass().getSimpleName(), e.getMessage());
        	if (keepAlive) {
                // The connection may have been reset, so try to reopen it and attempt the message again.
            	try {
                    System.out.println("Modbus4J: Keep-alive connection may have been reset. Attempting to re-open.");
                    Log.i(getClass().getSimpleName(), "Keep-alive connection may have been reset. Trying to re-open it.");
                    openConnection();
                    Log.i(getClass().getSimpleName(), "Trying to send an ipRequest over open connection");
                    ipResponse = (IpMessageResponse)conn.send(ipRequest);
                    Log.i(getClass().getSimpleName(), "Got response: " + ipResponse.getMessageData().toString());
                    return ipResponse.getModbusResponse();
                }
                catch (Exception e2) {
                	Log.e(getClass().getSimpleName(), e2.getMessage());
                    throw new ModbusTransportException(e2);
                }
            }
        	Log.e(getClass().getSimpleName(), "Throwing a new exception upwards");
            throw new ModbusTransportException(e);
        }
        finally {
            // Check if we should close the connection.
            if (!keepAlive) {
                closeConnection();
                Log.i(getClass().getSimpleName(), "Closing connection because keepalive is false" );
            }
        }
    }
    
    
    //
    ///
    /// Private methods
    ///
    //
    private void openConnection() throws IOException {
        // Make sure any existing connection is closed.
        closeConnection();
        
        // Try 'retries' times to get the socket open.
        int retries = getRetries();
        while (true) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ipParameters.getHost(), ipParameters.getPort()), getTimeout());
                transport = new StreamTransport(socket.getInputStream(), socket.getOutputStream(),
                        "Modbus4J TcpMaster");
                break;
            }
            catch (IOException e) {
                closeConnection();
                Log.e(getClass().getSimpleName(), e.getMessage() );
                if (retries <= 0)
                    throw e;
                System.out.println("Modbus4J: Open connection failed, trying again.");
                retries--;
            }
        }
        
        conn = getSenderConnection();
        //conn.DEBUG = true;
        conn.start(transport, ipMessageParser);
    }
    
    private void closeConnection() {
        closeSenderConnection(conn);
        try {
            if (socket != null)
                socket.close();
        }
        catch (IOException e) {
            getExceptionListener().receivedException(e);
        }
        
        conn = null;
        socket = null;
    }
}
