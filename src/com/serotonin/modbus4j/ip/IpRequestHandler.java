package com.serotonin.modbus4j.ip;

import com.serotonin.messaging.IncomingRequestMessage;
import com.serotonin.messaging.OutgoingResponseMessage;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.base.BaseRequestHandler;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;

public class IpRequestHandler extends BaseRequestHandler {
    public IpRequestHandler(ModbusSlaveSet slave) {
        super(slave);
    }

    public OutgoingResponseMessage handleRequest(IncomingRequestMessage req) throws Exception {
        IpMessageRequest tcpRequest = (IpMessageRequest) req;
        ModbusRequest request = tcpRequest.getModbusRequest();
        ModbusResponse response = handleRequestImpl(request);
        if (response == null)
            return null;
        return new IpMessageResponse(response, tcpRequest.transactionId);
    }
}
