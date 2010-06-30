package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteRegisterRequest extends ModbusRequest {
    private int writeOffset;
    private int writeValue;
    
    public WriteRegisterRequest(int slaveId, int writeOffset, int writeValue) throws ModbusTransportException {
        super(slaveId);
        
        // Do some validation of the data provided.
        ModbusUtils.validateOffset(writeOffset);
            
        this.writeOffset = writeOffset;
        this.writeValue = writeValue;
    }
    
    WriteRegisterRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, writeOffset);
        ModbusUtils.pushShort(queue, writeValue);
    }
    
    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof WriteRegisterResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }
    
    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        processImage.writeHoldingRegister(writeOffset, (short)writeValue);
        return new WriteRegisterResponse(slaveId, writeOffset, writeValue);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_REGISTER;
    }
    
    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteRegisterResponse(slaveId);
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        writeOffset = ModbusUtils.popUnsignedShort(queue);
        writeValue = ModbusUtils.popUnsignedShort(queue);
    }
}
