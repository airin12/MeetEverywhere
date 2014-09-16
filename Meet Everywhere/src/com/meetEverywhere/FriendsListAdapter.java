package com.meetEverywhere;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.NotifiableLayoutElement;
import com.meetEverywhere.common.User;
import com.meetEverywhere.common.messages.FinishAcquiantanceMessage;

public class FriendsListAdapter extends ArrayAdapter<User> implements NotifiableLayoutElement {

	private List<User> users;
	private List<User> toDeledeFriends;
	private Context context;
	private int actualMode;

	public FriendsListAdapter(Context context, int resource, List<User> users) {
		super(context, resource, users);
		this.users = users;
		this.context = context;
		toDeledeFriends = new LinkedList<User>();
		actualMode = FriendsListFragment.NORMAL_MODE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("getView", position + "");

		if (position >= users.size()) {
			return null;
		}

		User user = users.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.friends_list_content_info, null);

		TextView nick = (TextView) row.findViewById(R.id.friends_list_username);
		CheckBox checkBox = (CheckBox) row.findViewById(R.id.friends_list_checkbox);

		if (actualMode == FriendsListFragment.DELETE_MODE)
			checkBox.setVisibility(CheckBox.VISIBLE);
		else if (actualMode == FriendsListFragment.NORMAL_MODE)
			checkBox.setVisibility(CheckBox.GONE);

		nick.setText(user.getNickname());

		return row;

	}

	public List<User> getUsers() {
		return users;
	}

	public void toggle(int position) {

		User user = users.get(position);
		if (toDeledeFriends.contains(user)) {
			toDeledeFriends.remove(user);
		} else {
			toDeledeFriends.add(user);
		}
	}

	public void removeFromFriends() {
		DAO dao = new DAO();
		for (User user : toDeledeFriends) {
			// u.setAcquaintance(false);
			User myself = Configuration.getInstance().getUser();
			user.sendMessage(
			        new FinishAcquiantanceMessage("", myself.getNickname(), myself.getUserID(), user.getUserID()), null);
			Toast.makeText(context, "Usuniêto z listy znajomych", Toast.LENGTH_SHORT).show();
		}

		// toDeledeFriends.clear();
		notifyDataSetChanged();
	}

	public void changeMode(int mode) {
		actualMode = mode;
	}

	public void notifyDataChanged() {
		notifyDataSetChanged();
	}
}
