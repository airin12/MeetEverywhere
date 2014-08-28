package com.meetEverywhere;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

public class InvitationsReceivedActivity extends Activity {

	private ListView listView;
	private int actualMode;
	public static final int NORMAL_MODE = 1;
	public static final int ACCEPT_MODE = 2;
	private LinearLayout confirmRow;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitations_received_layout);

		actualMode = NORMAL_MODE;

		confirmRow=(LinearLayout) findViewById(R.id.confirm_invitation_row);
		confirmRow.setVisibility(LinearLayout.GONE);
		listView = (ListView) findViewById(R.id.invitations_received_list);
		ArrayList<User> invitationReceivedUsers = Configuration.getInstance()
				.getInvitationReceivedUsers();

		InvitationsListAdapter adapter = new InvitationsListAdapter(
				getApplicationContext(), R.id.invitations_received_list,
				invitationReceivedUsers, true);
		adapter.setMode(actualMode);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListener(listView);

		listView.setAdapter(adapter);

		registerForContextMenu(listView);
	}

	public void setListener(final ListView listView) {
		if (actualMode == NORMAL_MODE) {
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					User user = ((InvitationsListAdapter) listView.getAdapter())
							.getUsers().get(position);
					Intent intent = new Intent(
							InvitationsReceivedActivity.this,
							ServUserProfileActivity.class);
					BluetoothDispatcher.getInstance().setTempUserHolder(user);
					startActivity(intent);
				}
			});
		} else if (actualMode == ACCEPT_MODE) {
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
		}
	}

	public void acceptInvitation(View view) {
		((InvitationsListAdapter) listView.getAdapter()).removeSelected();
	}
	
	public void changeMode(View view) {
		if(actualMode==NORMAL_MODE){
			actualMode=ACCEPT_MODE;
			confirmRow.setVisibility(LinearLayout.VISIBLE);
		} else if(actualMode==ACCEPT_MODE) {
			actualMode=NORMAL_MODE;
			confirmRow.setVisibility(LinearLayout.GONE);
		}
				
		setListener(listView);
		((InvitationsListAdapter) listView.getAdapter()).setMode(actualMode);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		((InvitationsListAdapter) listView.getAdapter()).notifyDataSetChanged();
	}

}
