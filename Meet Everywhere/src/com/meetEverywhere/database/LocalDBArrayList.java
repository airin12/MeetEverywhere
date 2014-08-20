package com.meetEverywhere.database;

import java.util.ArrayList;

import com.meetEverywhere.common.Message;
import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;

public class LocalDBArrayList extends ArrayList {
/*	
	private static final long serialVersionUID = -3298817068603330447L;

	// typy objektów w liœcie:
	// Message, User, Tag
	// tag - equals po polu text
	// message, po 4 polach: from, to, text oraz type, co z ochron¹ przed
	// powielaniem zaproszeñ ? -> dodatkowa metoda wewn¹trz klasy

	private final UsersDAO usersDAO;
	private final User user;
	private final LocalDBArrayListType type;
	/*
	 * rozwa¿yæ instanceof i rozpoznawanie metod do wywo³ania wg typu private
	 * final String queryToSaveObject; private final String queryToDeleteObject;
	 */
/*
	public LocalDBArrayList(LocalDBArrayListType type, User user) {
		usersDAO = UsersDAO.getInstance(null);
		this.user = user;
		this.type = type;
	}

	@Override
	public boolean add(Object object) {
		
		if(super.add(object)){
		
		if(type.equals(LocalDBArrayListType.USERS)){
			return usersDAO.saveUser((User)object);
		}
		if(type.equals(LocalDBArrayListType.MESSAGES)){
			return usersDAO.saveMessage((Message) object, user);
		}
		if(type.equals(LocalDBArrayListType.TAGS)){
			
			return usersDAO.saveHashtags((Tag) object, user);
	//		usersDAO.saveMessage((Message) object, user);
		}
		
		return true;
		}else{return false;}
	}

	private boolean isObjectAppropriateToInsert(T object){
		
	}
	
	private boolean checkType(Object object){
		if(type.equals(LocalDBArrayListType.USERS)){
			if(object != null && object instanceof User){
				return true;
			}
		}
		if(type.equals(LocalDBArrayListType.MESSAGES)){
			if(object != null && object instanceof Message){
				return true;
			}
		}
		if(type.equals(LocalDBArrayListType.TAGS)){
			if(object != null && object instanceof Tag){
				return true;
			}
		}
		return false;
	}
*/	
}
