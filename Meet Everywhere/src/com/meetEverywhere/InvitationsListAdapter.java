package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.common.User;

public class InvitationsListAdapter extends ArrayAdapter<User>{
	private final boolean incomingInvitationsMode;
	private List<User> users;
	private Context context;
	private List<User> toAcceptInvitation;
	
	public InvitationsListAdapter(Context context, int resource, List<User> users, boolean incomingInvitations) {
		super(context, resource, users);
		this.users=users;
		toAcceptInvitation = new ArrayList<User>();
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

	public void toggle(int position) {
		
		User user = users.get(position);
		if(toAcceptInvitation.contains(user)){
			toAcceptInvitation.remove(user);
		}else{
			toAcceptInvitation.add(user);
		}
	}
	
	public void removeSelected(){
		for(User u: toAcceptInvitation){
			u.setAcquaintance(true);
			Toast.makeText(context, "Zaakceptowano zaproszenie", Toast.LENGTH_SHORT).show();
		}
		
		toAcceptInvitation.clear();
		
		notifyDataSetChanged();
	}
	


}
