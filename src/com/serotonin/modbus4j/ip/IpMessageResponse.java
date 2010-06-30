package com.serotonin.modbus4j.ip;

import com.serotonin.io.messaging.MessageResponse;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.util.queue.ByteQueue;

public class IpMessageResponse extends IpMessage implements MessageResponse {
    static IpMessageResponse createIpMessageResponse(ByteQueue queue) throws ModbusTransportException {
        // Remove the IP header
        int transactionId = ModbusUtils.popShort(queue);
        int protocolId = ModbusUtils.popShort(queue);
        if (protocolId != ModbusUtils.IP_PROTOCOL_ID)
            throw new ModbusTransportException("Unsupported IP protocol id: "+ protocolId);
        ModbusUtils.popShort(queue); // Length, which we don't care about.
        
        // Create the modbus response.
        ModbusResponse response = ModbusResponse.createModbusResponse(queue);
        return new IpMessageResponse(response, transactionId);
    }
    
    public IpMessageResponse(ModbusResponse modbusResponse, int transactionId) {
        super(modbusResponse, transactionId);
    }
    
    public ModbusResponse getModbusResponse() {
        return (ModbusResponse)modbusMessage;
    }
}
