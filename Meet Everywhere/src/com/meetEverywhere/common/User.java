package com.meetEverywhere.common;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;

/**
 * Klasa User zawiera dane dotycz¹ce u¿ytkownika. Objekty tej klasy mog¹ byæ
 * serializowane i wysy³ane za pomoc¹ InsecureRFCOMM lub sk³adowane lokalnie
 * (objekt dotycz¹cy w³aœciciela urz¹dzenia). //TODO: rozbudowaæ o
 * przechowywanie avatara u¿ytkownika
 * 
 * @author marekmagik
 * 
 */


public class User implements Serializable {

	private static final long serialVersionUID = -437242741203572594L;
	private List<String> hashTags;
	private final String nickname;
	private Bitmap picture;
	
	public User(String nickname, List<String> hashTags, Bitmap picture) {
		this.setHashTags(hashTags);
		this.nickname = nickname;
		this.setPicture(picture);
	}

	public String getNickname() {
		return nickname;
	}

	public List<String> getHashTags() {
		return hashTags;
	}

	public void setHashTags(List<String> hashTags) {
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
