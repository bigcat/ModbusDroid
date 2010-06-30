package com.serotonin.modbus4j.base;

import com.serotonin.modbus4j.ExceptionResult;
import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.code.ExceptionCode;

public class KeyedModbusLocator<K> {
    private final K key;
    private final ModbusLocator locator;

    public KeyedModbusLocator(K key, ModbusLocator locator) {
        this.key = key;
        this.locator = locator;
    }
    
    public KeyedModbusLocator(K key, int slaveId, int range, int offset, int dataType) {
        this.key = key;
        locator = new ModbusLocator(slaveId, range, offset, dataType);
    }

    public KeyedModbusLocator(K key, int slaveId, int range, int offset, byte bit) {
        this.key = key;
        locator = new ModbusLocator(slaveId, range, offset, bit);
    }

    public KeyedModbusLocator(K key, SlaveAndRange slaveAndRange, int offset, int dataType) {
        this.key = key;
        locator = new ModbusLocator(slaveAndRange, offset, dataType);
    }

    public K getKey() {
        return key;
    }
    
    public ModbusLocator getLocator() {
        return locator;
    }
    
    
    //
    ///
    /// Delegation.
    ///
    //
    public int getDataType() {
        return locator.getDataType();
    }
    public int getOffset() {
        return locator.getOffset();
    }
    public SlaveAndRange getSlaveAndRange() {
        return locator.getSlaveAndRange();
    }
    public int getEndOffset() {
        return locator.getEndOffset();
    }
    public int getLength() {
        return locator.getLength();
    }
    public byte getBit() {
        return locator.getBit();
    }
    public Object bytesToValue(byte[] data, int requestOffset) {
        try {
            return locator.bytesToValue(data, requestOffset);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // Some equipment will not return data lengths that we expect, which causes AIOOBEs. Catch them and convert
            // them into illegal data address exceptions.
            return new ExceptionResult(ExceptionCode.ILLEGAL_DATA_ADDRESS);
        }
    }
}
