package com.meetEverywhere;

import java.util.ArrayList;
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
import android.widget.ListAdapter;
import android.widget.ListView;

import com.meetEverywhere.common.ServUser;

public class FoundTagsActivity extends Activity{
	
	private UsersListRefresher refresher;
	private List<ServUser> users = new ArrayList<ServUser>();
	private ListView usersListView;
	private ListAdapter listAdapter;
	private Handler handler;
	private int percentage;
	private List<String> tags;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.found_tags_activity_layout);
		
		usersListView = (ListView) findViewById(R.id.foundUsersList);
		usersListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	ServUser user = ((MyUsersListAdapter)listAdapter).getUserFromIndex(position);
            	Intent intent = new Intent(FoundTagsActivity.this, ServUserProfileActivity.class);
            	intent.putExtra("user", user);
            	startActivity(intent);
            }
        });
		
		handler = new Handler(){
			  @Override
			  public void handleMessage(Message msg) {
				  Collections.sort(users,new ServUser.ServUserComparator ());
				  loadAdapterWithNewList(users);
			  }
			};

		loadAdapterWithNewList(users);
		refresher = new UsersListRefresher();
		refresher.start();
		
		Intent intent = getIntent();
		tags = intent.getStringArrayListExtra("tags");
		percentage = intent.getIntExtra("perc", 50);
	}

	public void back(View view) {
		finish();
	}
	
	
	public void loadAdapterWithNewList(List<ServUser> list) {
		listAdapter = new MyUsersListAdapter(this, R.layout.found_tags_content_info,
				(ArrayList<ServUser>) list);
		usersListView.setAdapter(listAdapter);

	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		refresher.stopThread();
	}
	
	public void refresh(View view){
		refresher.refresh();
	}
	
	
	public class UsersListRefresher extends Thread{
		private boolean shouldRun = true;
		private DAO dao;
		
		public UsersListRefresher(){
			dao = new DAO();
		}
		
		public void refresh(){
			users = dao.getUsersFromServer(tags,percentage);
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
		
		@Override
		public void run(){
			while(shouldRun){
				users = dao.getUsersFromServer(tags,percentage);
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
				
				try {
					Thread.currentThread();
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void stopThread(){
			shouldRun=false;
		}

	}
	
}
