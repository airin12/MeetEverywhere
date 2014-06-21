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

public class InvitationsListAdapter extends ArrayAdapter<InvitationMessage>{

	private List<InvitationMessage> invites;
	private Context context;
	
	public InvitationsListAdapter(Context context, int resource, List<InvitationMessage> objects) {
		super(context, resource, objects);
		invites=objects;
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
		notifyDataSetChanged();
	}
	
	public List<InvitationMessage> getInvitationMessages(){
		return invites;
	}

}
