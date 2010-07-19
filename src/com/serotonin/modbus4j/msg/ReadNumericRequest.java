package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

abstract public class ReadNumericRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfRegisters;
    
    public ReadNumericRequest(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        super(slaveId);
        
        // Do some validation of the data provided.
        ModbusUtils.validateOffset(startOffset);
        ModbusUtils.validateNumberOfRegisters(numberOfRegisters);
        ModbusUtils.validateEndOffset(startOffset + numberOfRegisters);
        
        this.startOffset = startOffset;
        this.numberOfRegisters = numberOfRegisters;
    }
    
    ReadNumericRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfRegisters);
    }

    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfRegisters = ModbusUtils.popUnsignedShort(queue);
    }

    protected byte[] getData(ProcessImage processImage) throws ModbusTransportException {
        short[] data = new short[numberOfRegisters];
        
        // Get the data from the process image.
        for (int i=0; i<numberOfRegisters; i++)
            data[i] = getNumeric(processImage, i + startOffset);

        return convertToBytes(data);
    }
        
    abstract protected short getNumeric(ProcessImage processImage, int index) throws ModbusTransportException;
}
