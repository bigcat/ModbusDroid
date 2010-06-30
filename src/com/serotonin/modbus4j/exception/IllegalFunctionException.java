package com.serotonin.modbus4j.exception;

public class IllegalFunctionException extends ModbusTransportException {
    private static final long serialVersionUID = -1;
    
    private byte functionCode;
    
    public IllegalFunctionException(byte functionCode) {
        this.functionCode = functionCode;
    }

    public byte getFunctionCode() {
        return functionCode;
    }
}
