package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class WriteRegistersRequest extends ModbusRequest {
    private int startOffset;
    private byte[] data;
    
    public WriteRegistersRequest(int slaveId, int startOffset, short[] sdata) throws ModbusTransportException {
        super(slaveId);
        
        ModbusUtils.validateOffset(startOffset);
        if (sdata.length < 1 || sdata.length > ModbusUtils.MAX_WRITE_REGISTER_COUNT)
            throw new ModbusTransportException("Invalid number of registers: "+ sdata.length);
        ModbusUtils.validateEndOffset(startOffset + sdata.length);
        
        this.startOffset = startOffset;
        data = convertToBytes(sdata);
    }
  
    WriteRegistersRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, data.length / 2);
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }
    
    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof WriteRegistersResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }
    
    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        short[] sdata = convertToShorts(data);
        for (int i=0; i<sdata.length; i++)
            processImage.writeHoldingRegister(startOffset + i, sdata[i]);
        return new WriteRegistersResponse(slaveId, startOffset, sdata.length);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_REGISTERS;
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteRegistersResponse(slaveId);
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        ModbusUtils.popUnsignedShort(queue); // register cound not needed.
        data = new byte[ModbusUtils.popByte(queue)];
        queue.pop(data);
    }
}
