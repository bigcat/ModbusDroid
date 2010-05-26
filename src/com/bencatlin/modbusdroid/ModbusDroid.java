package com.bencatlin.modbusdroid;

import java.net.InetAddress;

import net.wimpi.modbus.facade.ModbusTCPMaster;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModbusDroid extends Activity {
    /** Called when the activity is first created. */
	TextView RandomText = null;
	private static final int SETTINGS = Menu.FIRST + 2;
	private static final int CONNECT = Menu.FIRST;
	private static final int DISCONNECT = Menu.FIRST + 1;
	private static final int QUIT_MENU = Menu.FIRST + 3;
	
	
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
        menu.add(0, CONNECT, 0, "Connect");
        menu.add(0, DISCONNECT, 0, "Disconnect");
        menu.add(0, SETTINGS, 0, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, QUIT_MENU, 0, "Quit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case SETTINGS:
        	startActivity(new Intent(this, connectionSettings.class));
            return(true);
        case CONNECT:
        	Toast.makeText(this, "This should connect to something", 10).show();
        	return true;
        case DISCONNECT:
        	Toast.makeText(this, "This should disconnect from something", 10).show();
        	return true;
        case QUIT_MENU:
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