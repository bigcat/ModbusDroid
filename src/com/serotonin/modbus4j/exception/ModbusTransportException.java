package com.serotonin.modbus4j.exception;

public class ModbusTransportException extends Exception {
    private static final long serialVersionUID = -1;

    public ModbusTransportException() {
        super();
    }

    public ModbusTransportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModbusTransportException(String message) {
        super(message);
    }

    public ModbusTransportException(Throwable cause) {
        super(cause);
    }
}
