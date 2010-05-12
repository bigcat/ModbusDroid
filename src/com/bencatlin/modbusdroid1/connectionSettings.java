package com.bencatlin.modbusdroid1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class connectionSettings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_settings);
	
        final Button Connect = (Button) findViewById(R.id.Connect);
        final EditText IP_address_entry = (EditText) findViewById(R.id.IP_address_entry);
        final Button Cancel = (Button) findViewById(R.id.Cancel);
        final Button Save = (Button) findViewById(R.id.Save);
        
        SharedPreferences settings = getSharedPreferences(this.getString(R.string.modbus_prefs), 0);
        
        Save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        Cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
    }
}