package com.serotonin.modbus4j.exception;

public class IllegalDataTypeException extends ModbusIdException {
    private static final long serialVersionUID = -1;

    public IllegalDataTypeException(String message) {
        super(message);
    }
}
