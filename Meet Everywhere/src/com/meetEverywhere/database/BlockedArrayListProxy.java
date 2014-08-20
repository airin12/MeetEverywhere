package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.User;

public class BlockedArrayListProxy extends ArrayList<User> {
	private static final long serialVersionUID = -8618714474610249776L;
	private final GenericUsersDBArrayList genericList;
	private final ArrayList<User> blockedUsers;

	public BlockedArrayListProxy(GenericUsersDBArrayList genericList) {
		this.genericList = genericList;
		this.blockedUsers = new ArrayList<User>();
	}

	@Override
	public User get(int index) {
		refreshInvitedUsersList();
		return blockedUsers.get(index);
	}

	@Override
	public int size() {
		refreshInvitedUsersList();
		return blockedUsers.size();
	}

	private void refreshInvitedUsersList() {
		blockedUsers.clear();
		for (User u : genericList) {
			if (u.isBlocked()) {
				blockedUsers.add(u);
			}
		}
	}

}
