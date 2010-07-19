package com.serotonin.modbus4j.msg;

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
        // no op
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        // no op
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadExceptionStatusResponse(slaveId);
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
