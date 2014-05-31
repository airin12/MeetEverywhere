package com.meetEverywhere.bluetooth;

import com.meetEverywhere.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothChooseDeviceActivity extends Activity {

	private BluetoothDispatcher dispatcher;
	private BluetoothAdapter bluetoothAdapter;
	private static boolean discoveringServiceActivated = false;
	private ListView listView;
	private BluetoothListAdapter adapter;

	public void addToList(BluetoothConnection connection) {
		adapter.add(connection);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_choose_layout);
		dispatcher = BluetoothDispatcher.getInstance();

		if (BluetoothDispatcher.getInstance().getBluetoothListAdapter() == null) {
			BluetoothDispatcher.getInstance().setBluetoothListAdapter(
					new BluetoothListAdapter(getApplicationContext(), 0));
		}

		adapter = BluetoothDispatcher.getInstance().getBluetoothListAdapter();

		listView = (ListView) findViewById(R.id.chatListView);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

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
		if (discoveringServiceActivated == false) {
			discoveringServiceActivated = true;
			
			startService(new Intent(BluetoothChooseDeviceActivity.this, BluetoothDeviceSearchService.class));
		} else {
			setStartRefreshingImmediately(true);	
		}

		
		
	}
	
	public void showToast(final String text) {
		BluetoothDispatcher.getInstance().getHandler().post(new Runnable() {
			public void run() {
				Toast.makeText(dispatcher.getTempContextHolder(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void setStartRefreshingImmediately(boolean startRefreshingImmediately) {
		dispatcher.setFlagStartDiscoveryImmediateliy(startRefreshingImmediately);
	}

}

class BroadcastReceiverImpl extends BroadcastReceiver {
	private boolean flagDiscoveryExceptionOccured = false;
	private final BluetoothDispatcher dispatcher = BluetoothDispatcher
			.getInstance();
	private final BluetoothListAdapter adapter;
	
	public BroadcastReceiverImpl(BluetoothListAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// Pobierz object BluetoothDevice.
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			BluetoothConnection connection;
			try {
				if (dispatcher.getDevicesUnabledToConnect().contains(device)) {
//					bluetoothChooseDev.showToast("onReceive: " + device.getName() + "jest na liœcie blokowanych");
					return;
				}
//				bluetoothChooseDev.showToast("onReceive: " + device.getName());
				if (!dispatcher.getConnections().keySet().contains(device)) {
					connection = dispatcher
							.establishConnection(context, device);
					adapter.add(connection);
				} else {
					BluetoothConnection conn = dispatcher
							.getBluetoothConnectionForDevice(device);
					if (conn != null
							&& conn.getStatus().equals(
									BluetoothConnectionStatus.INACTIVE)) {
						dispatcher.activateConnection(context, conn, device,
								null);
					}
				}
			} catch (Exception e) {
				flagDiscoveryExceptionOccured = true;
				e.printStackTrace();
				dispatcher.getDevicesUnabledToConnect().add(device);
				return;
			}

			adapter.notifyDataSetChanged();
		}
		if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//			showToast("Wyszukiwanie zakoñczone");
			if(flagDiscoveryExceptionOccured){
				flagDiscoveryExceptionOccured = false;
				BluetoothAdapter.getDefaultAdapter().startDiscovery();
			}else{
				dispatcher.getDevicesUnabledToConnect().clear();
				dispatcher.setFlagDiscoveryFinished(true);
			}
		}
	}
	
	public void showToast(final String text) {
		BluetoothDispatcher.getInstance().getHandler().post(new Runnable() {
			public void run() {
				Toast.makeText(dispatcher.getTempContextHolder(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
