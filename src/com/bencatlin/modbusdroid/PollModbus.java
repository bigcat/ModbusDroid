package com.bencatlin.modbusdroid;


import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
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
	public synchronized void connect() throws Exception {
		if (this.isConnected()) {
			this.disconnect();
		}
		try {
			mbTCPMaster.init();
			
		}
		catch (ModbusInitException initException) {
			Log.e(getClass().getSimpleName(), initException.getMessage() );
			m_connected = false;
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
	public synchronized void disconnect() {
		if (m_connected || mbTCPMaster.isInitialized() ) {
			Log.i(getClass().getSimpleName(), "Try to destroy connection");	
			mbTCPMaster.destroy();
		}
		Log.i(getClass().getSimpleName(), "Destroyed connection or it wasn't there" );
		m_connected = false;
	}
	
	/**
	 * returns connection status.
	 * 
	 */
	public synchronized boolean isConnected() {
		
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
		long errorCount = 0;
		long startTime;
		long elapsedTime;
		
		if (!this.isConnected()) {
			try {
				this.connect();
			}
			catch (RuntimeException runtime_e)
			{
				Log.e(getClass().getSimpleName(), runtime_e.getMessage() );
				//Need a way to get a debug message here
			}
			catch (Exception connect_e)
			{	
				Log.e(getClass().getSimpleName(), connect_e.getMessage() );
			//Need a way to get a debug message here too
			}
		}
		try {
			while (m_connected) {
				//get start time for modbus query
				startTime = System.nanoTime();
				//send query, and turn response into decent values that we can use
				modbusValues = mbTCPMaster.getValues(mbLocator);
				//measure how long we waited for a response
				elapsedTime = (System.nanoTime() - startTime)/1000000; 
				if (elapsedTime > ( m_polltime*1.5 ) ) {
					errorCount++;
					Log.e(getClass().getSimpleName(), "Waited too long for response!");
				}	
				Log.i(getClass().getSimpleName(), "Total Error Count: " + errorCount);
				
				//Call back to the UI thread to update the listview
				m_ListView.post( new Runnable() {
					public void run() {
						m_ListView.updateData(modbusValues);
					}
				});
				
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
		catch (NullPointerException nullException) {
			Log.e(getClass().getSimpleName(), nullException.getMessage() );
		}
		catch (Exception poll_e) {
			Log.e(getClass().getSimpleName(), poll_e.getMessage() );
			
			//if (m_connected) {
				//this.disconnect();
			//}
			// TODO add something here to handle exception
		}
	}
	

}