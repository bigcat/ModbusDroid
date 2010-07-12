package com.bencatlin.modbusdroid;

import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.base.SlaveAndRange;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusIdException;


public class ModbusMultiLocator extends ModbusLocator {

	private int registersLength;  // This is to track how many registers to read
	
	public ModbusMultiLocator(SlaveAndRange slaveAndRange, int offset, int dataType, int length) {
        super(slaveAndRange, offset, dataType );
        this.registersLength = length;
    }
    
    public ModbusMultiLocator(int slaveId, int range, int offset, int dataType, int length) {
        super(new SlaveAndRange(slaveId, range), offset, dataType );
        this.registersLength = length;
    }
    
    public ModbusMultiLocator(int slaveId, int range, int offset, byte bit, int length) {
        super(slaveId, range, offset, DataType.BINARY, bit);
        if (range != RegisterRange.HOLDING_REGISTER && range != RegisterRange.INPUT_REGISTER)
            throw new ModbusIdException("Bit requests can only be made from holding registers and input registers");
        this.registersLength = length;
    }
    
    public ModbusMultiLocator(int slaveId, int range, int offset, int dataType, byte bit, int length) {
        this(new SlaveAndRange(slaveId, range), offset, dataType, bit);
    	this.registersLength = length;
    }

    /*
     * 
     */
    public synchronized void setDataType (int dataType ) {
    	this.dataType = dataType;
    }
    
    /*
     * 
     */
    public synchronized void setOffset ( int offset ) {
    	this.offset = offset;
    }
    
    /*
     * 
     */
    public synchronized void setSlaveAndRange (SlaveAndRange slaveAndRange ) {
    	this.slaveAndRange = slaveAndRange;
    	//this.setDataType(slaveAndRange.getRange());
    }
    
    /*
     * 
     */
	public synchronized void setRegistersLength(int registersLength) {
		this.registersLength = registersLength;
	}

	public int getRegistersLength() {
		return registersLength;
	}
	
    /*  Come back to this later
     * 
     */
	public synchronized Object[] bytesToValueArray ( byte[] bytes ) {
		int registersPerValue = this.getLength();
		int valueLength = registersLength / registersPerValue;
		
		byte[] temp = new byte[ ( this.getLength() * 2 )];

		Object[] values = new Object [valueLength];
		
		for (int i = 0; i < valueLength; i++) {
			
			System.arraycopy(bytes, ((registersPerValue *2)*i), 
					temp, 0, (registersPerValue * 2));
			
			values[i] = this.bytesToValue(temp, offset);
			
		}
		
		return values;
		
	}
	
	
}