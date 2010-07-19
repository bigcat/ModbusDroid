package com.serotonin.modbus4j.base;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.modbus4j.code.RegisterRange;

public class ReadFunctionGroup<K> {
    private final SlaveAndRange slaveAndRange;
    private final int functionCode;
    private final List<KeyedModbusLocator<K>> locators = new ArrayList<KeyedModbusLocator<K>>();
    private int startOffset = 65536;
    private int length = 0;
    
    public ReadFunctionGroup(KeyedModbusLocator<K> locator) {
        slaveAndRange = locator.getSlaveAndRange();
        functionCode = RegisterRange.getReadFunctionCode(slaveAndRange.getRange());
        add(locator);
    }
    
    public void add(KeyedModbusLocator<K> locator) {
        if (startOffset > locator.getOffset())
            startOffset = locator.getOffset();
        if (length < locator.getEndOffset() - startOffset + 1)
            length = locator.getEndOffset() - startOffset + 1;
        locators.add(locator);
    }

    public int getStartOffset() {
        return startOffset;
    }
    
    public int getEndOffset() {
        return startOffset + length - 1;
    }

    public SlaveAndRange getSlaveAndRange() {
        return slaveAndRange;
    }

    public int getLength() {
        return length;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public List<KeyedModbusLocator<K>> getLocators() {
        return locators;
    }
}
