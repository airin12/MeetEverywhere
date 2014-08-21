package com.meetEverywhere.database;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;

import com.meetEverywhere.common.Tag;
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
		mock_loader();
	}

	private void mock_loader(){
		List<Tag> emptyHashtags = new ArrayList<Tag>();
		List<Tag> wojtusOneTag = new ArrayList<Tag>();
		wojtusOneTag.add(new Tag("czaderski", "wojtkoweID"));
		
		add(UsersAbstractFactory.createOrGetUser("mietek", emptyHashtags, "mieczys³aw pozdrawia!", null, null, "mietkoweID", null, true, false, false, null, true));
		add(UsersAbstractFactory.createOrGetUser("wojtek", wojtusOneTag, "elo", null, null, "wojtkoweID", null, false, false, true, "Idziemy na piwo?", true));
		add(UsersAbstractFactory.createOrGetUser("romek", emptyHashtags, "weso³y romek", null, null, "romkoweID", null, false, true, false, "jestem weso³y romek", true));
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
