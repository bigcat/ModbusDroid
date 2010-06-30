/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.code;

/**
 * @author Matthew Lohbihler
 */
public class ExceptionCode {
    public static final byte ILLEGAL_FUNCTION                        = 0x1;
    public static final byte ILLEGAL_DATA_ADDRESS                    = 0x2;
//    public static final byte ILLEGAL_DATA_VALUE                      = 0x3;
    public static final byte SLAVE_DEVICE_FAILURE                    = 0x4;
//    public static final byte ACKNOWLEDGE                             = 0x5;
//    public static final byte SLAVE_DEVICE_BUSY                       = 0x6;
//    public static final byte MEMORY_PARITY_ERROR                     = 0x8;
//    public static final byte GATEWAY_PATH_UNAVAILABLE                = 0xa;
//    public static final byte GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND = 0xb;
    
    public static String getExceptionMessage(byte id) {
        switch (id) {
        case ILLEGAL_FUNCTION :
            return "Illegal function";
        case ILLEGAL_DATA_ADDRESS :
            return "Illegal data address";
//        case ILLEGAL_DATA_VALUE :
//            return "Illegal data value";
        case SLAVE_DEVICE_FAILURE :
            return "Slave device failure";
//        case ACKNOWLEDGE :
//            return "Acknowledge";
//        case SLAVE_DEVICE_BUSY :
//            return "Slave device busy";
//        case MEMORY_PARITY_ERROR :
//            return "Memory parity error";
//        case GATEWAY_PATH_UNAVAILABLE :
//            return "Gateway path unavailable";
//        case GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND :
//            return "Gateway target device failed to respond";
        }
        return null;
    }
}
