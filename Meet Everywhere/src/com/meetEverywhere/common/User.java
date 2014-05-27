package com.meetEverywhere.common;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;

/**
 * Klasa User zawiera dane dotycz�ce u�ytkownika. Objekty tej klasy mog� by�
 * serializowane i wysy�ane za pomoc� InsecureRFCOMM lub sk�adowane lokalnie
 * (objekt dotycz�cy w�a�ciciela urz�dzenia). //TODO: rozbudowa� o
 * przechowywanie avatara u�ytkownika
 * 
 * @author marekmagik
 * 
 */


public class User implements Serializable {

	private static final long serialVersionUID = -437242741203572594L;
	private List<Tag> hashTags;
	private final String nickname;
	private Bitmap picture;
	
	public User(String nickname, List<Tag> hashTags, Bitmap picture) {
		this.hashTags = hashTags;
		this.nickname = nickname;
		this.setPicture(picture);
	}

	public String getNickname() {
		return nickname;
	}

	public List<Tag> getHashTags() {
		return hashTags;
	}

	public void setHashTags(List<Tag> hashTags) {
		this.hashTags = hashTags;
	}

	@Override
	public boolean equals(Object o1) {
		if (o1 != null && o1 instanceof User) {
			if (((User) o1).getNickname().equals(nickname)) {
				return true;
			}
		}
		return false;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

}
