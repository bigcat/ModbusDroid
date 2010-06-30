package com.serotonin.modbus4j.enums;

import com.serotonin.modbus4j.base.ModbusUtils;

/**
 * @deprecated use com.serotonin.modbus4j.code.RegisterRange instead
 * @author Matthew Lohbihler
 */
public enum RegisterRange {
    COIL_STATUS      (0, 0xffff, FunctionCode.READ_COILS, ModbusUtils.MAX_READ_BIT_COUNT),
    INPUT_STATUS     (0x10000, 0x1ffff, FunctionCode.READ_DISCRETE_INPUTS, ModbusUtils.MAX_READ_BIT_COUNT),
    HOLDING_REGISTER (0x40000, 0x4ffff, FunctionCode.READ_HOLDING_REGISTERS, ModbusUtils.MAX_READ_REGISTER_COUNT),
    INPUT_REGISTER   (0x30000, 0x3ffff, FunctionCode.READ_INPUT_REGISTERS, ModbusUtils.MAX_READ_REGISTER_COUNT);
    
    private int from;
    private int to;
    private FunctionCode readFunctionCode;
    private int maxReadCount;
    
    RegisterRange(int from, int to, FunctionCode readFunctionCode, int maxReadCount) {
        this.from = from;
        this.to = to;
        this.readFunctionCode = readFunctionCode;
        this.maxReadCount = maxReadCount;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
    
    public FunctionCode getReadFunctionCode() {
        return readFunctionCode;
    }

    public int getMaxReadCount() {
        return maxReadCount;
    }
}
