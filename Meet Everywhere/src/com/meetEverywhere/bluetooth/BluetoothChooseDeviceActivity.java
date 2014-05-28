package com.meetEverywhere.bluetooth;

import com.meetEverywhere.R;
import com.meetEverywhere.common.Configuration;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothChooseDeviceActivity extends Activity implements Runnable{

	private BluetoothAdapter bluetoothAdapter;
	private Configuration configuration;
	private boolean startRefreshingImmediately;
	private static BluetoothChooseDeviceActivity discoveringThread = null;
	private static BroadcastReceiverImpl broadcastReceiver = null;

	private ListView listView;
	private BluetoothListAdapter adapter;

	public void addToList(BluetoothConnection connection) {
		adapter.add(connection);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_choose_layout);
		configuration = Configuration.getInstance();
		
		if (BluetoothDispatcher.getInstance().getBluetoothListAdapter() == null) {
			BluetoothDispatcher.getInstance().setBluetoothListAdapter(
					new BluetoothListAdapter(getApplicationContext(), 0));
		}

		adapter = BluetoothDispatcher.getInstance().getBluetoothListAdapter();

		listView = (ListView) findViewById(R.id.chatListView);
		listView.setAdapter(adapter);
		getAdapter().notifyDataSetChanged();

		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BluetoothConnection conn = (BluetoothConnection) listView
						.getItemAtPosition(position);
				BluetoothDispatcher dispatcher = BluetoothDispatcher
						.getInstance();
				BluetoothDevice item = null;
				for (BluetoothDevice dev : dispatcher.getConnections().keySet()) {
					if (dispatcher.getConnections().get(dev).equals(conn)) {
						item = dev;
					}
				}

				Intent i = new Intent(getApplicationContext(),
						BluetoothChat.class);
				i.putExtra("device", item);
				startActivity(i);
			}
		});

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// SprawdŸ czy bluetooth jest zainstalowany.
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Brak urz¹dzenia Bluetooth",
					Toast.LENGTH_SHORT).show();
		}
		// Uruchom modu³ Bluetooth.
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBT, 0xDEADBEEF);
		}
		if(discoveringThread == null){
			discoveringThread = this;
			broadcastReceiver = new BroadcastReceiverImpl(this);
			setStartRefreshingImmediately(false);
			(new Thread(discoveringThread)).start();
		}else{
			setStartRefreshingImmediately(true);
		}
		
		IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(broadcastReceiver, ifilter);
		
	}

	@Override
	public void onPause() {
		try {
			this.unregisterReceiver(broadcastReceiver);
		} catch (IllegalArgumentException e) {
			// Nie rób nic.
		}
		super.onPause();
	}

	public BluetoothListAdapter getAdapter() {
		return adapter;
	}

	public void run() {
		long counter = 0;
		while(true){
			counter = 0;
			if (bluetoothAdapter.isDiscovering()) {
				bluetoothAdapter.cancelDiscovery();
			}
			bluetoothAdapter.startDiscovery();
			while(counter < configuration.getBluetoothMillisRefreshingTime()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter += 1000;
			}
			if(bluetoothAdapter.isDiscovering()){
				bluetoothAdapter.cancelDiscovery();
			}
			counter = 0;
			while(!isStartRefreshingImmediately() && counter < configuration.getBluetoothMillisTimeBetweenRefreshing()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter += 1000;
			}
			setStartRefreshingImmediately(false);
		}
	}
	

	public boolean isStartRefreshingImmediately() {
		return startRefreshingImmediately;
	}

	public void setStartRefreshingImmediately(boolean startRefreshingImmediately) {
		discoveringThread.startRefreshingImmediately = startRefreshingImmediately;
	}

}

class BroadcastReceiverImpl extends BroadcastReceiver {
	private final BluetoothChooseDeviceActivity bluetoothChooseDev;
	private final BluetoothDispatcher dispatcher = BluetoothDispatcher.getInstance();
	
	public BroadcastReceiverImpl(
			BluetoothChooseDeviceActivity bluetoothChooseDev) {
		this.bluetoothChooseDev = bluetoothChooseDev;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// Pobierz object BluetoothDevice.
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			BluetoothConnection connection;
			try {
				if (!dispatcher.getConnections().keySet()
						.contains(device)) {
					connection = dispatcher
							.establishConnection(context, device);
					bluetoothChooseDev.addToList(connection);
				}else{
					BluetoothConnection conn = dispatcher.getBluetoothConnectionForDevice(device);
					if(conn != null && conn.getStatus().equals(BluetoothConnectionStatus.INACTIVE)){
						dispatcher.activateConnection(context, conn, device, null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			bluetoothChooseDev.getAdapter().notifyDataSetChanged();

		}
	}
}
