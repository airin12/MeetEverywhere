package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class OnlineChat implements Chat{

	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> list ;
	private Activity activity;
	private ListView listView;
	
	public OnlineChat(Activity activity){
		//text = (EditText) activity.findViewById(R.id.text_gen_chat);
		this.activity=activity;
		listView = (ListView) activity.findViewById(R.id.messagesList_gen_chat);
		list = new ArrayList<String>();
		putTagsIntoList();
		
	}
	
	public void sendMessage(String message, ListView listView) {
		list.add(message);
		putTagsIntoList();
		
	}
	
	private void putTagsIntoList() {
		listAdapter = new OnlineChatAdapter(activity, R.layout.online_chat_content_info
			,list) ;
		listView.setAdapter(listAdapter);

	}

}
