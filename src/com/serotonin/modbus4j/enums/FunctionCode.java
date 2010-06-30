package com.serotonin.modbus4j.enums;

/**
 * @deprecated use com.serotonin.modbus4j.code.FunctionCode instead
 * @author Matthew Lohbihler
 */
public enum FunctionCode {
    READ_COILS             (1),
    READ_DISCRETE_INPUTS   (2),
    READ_HOLDING_REGISTERS (3),
    READ_INPUT_REGISTERS   (4),
    WRITE_COIL             (5),
    WRITE_REGISTER         (6),
    READ_EXCEPTION_STATUS  (7),
    WRITE_COILS            (15),
    WRITE_REGISTERS        (16),
    REPORT_SLAVE_ID        (17),
    WRITE_MASK_REGISTER    (22);
    
    private byte code;
    
    FunctionCode(int code) {
        this.code = (byte)code;
    }
    
    public byte getCode() {
        return code;
    }
    
    public static FunctionCode valueOf(byte code) {
        if (code == READ_COILS.code)
            return READ_COILS;
        if (code == READ_DISCRETE_INPUTS.code)
            return READ_DISCRETE_INPUTS;
        if (code == READ_HOLDING_REGISTERS.code)
            return READ_HOLDING_REGISTERS;
        if (code == READ_INPUT_REGISTERS.code)
            return READ_INPUT_REGISTERS;
        if (code == WRITE_COIL.code)
            return WRITE_COIL;
        if (code == WRITE_REGISTER.code)
            return WRITE_REGISTER;
        if (code == READ_EXCEPTION_STATUS.code)
            return READ_EXCEPTION_STATUS;
        if (code == WRITE_COILS.code)
            return WRITE_COILS;
        if (code == WRITE_REGISTERS.code)
            return WRITE_REGISTERS;
        if (code == REPORT_SLAVE_ID.code)
            return REPORT_SLAVE_ID;
        if (code == WRITE_MASK_REGISTER.code)
            return WRITE_MASK_REGISTER;
        throw new IllegalArgumentException("Unknown code: "+ (code & 0xff));
    }
    
    @Override
    public String toString() {
        return Integer.toString(code & 0xff);
    }
}
