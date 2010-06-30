package com.serotonin.modbus4j;

public interface ProcessImageListener {
    public void coilWrite(int offset, boolean oldValue, boolean newValue);
    public void holdingRegisterWrite(int offset, short oldValue, short newValue);
}
