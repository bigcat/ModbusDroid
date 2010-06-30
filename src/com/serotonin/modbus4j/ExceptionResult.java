/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j;

import com.serotonin.modbus4j.code.ExceptionCode;

/**
 * @author Matthew Lohbihler
 */
public class ExceptionResult {
    private final byte exceptionCode;
    private final String exceptionMessage;
    
    public ExceptionResult(byte exceptionCode) {
        this.exceptionCode = exceptionCode;
        exceptionMessage = ExceptionCode.getExceptionMessage(exceptionCode);
    }

    public byte getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
