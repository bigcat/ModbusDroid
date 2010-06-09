package com.bencatlin.modbusdroid;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

    /**
     * Draws the register table dynamically
     * @author bcatlin
     *
     */    
   class ModbusListView extends ListView {

	    private Object modbusResponse;
    	private String[] modbusDisplayValues;
    	private String[] modbusDisplayAddresses;
    	
    	public ModbusListView (Context context, Object modbusResponse ){
    		super(context);
    		this.modbusResponse = modbusResponse;
    		//this.modbusDisplayValues = modbusDisplayValues;
    	}
    	
    	
    	/*
    	 * This is blantantly stolen from the example API code
    	 * and modified for my purposes
    	 */
    	private static class EfficientAdapter extends BaseAdapter {
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
                return 1; //DATA.length;
                //TODO Fix later so that whatever data passed in is correctly 
            }

            /**
             * Since the data comes from an array, just returning the index is
             * sufficent to get at the data. If we were using a more complex data
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
                //holder.text.setText(DATA[position]);
                //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
                
                
                return convertView;
            }

            static class ViewHolder {
                TextView address;
                TextView value;
            }
        }
    	
    	
    	
    	
    }