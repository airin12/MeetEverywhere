package com.meetEverywhere;

import java.util.List;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.InvitationMessage;

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
		List<InvitationMessage> invites = Configuration.getInstance()
				.getInvitationMessagesReceived();
		invites.add(new InvitationMessage("przyjmnij mnie do znajomych Panie", "Stañczyk", "asad112"));
		invites.add(new InvitationMessage(
				"Ziemiê pomierzy³ i g³êbokie morze,\n Wie, jako wstaj¹ i zachodz¹ zorze; \nWiatrom rozumie, praktykuje komu, \nA sam nie widzi, ¿e ma kurwê w domu.",
				"Kochanowski", "asad112"));
		invites.add(new InvitationMessage("Naród wspania³y, tylko ludzie kurwy.", "Marsza³ek Pi³sudski", "asad112"));
		InvitationsListAdapter adapter = new InvitationsListAdapter(
				getApplicationContext(), R.id.invitations_received_list, invites);

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
