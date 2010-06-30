package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class ReadExceptionStatusResponse extends ModbusResponse {
    private byte exceptionStatus;
    
    ReadExceptionStatusResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    ReadExceptionStatusResponse(int slaveId, byte exceptionStatus) throws ModbusTransportException {
        super(slaveId);
        this.exceptionStatus = exceptionStatus;
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_EXCEPTION_STATUS;
    }
    
    @Override
    protected void readResponse(ByteQueue queue) {
        exceptionStatus = queue.pop();
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        queue.push(exceptionStatus);
    }

    public byte getExceptionStatus() {
        return exceptionStatus;
    }
}
