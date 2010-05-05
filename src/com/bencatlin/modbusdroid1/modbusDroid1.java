package com.bencatlin.modbusdroid1;

import java.net.InetAddress;

import net.wimpi.modbus.facade.ModbusTCPMaster;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class modbusDroid1 extends Activity {
    /** Called when the activity is first created. */
	TextView RandomText = null;
	private static final int MENU1 = Menu.FIRST;
	private static final int MENU2 = Menu.FIRST + 1;
	
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
    

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU1, 0, "Connection");
        menu.add(0, MENU2, 0, "Quit");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU1:
            RandomText.setText("take to connection menu");
            return true;
        case MENU2:
            finish();
            return true;
        }
        return false;
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