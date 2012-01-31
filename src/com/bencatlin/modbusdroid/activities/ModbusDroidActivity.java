/*****************************
 * 
 * ModbusDroid Activity for the Android 3.2+ version of the original app
 * 
 * 
 * @author Ben Catlin
 * 2012
 * 
 *****************************/

package com.bencatlin.modbusdroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ModbusDroidActivity extends Activity {

	
	private ActionBar actionBar;
	
	/*********** Activity lifecycle ****************/
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        actionBar = getActionBar();
	
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		
	/*	if (settings == null) {
			settings = PreferenceManager.getDefaultSharedPreferences(this);
		}
		getSharedSettings();
		*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 * We are overriding this because we need to explicitly
	 * disconnect if we are connected when the app stops
	 * 
	 * Consider using onPause/onResume and a service to allow a hidden app
	 * to continue polling and then re-display results.
	 */
	@Override
	public void onStop () {
		super.onStop();
		/*if (isFinishing() && mb.isConnected()) {
			mb.disconnect();
		}*/
	}
	
	/**
	 * 
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		/*if (isFinishing() && mb.isConnected()) {
			mb.disconnect();
		}*/
	}
	
	@Override 
	protected void onSaveInstanceState(Bundle outState) { 
		super.onSaveInstanceState(outState);
		/*if (mb.isConnected()) {
			outState.putBoolean("Connected", true);
		}
		else {
			outState.putBoolean("Connected", false);
		}*/
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	   	//final PollModbus data = mb;
	    //return mb;
		return 1;
	}
	
	
}
