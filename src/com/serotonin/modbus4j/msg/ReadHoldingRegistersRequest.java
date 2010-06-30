package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadHoldingRegistersRequest extends ReadNumericRequest {
    public ReadHoldingRegistersRequest(int slaveId, int startOffset, int numberOfRegisters)
            throws ModbusTransportException {
        super(slaveId, startOffset, numberOfRegisters);
    }
    
    ReadHoldingRegistersRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_HOLDING_REGISTERS;
    }
    
    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof ReadHoldingRegistersResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadHoldingRegistersResponse(slaveId, getData(processImage));
    }

    @Override
    protected short getNumeric(ProcessImage processImage, int index) throws ModbusTransportException {
        return processImage.getHoldingRegister(index);
    }
    
    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadHoldingRegistersResponse(slaveId);
    }
}
