package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadCoilsResponse extends ReadResponse {
    ReadCoilsResponse(int slaveId, byte[] data) throws ModbusTransportException {
        super(slaveId, data);
    }
    
    ReadCoilsResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_COILS;
    }
}
