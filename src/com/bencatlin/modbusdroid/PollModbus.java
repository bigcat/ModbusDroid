package com.bencatlin.modbusdroid;

import android.widget.Toast;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.facade.ModbusTCPMaster;

public class PollModbus extends ModbusTCPMaster implements Runnable {
	
	private int m_polltime;
	private boolean m_connected;

	// This defines what register type we want to read/write
	// 0 = initial state, 1 = input descretes, 2 = holding coil, 
	// 3 = input register 4 = holding register
	private int m_registerType = 0;  
	private int m_reference = 0; // register/coil offset
	private int m_count = 1;  // number of registers/coils to read
	private String[] m_responseData;
	
	private static final int INPUT_DESCRETES = 1;
	private static final int HOLDING_COIL = 2;
	private static final int INPUT_REGISTER = 3;
	private static final int HOLDING_REGISTER = 4;
	
	
	/* PollModus Constructor
	 *  adr = IP address
	 *  port = default is 502
	 *  poll time = 500ms
	 */
	public PollModbus (String adr) {
		this(adr, 502, 500);
	}
	
	public PollModbus (String adr, int port) {
		this(adr, port, 500);
	}
	
	public PollModbus (String adr, int port, int polltime) {
		super(adr, port);
		this.m_polltime = polltime;
	}
	
	/**
	 * Gets current polltime
	 * @return polltime
	 */
	public int getPollTime() {
		return m_polltime;
	}
	
	/**
	 * Sets Modbus Polling time
	 * @param polltime
	 */
	public void setPollTime (int polltime) {
		this.m_polltime = polltime;
	}
	
	public void setReference (int ref) {
		this.m_reference = ref;
	}
	
	public void setCount (int count) {
		this.m_count = count;
	}
	
	/*public void setIPaddress (String IPaddress) {
		
	}*/
	
	/**
	 * Connects to server and starts polling thread
	 */
	public void connect() throws RuntimeException {
		try {
			super.connect();
			m_connected = true;
			// need to start polling thread here - should I create the thread object
		}
		catch (Exception e) {
			e.getMessage();
			//Toast.makeText(this, e.getMessage().toString(), 10).show();
			// do something -- figure this out later
		}
	}
	
	/**
	 *  Disconnects from server, and sets connected bit to false
	 *  This will cause the thread to complete that is doing the polling
	 */
	public void disconnect() {
		if (m_connected)
			super.disconnect();
		m_connected = false;
	}
	
	/**
	 * returns connection status.
	 * 
	 */
	public boolean isConnected() {
		m_connected = super.isConnected();
		return m_connected;
	}

	/**
	 * 
	 * 
	 */
	public void run() {
		try {
		this.connect();
		}
		catch (RuntimeException e)
		{
			//Need a way to get a debug message here
		}
		
		try {
			while (m_connected) {
				switch (m_registerType) {
				case INPUT_DESCRETES:
					String temp;
					temp =  this.readInputDiscretes(m_reference, m_count).toString();
					m_responseData = String2StringArray(temp);
					break;
				case HOLDING_COIL:	
					
					break;
				case INPUT_REGISTER:
					this.readInputRegisters(m_reference, m_count).toString();
					break;
				case HOLDING_REGISTER:
				
				}
				wait(m_polltime);
			}			
		}
		catch (ModbusException m_exception) {
			String error = m_exception.getMessage();
		}
		catch (Exception e) {
			if (m_connected) {
				this.disconnect();
			}
			//TODO add something here to handle exception
		}
	}
	
	private String[] String2StringArray (String str) {
		int strIndex = 0;
		char temp;
		String[] stringArray = new String[str.length()];
		for (int i = str.length(); i >= 0; i--) {
			temp = str.charAt(i);
			stringArray[strIndex] = Character.toString(temp);
			strIndex++;
		}
		return stringArray;
		
	}

}
	

