package com.meetEverywhere.bluetooth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.JSONException;

import com.meetEverywhere.R;
import com.meetEverywhere.common.messages.InvitationMessage;
import com.meetEverywhere.common.messages.Message;
import com.meetEverywhere.common.messages.TextMessage;
import com.meetEverywhere.common.User;
import com.meetEverywhere.common.messages.MessageACK;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class BluetoothConnection implements Runnable {

	private final BluetoothDispatcher dispatcher = BluetoothDispatcher
			.getInstance();
	private final ArrayAdapter<TextMessage> messagesAdapter;
	private final int MAX_WAIT_PERIOD = 3000;
	private final int PERIOD_BEETWEEN_RECONNECT_ATTEMPT = 30000;
	private BluetoothSocket bluetoothSocket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private BluetoothConnectionStatus status;
	private User user;
	private Handler handler;
	private MessageACK messageACK = null;

	public BluetoothConnection(Context context, BluetoothSocket socket)
			throws IOException, ClassNotFoundException, InterruptedException,
			JSONException {
		bluetoothSocket = socket;
		Log.i("bluetoothConnection", "attempt to connect");

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		Thread.sleep(10);

		inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream.writeObject(dispatcher.getOwnData().serializeObjectToJSON(
				false));

		user = User
				.deserializeObjectFromJSON((String) inputStream.readObject());

		user.setBluetoothConnection(this);

		Log.i("bt connection", "constructor");
		if (user.getMessagesArrayAdapter() == null) {
			user.setMessagesArrayAdapter(new ArrayAdapter<TextMessage>(context,
					R.layout.bluetooth_array_adapter));
		}
		Log.i("bt connection", "arrayAdapter created");
		messagesAdapter = user.getMessagesArrayAdapter();

		handler = dispatcher.getHandler();
		Log.i("bluetoothConnection", "connection activated");
	}

	public void launchConnectionThread() {
		setStatus(BluetoothConnectionStatus.ACTIVE);
		(new Thread(this)).start();
	}

	public void setReconnectedSocket(BluetoothSocket socket)
			throws IOException, InterruptedException, ClassNotFoundException,
			JSONException {
		bluetoothSocket = socket;

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		Thread.sleep(10);

		inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream.writeObject(dispatcher.getOwnData().serializeObjectToJSON(
				false));

		User user = User.deserializeObjectFromJSON((String) inputStream
				.readObject());
		updateUserData(user);
		setStatus(BluetoothConnectionStatus.ACTIVE);
	}

	private void updateUserData(User user) {
		this.user.setHashTags(user.getHashTags());
		this.user.setDescription(user.getDescription());
		this.user.setPicture(user.getPicture());
		// this.user.setUserToken(user.getUserToken());
	}

	public BluetoothSocket getBluetoothSocket() {
		return bluetoothSocket;
	}

	public User getUser() {
		return user;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public ArrayAdapter<TextMessage> getMessagesAdapter() {
		return messagesAdapter;
	}

	public void run() {
		Object receivedObject;
		while (true) {
			try {
				while ((receivedObject = inputStream.readObject()) != null) {
					if (receivedObject instanceof Message) {
						addMessage((Message) receivedObject);
					}
					if (receivedObject instanceof MessageACK) {
						if (((MessageACK) receivedObject).equals(messageACK)) {
							messageACK = null;
						}
					}

				}
			} catch (Exception e) {
				status = BluetoothConnectionStatus.INACTIVE;
			}

			handler.post(new Runnable() {
				public void run() {
					Toast.makeText(dispatcher.getTempContextHolder(),
							"Utracono po��czenie z: " + user.getNickname(),
							Toast.LENGTH_SHORT).show();
				}
			});

			while (getStatus().equals(BluetoothConnectionStatus.INACTIVE)) {
				try {
					Thread.sleep(PERIOD_BEETWEEN_RECONNECT_ATTEMPT);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.i("WYJATEK",
							"InterruptedException podczas oczekiwania na aktywacje");
				}
			}

		}
	}

	public void addMessage(final Message message) throws IOException,
			InterruptedException, ClassNotFoundException {
		if (message.isLocal()) {
			if (status.equals(BluetoothConnectionStatus.INACTIVE)) {
				throw new IOException("Connection inactive");
			}
			synchronized (this) {
				messageACK = new MessageACK(message.hashCode());
				outputStream.writeObject(message);
				int maxWaitingPeriod = MAX_WAIT_PERIOD;

				while (messageACK != null && maxWaitingPeriod > 0) {
					Thread.sleep(100);
					maxWaitingPeriod -= 100;
				}
				if (messageACK != null) {
					status = BluetoothConnectionStatus.INACTIVE;
					throw new IOException("Acknowledge not acquired!");
				}
			}
		} else {
			outputStream.writeObject(new MessageACK(message.hashCode()));
		}

        if(!message.isLocal()) {
            user.processIncomingMessage(message);
        }
	}

	public BluetoothConnectionStatus getStatus() {
		return status;
	}

	public void setStatus(BluetoothConnectionStatus status) {
		this.status = status;
	}

}
