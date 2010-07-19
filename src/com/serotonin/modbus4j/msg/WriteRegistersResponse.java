package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteRegistersResponse extends ModbusResponse {
    private int startOffset;
    private int numberOfRegisters;

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_REGISTERS;
    }

    WriteRegistersResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    WriteRegistersResponse(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        this.numberOfRegisters = numberOfRegisters;
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfRegisters);
    }

    @Override
    protected void readResponse(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfRegisters = ModbusUtils.popUnsignedShort(queue);
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getNumberOfRegisters() {
        return numberOfRegisters;
    }
}
