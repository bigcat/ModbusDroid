package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteCoilRequest extends ModbusRequest {
    private int writeOffset;
    private boolean writeValue;

    public WriteCoilRequest(int slaveId, int writeOffset, boolean writeValue) throws ModbusTransportException {
        super(slaveId);

        // Do some validation of the data provided.
        ModbusUtils.validateOffset(writeOffset);

        this.writeOffset = writeOffset;
        this.writeValue = writeValue;
    }

    WriteCoilRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, writeOffset);
        ModbusUtils.pushShort(queue, writeValue ? 0xff00 : 0);
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        processImage.writeCoil(writeOffset, writeValue);
        return new WriteCoilResponse(slaveId, writeOffset, writeValue);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_COIL;
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteCoilResponse(slaveId);
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        writeOffset = ModbusUtils.popUnsignedShort(queue);
        writeValue = ModbusUtils.popUnsignedShort(queue) == 0xff00;
    }
}
