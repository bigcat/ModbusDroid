package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadDiscreteInputsRequest extends ReadBinaryRequest {
    public ReadDiscreteInputsRequest(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId, startOffset, numberOfBits);
    }

    ReadDiscreteInputsRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_DISCRETE_INPUTS;
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadDiscreteInputsResponse(slaveId, getData(processImage));
    }

    @Override
    protected boolean getBinary(ProcessImage processImage, int index) throws ModbusTransportException {
        return processImage.getInput(index);
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadDiscreteInputsResponse(slaveId);
    }
}
