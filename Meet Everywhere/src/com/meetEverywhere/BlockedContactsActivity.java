package com.meetEverywhere;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

public class BlockedContactsActivity extends Activity{

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blocked_contacts_layout);

		listView = (ListView) findViewById(R.id.blocked_list);

		
		List<User> blockedUsers = Configuration.getInstance().getBlockedUsers();
		
		BlockedUsersListAdapter adapter = new BlockedUsersListAdapter(getApplicationContext(), 
				R.layout.blocked_user_content_info, blockedUsers);
		
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("OnItemClick", "Click");
				CheckBox checkBox = (CheckBox)view.findViewById(R.id.blockedUserCheckBox);
				checkBox.toggle();
				((BlockedUsersListAdapter) listView.getAdapter()).toogle(position);
			}
		});
	}
	
	public void unblockUser(View view) {
		((BlockedUsersListAdapter) listView.getAdapter()).removeSelected();
	}
	
}
