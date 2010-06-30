package com.bencatlin.modbusdroid;


import com.serotonin.modbus4j.ModbusFactory;
import android.widget.Toast;

/* After switching all the way from jamodbus to modbus4j
 * we don't need these anymore, need the modbus4j instead
 * 
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.facade.ModbusTCPMaster;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;
*/

/*PollModbus_old
 *  This is a runnable that polls in the background
 *  
 *  After being almost done with this, I think possibly extending 
 *  ModbusTCPMaster is a mistake, and it would have been simpler
 *  to just make a modbusmaster instance a member that is public, or
 *  has public methods for manipulation - maybe I'll fix that in
 *  the future as I have time, as this was mostly a learning experience
 *  for getting back into writing software, I'm not too worried about it.
 *  Ben Catlin - 6/24/2010
 * 
 */


public class PollModbus_old implements Runnable {
	
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
	
	private ModbusListView m_ListView = null;
	
	/* PollModus Constructor
	 *  adr = IP address
	 *  port = default is 502
	 *  poll time = 500ms
	 */

	public PollModbus_old (String adr, int port, int polltime, int ref, int count, 
			int regType, ModbusListView m_ListView) {
		
		//super(adr, port);
		this.setReference(ref);
		this.setCount(count);
		this.setPollTime(polltime);
		this.setRegType(regType);
		this.m_ListView = m_ListView;
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
	public synchronized void setPollTime (int polltime) {
		this.m_polltime = polltime;
	}
	
	public synchronized void setReference (int ref) {
		this.m_reference = ref;
	}
	
	public synchronized void setCount (int count) {
		this.m_count = count;
	}
	
	public synchronized void setRegType (int regType) {
		this.m_registerType = regType;
	}
	
	/*public void setIPaddress (String IPaddress) {
		
	}*/
	
	/**
	 * Connects to server and starts polling thread
	 */
	public void connect() throws Exception {
		try {
			//super.connect();
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
	public void run( ModbusFactory mbFactory ) {
		Object temp_obj = null;
		//final BitVector bv = null;
		//Register[] reg = null;
		String temp_string = null;
		try {
			this.connect();
		}
		catch (RuntimeException runtime_e)
		{
			String errormsg = runtime_e.getMessage();
			//Need a way to get a debug message here
		}
		catch (Exception connect_e)
		{	
			String errormsg = connect_e.getMessage();
			//Need a way to get a debug message here too
		}
		
		try {
			while (m_connected) {
				switch (m_registerType) {
				case INPUT_DESCRETES:
					
					final BitVector bv_descretes = this.readInputDiscretes(m_reference, m_count);
					//bv_descretes.toggleAccess(true);
					temp_string = bv_descretes.toString(); 
					//m_ListView.SetDataFromBytes( bv_descretes.getBytes() );
					//needs to be run in the UI thread
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromBitVector( bv_descretes );
						}
					} );
					//m_responseData = String2StringArray(temp_string);
					break;
				case HOLDING_COIL:
					final BitVector bv_coils = this.readCoils(m_reference, m_count);
					temp_string = bv_coils.toString();
					
					//needs to run back in the UI thread
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromBitVector( bv_coils );
						}
					} );
					
					break;
				case INPUT_REGISTER:
					final Register[] reg_Input = (Register[]) this.readInputRegisters(m_reference, m_count);
					//m_ListView.SetDataFromRegisters(reg);
					//Post to UI Thread queue
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromRegisters(reg_Input);
						}
					} );
					
					break;
				case HOLDING_REGISTER:
					final Register[] reg_Holding = this.readMultipleRegisters(m_reference, m_count);
					//m_ListView.SetDataFromRegisters(reg);
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromRegisters(reg_Holding);
						}
					} );
					break;
				}
				//For some lame-ass reason this has to be synchronized to work right
				if (m_polltime != 0 ) {
					synchronized (this){ 
						this.wait(m_polltime);
					}
				}
				else {
					this.disconnect();
				}
			}			
		}
		catch (ModbusException m_exception) {
			String error = m_exception.getMessage();
		}
		/*catch (IOException IOe) {
			
		}*/
		catch (Exception poll_e) {
			String error = poll_e.getMessage();
			//if (m_connected) {
				//this.disconnect();
			//}
			// TODO add something here to handle exception
		}
	}
	
	private String[] String2StringArray (String str) throws Exception {
		int strIndex = 0;
		char temp;
		String[] stringArray = new String[str.length()];
		for (int i = str.length()-1; i >= 0; i--) {
			try {
				temp = str.charAt(i);
			}
			catch (IndexOutOfBoundsException bounds_exception) {
				throw new Exception("Size exceeds byte[] store.");
			}
			stringArray[strIndex] = Character.toString(temp);
			strIndex++;
		}
		return stringArray;
		
	}

}
	

