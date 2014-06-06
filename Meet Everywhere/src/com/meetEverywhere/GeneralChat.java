package com.meetEverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothChat;
import com.meetEverywhere.common.Configuration;

public class GeneralChat extends Activity{
	private Chat chat;
	private EditText text;
	private ListView listView;
	private OnlineChat onlineChat;
	private BluetoothChat bluetoothChat;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_chat_layout);
		
		onlineChat = new OnlineChat(this);
		//bluetoothChat = null;
		
		text = (EditText) findViewById(R.id.text_gen_chat);
		listView = (ListView) findViewById(R.id.messagesList_gen_chat);
		//listView.setAdapter(messages);

	}
	
	public void sendMessage(View view){
		setSource();
		chat.sendMessage("ja:"+text.getText().toString(),listView);
	}

	private void setSource() {
		
		if(isOnline())
			chat=onlineChat;
		else
			chat=bluetoothChat;
		
	}
	
	private boolean isOnline(){
		Configuration config = Configuration.getInstance();
		//return config.isApplicationOnline();
		return true;
	}
}
