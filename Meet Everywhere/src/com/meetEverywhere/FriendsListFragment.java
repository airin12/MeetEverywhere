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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

public class FriendsListFragment extends Activity {

	private ListView listView;
	private LinearLayout confirmButton;
	public static final int NORMAL_MODE=1;
	public static final int DELETE_MODE=2;
	private int actualMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list_fragment_layout);

		listView = (ListView) findViewById(R.id.friends_list);
		confirmButton= (LinearLayout) findViewById(R.id.confirm_button_row);
		confirmButton.setVisibility(LinearLayout.GONE);
		
		actualMode=NORMAL_MODE;
		
		List<User> friends = Configuration.getInstance()
				.getAcquaintancesUsers();

		Log.d("friends", friends.size() + "");
		Log.d("STARTING", "FRIENDS");

		FriendsListAdapter adapter = new FriendsListAdapter(
				getApplicationContext(), R.layout.friends_list_content_info,
				friends);

		listView.setAdapter(adapter);

		setNormalOnClickListener(listView);
	}
	
	public void setNormalOnClickListener(final ListView listView){
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = ((FriendsListAdapter) listView.getAdapter())
						.getUsers().get(position);
				Intent intent = new Intent(FriendsListFragment.this,
						ServUserProfileActivity.class);
				BluetoothDispatcher.getInstance().setTempUserHolder(user);
				startActivity(intent);
			}
		});
	}
	
	
	public void setDeleteOnClickListener(final ListView listView){
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				debugFun();
				CheckBox checkBox = (CheckBox) view
						.findViewById(R.id.friends_list_checkbox);
				checkBox.toggle();
				((FriendsListAdapter) listView.getAdapter())
						.toggle(position);
			}
		});
	}
	
	private void debugFun(){
		String name = "";
		name = "12";
		name.length();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		((FriendsListAdapter)listView.getAdapter()).notifyDataSetChanged();
	}
	
	public void deleteFromFriendsList(View view){
		if(actualMode==NORMAL_MODE){
			actualMode=DELETE_MODE;
			((FriendsListAdapter)listView.getAdapter()).changeMode(actualMode);
			setDeleteOnClickListener(listView);
			confirmButton.setVisibility(LinearLayout.VISIBLE);
		}
		else if(actualMode==DELETE_MODE){
			actualMode=NORMAL_MODE;
			((FriendsListAdapter)listView.getAdapter()).changeMode(actualMode);
			setNormalOnClickListener(listView);
			confirmButton.setVisibility(LinearLayout.GONE);
		}
	}

	public void confirmDeleteFromFriendsList(View view) {
		((FriendsListAdapter)listView.getAdapter()).removeFromFriends();
	}
}
