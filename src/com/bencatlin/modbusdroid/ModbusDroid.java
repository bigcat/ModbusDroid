package com.bencatlin.modbusdroid;

import com.serotonin.modbus4j.base.SlaveAndRange;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.ip.IpParameters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.util.Log;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModbusDroid extends Activity {
    
	/** Called when the activity is first created. */

	/* Menu constants
	 * 
	 */
	private static final int SETTINGS = Menu.FIRST + 2;
	private static final int CONNECT = Menu.FIRST;
	private static final int DISCONNECT = Menu.FIRST + 1;
	private static final int DATATYPES = Menu.FIRST + 3;
	//
	
	private static final String IP_ADDRESS_PREFERENCE = "IpAddress";
	private static final String PORT_PREFERENCE = "PortSetting";
	private static final String POLL_TIME_PREFERENCE = "PollTime";
	
	private IpParameters ipParameters;
	private ModbusTCPFactory mbFactory;
	private ModbusTCPMaster mbTCPMaster;
	private ModbusMultiLocator mbLocator;
	
	private String hostIPaddress;
	private int hostPort;
	
	private int pollTime;
	private int offset;
	private int m_count;
	private int regType;
	private int dataType;
	
	private String oldHostIPaddress = hostIPaddress;
	//private int oldPollTime = pollTime;
	private int oldHostPort = hostPort;
	private int oldDataType;
	
	private PollModbus mb = null;
	private ModbusListView mbList;
	
	private RelativeLayout mainLayout;
	private TextView notConnTextView;
	private RelativeLayout.LayoutParams listParams;
	private AlertDialog.Builder dataTypeMenuBuilder;
	private AlertDialog dataTypeAlert;
	private MenuItem dataTypeMenuItem;
	
	private SharedPreferences settings;
	Thread mbThread = null;
	
	private Object[] modbusData;
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if (settings == null) {
			settings = PreferenceManager.getDefaultSharedPreferences(this);
		}
		getSharedSettings();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
            
        final EditText offset_editText = (EditText) findViewById(R.id.offset);
        final EditText registerLength = (EditText) findViewById(R.id.length);
        
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        //get the preferences currently stored in the SharedPreferences
        getSharedSettings();
        oldDataType = dataType;
        
        //Handler for spinner to select modbus data type
        final Spinner s = (Spinner) findViewById(R.id.point_Type);
        
        //build array with modbus point types
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.pointTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        
        //Build the menu for data type display
        dataTypeMenuBuilder = new AlertDialog.Builder(this);
        dataTypeMenuBuilder.setTitle("Display Registers as: ");
        dataTypeMenuBuilder.setSingleChoiceItems(R.array.dataTypeItems, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Set datatype here
            	oldDataType = item;
            	setDataType(item);
				
				dataTypeAlert.dismiss();
            }
        });
        dataTypeMenuBuilder.setCancelable(true);
        dataTypeAlert = dataTypeMenuBuilder.create();
        
        //Set values in UI elements according to shared preferences
        offset_editText.setText(Integer.toString(offset));
        registerLength.setText(Integer.toString(m_count));
        s.setSelection(regType - 1);  //double-check this vs. new ints for regType
        
        //set up some dummy list data
        modbusData = new Object[] {0};
        
        //lets get our new list
        mbList = new ModbusListView( this, modbusData );
        mbList.setFocusable( false );
        
        //need to get the parent relative layout before adding the view
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        //add a rule
        listParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        
        listParams.addRule(mainLayout.BELOW, R.id.param_table);
        listParams.setMargins(10, 5, 10, 5);
        setLayoutAnim_slideupfrombottom(mbList, this);
        
        //make it invisible until we need it - this doesn't work yet
        notConnTextView = new TextView(this);
        notConnTextView.setText("Not Connected!");
        
        //mainLayout.addView(mbList, listParams);
        mainLayout.addView(notConnTextView, listParams);
        
        switchRegType(regType);
        
        ipParameters = new IpParameters();
        ipParameters.setHost(hostIPaddress);
        ipParameters.setPort(hostPort);
        mbFactory = new ModbusTCPFactory ();
        mbTCPMaster = mbFactory.createModbusTCPMaster(ipParameters, true);
        mbTCPMaster.setTimeout(360000);
        try {
        	mbLocator = new ModbusMultiLocator (1, regType, offset, dataType, m_count);
        }
        catch (Exception e) {
        	Log.e(getClass().getSimpleName(), e.getMessage() );
        }
        // get a new Poll modbus object that we will pass to the thread starter
        mb = new PollModbus(mbTCPMaster, pollTime, mbLocator, mbList);  
       
        // Set up Listeners for
        // all the different changes on the main screen
        
        // Listener for spinner selection
        s.setOnItemSelectedListener( new OnItemSelectedListener() {
        		public void onItemSelected ( AdapterView<?> parent, View view, int pos, long id) {
    					regType = s.getSelectedItemPosition() + 1;        				
        				//mbLocator.setSlaveAndRange(new SlaveAndRange (1, regType));
        				
        				switchRegType(regType);
        				
        		    }
        		public void onNothingSelected(AdapterView parent) {
        		      // Do nothing.
        		    }
        });

        
        //register keypress handler to change register offset
        
        //TODO: I should change the onKey to a method and call it here, to avoid duplicating code
        offset_editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	offset = Integer.parseInt(offset_editText.getText().toString());
                	mbLocator.setOffset(offset);
                	
                	SharedPreferences.Editor editor = settings.edit();
    				editor.putInt("registerOffset", offset);
    				editor.commit();
    				
    				switchRegType(regType);
                	
                	//Hide the keyboard
                	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                	
                	return true;
                }
                else {
                	return false;
                }
            }
        });
        
        //register "done button" press
        // insides are the same as the "enter" keypress from hardware keyboards
        offset_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                	offset = Integer.parseInt(offset_editText.getText().toString());
                	mbLocator.setOffset(offset);
                	
                	SharedPreferences.Editor editor = settings.edit();
    				editor.putInt("registerOffset", offset);
    				editor.commit();
    				
                	switchRegType(regType);
                	
                	//Hide the keyboard
                	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                	
                    return true;
                }
                return false;
            }
        });
        
        //register keypress handler to change length to read
        registerLength.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {                	
                if ( (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) ) {
               		m_count = Integer.parseInt(registerLength.getText().toString());
               		
               		mbLocator.setRegistersLength(m_count);
               		//mb.setCount(m_count);
               		
                	SharedPreferences.Editor editor = settings.edit();
    				editor.putInt("registerCount", m_count);
    				editor.commit();
               		
               		//Hide the keyboard
                   	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                   	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
               		
               		return true; 

               	}
                else {
                    	return false;
                }
            }
        });  
    
    
        // Catch the "done or next" keypress from the virtual keyboard
        // insides are the same as the "enter" keypress from hardware keyboards above
        registerLength.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		if ( actionId == EditorInfo.IME_ACTION_DONE ) {
        			m_count = Integer.parseInt(registerLength.getText().toString());
               		
        			mbLocator.setRegistersLength(m_count);
        			//mb.setCount(m_count);
               		
                	SharedPreferences.Editor editor = settings.edit();
    				editor.putInt("registerCount", m_count);
    				editor.commit();
               		
               		// Hide Keyboard
                   	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                   	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
               		
               		return true; 
        		}
        		return false;
        	}
        });

    }
    
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, CONNECT, 0, "Connect");
        menu.add(0, DISCONNECT, 0, "Disconnect");
        menu.add(0, SETTINGS, 0, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
        dataTypeMenuItem = menu.add(0, DATATYPES, 0, "Data Display").setIcon(android.R.drawable.ic_input_get);
        setDataType(dataType);
        return true;
    }
    

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        switch (item.getItemId()) {
        case SETTINGS:
        	startActivityForResult(new Intent(this, connectionSettings.class), 0);
            
        	return(true);
            
        case CONNECT:
        	
        	if (mb == null) {
        		mb = new PollModbus(mbTCPMaster, pollTime, mbLocator, mbList);	
        	}
        	else if (mb.isConnected()) {
        		mb.disconnect();	
        	}
        	
        	//Check to see if the thread is running - if it is, shut it down before creating a new one.
        	if (mbThread != null ) {
        		if (mbThread.isAlive()) {
        			mbThread.interrupt();
        			mbThread = null;
        		}
        		mbThread = null;
        	}
        	mbThread = new Thread(mb, "PollingThread");
        	//mb.connect();
        	Toast.makeText(this, "Connecting to " + hostIPaddress, 10).show();
        	mbThread.start();
        	
        	if (mb.isConnected()){
        		Toast.makeText(this, "Connected!!!!", 5).show();
        	}
        	
        	if ( notConnTextView.isShown() ) {
        		mainLayout.removeView(notConnTextView);
        		mainLayout.addView(mbList, listParams);
        		//showView(mbList);
        	}
        	
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
        	
        	if ( mbList.isShown() ) {
        		mainLayout.removeView(mbList);
        		mainLayout.addView(notConnTextView);
        		//hideView(mbList);
        	}
        	
        	return true;
        case DATATYPES:
            dataTypeAlert.show();
        	
            return true;
        }
        return false;
    }
    
    /*
     * onActivityResult
     * Overrides the default Android method
     * Calls the default, and then re-checks the preferences, and if they have changed
     * this disconnects from the current server and sets a new IP address or port
     * 
     * In the future, it would be wise to actually set a result code if we add
     * more than one activity, and then take action based on which activity is returning.
     * Until there is more than one activity, this can be left alone and assumed it is always
     * the preference activity.
     * 
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    
    @Override
    public void onActivityResult ( int reqCode, int resultCode, Intent data ) {
    	super.onActivityResult(reqCode, resultCode, data);
    	
    	getSharedSettings();	
    	
    	if ( (hostIPaddress != oldHostIPaddress) || (oldHostPort != hostPort ) ) {
    		if (mb.isConnected()){
    			mb.disconnect();
    			mainLayout.removeView(mbList);
            	mainLayout.addView(notConnTextView);
    		}
    		
    		ipParameters.setHost(hostIPaddress);
    		ipParameters.setPort(hostPort);	
    		if (mb != null) {
    			mb.setPollTime(pollTime);
    		}
    	}
    
    }
    
    //
    /*
     * Helper functions
     */
    //
    
    /**
     * Gets settings from the Shared Preferences, and sets local variables
     */
    private void getSharedSettings () {
    	hostIPaddress = settings.getString(IP_ADDRESS_PREFERENCE, "10.0.2.2");
        hostPort = Integer.parseInt(settings.getString(PORT_PREFERENCE, "502"));
        pollTime = Integer.parseInt(settings.getString(POLL_TIME_PREFERENCE, "500"));
        
        m_count = settings.getInt("registerCount", 1);
        offset = settings.getInt("registerOffset", 0);
        regType = settings.getInt("registerType", 0);
        dataType = settings.getInt("dataType", 1);
        //switchRegType(regType);
        
    } //getSharedSettings
    
    /**
     * 
     */
    public void setDataType (int dataType ) {
    	// We need to check first if this dataType is valid for the register range
    	if ( (regType != RegisterRange.COIL_STATUS) && 
    			(regType != RegisterRange.INPUT_STATUS) ) {
    		this.dataType = dataType;
    		if (dataTypeMenuItem != null ) {
    			dataTypeMenuItem.setEnabled(true);
    		}
    	}
    	else {
    		this.dataType = DataType.BINARY;
    		if (dataTypeMenuItem != null ) {
    			dataTypeMenuItem.setEnabled(false); 	
    		}
    	}
    	if (mbLocator != null) {
    		mbLocator.setDataType(this.dataType);
    	}
    	
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt("dataType", dataType);
		editor.commit();
    	
    }
    
    
    /**
     * 
     */
    public void switchRegType (int regType) {
    	
    	this.regType = regType;
    	
    	switch (regType) {
    	
    	case RegisterRange.COIL_STATUS:
    		mbList.setStartAddress(1000 + offset);
    		//if (dataType != DataType.BINARY) {
    			oldDataType = dataType;
        		setDataType(DataType.BINARY);
    		//}
    		break;
    	case RegisterRange.INPUT_STATUS:
    		mbList.setStartAddress(0000 + offset);
    		//if (dataType != DataType.BINARY) {
    			oldDataType = dataType;
        		setDataType(DataType.BINARY);
    		//}
    		break;
    	case RegisterRange.HOLDING_REGISTER:
    		mbList.setStartAddress(4000 + offset);
    		setDataType(dataType);
    		break;
    	case RegisterRange.INPUT_REGISTER:
    		mbList.setStartAddress(3000 + offset);
    		setDataType(dataType);
    	}
    	
    	if (mbLocator != null ) {
        	mbLocator.setSlaveAndRange(new SlaveAndRange(1, regType) );    		
    	}

    	
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt("registerType", regType);
		editor.commit();
    	
    }
    
    
    // Add some annimation to things - sliding in from the bottom
    public static void setLayoutAnim_slideupfrombottom(ViewGroup panel, Context ctx) {

    	  AnimationSet set = new AnimationSet(true);

    	  Animation animation = new AlphaAnimation(0.0f, 1.0f);
    	  animation.setDuration(500);
    	  set.addAnimation(animation);

    	  animation = new TranslateAnimation(
    	      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
    	      Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
    	  );
    	  animation.setDuration(500);
    	  set.addAnimation(animation);

    	//  set.setFillBefore(false);
    	//  set.setFillAfter(false);

    	  LayoutAnimationController controller =
    	      new LayoutAnimationController(set, 0.25f);
    	  panel.setLayoutAnimation(controller);

    	}
}