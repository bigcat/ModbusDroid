package com.serotonin.modbus4j.ip.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpMessageParser;
import com.serotonin.modbus4j.ip.IpMessageRequest;
import com.serotonin.modbus4j.ip.IpMessageResponse;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.util.queue.ByteQueue;

public class UdpMaster extends ModbusMaster {
    private static final int MESSAGE_LENGTH = 1024;
    
    private short nextTransactionId = 0;
    private final IpParameters ipParameters;

    // Runtime fields.
    private IpMessageParser messageParser;
    private DatagramSocket socket;
//  private Transport transport;
//  private SenderConnection conn;
//
    public UdpMaster(IpParameters params) {
        ipParameters = params;
    }

    protected short getNextTransactionId() {
        return nextTransactionId++;
    }
    
    @Override
    public void init() throws ModbusInitException {
        messageParser = new IpMessageParser();
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(getTimeout());
        }
        catch (SocketException e) {
            throw new ModbusInitException(e);
        }
        initialized = true;
    }
    
    @Override
    public void destroy() {
        socket.close();
    }
    
    @Override
    public ModbusResponse send(ModbusRequest request) throws ModbusTransportException {
        // Wrap the modbus request in an ip request.
        IpMessageRequest ipRequest = new IpMessageRequest(request, getNextTransactionId());
        IpMessageResponse ipResponse;
        
        try {
            int attempts = getRetries() + 1;
            
            while (true) {
                // Send the request.
                sendImpl(ipRequest);
                
                // Recieve the response.
                try {
                    ipResponse = receiveImpl();
                }
                catch (SocketTimeoutException e) {
                    attempts--;
                    if (attempts > 0)
                        // Try again.
                        continue;
                    
                    throw new ModbusTransportException(e);
                }
                
                // We got the response
                break;
            }
            
            return ipResponse.getModbusResponse();
        }
        catch (IOException e) {
            throw new ModbusTransportException(e);
        }
    }
    
    private void sendImpl(IpMessageRequest request) throws IOException {
        byte[] data = request.getMessageData();
        DatagramPacket packet = new DatagramPacket(data, data.length,
                InetAddress.getByName(ipParameters.getHost()), ipParameters.getPort());
        socket.send(packet);
    }
    
    private IpMessageResponse receiveImpl() throws IOException, ModbusTransportException {
        DatagramPacket packet = new DatagramPacket(new byte[MESSAGE_LENGTH], MESSAGE_LENGTH);
        socket.receive(packet);
        
        // We could verify that the packet was received from the same address to which the request was sent,
        // but let's not bother with that yet.
        
        ByteQueue queue = new ByteQueue(packet.getData(), 0, packet.getLength());
        IpMessageResponse response;
        try {
            response = (IpMessageResponse)messageParser.parseResponse(queue);
        }
        catch (Exception e) {
            throw new ModbusTransportException(e);
        }
        
        if (response == null)
            throw new ModbusTransportException("Invalid response received");
        
        return response;
    }
}
