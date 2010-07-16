package com.bencatlin.modbusdroid;

import android.util.Log;

import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.IllegalFunctionException;
import com.serotonin.modbus4j.exception.InvalidDataConversionException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadResponse;


public class ModbusTCPMaster extends TcpMaster {

	public ModbusTCPMaster(IpParameters params, boolean keepAlive) {
		super(params, keepAlive);
	}
	
	public boolean isInitialized () {
		return initialized;
	}
	
	public synchronized Object[] getValues ( ModbusMultiLocator locator ) throws ModbusTransportException, ErrorResponseException {
		//int slaveId = locator.getSlaveAndRange().getSlaveId();
        int registerRange = locator.getSlaveAndRange().getRange();
        //int writeOffset = locator.getOffset();
        
        int registersPerValue = locator.getLength();
        
        //Not sure how yet, but somehow the dataType got set to 0, so this fixes that        
        if (registersPerValue == 0 ) {
        	if ((registerRange == RegisterRange.COIL_STATUS) || (registerRange == RegisterRange.INPUT_STATUS) ) {
        		locator.setDataType(DataType.BINARY);
        	}
        	else {
        		locator.setDataType(DataType.TWO_BYTE_INT_UNSIGNED);
        	}
        }
		
        int valueLength = locator.getRegistersLength() / registersPerValue;
        
        byte[] data = new byte[locator.getRegistersLength()];
        Object[] values = new Object[valueLength];
        
        ModbusRequest request;
        ReadResponse response;
        
        if (locator.getRegistersLength() == 1 || locator.getLength() == locator.getRegistersLength() ) {
        	
        	values[0] = this.getValue(locator);
        }
        else {
        	// Determine the request type that we will use
        	switch (registerRange) {
        
        	case RegisterRange.COIL_STATUS:
        		request = new ReadCoilsRequest (locator.getSlaveAndRange().getSlaveId(), locator.getOffset(), locator.getRegistersLength() );
        		break;
        
        	case RegisterRange.INPUT_STATUS:
        		request = new ReadDiscreteInputsRequest (locator.getSlaveAndRange().getSlaveId(), locator.getOffset(), locator.getRegistersLength() );
        		break;
        
        	case RegisterRange.INPUT_REGISTER:
        		request = new ReadInputRegistersRequest (locator.getSlaveAndRange().getSlaveId(), locator.getOffset(), locator.getRegistersLength() );
        		break;
        
        	case RegisterRange.HOLDING_REGISTER:
       
        		request = new ReadHoldingRegistersRequest (locator.getSlaveAndRange().getSlaveId(), locator.getOffset(), locator.getRegistersLength() );
        		break;
        	
        	default:
        		request = null;
        		throw new IllegalFunctionException( (byte) registerRange );
        	}	
        	//Not sure putting a try-catch combo here is a good idea, might be better to pass exception upstream
        	try {
        		Log.i(getClass().getSimpleName(), "Sending request: " + request.getFunctionCode());
        		response = (ReadResponse) send(request);
        		data = response.getData();
        		//Log.i(getClass().getSimpleName(), "Returned data: " + data.toString() );
        		values = locator.bytesToValueArray(data);
        	}
        	catch ( Exception e ) {
        		Log.e(getClass().getSimpleName(), e.getMessage() );
        	}
        }
		
		return values;
	}
	
}