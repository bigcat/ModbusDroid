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
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
//import android.util.Log;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.AdapterView.OnItemClickListener;
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
	private int writeRegOffset;
	
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
	private View textEntryNumericView;
	private AlertDialog writeDialog;
	private AlertDialog writeBoolRegisterDialog;
	//private AlertDialog.Builder writeBoolRegisterDialogBuilder;
	private AlertDialog writeBoolCoilDialog;
	private AlertDialog writeNumericRegisterDialog;
	
	private Object mbWriteValue;
	
	private MbDroidMsgExceptionHandler exceptionHandler;
	
	private SharedPreferences settings;
	Thread mbThread = null;
	
	private Object[] modbusData;

	
	// Make a new handler to get messages from the polling thread and display them in the UI
	Handler pollHandler = new Handler () {
		
		@Override
		public void handleMessage (Message pollingMsg) {
			int arg1 = pollingMsg.arg1;
			int arg2 = pollingMsg.arg2;
			String msgString = (String) pollingMsg.obj;
			String displayString;
			
			switch (arg1) {
			
				case 0: //We are disconnected or disconnecting
					hideMBList();
					switch (arg2) {
					
					case 0: //Disconnected from the host
						displayString = "Disconnected from " + hostIPaddress;
						break;
					case 1: // We never connected to a host
						displayString = "Not connected to anything!";
						break;
					default:
						displayString = "Whoops! Something is wrong";
						break;
					}
					break;
				case 1: //We are connected
					displayString = "Connected to " + hostIPaddress;
					showMBList();
					break;
				case -1: //Got some type of error
					displayString = "Error: " + msgString;
					break;
				default:
					displayString = "Busted!";
					break;
			}
			Toast.makeText(getBaseContext(), displayString, 10).show();	
			super.handleMessage(pollingMsg);
		}
		
	};
	
	
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
	 * @see android.app.Activity#onStop()
	 * We are overriding this because we need to explicitly
	 * disconnect if we are connected when the app stops
	 * 
	 * Consider using onPause/onResume to allow a hidden app
	 * to continue polling and then re-display results.
	 */
	@Override
	public void onStop () {
		super.onStop();
		
		if (mb.isConnected()) {
			mb.disconnect();
		}
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
        
        //set up some dummy list data
        modbusData = new Object[] {0};
        
        //lets get our new list
        mbList = new ModbusListView( this, modbusData , DataType.getRegisterCount(dataType) );
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
        
        /** 
         * 		We are going to create three dialogs, 
         * 		and then show the correct one depending on what
         * 		register range and data type we are using 
         */
        
        // Set up the numeric entry write dialog
   		LayoutInflater factory = LayoutInflater.from(this);
   		textEntryNumericView = factory.inflate(R.layout.write_value_numeric, null);
   		AlertDialog.Builder writeNumericDialogBuilder = new
       		AlertDialog.Builder(this);
   		writeNumericDialogBuilder.setTitle("Write to Register")
   			.setView(textEntryNumericView)
   			.setIcon(android.R.drawable.ic_menu_edit) //TODO: Better icon here
   			.setMessage("Value to Write to Register")
   			.setNegativeButton("Cancel", 
   					new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
   					dialog.dismiss();
   				}
   			})
   			.setPositiveButton("Write", 
   					new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
       			
   					// Let's set up the correct values depending on what type of display we are showing
   					switch (dataType) {
   					case DataType.TWO_BYTE_INT_UNSIGNED:
   					case DataType.TWO_BYTE_INT_SIGNED:
   	   					mbWriteValue = (short) Short.parseShort( ((EditText) textEntryNumericView.findViewById(R.id.write_value_number)).getText().toString() );
   						break;
   					case DataType.FOUR_BYTE_INT_UNSIGNED:
   					case DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED:
   					case DataType.FOUR_BYTE_INT_SIGNED:
   					case DataType.FOUR_BYTE_INT_SIGNED_SWAPPED:
   	   					mbWriteValue = (int) Integer.parseInt( ((EditText) textEntryNumericView.findViewById(R.id.write_value_number)).getText().toString() );
   						break;
   					case DataType.EIGHT_BYTE_INT_UNSIGNED:
   					case DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED:
   					case DataType.EIGHT_BYTE_INT_SIGNED:
   					case DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED:
   	   					mbWriteValue = (long) Float.parseFloat( ((EditText) textEntryNumericView.findViewById(R.id.write_value_number)).getText().toString() );
   						break;
   					case DataType.FOUR_BYTE_FLOAT_SWAPPED:
   					case DataType.FOUR_BYTE_FLOAT:
   	   					mbWriteValue = (float) Float.parseFloat( ((EditText) textEntryNumericView.findViewById(R.id.write_value_number)).getText().toString() );
   						break;
   					case DataType.EIGHT_BYTE_FLOAT:
   					case DataType.EIGHT_BYTE_FLOAT_SWAPPED:
   	   					mbWriteValue = (double) Double.parseDouble( ((EditText) textEntryNumericView.findViewById(R.id.write_value_number)).getText().toString() );
   						break;
   					}

   					//Get rid of the dialog so the UI isn't waiting around for the write to happen before the dialog is dismissed
   					dialog.dismiss();
       			
   					Toast.makeText(getBaseContext(), "Writing Value: " + mbWriteValue, 5).show();
       			
   					mb.writeValue(new ModbusMultiLocator (1, regType, writeRegOffset, dataType, 1)
   						, mbWriteValue);
       			
   				}
       	});
   		writeNumericRegisterDialog = writeNumericDialogBuilder.create();
        
   		
   		//Next create the Boolean display register dialog
   		AlertDialog.Builder writeBoolRegisterDialogBuilder = new AlertDialog.Builder(this);
        writeBoolRegisterDialogBuilder.setTitle("Write to Register")
   			.setIcon(android.R.drawable.ic_menu_edit) //TODO: Better icon here
   			.setMessage("Bits to write to Register");
        //TODO: Change to MultipleChoiceItems
        writeBoolRegisterDialogBuilder.setMultiChoiceItems(
        		new CharSequence[] {"Bit 0", "Bit 1", "Bit 2", "Bit 3", "Bit 4", "Bit 5", "Bit 6", "Bit 7", "Bit 8", "Bit 9", "Bit 10", "Bit 11", "Bit 12", "Bit 13", "Bit 14", "Bit 15"},
        		new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
        		new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						return;  //Don't need to do anything here, just need a blank 						
					}
				}
        	)
        	.setPositiveButton("Write", new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
   				//TODO: Turn array of booleans into a value and write it
   					dialog.dismiss();
   				}
   				
   			})
   			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int which) {
   					dialog.dismiss();
   				}
   				
   			})
   			.setCancelable(true);
       //create the dialog
        writeBoolRegisterDialog = writeBoolRegisterDialogBuilder.create();
   		
        
        
   		//Finally we create the Boolean display for Coils dialog
        AlertDialog.Builder writeBoolCoilDialogBuilder = new AlertDialog.Builder(this);
        writeBoolCoilDialogBuilder.setTitle("Write to Coil")
   			.setIcon(android.R.drawable.ic_menu_edit) //TODO: Better icon here
        .setSingleChoiceItems( new CharSequence[]  {"False", "True"}, 0, 
        		new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //TODO: write true or false to a holding coil
            	mbWriteValue = (boolean) (item != 0);
				Toast.makeText(getBaseContext(), "Writing Value: " + mbWriteValue, 5).show();

            	mb.writeValue(new ModbusMultiLocator (1, regType, writeRegOffset, dataType, 1)
					, mbWriteValue);
            	dialog.dismiss();
            	return;
            }
        });
        writeBoolCoilDialogBuilder.setCancelable(true);
        writeBoolCoilDialog = writeBoolCoilDialogBuilder.create();
   		
        
        switchRegType(regType);
        
        ipParameters = new IpParameters();
        ipParameters.setHost(hostIPaddress);
        ipParameters.setPort(hostPort);
        mbFactory = new ModbusTCPFactory ();
        mbTCPMaster = mbFactory.createModbusTCPMaster(ipParameters, true);
        exceptionHandler = new MbDroidMsgExceptionHandler();
        mbTCPMaster.setTimeout(15000);
        mbTCPMaster.setExceptionHandler(exceptionHandler);
        
        setModbusMultiLocator();
        // get a new Poll modbus object that we will pass to the thread starter
        mb = new PollModbus(mbTCPMaster, pollTime, mbLocator, mbList, pollHandler);  
       
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
        dataTypeMenuBuilder.setSingleChoiceItems(R.array.dataTypeItems, dataType, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Set datatype here
            	oldDataType = item +1;
            	setDataType(item + 1);
				
				dataTypeAlert.dismiss();
            }
        });
        dataTypeMenuBuilder.setCancelable(true);
        dataTypeAlert = dataTypeMenuBuilder.create();
        
        //Set values in UI elements according to shared preferences
        offset_editText.setText(Integer.toString(offset));
        registerLength.setText(Integer.toString(m_count));
        s.setSelection(regType - 1);  //double-check this vs. new ints for regType
        
        // Set up Listeners for
        // all the different changes on the main screen
        mbList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, 
            		int position, long id) {
            	
            	if (dataType == DataType.BINARY) {
            		if (regType == RegisterRange.COIL_STATUS) {
            			
            			int listSelector = -1;
            			String tempString = mbList.getAdapter().getItem( position ).toString();
            			if (tempString.equals("true"))
            				listSelector = 1;
            			else if (tempString.equals("false"))
            				listSelector = 0;
            			// This sets the correct value to be checked. - may need to do some 'unchecking' for the initial appearance of this dialog.
            			writeBoolCoilDialog.getListView().setItemChecked( listSelector, true );
            			
            		}
            		else if (regType == RegisterRange.HOLDING_REGISTER) {
            			//TODO: set the current register boolean to multi-choice items
            			char[] boolValues = ((String) mbList.getAdapter().getItem( position ) ).toCharArray();
            			for(int i=0; i<boolValues.length;i++){  
            				if (boolValues[i] == '0')                             
            					writeBoolRegisterDialog.getListView().setItemChecked(i, false);
            				else if (boolValues[i] == '1')
            					writeBoolRegisterDialog.getListView().setItemChecked(i, true);
            			}
            		}
            		
            	}
            	else if (regType == RegisterRange.HOLDING_REGISTER ) {
            		//Get the position in the listview's value, and then select it all
            		// this should make it easier for the user to not get confused which item was touched
            		( (EditText) textEntryNumericView.findViewById(R.id.write_value_number) ).setText( 
            				mbList.getAdapter().getItem( position ).toString() );
            		( (EditText) textEntryNumericView.findViewById(R.id.write_value_number) ).selectAll();
            		
            	}
            	else {
            		// if this isn't a write-able registerRange then just exit
            		return;
            	}
            	writeRegOffset = offset + (DataType.getRegisterCount(dataType) * position );
            	writeDialog.show();
              }
        	}
        
        );
        // Listener for spinner selection
        s.setOnItemSelectedListener( new OnItemSelectedListener() {
        		public void onItemSelected ( AdapterView<?> parent, View view, int pos, long id) {
    					regType = s.getSelectedItemPosition() + 1;        				
        				
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
        	
        	startPollingThread();
        	
        	return true;
        	
        case DISCONNECT:
        	killPollingThread();
        	return true;
        case DATATYPES:
            dataTypeAlert.show();
        	
            return true;
        }
        return false;
    }
    
    /*
     * startPollingThread
     * 
     * Determines if there is a connection, and if there is a polling thread object
     * already created.  If not, it connects and creates a new polling thread.
     * 
     */
    private void startPollingThread() {
    	if (mb == null) {
    		mb = new PollModbus(mbTCPMaster, pollTime, mbLocator, mbList, pollHandler);	
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
    	mbThread.start();
    	
    }
    
    /*
     * killPollingThread
     * 
     *  If there is a polling thread object then disconnect
     *  
     */
    private void killPollingThread(){
    	if (!mb.isConnected()){
    		Toast.makeText(this, "Not Connected to Anything!", 10).show();
    	}
    	else {
    		//mbThread.interrupt();
    		mb.disconnect();
    	}    	
    }
    
    /*
     * showMBDisconnected
     * Changes the modbus values list to 'disconnected' message
     */
    
    public void hideMBList() {
    	if ( mbList.isShown() ) {
    		mainLayout.removeView(mbList);
    		mainLayout.addView(notConnTextView);
    		//hideView(mbList);
    	}
    }
    
    public void showMBList () {
    	if ( notConnTextView.isShown() ) {
    		mainLayout.removeView(notConnTextView);
    		mainLayout.addView(mbList, listParams);
    		//showView(mbList);
    	}
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
     * 
     */
    private void setModbusMultiLocator () {
    	try {
        	mbLocator = new ModbusMultiLocator (1, regType, offset, dataType, m_count);
        }
        catch (Exception e) {
        	//TODO: Might want to add more better error handling here
        	Log.e(getClass().getSimpleName(), e.getMessage() );
        }
    }
    
    
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
     * Sets the datatype (i.e. Float, 16-bit decimal, etc.)
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
    	
    	mbList.setRegistersPerValue(DataType.getRegisterCount(this.dataType));
    	
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt("dataType", dataType);
		editor.commit();
		
		// Now we switch the write-dialog display to be correct when it is called.
		switch (dataType) {
		case DataType.BINARY:
			//TODO: setup a special View for registers when boolean display (after boolean is fixed)
			// and another for just a single boolean
			if (regType == RegisterRange.COIL_STATUS)
				writeDialog = writeBoolCoilDialog;
			else if (regType == RegisterRange.HOLDING_REGISTER)
				writeDialog = writeBoolRegisterDialog;
			break;
		case DataType.TWO_BYTE_INT_UNSIGNED:
		case DataType.FOUR_BYTE_INT_UNSIGNED:
		case DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED:
		case DataType.EIGHT_BYTE_INT_UNSIGNED:
		case DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED:
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setInputType((InputType.TYPE_CLASS_NUMBER));
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setRawInputType((InputType.TYPE_CLASS_NUMBER));
			writeDialog = writeNumericRegisterDialog;
			break;
		case DataType.TWO_BYTE_INT_SIGNED:
		case DataType.FOUR_BYTE_INT_SIGNED:
		case DataType.FOUR_BYTE_INT_SIGNED_SWAPPED:
		case DataType.EIGHT_BYTE_INT_SIGNED:
		case DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED:
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setInputType((InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_SIGNED));
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setRawInputType((InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
			writeDialog = writeNumericRegisterDialog;
			break;
		case DataType.EIGHT_BYTE_FLOAT:
		case DataType.EIGHT_BYTE_FLOAT_SWAPPED:
		case DataType.FOUR_BYTE_FLOAT_SWAPPED:
		case DataType.FOUR_BYTE_FLOAT:
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setInputType((InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL));
			( (TextView) textEntryNumericView.findViewById(R.id.write_value_number) ).setRawInputType((InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL));
			writeDialog = writeNumericRegisterDialog;
			break;
		}
    	
    }

    
    /**
     * 
     */
    public void switchRegType (int regType) {
    	
    	this.regType = regType;
    	
    	switch (regType) {
    	
    	case RegisterRange.COIL_STATUS:
    		mbList.setStartAddress(0000 + offset);
    		//if (dataType != DataType.BINARY) {
    			oldDataType = dataType;
        		setDataType(DataType.BINARY);
    		//}
    		break;
    	case RegisterRange.INPUT_STATUS:
    		mbList.setStartAddress(1000 + offset);
    		//if (dataType != DataType.BINARY) {
    			oldDataType = dataType;
        		setDataType(DataType.BINARY);
    		//}
    		break;
    	case RegisterRange.HOLDING_REGISTER:
    		mbList.setStartAddress(4000 + offset);
    		if (dataType <= 1 )
    			dataType = 2;
    		setDataType(dataType);
    		break;
    	case RegisterRange.INPUT_REGISTER:
    		mbList.setStartAddress(3000 + offset);
    		if (dataType <= 1 )
    			dataType = 2;
    		setDataType(dataType);
    	}
    	
    	if (mbLocator == null ) {
            setModbusMultiLocator();
    	}
    	mbLocator.setSlaveAndRange(new SlaveAndRange(1, regType) );
    	
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