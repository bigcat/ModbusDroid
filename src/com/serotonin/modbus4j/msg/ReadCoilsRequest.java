package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadCoilsRequest extends ReadBinaryRequest {
    public ReadCoilsRequest(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId, startOffset, numberOfBits);
    }
    
    ReadCoilsRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_COILS;
    }
    
    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof ReadCoilsResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadCoilsResponse(slaveId, getData(processImage));
    }

    @Override
    protected boolean getBinary(ProcessImage processImage, int index) throws ModbusTransportException {
        return processImage.getCoil(index);
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadCoilsResponse(slaveId);
    }
}
