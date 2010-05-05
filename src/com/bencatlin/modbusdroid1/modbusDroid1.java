package com.bencatlin.modbusdroid1;

import java.net.InetAddress;

import net.wimpi.modbus.facade.ModbusTCPMaster;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class modbusDroid1 extends Activity {
    /** Called when the activity is first created. */
	
	private String hostIPaddress;
	private int hostPort;
	private ModbusTCPMaster modbusMaster = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //Handler for spinner to select modbus data type
        {
        Spinner s = (Spinner) findViewById(R.id.point_Type);
        //build array with modbus point types
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.pointTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        }
        
        
        
    }
    /* modbusPollingThread is a separate background thread that 
     * continuously polls modbus values on the host in the background
     * 
     */
    
    private class modbusPollingThread implements Runnable {
        	
    	private ModbusTCPMaster modbusMaster = null;
 
       	//@Override 
       	public void run(){
			try {
       		modbusMaster.connect();
			}
			catch (Exception e) {
			
			}
       	}
    	
    	public modbusPollingThread (ModbusTCPMaster modMaster) {
    		this.modbusMaster = modMaster;
    	}


    }
    
    class registerListView extends ListActivity {

    	private String[] modbusDisplayValues;
    	
    	registerListView (String[] modbusDisplayValues){
    		this.modbusDisplayValues = modbusDisplayValues;
    	}
    }
}