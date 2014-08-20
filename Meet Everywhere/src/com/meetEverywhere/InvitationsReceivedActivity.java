package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

public class InvitationsReceivedActivity extends Activity {

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitations_received_layout);

		listView = (ListView) findViewById(R.id.invitations_received_list);		
		ArrayList<User> invitationReceivedUsers = Configuration.getInstance().getInvitationReceivedUsers();
		
		InvitationsListAdapter adapter = new InvitationsListAdapter(
				getApplicationContext(), R.id.invitations_received_list, invitationReceivedUsers, true);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				Log.d("OnItemClick", "Click");
				CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.invites_check_box);
				checkBox.toggle();
				((InvitationsListAdapter) listView.getAdapter())
						.toggle(position);
			}
		});

		listView.setAdapter(adapter);

		registerForContextMenu(listView);
	}

	public void acceptInvitation(View view) {
		((InvitationsListAdapter) listView.getAdapter()).removeSelected();
	}

}
