package com.serotonin.modbus4j.enums;

/**
 * @deprecated use com.serotonin.modbus4j.code.ExceptionCode instead
 * @author Matthew Lohbihler
 */
public enum ExceptionCode {
    ILLEGAL_FUNCTION                        (0x1, "Illegal function"),
    ILLEGAL_DATA_ADDRESS                    (0x2, "Illegal data address"),
//    ILLEGAL_DATA_VALUE                      (0x3, "Illegal data value"),
    SLAVE_DEVICE_FAILURE                    (0x4, "Slave device failure");
//    ACKNOWLEDGE                             (0x5, "Acknowledge"),
//    SLAVE_DEVICE_BUSY                       (0x6, "Slave device busy"),
//    MEMORY_PARITY_ERROR                     (0x8, "Memory parity error"),
//    GATEWAY_PATH_UNAVAILABLE                (0xa, "Gateway path unavailable"),
//    GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND (0xb, "Gateway target device failed to respond"),
    
    private byte exceptionCode;
    private String exceptionMessage;
    
    ExceptionCode(int exceptionCode, String exceptionMessage) {
        this.exceptionCode = (byte)exceptionCode;
        this.exceptionMessage = exceptionMessage;
    }

    public byte getExceptionCode() {
        return exceptionCode;
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }
    
    public static ExceptionCode valueOf(byte code) {
        if (code == ILLEGAL_FUNCTION.exceptionCode)
            return ILLEGAL_FUNCTION;
        if (code == ILLEGAL_DATA_ADDRESS.exceptionCode)
            return ILLEGAL_DATA_ADDRESS;
//        if (code == ILLEGAL_DATA_VALUE.exceptionCode)
//            return ILLEGAL_DATA_VALUE;
        if (code == SLAVE_DEVICE_FAILURE.exceptionCode)
            return SLAVE_DEVICE_FAILURE;
//        if (code == ACKNOWLEDGE.exceptionCode)
//            return ACKNOWLEDGE;
//        if (code == SLAVE_DEVICE_BUSY.exceptionCode)
//            return SLAVE_DEVICE_BUSY;
//        if (code == MEMORY_PARITY_ERROR.exceptionCode)
//            return MEMORY_PARITY_ERROR;
//        if (code == GATEWAY_PATH_UNAVAILABLE.exceptionCode)
//            return GATEWAY_PATH_UNAVAILABLE;
//        if (code == GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND.exceptionCode)
//            return GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND;
            
        throw new IllegalArgumentException("Unknown code: "+ code);
    }
}
