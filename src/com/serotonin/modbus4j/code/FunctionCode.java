/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.code;

/**
 * @author Matthew Lohbihler
 */
public class FunctionCode {
    public static final byte READ_COILS             = 1;
    public static final byte READ_DISCRETE_INPUTS   = 2;
    public static final byte READ_HOLDING_REGISTERS = 3;
    public static final byte READ_INPUT_REGISTERS   = 4;
    public static final byte WRITE_COIL             = 5;
    public static final byte WRITE_REGISTER         = 6;
    public static final byte READ_EXCEPTION_STATUS  = 7;
    public static final byte WRITE_COILS            = 15;
    public static final byte WRITE_REGISTERS        = 16;
    public static final byte REPORT_SLAVE_ID        = 17;
    public static final byte WRITE_MASK_REGISTER    = 22;
    
    public static String toString(byte code) {
        return Integer.toString(code & 0xff);
    }
}
