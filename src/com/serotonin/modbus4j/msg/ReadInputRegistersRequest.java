package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadInputRegistersRequest extends ReadNumericRequest {
    public ReadInputRegistersRequest(int slaveId, int startOffset, int numberOfRegisters)
            throws ModbusTransportException {
        super(slaveId, startOffset, numberOfRegisters);
    }

    ReadInputRegistersRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_INPUT_REGISTERS;
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadInputRegistersResponse(slaveId, getData(processImage));
    }

    @Override
    protected short getNumeric(ProcessImage processImage, int index) throws ModbusTransportException {
        return processImage.getInputRegister(index);
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadInputRegistersResponse(slaveId);
    }
}
