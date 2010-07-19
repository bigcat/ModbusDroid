/*
 * Created on 28-Sep-2006
 */
package com.serotonin.modbus4j;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.serotonin.modbus4j.exception.ModbusInitException;

abstract public class ModbusSlaveSet extends Modbus {
    protected LinkedHashMap<Integer, ProcessImage> processImages = new LinkedHashMap<Integer, ProcessImage>();

    public void addProcessImage(ProcessImage processImage) {
        processImages.put(processImage.getSlaveId(), processImage);
    }

    public ProcessImage getProcessImage(int slaveId) {
        return processImages.get(slaveId);
    }

    public Collection<ProcessImage> getProcessImages() {
        return processImages.values();
    }

    /**
     * Starts the slave. If an exception is not thrown, this method does not return, but uses the thread to execute the
     * listening.
     * 
     * @throws ModbusInitException
     */
    abstract public void start() throws ModbusInitException;

    abstract public void stop();
}
