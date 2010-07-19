package com.serotonin.modbus4j.ip;

import com.serotonin.messaging.IncomingRequestMessage;
import com.serotonin.messaging.OutgoingRequestMessage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.util.queue.ByteQueue;

public class IpMessageRequest extends IpMessage implements OutgoingRequestMessage, IncomingRequestMessage {
    static IpMessageRequest createIpMessageRequest(ByteQueue queue) throws ModbusTransportException {
        // Remove the IP header
        int transactionId = ModbusUtils.popShort(queue);
        int protocolId = ModbusUtils.popShort(queue);
        if (protocolId != ModbusUtils.IP_PROTOCOL_ID)
            throw new ModbusTransportException("Unsupported IP protocol id: " + protocolId);
        ModbusUtils.popShort(queue); // Length, which we don't care about.

        // Create the modbus response.
        ModbusRequest request = ModbusRequest.createModbusRequest(queue);
        return new IpMessageRequest(request, transactionId);
    }

    public IpMessageRequest(ModbusRequest modbusRequest, int transactionId) {
        super(modbusRequest, transactionId);
    }

    //@Override
    public boolean expectsResponse() {
        return modbusMessage.getSlaveId() != 0;
    }

    public ModbusRequest getModbusRequest() {
        return (ModbusRequest) modbusMessage;
    }
}
