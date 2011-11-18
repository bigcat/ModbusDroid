package com.bencatlin.modbusdroid.OldVersion;


import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/*PollModbus
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
	
	private static final boolean DEBUG = false;
	
	private int m_polltime;
	private boolean m_connected = false;

	// This defines what register type we want to read/write
	// 0 = initial state, 1 = input descretes, 2 = holding coil, 
	// 3 = input register 4 = holding register
	
	private Object[] modbusValues;
	
	private ModbusTCPMaster mbTCPMaster;
	private ModbusMultiLocator mbLocator;
	private ModbusMultiLocator mbWriteLocator;
	
	private Handler mainThreadHandler;
	
	private ModbusListView m_ListView = null;
	
	private boolean doWriteValue = false;
	private Object writeValue;
	
	
	/* PollModus Constructor
	 *  adr = IP address
	 *  port = default is 502
	 *  poll time = 500ms
	 */

	public PollModbus (ModbusTCPMaster mbMaster, int polltime, 
			ModbusMultiLocator mbLocator, ModbusListView m_ListView, Handler mainThreadHandler) {
		
		//super(adr, port);
		this.m_polltime = polltime;
		this.mbTCPMaster = mbMaster;
		this.mbLocator = mbLocator;
		this.m_ListView = m_ListView;
		this.mainThreadHandler = mainThreadHandler;
		this.mbWriteLocator = null;
		this.writeValue = false;
		
	}
	
	public void updateMembersFromUI (ModbusMultiLocator mbLocator, ModbusListView mListView, 
				Handler mainThreadHandler) {
		this.mbLocator = mbLocator;
		this.m_ListView = mListView;
		this.mainThreadHandler = mainThreadHandler;
		
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
			throw initException;
		}
		catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage() );
			m_connected = false;
			throw e;
			//TODO: do something here to catch other exceptions -- figure this out later
		}
		
		m_connected = true;
		
		Message m = this.mainThreadHandler.obtainMessage();
		
		m.arg1 = 1;
		mainThreadHandler.sendMessage(m);
	}
	
	/**
	 *  Disconnects from server, and sets connected bit to false
	 *  This will cause the thread to complete that is doing the polling
	 */
	public synchronized void disconnect() {
		Message m = this.mainThreadHandler.obtainMessage();
		if (m_connected || mbTCPMaster.isInitialized() ) {
			if (DEBUG)
				Log.i(getClass().getSimpleName(), "Try to destroy connection");	
			mbTCPMaster.destroy();
			if (DEBUG)
				Log.i(getClass().getSimpleName(), "Destroyed connection" );
			m.arg2 = 0;
		}
		else {
			m.arg2 = 1;
			if (DEBUG)
				Log.i(getClass().getSimpleName(), "Tried to destroy connection, but nothing was there!" );
		}
		
		m_connected = false;
		
		m.arg1 = 0;
		mainThreadHandler.sendMessage(m);
	}
	
	/**
	 * returns connection status.
	 * 
	 */
	public synchronized boolean isConnected() {
		
		/*if ( mbTCPMaster.isInitialized() )
			m_connected = true;
		else
			m_connected = false; */
		
		return m_connected;
	}

	public synchronized void writeValue (ModbusMultiLocator writeLocator, Object value) {
		this.mbWriteLocator = writeLocator;
		this.writeValue = value;
		this.doWriteValue = true;
		this.notify();
	}
	
	/**
	 * This is where the magic happens - we just loop until disconnected
	 * 
	 */
	
	public void run () {
		modbusValues = null;
		Message m = this.mainThreadHandler.obtainMessage();
		
		if (!this.isConnected()) {
			try {
				this.connect();
				//wait while we connect
				while(!this.isConnected()) {
					Thread.currentThread();
					Thread.sleep(200);
				}

			}
			catch (RuntimeException runtime_e)
			{
				Log.e(getClass().getSimpleName(), runtime_e.getMessage() );
				//Need a way to get a debug message here
				
				//Send a message to the main UI thread that we got a connection error
				m.arg1 = -1;
				m.obj = runtime_e.getMessage();
				mainThreadHandler.sendMessage(m);
			}
			catch (Exception connect_e)
			{	
				Log.e(getClass().getSimpleName(), connect_e.getMessage() );
				//Need a way to get a debug message here too
				
				//Send a message to the main UI thread that we got a connection error
				m.arg1 = -1;
				m.obj = connect_e.getMessage();
				mainThreadHandler.sendMessage(m);
			}
		}
		try {
			while (m_connected) {
				
				//Log.i(getClass().getSimpleName(), "DataType: "+ mbLocator.getDataType() + ", Range: " + mbLocator.getSlaveAndRange().getRange() + ", Length: " + mbLocator.getLength() );
				
				//send query, and turn response into decent values that we can use
				modbusValues = mbTCPMaster.getValues(mbLocator);
				
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
				
				// after we call writeValue from the UI thread, 
				// then we call notify() and this code gets processed 
				if (doWriteValue) {
					if (DEBUG)
						Log.i(getClass().getSimpleName(), "Writing Value: " + writeValue );
					mbTCPMaster.setValue(mbWriteLocator, writeValue);
					doWriteValue = false;
				}
			}			
		}
		catch (ModbusTransportException m_exception) {
			Log.e(getClass().getSimpleName(), m_exception.getMessage() );
			this.disconnect();
			m.arg1 = -1;
			m.obj = m_exception.getMessage();
			mainThreadHandler.sendMessage(m);
		}
		/*catch (IOException IOe) {
			
		}*/
		catch (NullPointerException nullException) {
			Log.e(getClass().getSimpleName(), "Null Pointer Exception" );
			m.arg1 = -1;
			m.obj = nullException.getMessage();
			mainThreadHandler.sendMessage(m);
			this.disconnect();
		}
		catch (Exception poll_e) {
			Log.e(getClass().getSimpleName(), poll_e.getMessage() );
			m.arg1 = -1;
			m.obj = poll_e.getMessage();
			mainThreadHandler.sendMessage(m);
			//if (m_connected) {
				//this.disconnect();
			//}
			// TODO: add something here to handle exception
		}
	}
	

}