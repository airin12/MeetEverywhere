package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.User;

public class InvitationReceivedArrayListProxy extends ArrayList<User> {
	private static final long serialVersionUID = -4002651333624385009L;
	private final GenericUsersDBArrayList genericList;
	private final ArrayList<User> invitationReceivedUsers;

	public InvitationReceivedArrayListProxy(GenericUsersDBArrayList genericList) {
		this.genericList = genericList;
		this.invitationReceivedUsers = new ArrayList<User>();
	}

	@Override
	public User get(int index) {
		refreshInvitedUsersList();
		return invitationReceivedUsers.get(index);
	}

	@Override
	public int size() {
		refreshInvitedUsersList();
		return invitationReceivedUsers.size();
	}

	private void refreshInvitedUsersList() {
		invitationReceivedUsers.clear();
		for (User u : genericList) {
			if (u.getInvitationMessage() != null) {
				invitationReceivedUsers.add(u);
			}
		}
	}

}
