package com.meetEverywhere.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.meetEverywhere.common.Configuration;

public class BluetoothDeviceSearchService extends Service implements Runnable {

	private BluetoothDispatcher dispatcher;
	private BluetoothAdapter bluetoothAdapter;
	private Configuration configuration;
	private BluetoothListAdapter adapter;
	private BroadcastReceiverImpl broadcastReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// Implementacja nie jest konieczna.
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		dispatcher = BluetoothDispatcher.getInstance();
		configuration = Configuration.getInstance();
		adapter = BluetoothDispatcher.getInstance().getBluetoothListAdapter();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		broadcastReceiver = new BroadcastReceiverImpl(adapter);
		IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		ifilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(broadcastReceiver, ifilter);
		(new Thread(this)).start();

	}

	public void run() {
		long counter = 0;
		while (true) {
			dispatcher.setFlagDiscoveryFinished(false);

			
			if (bluetoothAdapter != null) {
				bluetoothAdapter.startDiscovery();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!bluetoothAdapter.isDiscovering()) {
					continue;
				}
			}
			while (!dispatcher.isFlagDiscoveryFinished()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			counter = 0;
			while (counter < configuration
					.getBluetoothSecsTimeBetweenRefreshing() * 1000) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter += 1000;
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
