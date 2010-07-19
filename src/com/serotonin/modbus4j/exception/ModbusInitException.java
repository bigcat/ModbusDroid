package com.serotonin.modbus4j.exception;

public class ModbusInitException extends Exception {
    private static final long serialVersionUID = -1;
    
    public ModbusInitException() {
        super();
    }

    public ModbusInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModbusInitException(String message) {
        super(message);
    }

    public ModbusInitException(Throwable cause) {
        super(cause);
    }
}
