package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.bluetooth.BluetoothConnection;
import com.meetEverywhere.bluetooth.BluetoothConnectionStatus;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.CompatibilityAlgorithm;
import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyUsersListAdapter extends ArrayAdapter<User> {

	private final Context context;
	private final Configuration config;
	private final BluetoothDispatcher dispatcher;
	private static List<User> usersFoundByOwnTagsList = new ArrayList<User>();
	private static List<User> usersFoundBySpecifiedTagsList = new ArrayList<User>();
	private final boolean useOwnTagsList;

	public MyUsersListAdapter(Context context, int textViewResourceId,
			boolean useOwnTagsList) {
		super(context, textViewResourceId, usersFoundByOwnTagsList);
		this.context = context;
		config = Configuration.getInstance();
		this.useOwnTagsList = useOwnTagsList;
		this.dispatcher = BluetoothDispatcher.getInstance();
	}

	public MyUsersListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, usersFoundBySpecifiedTagsList);
		this.context = context;
		config = Configuration.getInstance();
		this.useOwnTagsList = false;
		this.dispatcher = BluetoothDispatcher.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("adapter", "pobieram widok! ");

		if (position >= getUsersList().size()) {
			return null;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.found_tags_content_info, null);

		ImageView photo = (ImageView) row.findViewById(R.id.photo);
		TextView nick = (TextView) row.findViewById(R.id.user_name);
		TextView percentage = (TextView) row.findViewById(R.id.percentage);
		ImageView friend = (ImageView) row.findViewById(R.id.friend_icon);

		User user = getUserFromIndex(position);

		if(Configuration.getInstance().getUser().getMyFriendsList().contains(user))	
			friend.setVisibility(ImageView.VISIBLE);
		
		nick.setText(user.getNickname());
		int percentOfCompatibility = CompatibilityAlgorithm
				.computePercentageValue(config.getUser().getHashTags(),
						user.getHashTags());
		percentage.setText(percentOfCompatibility + "%");
		double perc = ((double) percentOfCompatibility) / 100.0;
		int color = Color.rgb((int) (255 - 255 * perc), (int) (0 + 255 * perc),
				0);
		percentage.setTextColor(color);
		photo.setImageBitmap(user.getPicture());

		return row;

	}

	public User getUserFromIndex(int index) {
		return getUsersList().get(index);
	}

	public void addUser(User user) {
		getUsersList().add(user);
		notifyDataSetChanged();
	}

	public List<User> getUsersList() {
		if (useOwnTagsList) {
			return usersFoundByOwnTagsList;
		} else {
			return usersFoundBySpecifiedTagsList;
		}
	}

	public void setUsersList(List<User> usersList) {
		getUsersList().clear();
		getUsersList().addAll(usersList);
		Log.i("user adapter", "ustawiono user list!");
		notifyDataSetChanged();
	}

	public void notifyBluetoothDevicesChanged() {
		if (useOwnTagsList) {
			List<User> list = getUsersList();
			for (BluetoothConnection conn : dispatcher.getConnections()
					.values()) {
				if (conn.getStatus().equals(BluetoothConnectionStatus.ACTIVE)
						&& !list.contains(conn.getUser())) {
					list.add(conn.getUser());
				}
				if (conn.getStatus().equals(BluetoothConnectionStatus.INACTIVE)
						&& list.contains(conn.getUser())) {
					list.remove(conn.getUser());
				}
			}
			dispatcher.getHandler().post(new Runnable() {
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
	}

}
