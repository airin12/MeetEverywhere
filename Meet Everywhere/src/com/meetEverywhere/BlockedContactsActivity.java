package com.meetEverywhere;

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
import com.meetEverywhere.common.DataChangedNotificationService;
import com.meetEverywhere.common.User;

public class BlockedContactsActivity extends Activity{

	private ListView listView;
    private BlockedUsersListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blocked_contacts_layout);

		listView = (ListView) findViewById(R.id.blocked_list);

		
		List<User> blockedUsers = Configuration.getInstance().getBlockedUsers();
		
		adapter = new BlockedUsersListAdapter(getApplicationContext(),
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

    @Override
    public void onResume(){
        super.onResume();
        DataChangedNotificationService.register(adapter);
    }

    @Override
    public void onPause(){
        super.onPause();
        DataChangedNotificationService.unregister(adapter);
    }

	public void unblockUser(View view) {
		((BlockedUsersListAdapter) listView.getAdapter()).removeSelected();
	}
	
}
