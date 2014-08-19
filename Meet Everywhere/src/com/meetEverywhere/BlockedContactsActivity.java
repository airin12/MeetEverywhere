package com.meetEverywhere;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		
		List<User> blocked = Configuration.getInstance().getBlocked();

		FriendsListAdapter adapter = new FriendsListAdapter(getApplicationContext(),
				R.layout.found_tags_content_info, blocked );
		
		listView.setAdapter(adapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = ((FriendsListAdapter)listView.getAdapter()).getUsers().get(position);
				Intent intent = new Intent(BlockedContactsActivity.this,
						ServUserProfileActivity.class);
				BluetoothDispatcher.getInstance().setTempUserHolder(user);
				startActivity(intent);
			}
		});
	}

}
