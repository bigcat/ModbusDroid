package com.serotonin.modbus4j.exception;

public class IllegalFunctionException extends ModbusTransportException {
    private static final long serialVersionUID = -1;

    private final byte functionCode;

    public IllegalFunctionException(byte functionCode, int slaveId) {
        super("Function code: 0x" + Integer.toHexString(functionCode & 0xff), slaveId);
        this.functionCode = functionCode;
    }

    public byte getFunctionCode() {
        return functionCode;
    }
}
