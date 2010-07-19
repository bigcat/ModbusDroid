/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class ExceptionResponse extends ModbusResponse {
    private final byte functionCode;

    public ExceptionResponse(int slaveId, byte functionCode, byte exceptionCode) throws ModbusTransportException {
        super(slaveId);
        this.functionCode = functionCode;
        setException(exceptionCode);
    }

    @Override
    public byte getFunctionCode() {
        return functionCode;
    }

    @Override
    protected void readResponse(ByteQueue queue) {
        // no op
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        // no op
    }
}
