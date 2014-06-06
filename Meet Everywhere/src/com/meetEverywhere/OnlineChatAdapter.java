package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.meetEverywhere.common.Tag;

public class OnlineChatAdapter extends ArrayAdapter<String>{
	  private ArrayList<String> messages;
	  private Activity activity;
	 
	  public OnlineChatAdapter(Context context, int textViewResourceId,
	    ArrayList<String> contactList) {
	   super(context, textViewResourceId, contactList);
	   this.activity=(Activity) context;
	   this.messages = new ArrayList<String>();
	   this.messages.addAll(contactList); 
	  }
	 
	  private class ViewHolder {
	   TextView msg;
	  }
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	 
	   ViewHolder holder = null;
	   Log.v("ConvertView", String.valueOf(position));
	 
	   if (convertView == null) {
	   LayoutInflater vi = (LayoutInflater)activity.getSystemService(
	     Context.LAYOUT_INFLATER_SERVICE);
	   convertView = vi.inflate(R.layout.online_chat_content_info, null);
	 
	   holder = new ViewHolder();
	   holder.msg = (TextView) convertView.findViewById(R.id.chat_message);
	   convertView.setTag(holder);
	 
	   }
	   else {
	    holder = (ViewHolder) convertView.getTag();
	   }
	 
	   if(messages.size()>0){
		   String message = messages.get(position);
		   holder.msg.setText(message);
		   //holder.name.setChecked(country.isChecked());
		   //holder.name.setTag(country);
	   }
	 
	   return convertView;
	 
	  }
}
