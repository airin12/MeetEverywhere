package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.User;

public class InvitationsSentActivity extends Activity {

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitations_sent_layout);

		listView = (ListView) findViewById(R.id.invitations_sent_list);
		
/*		
		List<InvitationMessage> invites = Configuration.getInstance()
				.getInvitationMessagesSent();
		invites.add(new InvitationMessage("hejka", "magik", "marek", "asad112"));
		invites.add(new InvitationMessage(
				"hejka baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
				"magik12", "marek", "asad112"));
*/		
		ArrayList<User> invitedUsers = Configuration.getInstance().getInvitedUsers();
		
		InvitationsListAdapter adapter = new InvitationsListAdapter(
				getApplicationContext(), R.id.invitations_sent_list, invitedUsers, false);

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
