package com.meetEverywhere;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_tags_activity_layout);

		Intent intent = getIntent();
		tags = intent.getStringArrayListExtra("tags");
		percentage = intent.getIntExtra("perc", 50);
		String typeOfAdapter = intent.getStringExtra("typeOfAdapter");
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
				intent.putExtra("user", user);
				startActivity(intent);
			}
		});

		refresher = new UsersListRefresher();
		refresher.start();

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
		refresher.stopThread();
	}

	public void refresh(View view) {
		refresher.refresh();
	}

	public class UsersListRefresher extends Thread {
		private boolean shouldRun = true;
		private DAO dao;

		public UsersListRefresher() {
			dao = new DAO();
		}

		public void refresh() {
			handler.post(new Runnable() {
				public void run() {
					listAdapter.setUsersList(dao.getUsersFromServer(tags,
							percentage));
				}
			});

		}

		@Override
		public void run() {
			while (shouldRun) {
				handler.post(new Runnable() {
					public void run() {
						listAdapter.setUsersList(dao.getUsersFromServer(tags,
								percentage));
					}
				});

				try {
					//Thread.currentThread();
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
