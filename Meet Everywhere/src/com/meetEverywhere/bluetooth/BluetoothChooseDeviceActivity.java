package com.meetEverywhere.bluetooth;

import java.util.LinkedList;
import java.util.List;

import com.meetEverywhere.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
		adapter.notifyDataSetChanged();

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
		// Sprawdü czy bluetooth jest zainstalowany.
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Brak urzπdzenia Bluetooth",
					Toast.LENGTH_SHORT).show();
		}
		// Uruchom modu≥ Bluetooth.
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBT, 0xDEADBEEF);
		}
		if (discoveringServiceActivated == false) {
			discoveringServiceActivated = true;
			dispatcher.setFlagDiscoveryFinished(true);
			startService(new Intent(BluetoothChooseDeviceActivity.this,
					BluetoothDeviceSearchService.class));
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
		dispatcher
				.setFlagStartDiscoveryImmediateliy(startRefreshingImmediately);
	}

}

class BroadcastReceiverImpl extends BroadcastReceiver {
	private final BluetoothDispatcher dispatcher = BluetoothDispatcher
			.getInstance();
	private final BluetoothListAdapter adapter;
	private int completedAsyncTasksCounter;
	private static List<BluetoothDevice> devicesFound = new LinkedList<BluetoothDevice>();

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

			if (!devicesFound.contains(device)) {
				devicesFound.add(device);
				showToast("onReceive: " + device.getName());

			}
		}
		if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			completedAsyncTasksCounter = 0;
			for (final BluetoothDevice dev : devicesFound) {
				showToast("Attempting to connect: " + dev.getName());
				(new AsyncTask<Context, Void, Void>() {
					BluetoothConnection connection;

					@Override
					protected Void doInBackground(Context... params) {
						Context tempContext = params[0];
						try {
							if (!dispatcher.getConnections().keySet()
									.contains(dev)) {
								connection = dispatcher.establishConnection(
										tempContext, dev);
							} else {
								BluetoothConnection conn = dispatcher
										.getBluetoothConnectionForDevice(dev);
								if (conn != null
										&& conn.getStatus()
												.equals(BluetoothConnectionStatus.INACTIVE)) {
									dispatcher.activateConnection(tempContext,
											conn, dev, null);
								}
							}
							return (Void) null;
						} catch (Exception e) {
							return (Void) null;
						}finally{
							increaseAsyncTaskCounter();
						}
					}

					@Override
					public void onPostExecute(Void result) {
						BluetoothDispatcher.getInstance().getHandler()
								.post(new Runnable() {
									public void run() {
										if (connection != null) {
											connection.launchConnectionThread();
											adapter.add(connection);
											adapter.notifyDataSetChanged();
										}
									}
								});
					}

				}).execute(dispatcher.getTempServiceContextHolder());
			}
		}
	}

	public synchronized void increaseAsyncTaskCounter(){
		completedAsyncTasksCounter++;
		if(completedAsyncTasksCounter == devicesFound.size()){
			devicesFound.clear();
			dispatcher.setFlagDiscoveryFinished(true);
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
