package com.bencatlin.modbusdroid;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

    /**
     * Draws the register table dynamically
     * @author Ben Catlin
     *
     */    
   class ModbusListView extends ListView {

	   	private EfficientAdapter modbusAdapter;
	    static private Object[] modbusResponse;
	    private Object[] oldValues;
    	private int regStartAddress;
	    
	    private String[] modbusDisplayValues;
    	private String[] modbusDisplayAddresses;
    	public EfficientAdapter adapter = null;
    	
    	/** Constructor
    	 * @param context
    	 * @param modbusResponse - data passed to list adapter
    	 */
    	public ModbusListView (Context context, Object[] modbusResponse) {
    		super(context);
    		this.modbusResponse = modbusResponse;
    		this.oldValues = oldValues = modbusResponse.clone();
    		// set the adapter immediately
    		adapter  = new EfficientAdapter(context);
    		this.setAdapter(adapter);
    	}
    	
    	public void setStartAddress (int address) {
    		regStartAddress = address;
    		adapter.notifyDataSetChanged();
    	}
    	
    	public int getStartAddress () {
    		return regStartAddress;
    	}
    	
    	/*
    	 * This is blantantly stolen from the example API code
    	 * and modified for my purposes
    	 */
    	public class EfficientAdapter extends BaseAdapter {
            private LayoutInflater mInflater;
            

            public EfficientAdapter(Context context) {
                // Cache the LayoutInflate to avoid asking for a new one each time.
                mInflater = LayoutInflater.from(context);

                // Icons bound to the rows.
                //mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_1);
                //mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
            }

            /**
             * The number of items in the list is determined by the number of speeches
             * in our array.
             *
             * @see android.widget.ListAdapter#getCount()
             */
            public int getCount() {
                return modbusResponse.length; //DATA.length;
                //TODO Fix later so that whatever data passed in is correctly Handled
            }

            /**
             * Since the data comes from an array, just returning the index is
             * sufficient to get at the data. If we were using a more complex data
             * structure, we would return whatever object represents one row in the
             * list.
             *
             * @see android.widget.ListAdapter#getItem(int)
             */
            public Object getItem(int position) {
                return position;
            }

            /**
             * Use the array index as a unique id.
             *
             * @see android.widget.ListAdapter#getItemId(int)
             */
            public long getItemId(int position) {
                return position;
            }

            /**
             * Make a view to hold each row.
             *
             * @see android.widget.ListAdapter#getView(int, android.view.View,
             *      android.view.ViewGroup)
             */
            public View getView(int position, View convertView, ViewGroup parent) {
                // A ViewHolder keeps references to children views to avoid unneccessary calls
                // to findViewById() on each row.
                ViewHolder holder;

                // When convertView is not null, we can reuse it directly, there is no need
                // to reinflate it. We only inflate a new View when the convertView supplied
                // by ListView is null.
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.modbus_value_row, null);

                    // Creates a ViewHolder and store references to the two children views
                    // we want to bind data to.
                    holder = new ViewHolder();
                    holder.address = (TextView) convertView.findViewById(R.id.mod_Address);
                    holder.value = (TextView) convertView.findViewById(R.id.mod_Value);

                    convertView.setTag(holder);
                } else {
                    // Get the ViewHolder back to get fast access to the TextView
                    // and the ImageView.
                    holder = (ViewHolder) convertView.getTag();
                }

                // Bind the data efficiently with the holder.
                //Log.i(getClass().getSimpleName(), "Set values for list row");
                holder.value.setText( (String) modbusResponse[position].toString());
                holder.address.setText( Integer.toString(regStartAddress + position));
                
                return convertView;
            }

            class ViewHolder {
                TextView address;
                TextView value;
            }
        }

    	
    	public void updateData (Object [] values ) {
    		if ( (values.getClass() != modbusResponse.getClass() ) || !(Arrays.equals(values, modbusResponse)) ) {
    			modbusResponse = null;
    			modbusResponse = values.clone();
    			adapter.notifyDataSetChanged();
    		}
    	}
    	
    }