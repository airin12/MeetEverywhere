package com.meetEverywhere.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meetEverywhere.database.GenericUsersDBArrayList;

/**
 * Klasa jest jednoczeœnie fabryk¹ abstrakcyjn¹ oraz 'magazynem' przechowuj¹cym
 * referencje do wszystkich stworzonych userów. Zapobiega utworzeniu dwóch
 * instancji objektów User reprezentuj¹cych t¹ sam¹ osobê (to samo userID).
 * 
 * @author marekmagik
 * 
 */
public abstract class UsersAbstractFactory {

//@formatter:off
	/*
	 * TODO: mechanizm wersjonowania, pole w klasie User i bazie danych.
	 * Wed³ug numeru wersji bêdzie wybierana akcja, gdy istnieje ju¿ instancja User'a:
	 *  - aktualizacja Usera i bazy danych
	 *  - b¹dŸ odrzucenie zmian 
	 * przed zwróceniem objektu.
	 * */
	/*
	 * TODO: mechanizm zliaczania referencji i usuwania zbêdnych objektów z cachedUsers.
	 * */
//@formatter:on	
	private static Map<String, User> cachedUsers = new HashMap<String, User>();

	public static void InitializeFactory(GenericUsersDBArrayList allUsersInDB) {
		for (User user : allUsersInDB) {
			cachedUsers.put(user.getUserID(), user);
		}
	}

	public synchronized static User createOrGetUser(String nickname,
			List<Tag> hashTags, String description, String userToken,
			byte[] picture, String userID, String password,
			boolean isAcquaintance, boolean isBlocked, boolean isInvited,
			String incomingInvitationMessage, boolean isSyncedWithServer) {

		if (cachedUsers.keySet().contains(userID)) {
			User user = cachedUsers.get(userID);

			/* TODO: dodaæ tutaj sprawdzanie wersji */
			user.setHashTags(hashTags);
			user.setNickname(nickname);
			user.setDescription(description);
			user.setPicture(picture);

			user.setAcquaintance(isAcquaintance);
			user.setBlocked(isBlocked);
			user.setInvited(isInvited);
			user.setInvitationMessage(incomingInvitationMessage);
			user.setSyncedWithServer(isSyncedWithServer);

			return user;
		}

		User user = new User(nickname, hashTags, description, userToken,
				picture, userID, password, isAcquaintance, isBlocked,
				isInvited, incomingInvitationMessage, isSyncedWithServer);

		cachedUsers.put(user.getUserID(), user);
		return user;
	}
}
