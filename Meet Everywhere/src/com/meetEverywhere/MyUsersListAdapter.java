package com.meetEverywhere;

import java.util.ArrayList;

import com.meetEverywhere.common.ServUser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyUsersListAdapter extends ArrayAdapter<ServUser>{
	
	private Activity activity;
	private ArrayList<ServUser> usersList;
	
	public MyUsersListAdapter(Context context, int textViewResourceId,
		    ArrayList<ServUser> contactList) {
		   super(context, textViewResourceId, contactList);
		   this.activity=(Activity) context;
		   this.usersList = new ArrayList<ServUser>();
		   this.usersList.addAll(contactList); 
		  }
		 
		  private class ViewHolder {
			  ImageView photo;
			  TextView nick;
			  TextView percentage;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
			   ViewHolder holder = null;
			 
			   if (convertView == null) {
				   LayoutInflater vi = (LayoutInflater)activity.getSystemService(
						   Context.LAYOUT_INFLATER_SERVICE);
				   convertView = vi.inflate(R.layout.found_tags_content_info, null);
				 
				   holder = new ViewHolder();
				   holder.photo = (ImageView) convertView.findViewById(R.id.photo);
				   holder.nick = (TextView) convertView.findViewById(R.id.user_name);
				   holder.percentage = (TextView) convertView.findViewById(R.id.percentage);
				   convertView.setTag(holder);
			   }
			   else {
				   holder = (ViewHolder) convertView.getTag();
			   }
			 
			   if(usersList.size()>0){
				   ServUser user = usersList.get(position);
				   //holder.photo.setImageBitmap(user.getBitmap());
				   holder.nick.setText(user.getNick());
				   holder.percentage.setText(user.getPercentage()+"%");
				   double perc = Double.parseDouble(user.getPercentage())/100;
				   int color = Color.rgb((int)(255-255*perc), (int)(0+255*perc), 0);
				   holder.percentage.setTextColor(color);
			   }
			 
			   return convertView;
		 
		  }
		  
		  public ServUser getUserFromIndex(int index){
			  return usersList.get(index);
		  }
}
