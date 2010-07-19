package com.serotonin.modbus4j.exception;

public class InvalidDataConversionException extends RuntimeException {
    private static final long serialVersionUID = -1;

    public InvalidDataConversionException(String message) {
        super(message);
    }
}
