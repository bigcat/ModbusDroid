/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.msg;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class ExceptionRequest extends ModbusRequest {
    private final byte functionCode;
    private final byte exceptionCode;
    
    public ExceptionRequest(int slaveId, byte functionCode, byte exceptionCode) throws ModbusTransportException {
        super(slaveId);
        this.functionCode = functionCode;
        this.exceptionCode = exceptionCode;
    }

    @Override
    protected void writeRequest(ByteQueue queue) {
        throw new ShouldNeverHappenException("wha");
    }

    @Override
    protected void readRequest(ByteQueue queue) {
        queue.clear();
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ExceptionResponse(slaveId, functionCode, exceptionCode);
    }

    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof ExceptionResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return getResponseInstance(slaveId);
    }

    @Override
    public byte getFunctionCode() {
        return functionCode;
    }

    public byte getExceptionCode() {
        return exceptionCode;
    }
}
