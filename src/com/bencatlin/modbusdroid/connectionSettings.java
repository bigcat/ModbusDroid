package com.bencatlin.modbusdroid;

import android.R.string;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class connectionSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	public static final String IP_ADDRESS_PREFERENCE = "IpAddress";
	public static final String PORT_PREFERENCE = "PortSetting";
	public static final String POLL_TIME_PREFERENCE = "PollTime";
	
    private EditTextPreference IPAddressPreference; 
    private EditTextPreference PortPreference;
    private EditTextPreference PollTimePreference;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.connection_settings);
        addPreferencesFromResource(R.xml.preferences);
        
        IPAddressPreference = (EditTextPreference)getPreferenceScreen().findPreference(IP_ADDRESS_PREFERENCE);
        IPAddressPreference.setSummary(getPreferenceScreen().getSharedPreferences().getString(IP_ADDRESS_PREFERENCE, "Set a new IP address"));
        
        PortPreference = (EditTextPreference)getPreferenceScreen().findPreference(PORT_PREFERENCE);
        PortPreference.setSummary(getPreferenceScreen().getSharedPreferences().getString(PORT_PREFERENCE, "Set a destination Port (default = 502)"));
        
        PollTimePreference = (EditTextPreference)getPreferenceScreen().findPreference(POLL_TIME_PREFERENCE);
        PollTimePreference.setSummary(getPreferenceScreen().getSharedPreferences().getString(POLL_TIME_PREFERENCE, "Set Poll Time in ms (0 = Poll Once)") + "ms");
        
        
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
        */
    }

    @Override
    protected void onResume() {
        super.onResume();   
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    } 

   @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        
    }

    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something when my value changes
        if (key.equals(IP_ADDRESS_PREFERENCE))
        {
        	IPAddressPreference.setSummary(sharedPreferences.getString(key, "Set an new IP address"));
        }
        else if (key.equals(PORT_PREFERENCE))
        {
        	PortPreference.setSummary(sharedPreferences.getString(key, "Set a destination Port (default = 502)"));
        }
        else if (key.equals(POLL_TIME_PREFERENCE))
        {
        	PollTimePreference.setSummary(sharedPreferences.getString(key, "Set Poll Time in ms (0 = Poll Once)") + "ms");
        }
    }
    
}