package com.meetEverywhere;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.meetEverywhere.FoundTagsActivity.UsersListRefresher;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.User;

public class FriendsListFragment extends Activity {

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list_fragment_layout);

		listView = (ListView) findViewById(R.id.friends_list);
		
		//List<User> friends = Configuration.getInstance()
			//	.getUser().getMyFriendsList();
		
		DAO dao = new DAO();
		List<User> friends = dao.getUsersFromServer(null, 0);
		
		Log.d("friends",friends.size()+"");
		Log.d("STARTING","FRIENDS");

		FriendsListAdapter adapter = new FriendsListAdapter(getApplicationContext(),
				R.layout.found_tags_content_info, friends );
		
		listView.setAdapter(adapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = ((FriendsListAdapter)listView.getAdapter()).getUsers().get(position);
				Intent intent = new Intent(FriendsListFragment.this,
						ServUserProfileActivity.class);
				BluetoothDispatcher.getInstance().setTempUserHolder(user);
				startActivity(intent);
			}
		});
	}
}
