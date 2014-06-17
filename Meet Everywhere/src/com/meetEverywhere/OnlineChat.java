package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class OnlineChat implements Chat{

	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> list ;
	private Activity activity;
	private ListView listView;
	private Handler handler;
	
	public OnlineChat(Activity activity,ArrayAdapter<String> adapter, Handler handler){
		//text = (EditText) activity.findViewById(R.id.text_gen_chat);
		this.activity=activity;
		listView = (ListView) activity.findViewById(R.id.messagesList_gen_chat);
		list = new ArrayList<String>();
		listAdapter=adapter;
		this.handler=handler;
		DAO dao = new DAO();
		dao.listenIncomingMessages(this);
		//putTagsIntoList();
		
	}
	
	public void sendMessage(String message, ListView listView) {
		//list.add(message);
		//listView.getAdapter();
		//putTagsIntoList();
		
	}
	
	public void messageReceived(String message){
		Message msg = new Message();
		msg.obj=message;
		handler.sendMessage(msg);
	}
	

}
