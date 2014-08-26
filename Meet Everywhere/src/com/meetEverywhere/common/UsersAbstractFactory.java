package com.meetEverywhere.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meetEverywhere.database.GenericUsersDBArrayList;

/**
 * Klasa jest jednocze�nie fabryk� abstrakcyjn� oraz 'magazynem' przechowuj�cym
 * referencje do wszystkich stworzonych user�w. Zapobiega utworzeniu dw�ch
 * instancji objekt�w User reprezentuj�cych t� sam� osob� (to samo userID).
 * 
 * @author marekmagik
 * 
 */
public abstract class UsersAbstractFactory {

//@formatter:off
	/*
	 * TODO: mechanizm wersjonowania, pole w klasie User i bazie danych.
	 * Wed�ug numeru wersji b�dzie wybierana akcja, gdy istnieje ju� instancja User'a:
	 *  - aktualizacja Usera i bazy danych
	 *  - b�d� odrzucenie zmian 
	 * przed zwr�ceniem objektu.
	 * */
	/*
	 * TODO: mechanizm zliaczania referencji i usuwania zb�dnych objekt�w z cachedUsers.
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

			/* TODO: doda� tutaj sprawdzanie wersji */
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
