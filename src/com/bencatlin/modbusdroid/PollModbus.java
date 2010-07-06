package com.bencatlin.modbusdroid;


import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import android.util.Log;
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


public class PollModbus implements Runnable {
	
	private int m_polltime;
	private boolean m_connected;

	// This defines what register type we want to read/write
	// 0 = initial state, 1 = input descretes, 2 = holding coil, 
	// 3 = input register 4 = holding register
	
	private Object[] modbusValues;
	
	private ModbusTCPMaster mbTCPMaster;
	private ModbusMultiLocator mbLocator;
	
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

	public PollModbus (ModbusTCPMaster mbMaster, int polltime, ModbusMultiLocator mbLocator, ModbusListView m_ListView) {
		
		//super(adr, port);
		this.m_polltime = polltime;
		this.mbTCPMaster = mbMaster;
		this.mbLocator = mbLocator;
		this.m_ListView = m_ListView;
	}
	
	/**
	 * Sets Modbus Polling time
	 * @param polltime
	 */
	public synchronized void setPollTime (int polltime) {
		this.m_polltime = polltime;
	}
	
	/**
	 * Connects to server and starts polling thread
	 */
	public void connect() throws Exception {
		if (this.isConnected()) {
			this.disconnect();
		}
		try {
			mbTCPMaster.init();
			
		}
		catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage() );
			m_connected = false;
			//Toast.makeText(this, e.getMessage().toString(), 10).show();
			//TODO: do something here to catch other exceptions -- figure this out later
		}
		m_connected = true;
	}
	
	/**
	 *  Disconnects from server, and sets connected bit to false
	 *  This will cause the thread to complete that is doing the polling
	 */
	public void disconnect() {
		if (m_connected || mbTCPMaster.isInitialized() )
			mbTCPMaster.destroy();
		m_connected = false;
	}
	
	/**
	 * returns connection status.
	 * 
	 */
	public boolean isConnected() {
		
		if ( mbTCPMaster.isInitialized() )
			m_connected = true;
		else
			m_connected = false;
		
		return m_connected;
	}

	/**
	 * 
	 * 
	 */
	
	public void run () {
		modbusValues = null;
		
		if (!this.isConnected()) {
			try {
				this.connect();
			}
			catch (RuntimeException runtime_e)
			{
				String errormsg = runtime_e.getMessage();
				Log.i(getClass().getSimpleName(), runtime_e.getMessage() );
				//Need a way to get a debug message here
			}
			catch (Exception connect_e)
			{	
				String errormsg = connect_e.getMessage();
				Log.i(getClass().getSimpleName(), connect_e.getMessage() );
			//Need a way to get a debug message here too
			}
		}
		try {
			while (m_connected) {
				
				modbusValues = mbTCPMaster.getValues(mbLocator);
				
				m_ListView.post( new Runnable() {
					public void run() {
						m_ListView.updateData(modbusValues);
					}
				});
				/*  //This gets to go away and is replaced by new library calls

				switch (mbLocator.getSlaveAndRange().getRange()) {
				case RegisterRange.INPUT_STATUS:
					
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
				case RegisterRange.COIL_STATUS:
					final BitVector bv_coils = this.readCoils(m_reference, m_count);
					temp_string = bv_coils.toString();
					
					//needs to run back in the UI thread
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromBitVector( bv_coils );
						}
					} );
					
					break;
				case RegisterRange.INPUT_REGISTER:
					final Register[] reg_Input = (Register[]) this.readInputRegisters(m_reference, m_count);
					//m_ListView.SetDataFromRegisters(reg);
					//Post to UI Thread queue
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromRegisters(reg_Input);
						}
					} );
					
					break;
				case RegisterRange.HOLDING_REGISTER:
					final Register[] reg_Holding = this.readMultipleRegisters(m_reference, m_count);
					//m_ListView.SetDataFromRegisters(reg);
					m_ListView.post( new Runnable() {
						public void run() {
							m_ListView.SetDataFromRegisters(reg_Holding);
						}
					} );
					break;
					
				} */
				
				
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
		catch (ModbusTransportException m_exception) {
			Log.e(getClass().getSimpleName(), m_exception.getMessage() );
		}
		/*catch (IOException IOe) {
			
		}*/
		catch (Exception poll_e) {
			Log.e(getClass().getSimpleName(), poll_e.getMessage() );
			//if (m_connected) {
				//this.disconnect();
			//}
			// TODO add something here to handle exception
		}
	}
	

}
	

