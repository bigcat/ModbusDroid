package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

abstract public class ReadBinaryRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfBits;
    
    public ReadBinaryRequest(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId);
        
        // Do some validation of the data provided.
        ModbusUtils.validateOffset(startOffset);
        ModbusUtils.validateNumberOfBits(numberOfBits);
        ModbusUtils.validateEndOffset(startOffset + numberOfBits);
            
        this.startOffset = startOffset;
        this.numberOfBits = numberOfBits;
    }
    
    ReadBinaryRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfBits);
    }
    
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfBits = ModbusUtils.popUnsignedShort(queue);
    }

    protected byte[] getData(ProcessImage processImage) throws ModbusTransportException {
        boolean[] data = new boolean[numberOfBits];
        
        // Get the data from the process image.
        for (int i=0; i<numberOfBits; i++)
            data[i] = getBinary(processImage, i + startOffset);

        // Convert the boolean array into an array of bytes.
        return convertToBytes(data);
    }
    
    abstract protected boolean getBinary(ProcessImage processImage, int index) throws ModbusTransportException;
}
