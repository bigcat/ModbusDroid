package com.serotonin.modbus4j.ip;

import com.serotonin.io.messaging.MessageRequest;
import com.serotonin.io.messaging.MessageResponse;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.BaseRequestHandler;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;

public class IpRequestHandler extends BaseRequestHandler {
    public IpRequestHandler(int slaveId, ProcessImage processImage) {
        super(slaveId, processImage);
    }
    
    public MessageResponse handleRequest(MessageRequest req) throws Exception {
        IpMessageRequest tcpRequest = (IpMessageRequest)req;
        ModbusRequest request = tcpRequest.getModbusRequest();
        
        if (!checkSlaveId(request))
            return null;
            
        ModbusResponse response = request.handle(processImage);
        return new IpMessageResponse(response, tcpRequest.transactionId);
    }
}
