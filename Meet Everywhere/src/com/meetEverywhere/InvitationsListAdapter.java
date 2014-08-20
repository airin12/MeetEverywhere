package com.meetEverywhere;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.User;

public class InvitationsListAdapter extends ArrayAdapter<User>{
	private final boolean incomingInvitationsMode;
	private List<User> users;
	private Context context;
	
	
	public InvitationsListAdapter(Context context, int resource, List<User> users, boolean incomingInvitations) {
		super(context, resource, users);
		this.users=users;
		this.context=context;
		this.incomingInvitationsMode = incomingInvitations;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if (position >= users.size()) {
			return null;
		}
		
		
		User user = users.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.invites_content_info, null);

		TextView nick = (TextView) row.findViewById(R.id.invitation_nick);
		TextView msg = (TextView) row.findViewById(R.id.invitation_message);
		nick.setText(user.getNickname());
		if(incomingInvitationsMode){
		msg.setText(user.getInvitationMessage());
		}else{
			msg.setText("");
		}
		nick.setTextColor(Color.BLACK);
		msg.setTextColor(Color.DKGRAY);
		
		return row;
		
	}
	
	public void add(InvitationMessage invite) {
		//invites.add(invite);
		notifyDataSetChanged();
	}
	

}
