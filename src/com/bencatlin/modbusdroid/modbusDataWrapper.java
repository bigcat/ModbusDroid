package com.bencatlin.modbusdroid;

/*
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;

*/

public class modbusDataWrapper {
	
/*	private BitVector m_descretes;
	private Register[] m_registers; */
	private Boolean highByteFirst;
	private Boolean highWordFirst;
	private Boolean MSBfirst; //Most significant bit first - may not need this
	
/*	
	public modbusDataWrapper () {
		m_descretes = null;
		m_registers = null;
		
	}
	
	public modbusDataWrapper ( Register[] registers ) {
		m_descretes = null;
		m_registers = registers;
	}
	
	public modbusDataWrapper ( BitVector descretes ) {
		m_registers = null;
		m_descretes = descretes;
	}
	
	public void setData ( Object data ) {
		if (data instanceof BitVector ) {
			m_descretes = (BitVector) data;
			m_registers = null;
		}
		else if (data instanceof Register[] ) {
			m_registers = (Register[]) data;
			m_descretes = null;
		}
		
	}
*/	
	
	
	
}