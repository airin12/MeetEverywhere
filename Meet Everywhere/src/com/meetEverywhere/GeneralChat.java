package com.meetEverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothChat;
import com.meetEverywhere.common.Configuration;

public abstract class GeneralChat extends Activity{
	private Chat chat;
	private EditText text;
	private ListView listView;
	private OnlineChat onlineChat;
	private BluetoothChat bluetoothChat;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_chat_layout);
		
		onlineChat = new OnlineChat();
		bluetoothChat = new BluetoothChat();
		
		text = (EditText) this.findViewById(R.id.text);
		listView = (ListView) findViewById(R.id.messagesList);
		//listView.setAdapter(messages);

	}
	
	public void sendMessage(Bundle bundle){
		setSource();
		chat.sendMessage(text.getText().toString(),listView);
	}

	private void setSource() {
		Configuration config = Configuration.getInstance();
		if(config.isApplicationOnline())
			chat=onlineChat;
		else
			chat=bluetoothChat;
		
	}
}
