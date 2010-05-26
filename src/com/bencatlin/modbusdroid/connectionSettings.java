package com.bencatlin.modbusdroid;

import android.R.string;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class connectionSettings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.connection_settings);
        addPreferencesFromResource(R.xml.preferences);
        
        
        /*final EditText IP_address_entry = (EditText) findViewById(R.id.IP_address_entry);
        final Button Cancel = (Button) findViewById(R.id.Cancel);
        final Button Save = (Button) findViewById(R.id.Save);
        final Button SaveConnect = (Button) findViewById(R.id.SaveConnect);
        final EditText Port_Entry = (EditText) findViewById(R.id.Port_entry);
        final EditText Poll_Entry = (EditText) findViewById(R.id.Poll_entry);
        
        SharedPreferences settings = getSharedPreferences(this.getString(R.string.modbus_prefs), 0);
        String IpAddress = settings.getString("IpAddress", "10.0.2.2");
        int PortSetting = settings.getInt("PortEntry", 502);
        int PollSetting = settings.getInt("PollTime", 30);
        IP_address_entry.setText(IpAddress);
        Port_Entry.setText(Integer.toString(PortSetting) );
        Poll_Entry.setText(Integer.toString(PollSetting));
        
        Save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences settings = getSharedPreferences(v.getContext().getString(R.string.modbus_prefs), 0);
            	SharedPreferences.Editor editor = settings.edit();
                editor.putString("IpAddress", IP_address_entry.getText().toString());
                editor.putInt("Port", Integer.parseInt(Port_Entry.getText().toString()));
           		editor.putInt("PollTime", Integer.parseInt(Poll_Entry.getText().toString()));
                
           		// Don't forget to commit your edits!!!
                editor.commit();
            }
        });
        
        SaveConnect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences settings = getSharedPreferences(v.getContext().getString(R.string.modbus_prefs), 0);
            	SharedPreferences.Editor editor = settings.edit();
                editor.putString("IpAddress", IP_address_entry.getText().toString());
                editor.putInt("Port", Integer.parseInt(Port_Entry.getText().toString()));
           		editor.putInt("PollTime", Integer.parseInt(Poll_Entry.getText().toString()));
                
           		// Don't forget to commit your edits!!!
                editor.commit();
                finish();
            }
        });
        
        Cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        }); */
    }
}