package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteCoilsResponse extends ModbusResponse {
    private int startOffset;
    private int numberOfBits;

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_COILS;
    }

    WriteCoilsResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    WriteCoilsResponse(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        this.numberOfBits = numberOfBits;
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfBits);
    }

    @Override
    protected void readResponse(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfBits = ModbusUtils.popUnsignedShort(queue);
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getNumberOfBits() {
        return numberOfBits;
    }
}
