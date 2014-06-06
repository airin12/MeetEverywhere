package com.meetEverywhere.common;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Klasa User zawiera dane dotycz¹ce u¿ytkownika. Objekty tej klasy mog¹ byæ
 * serializowane i wysy³ane za pomoc¹ InsecureRFCOMM lub sk³adowane lokalnie
 * (objekt dotycz¹cy w³aœciciela urz¹dzenia). 
 * 
 * @author marekmagik
 * 
 */

public class User implements Serializable {

	private static final long serialVersionUID = -437242741203572594L;
	private String nickname;
	private long userToken;
	private long userID;
	private List<Tag> hashTags;
	private String description;
	private byte[] picture;
	private String password;

	public User(String nickname, List<Tag> hashTags, String description,
			Bitmap picture) {
		this.hashTags = hashTags;
		this.nickname = nickname;
		this.description = description;
		this.setPicture(picture);
	}

	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
		if (picture != null) {
			Bitmap bmp = BitmapFactory.decodeByteArray(picture, 0, picture.length);
			return bmp.copy(Bitmap.Config.ARGB_8888, true);
		} else {
			return null;
		}
	}

	public void setPicture(Bitmap picture) {
		if (picture == null) {
			this.picture = null;
		} else {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
			this.picture = stream.toByteArray();			
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getUserToken() {
		return userToken;
	}

	public void setUserToken(long userToken) {
		this.userToken = userToken;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public static class UserComparator implements Comparator<User>{
		public int compare(User s1, User s2) {
			return CompatibilityAlgorithm.computePercentageValue(s1.getHashTags(), s2.getHashTags());
		}
	}
	
}
