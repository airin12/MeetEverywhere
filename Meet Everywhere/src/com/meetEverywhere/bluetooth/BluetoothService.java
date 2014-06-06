package com.meetEverywhere.bluetooth;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class BluetoothService extends Service implements Runnable {

	private Handler handler;

	@SuppressLint("ShowToast")
	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Implementacja nie jest konieczna.
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {

		BluetoothDispatcher.getInstance().setTempServiceContextHolder(
				getApplicationContext());
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		// Sprawd� czy bluetooth jest zainstalowany.
		if (bluetoothAdapter == null) {
			showToast("Brak urz�dzenia Bluetooth!");
			return;
		}
		// Uruchom modu� Bluetooth.
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}

		// Zatrzymaj wyszukiwanie urz�dze�.
		// if (bluetoothAdapter.isDiscovering()) {
		bluetoothAdapter.cancelDiscovery();
		// }

		// Uruchom w�tek us�ugi, kt�ry akceptuje po��czenia.
		(new Thread(this)).start();

	}

	public void run() {
		while (true) {
			showToast("Us�uga Bluetooth uruchomiona.");
			BluetoothDispatcher dispatcher = BluetoothDispatcher.getInstance();
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			BluetoothServerSocket serverSocket = null;
			BluetoothSocket socket = null;
			try {
				serverSocket = adapter
						.listenUsingInsecureRfcommWithServiceRecord(
								"MeetEverywhere", dispatcher.getUUID());

				while ((socket = serverSocket.accept()) != null) {

					final BluetoothSocket tempSocket = socket;
					final BluetoothDispatcher tempDispatcher = dispatcher;
					handler.post(new Runnable() {
						public void run() {
							BluetoothConnection connection = null;
							try {
								connection = tempDispatcher.getConnections()
										.get(tempSocket.getRemoteDevice());
								if (connection != null) {
									tempDispatcher.activateConnection(
											tempDispatcher
													.getTempContextHolder(),
											connection, tempSocket
													.getRemoteDevice(),
											tempSocket);
									showToast("Przywr�cono po��czenie z: "
											+ connection.getUser()
													.getNickname());
								} else {

									connection = new BluetoothConnection(
											getApplicationContext(), tempSocket);
									connection.launchConnectionThread();
									tempDispatcher.addConnection(null,
											tempSocket.getRemoteDevice(),
											connection);
									if (tempDispatcher
											.getBluetoothListAdapter() == null) {
										tempDispatcher
												.setBluetoothListAdapter(new BluetoothListAdapter(
														getApplicationContext(),
														0));
									}
									tempDispatcher.getBluetoothListAdapter()
											.add(connection);
									showToast("Nawi�zano po��czenie z: "
											+ connection.getUser()
													.getNickname());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

				}

			} catch (IOException e) {
				showToast("Us�uga Bluetooth zatrzymana, poczekaj na wznowienie!");
			} finally {
				try {
					if (serverSocket != null) {
						serverSocket.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				showToast("B��d podczas wznawiania us�ugi Bluetooth!");
			}
		}

	}

	public void showToast(final String text) {
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
