package com.meetEverywhere;

import java.util.Collections;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothConnection;
import com.meetEverywhere.bluetooth.BluetoothConnectionStatus;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

public class FoundTagsActivity extends Activity {

	private UsersListRefresher refresher;
	private Configuration config = Configuration.getInstance();
	private ListView usersListView;
	private MyUsersListAdapter listAdapter;
	private Handler handler = BluetoothDispatcher.getInstance().getHandler();
	private int percentage;
	private List<String> tags;
	private String typeOfAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_tags_activity_layout);

		Intent intent = getIntent();
		tags = intent.getStringArrayListExtra("tags");
		percentage = intent.getIntExtra("perc", 50);
		typeOfAdapter = intent.getStringExtra("typeOfAdapter");
		if (typeOfAdapter.equals("byOwnTags")) {
			if (config.getUsersFoundByOwnTagsAdapter() == null) {
				config.setUsersFoundByOwnTagsAdapter(new MyUsersListAdapter(
						getApplicationContext(),
						R.layout.found_tags_content_info, true));
			}
			listAdapter = config.getUsersFoundByOwnTagsAdapter();

		} else {
			if (config.getUsersFoundBySpecifiedTagsAdapter() == null) {
				config.setUsersFoundBySpecifiedTagsAdapter(new MyUsersListAdapter(
						getApplicationContext(),
						R.layout.found_tags_content_info));
			}
			listAdapter = config.getUsersFoundBySpecifiedTagsAdapter();
		}

		usersListView = (ListView) findViewById(R.id.foundUsersList);
		usersListView.setAdapter(listAdapter);

		usersListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = listAdapter.getUserFromIndex(position);
				Intent intent = new Intent(FoundTagsActivity.this,
						ServUserProfileActivity.class);
				//intent.putExtra("user", user);
				BluetoothDispatcher.getInstance().setTempUserHolder(user);
				startActivity(intent);
			}
		});

		if (typeOfAdapter.equals("byOwnTags")) {
			//tags = config.getUser().getHashTags();
			refresher = new UsersListRefresher();
			refresher.start();
		} else {
			//TODO
			refresher = new UsersListRefresher();
			refresher.refresh();
		}
	}

	public void back(View view) {
		finish();
	}

	public void loadAdapterWithNewList(List<User> usersList) {
		listAdapter.setUsersList(usersList);
		Collections.sort(listAdapter.getUsersList(), new User.UserComparator());

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (typeOfAdapter.equals("byOwnTags")) {
			refresher.stopThread();
		}
	}

	public void refresh(View view) {
		refresher.refresh();
	}

	public class UsersListRefresher extends Thread {
		private boolean shouldRun = true;
		private DAO dao;
		private BluetoothDispatcher dispatcher = BluetoothDispatcher
				.getInstance();

		public UsersListRefresher() {
			dao = new DAO();
		}

		public void refresh() {
			handler.post(new Runnable() {
				public void run() {
					listAdapter.setUsersList(dao.getUsersFromServer(tags,
							percentage));
					for (BluetoothConnection conn : dispatcher.getConnections()
							.values()) {
						if (conn.getStatus().equals(
								BluetoothConnectionStatus.ACTIVE)) {
							listAdapter.add(conn.getUser());
						}
					}
				}
			});
		}

		@Override
		public void run() {
			while (shouldRun) {
				refresh();
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stopThread() {
			shouldRun = false;
		}

	}

}
