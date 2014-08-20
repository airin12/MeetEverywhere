package com.meetEverywhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.TextMessage;
import com.meetEverywhere.common.User;

public class GeneralChat extends Activity{
	private EditText text;
	private ListView listView;
	private User user;
	private ArrayAdapter<TextMessage> messagesarrayAdapter;
	private Configuration config;
	private Button sendButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_chat_layout);
		
		config = Configuration.getInstance();
		
		text = (EditText) findViewById(R.id.text_gen_chat);
		listView = (ListView) findViewById(R.id.messagesList_gen_chat);
		sendButton = (Button) findViewById(R.id.send_button);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		user = BluetoothDispatcher.getInstance().getTempUserHolder();
	
		user.setSendButton(sendButton);
		user.setChatFocused(true);
		
		if(user.getMessagesArrayAdapter() == null){
			user.setMessagesArrayAdapter(new ArrayAdapter<TextMessage>(this, R.layout.bluetooth_array_adapter));
		}
		messagesarrayAdapter = user.getMessagesArrayAdapter();
		listView.setAdapter(messagesarrayAdapter);
		
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		user.setChatFocused(false);
	}
	
	
	public void sendMessage(View view){
	
		String message = text.getText().toString();
		if(!message.equals("")){
			user.sendMessage(new TextMessage(message, config.getUser().getNickname(), config.getUser().getUserID(), user.getUserID()));
		}
		text.setText("");
	}

}
