package com.bencatlin.modbusdroid;

import java.net.InetAddress;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModbusDroid extends Activity {
    
	/** Called when the activity is first created. */
	private static final int SETTINGS = Menu.FIRST + 2;
	private static final int CONNECT = Menu.FIRST;
	private static final int DISCONNECT = Menu.FIRST + 1;
	private static final int QUIT_MENU = Menu.FIRST + 3;
	
	private static final String IP_ADDRESS_PREFERENCE = "IpAddress";
	private static final String PORT_PREFERENCE = "PortSetting";
	private static final String POLL_TIME_PREFERENCE = "PollTime";
	
	private String hostIPaddress;
	private int hostPort;
	private int pollTime;
	private int offset;
	private int m_count;
	private int regType;
	private PollModbus mb = null;
	
	private SharedPreferences settings;
	Thread mbThread = null;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (settings == null) {
			settings = PreferenceManager.getDefaultSharedPreferences(this);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        final EditText offset_editText = (EditText) findViewById(R.id.offset);
        final EditText registerLength = (EditText) findViewById(R.id.length);
        
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        
        //Handler for spinner to select modbus data type
        final Spinner s = (Spinner) findViewById(R.id.point_Type);
        
        //build array with modbus point types
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.pointTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        
        //get the preferences currently stored in the SharedPreferences
        getSharedSettings();
        
        offset = Integer.parseInt(offset_editText.getText().toString());
        m_count = Integer.parseInt(registerLength.getText().toString());
        regType = s.getSelectedItemPosition() + 1;
        
        
        mb = new PollModbus(hostIPaddress, hostPort, pollTime, offset,	m_count, regType);  
        
       
        s.setOnItemSelectedListener( new OnItemSelectedListener() {
        		public void onItemSelected ( AdapterView<?> parent, View view, int pos, long id) {
    					regType = s.getSelectedItemPosition() + 1;        				
        				mb.setRegType(regType);

        		    }

        		    public void onNothingSelected(AdapterView parent) {
        		      // Do nothing.
        		    }
        });
      
        //register keypress handler to change register offset
        offset_editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	offset = Integer.parseInt(offset_editText.getText().toString());
                	mb.setReference(offset);
                	return true;
                }
                else {
                	return false;
                }
            }
        });
        
        //register keypress handler to change length to read
        registerLength.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	
                	m_count = Integer.parseInt(registerLength.getText().toString());
                	mb.setCount(m_count);
                	return true;
                
                }
                else {
                	return false;
                }
            }
        });  
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
    	
    	String oldHostIPaddress = hostIPaddress;
    	int oldPollTime = pollTime;
    	int oldHostPort = hostPort;
    	
        switch (item.getItemId()) {
        case SETTINGS:
        	startActivity(new Intent(this, connectionSettings.class));

        	getSharedSettings();	
        	
        	if (hostIPaddress != oldHostIPaddress) {
        		if (mb.isConnected()){
        			mb.disconnect();
        		}
        		mb.setIPAddress(hostIPaddress);
        	}       	
            return(true);
            
        case CONNECT:
        	Toast.makeText(this, "This should connect to something", 10).show();
        	if (mb == null) {
        		mb = new PollModbus(hostIPaddress, hostPort, pollTime, 
                		offset,	m_count, regType);	
        	}
        	else if (mb.isConnected()) {
        		mb.disconnect();	
        	}
        	
        	if (mbThread == null) {
        		mbThread = new Thread(mb, "PollingThread");        			
    		}
        	else if (mbThread.isAlive()) {
        		mbThread.interrupt();
        		mbThread = null;
        		mbThread = new Thread(mb, "PollingThread");
        	}
        	else {
        		mbThread = null;
        		mbThread = new Thread(mb, "PollingThread");
        	}
        	//mb.connect();
        	mbThread.start();
        	
        	return true;
        	
        case DISCONNECT:
        	if (!mb.isConnected()){
        		Toast.makeText(this, "Not Connected to Anything!", 10).show();
        	}
        	else {
        		//mbThread.interrupt();
        		mb.disconnect();
        		Toast.makeText(this, "Disconnected from " + hostIPaddress, 10).show();
        	}
        
        	return true;
        case QUIT_MENU:
            finish();
            return true;
        }
        return false;
    }
    
    /**
     * Gets settings from the Shared Preferences, and sets local variables
     */
    
    private void getSharedSettings () {
    	hostIPaddress = settings.getString(IP_ADDRESS_PREFERENCE, "10.0.2.2");
        hostPort = Integer.parseInt(settings.getString(PORT_PREFERENCE, "502"));
        pollTime = Integer.parseInt(settings.getString(POLL_TIME_PREFERENCE, "500"));
    }
    
}