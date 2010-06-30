package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class ReadExceptionStatusRequest extends ModbusRequest {
    public ReadExceptionStatusRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    protected void writeRequest(ByteQueue queue) {
    }
    
    @Override
    protected void readRequest(ByteQueue queue) {
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadExceptionStatusResponse(slaveId);
    }

    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof ReadExceptionStatusResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }
    
    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadExceptionStatusResponse(slaveId, processImage.getExceptionStatus());
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_EXCEPTION_STATUS;
    }
}
