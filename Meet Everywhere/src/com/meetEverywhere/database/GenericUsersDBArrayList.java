package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.User;
import com.meetEverywhere.common.UsersAbstractFactory;

public class GenericUsersDBArrayList extends ArrayList<User> {
	private static final long serialVersionUID = 6727578468178600176L;
	
	public GenericUsersDBArrayList(){
		loadData();
		UsersAbstractFactory.InitializeFactory(this);
	}
	
	private void loadData() {
		addAll(UsersDAO.getInstance(null).getAllUsers());
	}
	
	@Override
	public boolean add(User user){
		if(!contains(user)){
			super.add(user);
			return user.saveInDB();
		}
		return false;
	}
	
	@Override
	public boolean remove(Object object){
		if(object != null && object instanceof User){
			User user = (User) object;
			if( super.remove(user)){
				return user.removeFromDB();
			}
		}
		return false;
	}

}
