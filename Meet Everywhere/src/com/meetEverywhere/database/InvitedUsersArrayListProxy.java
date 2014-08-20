package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.User;

public class InvitedUsersArrayListProxy extends ArrayList<User> {
	private static final long serialVersionUID = -7722974664261304889L;
	private final GenericUsersDBArrayList genericList;
	private final ArrayList<User> invitedUsers;
	
	public InvitedUsersArrayListProxy(GenericUsersDBArrayList genericList){
		this.genericList = genericList;
		this.invitedUsers = new ArrayList<User>();
	}
	
	@Override
	public User get(int index) {
		refreshInvitedUsersList();
		return invitedUsers.get(index);
	}
	
	@Override
	public int size() {
		refreshInvitedUsersList();
		return invitedUsers.size();
	}
	
	private void refreshInvitedUsersList(){
		invitedUsers.clear();
		for(User u: genericList){
			if(u.isInvited()){
				invitedUsers.add(u);
			}
		}
	}
	
}
