package com.meetEverywhere.bluetooth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.meetEverywhere.R;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.TextMessage;
import com.meetEverywhere.common.User;
import com.meetEverywhere.common.TextMessageACK;

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
	private BluetoothSocket bluetoothSocket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private BluetoothConnectionStatus status;
	private User user;
	private Handler handler;
	private TextMessageACK messageACK = null;

	public BluetoothConnection(Context context, BluetoothSocket socket)
			throws IOException, ClassNotFoundException, InterruptedException {
		messagesAdapter = new ArrayAdapter<TextMessage>(context,
				R.layout.bluetooth_array_adapter);
		bluetoothSocket = socket;

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		Thread.sleep(10);

		inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream
				.writeObject(BluetoothDispatcher.getInstance().getOwnData());
		user = (User) inputStream.readObject();
		handler = dispatcher.getHandler();
		setStatus(BluetoothConnectionStatus.ACTIVE);
		(new Thread(this)).start();
	}

	public void setReconnectedSocket(BluetoothSocket socket)
			throws IOException, InterruptedException, ClassNotFoundException {
		bluetoothSocket = socket;

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		Thread.sleep(10);

		inputStream = new ObjectInputStream(socket.getInputStream());

		outputStream
				.writeObject(BluetoothDispatcher.getInstance().getOwnData());
		user = (User) inputStream.readObject();
		setStatus(BluetoothConnectionStatus.ACTIVE);
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
					if (receivedObject instanceof TextMessage) {
						addMessage((TextMessage) receivedObject);
					}
					if (receivedObject instanceof TextMessageACK) {
						if (((TextMessageACK) receivedObject)
								.equals(messageACK)) {
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
							"Utracono po³¹czenie z: " + user.getNickname(),
							Toast.LENGTH_SHORT).show();
				}
			});

			while (getStatus().equals(BluetoothConnectionStatus.INACTIVE)) {
				try {
					Thread.sleep(Configuration.getInstance()
							.getBluetoothMillisToReconnectAttempt());
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.i("WYJATEK",
							"InterruptedException podczas oczekiwania na aktywacje");
				}
			}

		}
	}

	public void addMessage(final TextMessage message) throws IOException,
			InterruptedException, ClassNotFoundException {
		if (message.isLocal()) {
			synchronized (this) {
				messageACK = new TextMessageACK(message.hashCode());
				outputStream.writeObject(message);
				int maxWaitingPeriod = 2000;
				while (messageACK != null && maxWaitingPeriod > 0) {
					Thread.sleep(30);
					maxWaitingPeriod -= 30;
				}
				if (messageACK != null) {
					throw new IOException("Acknowledge not acquired!");
				}
			}
		} else {
			outputStream.writeObject(new TextMessageACK(message.hashCode()));
		}

		handler.post(new Runnable() {
			public void run() {
				messagesAdapter.add(message);
			}
		});

	}

	public BluetoothConnectionStatus getStatus() {
		return status;
	}

	public void setStatus(BluetoothConnectionStatus status) {
		this.status = status;
	}

}
