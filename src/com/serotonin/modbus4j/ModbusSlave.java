/*
 * Created on 28-Sep-2006
 */
package com.serotonin.modbus4j;

import com.serotonin.modbus4j.exception.ModbusInitException;

abstract public class ModbusSlave extends Modbus {
    protected ProcessImage processImage;
    protected int slaveId;
    
    public ModbusSlave(ProcessImage processImage, int slaveId) {
        this.processImage = processImage;
        this.slaveId = slaveId;
    }
    
    /**
     * Starts the slave. If an exception is not thrown, this method does not return, but uses the thread
     * to execute the listening.
     * @throws ModbusInitException
     */
    abstract public void start() throws ModbusInitException;
    abstract public void stop();
}
