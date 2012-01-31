package com.bencatlin.modbusdroid;

import com.bencatlin.modbusdroid.OldVersion.ModbusDroid;
import com.bencatlin.modbusdroid.activities.ModbusDroidActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends Activity {
	
	//Check for android 3.0 or higher and a large screen format, 
	private final boolean isHoneyCombOrNewer = android.os.Build.VERSION.SDK_INT 
			>= android.os.Build.VERSION_CODES.HONEYCOMB;
	
    /** Called when the activity is first created. 
     * 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        // Start the appropriate activity for the version running
        // this is for legacy support - the old version is for < 3.0.  
        // If we want, later we can add a separate activity for phones > 3.0
        // if there is a reason for that
        Intent startActivityIntent = null;
        if ( isHoneyCombOrNewer /*& extraLargeScreen*/ ) {
        	startActivityIntent = new Intent(this, ModbusDroidActivity.class);        	
        }
        else {
        	// Old Version
        	startActivityIntent = new Intent(this, ModbusDroid.class);
        }
        startActivity(startActivityIntent);
        finish();
    }
}