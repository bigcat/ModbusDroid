package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteCoilsRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfBits;
    private byte[] data;

    public WriteCoilsRequest(int slaveId, int startOffset, boolean[] bdata) throws ModbusTransportException {
        super(slaveId);

        ModbusUtils.validateOffset(startOffset);
        ModbusUtils.validateNumberOfBits(bdata.length);
        ModbusUtils.validateEndOffset(startOffset + bdata.length);

        this.startOffset = startOffset;
        numberOfBits = bdata.length;
        data = convertToBytes(bdata);
    }

    WriteCoilsRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfBits);
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        boolean[] bdata = convertToBooleans(data);
        for (int i = 0; i < numberOfBits; i++)
            processImage.writeCoil(startOffset + i, bdata[i]);
        return new WriteCoilsResponse(slaveId, startOffset, numberOfBits);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_COILS;
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteCoilsResponse(slaveId);
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfBits = ModbusUtils.popUnsignedShort(queue);
        data = new byte[ModbusUtils.popUnsignedByte(queue)];
        queue.pop(data);
    }
}
