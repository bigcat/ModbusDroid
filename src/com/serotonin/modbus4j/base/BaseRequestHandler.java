package com.serotonin.modbus4j.base;

import com.serotonin.io.messaging.RequestHandler;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;

abstract public class BaseRequestHandler implements RequestHandler {
    protected int slaveId;
    protected ProcessImage processImage;

    public BaseRequestHandler(int slaveId, ProcessImage processImage) {
        this.slaveId = slaveId;
        this.processImage = processImage;
    }
    
    protected boolean checkSlaveId(ModbusRequest request) throws ModbusTransportException {
        // Check the slave id.
        if (request.getSlaveId() == 0) {
            request.handle(processImage);
            return false;
        }
        
        if (request.getSlaveId() != slaveId)
            return false;
        
        return true;
    }
}
