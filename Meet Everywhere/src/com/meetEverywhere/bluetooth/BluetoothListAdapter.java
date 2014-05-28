package com.meetEverywhere.bluetooth;

import java.util.LinkedHashMap;

import com.meetEverywhere.R;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothListAdapter extends ArrayAdapter<BluetoothConnection> {

	private final Context context;
	private final LinkedHashMap<BluetoothDevice, BluetoothConnection> connections;

	public BluetoothListAdapter(Context context, int resource) {
		super(context, R.layout.bluetooth_device_list_element);
		this.context = context;
		this.connections = BluetoothDispatcher.getInstance()
				.getConnections();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
			if(position >= connections.size()){
				return null;
			}
	    	LayoutInflater inflater = (LayoutInflater) context
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View row = inflater.inflate(R.layout.bluetooth_device_list_element, parent, false);
	        TextView textView = (TextView) row.findViewById(R.id.bluetooth_username);
	        ImageView imageView = (ImageView) row.findViewById(R.id.bluetooth_thumb);
	                                                                                                                    
	        textView.setText(getNickname(position));
	        imageView.setImageBitmap(getThumb(position));

	        
	        return row;
	        
	        
	}

	public String getNickname(int position) {
		return connections
				.get(((BluetoothDevice) connections.keySet().toArray()[position]))
				.getUser().getNickname();
	}

	public Bitmap getThumb(int position) {
		return connections
				.get(((BluetoothDevice) connections.keySet().toArray()[position]))
				.getUser().getPicture();
	}
	
}
