package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

abstract public class ReadResponse extends ModbusResponse {
    private byte[] data;
    
    ReadResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    ReadResponse(int slaveId, byte[] data) throws ModbusTransportException {
        super(slaveId);
        this.data = data;
    }
    
    protected void readResponse(ByteQueue queue) {
        int numberOfBytes = ModbusUtils.popUnsignedByte(queue);
        if (queue.size() < numberOfBytes)
            throw new ArrayIndexOutOfBoundsException();
        
        data = new byte[numberOfBytes];
        queue.pop(data);
    }
    
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }
    
    public byte[] getData() {
        return data;
    }
    
    public short[] getShortData() {
        return convertToShorts(data);
    }
    
    public boolean[] getBooleanData() {
        return convertToBooleans(data);
    }
}
