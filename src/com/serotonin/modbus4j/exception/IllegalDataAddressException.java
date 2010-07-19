package com.serotonin.modbus4j.exception;

public class IllegalDataAddressException extends ModbusTransportException {
    private static final long serialVersionUID = -1;

    public IllegalDataAddressException() {
        super();
    }

    public IllegalDataAddressException(int slaveId) {
        super(slaveId);
    }
}
