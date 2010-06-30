package com.serotonin.modbus4j.msg;

import com.serotonin.io.messaging.MessageMismatchException;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class ReportSlaveIdRequest extends ModbusRequest {
    public ReportSlaveIdRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }
    
    @Override
    protected void writeRequest(ByteQueue queue) {
    }
    
    @Override
    protected void readRequest(ByteQueue queue) {
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReportSlaveIdResponse(slaveId);
    }

    @Override
    protected void matchesImpl(ModbusResponse response) throws MessageMismatchException {
        if (!(response instanceof ReportSlaveIdResponse))
            throw new MessageMismatchException(response.getClass().toString());
    }
    
    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReportSlaveIdResponse(slaveId, processImage.getReportSlaveIdData());
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.REPORT_SLAVE_ID;
    }
}
