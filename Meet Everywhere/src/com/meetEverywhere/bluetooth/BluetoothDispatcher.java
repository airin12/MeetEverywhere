package com.meetEverywhere.bluetooth;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.meetEverywhere.MyUsersListAdapter;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.TextMessage;
import com.meetEverywhere.common.User;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Klasa BluetoothDispatcher jest g³ównym komponentem modu³u do obs³ugi
 * Bluetooth. Jest Singletonem, stanowi kontener na aktywne po³¹czenia, za jego
 * pomoc¹ s¹ przekazywane wartoœci, których nie mo¿na przekazaæ u¿ywaj¹c Intent,
 * podczas otwierania nowego Activity (pola nazwane: (...)Holder).
 * 
 * @author marekmagik
 * 
 */
public class BluetoothDispatcher {

	private static BluetoothDispatcher instance;
	private Handler handler;
	private Context tempContextHolder;
	private BluetoothSocket tempSocketHolder;
	private Context tempServiceContextHolder;
	private User tempUserHolder;
	//private List<BluetoothDevice> devicesUnabledToConnect;
	private boolean flagDiscoveryFinished;
	private final UUID ownUUID = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private final LinkedHashMap<BluetoothDevice, BluetoothConnection> connections;
	private BluetoothListAdapter bluetoothListAdapter = null;
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private Configuration configuration;
	private boolean discoveringServiceActivated = false;

	private BluetoothDispatcher() {
		connections = new LinkedHashMap<BluetoothDevice, BluetoothConnection>();
		configuration = Configuration.getInstance();
		//devicesUnabledToConnect = new LinkedList<BluetoothDevice>();
		setFlagDiscoveryFinished(false);
	}

	public static BluetoothDispatcher getInstance() {
		if (instance == null) {
			instance = new BluetoothDispatcher();
		}
		return instance;
	}

	public ArrayAdapter<TextMessage> getArrayAdapterForDevice(Context context,
			BluetoothDevice device) {
		if (!connections.containsKey(device)) {
			try {
				establishConnection(context, device);
			} catch (IOException e) {
				return null;
			} catch (ClassNotFoundException e) {
				return null;
			} catch (InterruptedException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		return connections.get(device).getMessagesAdapter();
	}

	public BluetoothConnection getBluetoothConnectionForDevice(
			BluetoothDevice device) {
		return connections.get(device);
	}

	public synchronized BluetoothConnection establishConnection(
			Context context, BluetoothDevice device) throws IOException,
			ClassNotFoundException, Exception, InterruptedException {
		BluetoothSocket socket = tempSocketHolder;
		tempSocketHolder = null;

		if (socket == null) {
			bluetoothAdapter.cancelDiscovery();
			socket = device.createInsecureRfcommSocketToServiceRecord(ownUUID);
			socket.connect();
		}
		Log.i("socket", " socket pod³¹czony");
		BluetoothConnection connection = new BluetoothConnection(context,
				socket);

		showToast("Nawi¹zano po³¹czenie z: "
				+ connection.getUser().getNickname());
		addConnection(context, device, connection);
		return connection;
	}

	public synchronized void activateConnection(Context context,
			BluetoothConnection connection, BluetoothDevice device,
			BluetoothSocket socket) throws Exception {
		if (socket == null) {
			bluetoothAdapter.cancelDiscovery();
			socket = device.createInsecureRfcommSocketToServiceRecord(ownUUID);
			socket.connect();
		}
		connection.setReconnectedSocket(socket);
		showToast("Przywrócono po³¹czenie z: "
				+ connection.getUser().getNickname());

	}

	public void deactivateConnection(BluetoothConnection connection,
			BluetoothSocket socket) {

	}

	public void addConnection(Context context, BluetoothDevice device,
			BluetoothConnection connection) {
		connections.put(device, connection);
	}

	public void deleteConnection(BluetoothConnection connection) {
		connections.remove(connection.getBluetoothSocket().getRemoteDevice());
	}

	public LinkedHashMap<BluetoothDevice, BluetoothConnection> getConnections() {
		return connections;
	}

	public User getOwnData() {
		return configuration.getUser();
	}

	public UUID getUUID() {
		return ownUUID;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Handler getHandler() {
		return handler;
	}

	public BluetoothSocket getTempSocketHolder() {
		return tempSocketHolder;
	}

	public void setTempSocketHolder(BluetoothSocket tempSocketHolder) {
		this.tempSocketHolder = tempSocketHolder;
	}

	public Context getTempContextHolder() {
		return tempContextHolder;
	}

	public void setTempContextHolder(Context tempContextHolder) {
		this.tempContextHolder = tempContextHolder;
	}

	public BluetoothListAdapter getBluetoothListAdapter() {
		return bluetoothListAdapter;
	}

	public void setBluetoothListAdapter(
			BluetoothListAdapter bluetoothListAdapter) {
		this.bluetoothListAdapter = bluetoothListAdapter;
	}
	
	public boolean isFlagDiscoveryFinished() {
		return flagDiscoveryFinished;
	}

	public synchronized void setFlagDiscoveryFinished(boolean flagDiscoveryFinished) {
		this.flagDiscoveryFinished = flagDiscoveryFinished;
	}

	public void setTempServiceContextHolder(Context applicationContext) {
		this.tempServiceContextHolder = applicationContext;
	}

	public Context getTempServiceContextHolder() {
		return tempServiceContextHolder;
	}

	public void showToast(final String text) {
		handler.post(new Runnable() {

			public void run() {
				Toast.makeText(tempContextHolder, text, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	public boolean isDiscoveringServiceActivated() {
		return discoveringServiceActivated;
	}

	public void setDiscoveringServiceActivated(
			boolean discoveringServiceActivated) {
		this.discoveringServiceActivated = discoveringServiceActivated;
	}

	public void notifyBluetoothDevicesChanged() {
		MyUsersListAdapter adapter = configuration
				.getUsersFoundByOwnTagsAdapter();
		if (adapter != null) {
			adapter.notifyBluetoothDevicesChanged();
		}
	}

	public User getTempUserHolder() {
		return tempUserHolder;
	}

	public void setTempUserHolder(User tempUserHolder) {
		this.tempUserHolder = tempUserHolder;
	}
}
