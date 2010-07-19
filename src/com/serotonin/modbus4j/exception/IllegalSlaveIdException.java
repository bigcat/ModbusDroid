package com.serotonin.modbus4j.exception;

public class IllegalSlaveIdException extends ModbusIdException {
    private static final long serialVersionUID = -1;
    
    public IllegalSlaveIdException(String message) {
        super(message);
    }
}
