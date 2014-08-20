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


/*
=======
package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
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
	private ArrayList<String> messages;
	private ArrayAdapter<String> listAdapter;
	private String nick;
	
	private final Handler handler = new Handler(){
		  @Override
		  public void handleMessage(Message msg) {
			  addMessage(nick+":"+(String)msg.obj);
			  putTagsIntoList();
			  listView.setSelection(messages.size()-1);
		  }
		};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_chat_layout);
		
		messages=new ArrayList<String>();
		
		//bluetoothChat = null;
		nick=getIntent().getStringExtra("nick");
		
		text = (EditText) findViewById(R.id.text_gen_chat);
		listView = (ListView) findViewById(R.id.messagesList_gen_chat);
		listAdapter = new OnlineChatAdapter(this, R.layout.online_chat_content_info
				,messages) ;
		
		
		
		onlineChat = new OnlineChat(this,listAdapter,handler);
		listView.setAdapter(listAdapter);
		//listView.setAdapter(messages);

	}
	
	public void sendMessage(View view){
		setSource();
		String message = text.getText().toString();
		chat.sendMessage(message,listView);
		text.setText("");
		addMessage("ja:"+message);
		putTagsIntoList();	
		listView.setSelection(messages.size()-1);
	}

	private void setSource() {
		
		if(isOnline())
			chat=onlineChat;
		else
			chat=bluetoothChat;
		
	}
	
	public void addMessage(String msg){
		messages.add(msg);
	}
	
	
	private boolean isOnline(){
		Configuration config = Configuration.getInstance();
		//return config.isApplicationOnline();
		return true;
	}
	
	private void putTagsIntoList() {
		listAdapter = new OnlineChatAdapter(this, R.layout.online_chat_content_info
			,messages) ;
		listView.setAdapter(listAdapter);

	}
}
>>>>>>> branch 'master' of https://github.com/airin12/MeetEverywhere
*/