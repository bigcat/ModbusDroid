package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteRegisterResponse extends ModbusResponse {
    private int writeOffset;
    private int writeValue;

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_REGISTER;
    }

    WriteRegisterResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    WriteRegisterResponse(int slaveId, int writeOffset, int writeValue) throws ModbusTransportException {
        super(slaveId);
        this.writeOffset = writeOffset;
        this.writeValue = writeValue;
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushShort(queue, writeOffset);
        ModbusUtils.pushShort(queue, writeValue);
    }

    @Override
    protected void readResponse(ByteQueue queue) {
        writeOffset = ModbusUtils.popUnsignedShort(queue);
        writeValue = ModbusUtils.popUnsignedShort(queue);
    }

    public int getWriteOffset() {
        return writeOffset;
    }

    public int getWriteValue() {
        return writeValue;
    }
}
