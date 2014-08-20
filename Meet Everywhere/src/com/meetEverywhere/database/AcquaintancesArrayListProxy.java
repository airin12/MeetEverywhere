package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.User;

public class AcquaintancesArrayListProxy extends ArrayList<User> {
	private static final long serialVersionUID = -59534509292887718L;
	private final GenericUsersDBArrayList genericList;
	private final ArrayList<User> acquaintanceUsers;

	public AcquaintancesArrayListProxy(GenericUsersDBArrayList genericList) {
		this.genericList = genericList;
		this.acquaintanceUsers = new ArrayList<User>();
	}

	@Override
	public User get(int index) {
		refreshInvitedUsersList();
		return acquaintanceUsers.get(index);
	}

	@Override
	public int size() {
		refreshInvitedUsersList();
		return acquaintanceUsers.size();
	}

	private void refreshInvitedUsersList() {
		acquaintanceUsers.clear();
		for (User u : genericList) {
			if (u.isAcquaintance()) {
				acquaintanceUsers.add(u);
			}
		}
	}

}
