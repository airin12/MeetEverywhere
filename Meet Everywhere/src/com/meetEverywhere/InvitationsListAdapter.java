package com.meetEverywhere;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.User;

public class InvitationsListAdapter extends ArrayAdapter<InvitationMessage>{

	private List<InvitationMessage> invites;
	private List<Boolean> selections;
	private Context context;
	
	public InvitationsListAdapter(Context context, int resource, List<InvitationMessage> objects) {
		super(context, resource, objects);
		invites=objects;
		selections = new LinkedList<Boolean>();
		for(int i=0;i<objects.size();i++)
			selections.add(false);
		this.context=context;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if (position >= invites.size()) {
			return null;
		}
		
		
		InvitationMessage invite = invites.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.invites_content_info, null);

		TextView nick = (TextView) row.findViewById(R.id.invitation_nick);
		TextView msg = (TextView) row.findViewById(R.id.invitation_message);
		nick.setText(invite.getAuthorNickname());
		msg.setText(invite.getText());
		nick.setTextColor(Color.BLACK);
		msg.setTextColor(Color.DKGRAY);
		
		return row;
		
	}
	
	public void add(InvitationMessage invite) {
		invites.add(invite);
		selections.add(false);
		notifyDataSetChanged();
	}
	
	
	public List<InvitationMessage> getInvitationMessages(){
		return invites;
	}

	public void toggle(int position) {
		Boolean value = selections.get(position);
		selections.set(position, !value);
		
	}
	
	public void removeSelected(){
		Iterator<Boolean> selectionsIterator = selections.iterator();
		Iterator<InvitationMessage> invitationsIterator = invites.iterator();
		
		while(selectionsIterator.hasNext()){
			InvitationMessage message = invitationsIterator.next();
			Boolean actualValue = selectionsIterator.next();
			if(actualValue){
				invitationsIterator.remove();
				selectionsIterator.remove();
				User user = new User(message.getAuthorNickname(), null, null, null, null);
				Configuration.getInstance().getUser().getMyFriendsList().add(user);
				
				Toast.makeText(context, "Zaakceptowano zaproszenie", Toast.LENGTH_SHORT).show();
			}
		}
		
		notifyDataSetChanged();
	}
	


}
