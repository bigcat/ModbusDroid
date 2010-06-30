package com.serotonin.modbus4j.ip;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.io.messaging.MessageRequest;
import com.serotonin.io.messaging.MessageResponse;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.util.queue.ByteQueue;

public class IpMessageRequest extends IpMessage implements MessageRequest {
    static IpMessageRequest createIpMessageRequest(ByteQueue queue) throws ModbusTransportException {
        // Remove the IP header
        int transactionId = ModbusUtils.popShort(queue);
        int protocolId = ModbusUtils.popShort(queue);
        if (protocolId != ModbusUtils.IP_PROTOCOL_ID)
            throw new ModbusTransportException("Unsupported IP protocol id: "+ protocolId);
        ModbusUtils.popShort(queue); // Length, which we don't care about.
        
        // Create the modbus response.
        ModbusRequest request = ModbusRequest.createModbusRequest(queue);
        return new IpMessageRequest(request, transactionId);
    }
    
    public IpMessageRequest(ModbusRequest modbusRequest, int transactionId) {
        super(modbusRequest, transactionId);
    }
    
    public void isValidResponse(MessageResponse res) throws MessageMismatchException {
        if (!(res instanceof IpMessageResponse))
            throw new MessageMismatchException("Response is not an IpMessageResponse: "+ res.getClass());
        
        IpMessageResponse response = (IpMessageResponse)res;
        if (transactionId != response.transactionId)
            throw new MessageMismatchException("Response transaction id does not match: expected="+ transactionId +
                    ", received="+ response.transactionId);
        
        getModbusRequest().matches(response.getModbusResponse());
    }
    
    public ModbusRequest getModbusRequest() {
        return (ModbusRequest)modbusMessage;
    }
}
