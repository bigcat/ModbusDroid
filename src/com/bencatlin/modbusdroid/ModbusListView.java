package com.bencatlin.modbusdroid;

import android.app.ListActivity;

    /**
     * Draws the register table dynamically
     * @author bcatlin
     *
     */    
   class ModbusListView extends ListActivity {

    	private String[] modbusDisplayValues;
    	
    	
    	public ModbusListView ( String[] modbusDisplayValues ){
    		this.modbusDisplayValues = modbusDisplayValues;
    	}
    }