package com.meetEverywhere.common;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meetEverywhere.bluetooth.BluetoothConnection;

import android.bluetooth.BluetoothDevice;
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

public class User {//implements Serializable {

	private static final long serialVersionUID = -437242741203572594L;
	private String nickname;
	private String userToken;
	private long userID;
	private List<Tag> hashTags;
	private String description;
	private byte[] picture;
	private String password;
	private boolean invited;
	private boolean isFriend;
	private String id;
	private BluetoothConnection bluetoothConnection;
	private List<User> myFriendsList;
	private Configuration config;

	public User(String nickname, List<Tag> hashTags, String description,
			Bitmap picture) {
		config = Configuration.getInstance();
		this.hashTags = hashTags;
		this.nickname = nickname;
		this.description = description;
		this.userToken = String.valueOf((new Random()).nextInt(1000));
		this.setPicture(picture);
		this.myFriendsList = new ArrayList<User>();
		this.isFriend=false;
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
			Bitmap bmp = BitmapFactory.decodeByteArray(picture, 0,
					picture.length);
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
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

	public static class UserComparator implements Comparator<User> {
		public int compare(User s1, User s2) {
			return CompatibilityAlgorithm.computePercentageValue(
					s1.getHashTags(), s2.getHashTags());
		}
	}

	public boolean getInvited() {
		return invited;
	}

	public void setInvited(boolean isInvited) {
		this.invited = isInvited;
	}

	public void changeInvited() {
		if (invited) {
			invited = false;
		} else {
			invited = true;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BluetoothConnection getBluetoothConnection() {
		return bluetoothConnection;
	}

	public void setBluetoothConnection(BluetoothConnection bluetoothConnection) {
		this.bluetoothConnection = bluetoothConnection;
	}

	public String serializeObjectToJSON(boolean attachPicture)
			throws JSONException {
		User user = config.getUser();

		JSONObject serializedUser = new JSONObject();
		serializedUser.put("nickname", user.getNickname());
		serializedUser.put("userToken", user.getUserToken());

		List<Tag> hashTags = user.getHashTags();
		
		JSONArray hashTagsArray = new JSONArray();
		for(Tag t: hashTags){
			hashTagsArray.put(t.getName());
		}
		serializedUser.put("hashTags", hashTagsArray);
		
		if (user.getDescription() != null) {
			serializedUser.put("description", user.getDescription());
		} else {
			serializedUser.put("description", JSONObject.NULL);
		}

		if (attachPicture && user.getPicture() != null) {
			serializedUser.put("picture", user.getPicture());
		} else {
			serializedUser.put("picture", JSONObject.NULL);
		}

		return serializedUser.toString();
	}

	public static User deserializeObjectFromJSON(String jsonUser) throws JSONException {
		JSONObject serializedUser = new JSONObject(jsonUser);

		String nickname = serializedUser.getString("nickname");
		String userToken = (String) serializedUser.get("userToken");

		String description = null;
		if (serializedUser.get("description") != null) {
			description = (String) serializedUser.get("description");
		}
		Bitmap picture = null;
		if (!serializedUser.get("picture").equals(JSONObject.NULL)) {
			picture = (Bitmap) serializedUser.get("picture");
		}
		
		ArrayList<Tag> hashTags = new ArrayList<Tag>();
		JSONArray hashTagsArray = serializedUser.getJSONArray("hashTags");
		for(int i=0; i < hashTagsArray.length(); i++){
			hashTags.add(new Tag((String) hashTagsArray.get(i)));
		}
		
		//List<Tag> hashTags = (List<Tag>) serializedUser.get("hashTags");

		
		
		User user = new User(nickname, hashTags, nickname, picture);
		user.setUserToken(userToken);
		user.setDescription(description);
		return user;
	}

	public List<User> getMyFriendsList() {
		return myFriendsList;
	}

	public void setMyFriendsList(List<User> myFriendsList) {
		this.myFriendsList = myFriendsList;
	}

	public boolean areFriendsWithSpecifiedUser(String userToken) {
		String myToken = config.getUser().getUserToken();
		for (User u : myFriendsList) {
			if (myToken.equals(u.getUserToken())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

}
