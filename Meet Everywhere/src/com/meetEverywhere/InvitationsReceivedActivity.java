package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class InvitationsReceivedActivity extends Activity{

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitations_received_layout);

		listView = (ListView) findViewById(R.id.invitations_received_list);
//		List<InvitationMessage> invites = Configuration.getInstance()
//				.getInvitationMessagesReceived();
		
/*		
		invites.add(new InvitationMessage("przyjmnij mnie do znajomych Panie", "Sta�czyk", "asad112", "marek"));
		invites.add(new InvitationMessage(
				"Ziemi� pomierzy� i g��bokie morze,\n Wie, jako wstaj� i zachodz� zorze; \nWiatrom rozumie, praktykuje komu, \nA sam nie widzi, �e ma kurw� w domu.",
				"Kochanowski", "asad112", "marek"));
		invites.add(new InvitationMessage("Nar�d wspania�y, tylko ludzie kurwy.", "Marsza�ek Pi�sudski", "asad112", "marek"));
*/		
		ArrayList<User> invitationReceivedUsers = Configuration.getInstance().getInvitationReceivedUsers();
		
		InvitationsListAdapter adapter = new InvitationsListAdapter(
				getApplicationContext(), R.id.invitations_received_list, invitationReceivedUsers, true);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Log.d("OnItemClick","Click");
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.invites_check_box);
				checkBox.toggle();
				
			}
		});
		
		listView.setAdapter(adapter);
		
		registerForContextMenu(listView);
	}

}
