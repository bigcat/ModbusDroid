package com.serotonin.modbus4j.exception;

public class ModbusTransportException extends Exception {
    private static final long serialVersionUID = -1;

    private final int slaveId;

    public ModbusTransportException() {
        this.slaveId = -1;
    }

    public ModbusTransportException(int slaveId) {
        this.slaveId = slaveId;
    }

    public ModbusTransportException(String message, Throwable cause, int slaveId) {
        super(message, cause);
        this.slaveId = slaveId;
    }

    public ModbusTransportException(String message, int slaveId) {
        super(message);
        this.slaveId = slaveId;
    }

    public ModbusTransportException(String message) {
        super(message);
        this.slaveId = -1;
    }

    public ModbusTransportException(Throwable cause) {
        super(cause);
        this.slaveId = -1;
    }

    public ModbusTransportException(Throwable cause, int slaveId) {
        super(cause);
        this.slaveId = slaveId;
    }

    public int getSlaveId() {
        return slaveId;
    }
}
