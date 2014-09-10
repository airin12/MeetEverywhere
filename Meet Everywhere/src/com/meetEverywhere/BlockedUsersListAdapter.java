package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.common.NotifiableLayoutElement;
import org.w3c.dom.Text;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.common.User;

public class BlockedUsersListAdapter extends ArrayAdapter<User> implements NotifiableLayoutElement {

	private List<User> blockedUsers;
	private Context context;
	private List<User> usersToUnblock;
	
	public BlockedUsersListAdapter(Context context, int resource,  List<User> blockedUsers) {
		super(context, resource, blockedUsers);
		this.blockedUsers = blockedUsers;
		this.context = context;
		usersToUnblock = new ArrayList<User>();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position >= blockedUsers.size()) {
			return null;
		}
		
		User blockedUser = blockedUsers.get(position);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.blocked_user_content_info, null);
		
		TextView nick = (TextView)row.findViewById(R.id.blockedUserNick);
		nick.setText(blockedUser.getNickname());
		nick.setTextColor(Color.BLACK);
		
		return row;
	}
	
	public void toogle(int position) {
		User user = blockedUsers.get(position);
		if(usersToUnblock.contains(user)) {
			blockedUsers.remove(user);
		} else {
			usersToUnblock.add(user);
		}
	}
	
	public void removeSelected() {
		for(User user : usersToUnblock) {
			user.setBlocked(false);
			Toast.makeText(context, "U¿ytownik " + user.getNickname() + " zostal odblokowany", Toast.LENGTH_SHORT).show();
		}
		
		notifyDataSetChanged();
	}
	
	public List<User> getUsers() {
		return blockedUsers;
	}

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }
}
