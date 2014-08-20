package com.meetEverywhere;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetEverywhere.common.User;

public class FriendsListAdapter extends ArrayAdapter<User> {

	private List<User> users;
	private Context context;

	public FriendsListAdapter(Context context, int resource, List<User> users) {
		super(context, resource, users);
		this.users = users;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("getView", position + "");

		if (position >= users.size()) {
			return null;
		}

		User user = users.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.found_tags_content_info, null);

		ImageView photo = (ImageView) row.findViewById(R.id.photo);
		TextView nick = (TextView) row.findViewById(R.id.user_name);

		photo.setImageBitmap(user.getPicture());
		nick.setText(user.getNickname());

		return row;

	}

	public List<User> getUsers() {
		return users;
	}

}
