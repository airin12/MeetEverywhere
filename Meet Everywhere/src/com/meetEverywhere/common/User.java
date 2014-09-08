package com.meetEverywhere.common;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meetEverywhere.DAO;
import com.meetEverywhere.bluetooth.BluetoothConnection;
import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.database.AccountsDAO;
import com.meetEverywhere.database.LocalDAO;
import com.meetEverywhere.database.UsersDAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Klasa User zawiera dane dotycz¹ce u¿ytkownika. Objekty tej klasy mog¹ byæ
 * serializowane i wysy³ane za pomoc¹ InsecureRFCOMM lub sk³adowane lokalnie
 * (objekt dotycz¹cy w³aœciciela urz¹dzenia).
 * 
 * @author marekmagik
 * 
 */

public class User implements Comparable<User> {

	private String nickname;
	private final String userToken;
	private final String userID;
	private List<Tag> hashTags = new ArrayList<Tag>();
	private String description;
	private byte[] picture;
	private String password;

	private boolean isAcquaintance = false;
	private boolean isBlocked = false;
	private boolean isInvited = false;

	private String incomingInvitationMessage = null;
	private boolean isSyncedWithServer;

	private BluetoothConnection bluetoothConnection;
	private ArrayAdapter<TextMessage> messagesArrayAdapter = null;
	private boolean isSendButtonEnabled;
	private boolean isChatFocused;

	private Button sendButton;
	private Configuration config;
	private LocalDAO localDAO;
	
	public User(String nickname, String password, String description, String userToken, boolean isSynced) {
		config = Configuration.getInstance();
		this.nickname = nickname;
		this.description = description;
		this.userToken = userToken;
		this.password = password;
		this.userID = userToken;

		this.isSyncedWithServer = isSynced;

		this.setSendButtonEnabled(true);
		this.setChatFocused(false);
		this.setSendButton(null);

		if (userToken == null) {
			localDAO = UsersDAO.getInstance(userToken);
		} else {
			localDAO = AccountsDAO.getInstance(null);
		}
	}

	public User(String nickname, List<Tag> hashTags, String description,
			String userToken, byte[] picture, String userID, String password,
			boolean isAcquaintance, boolean isBlocked, boolean isInvited,
			String incomingInvitationMessage, boolean isSyncedWithServer) {

		config = Configuration.getInstance();
		this.hashTags = hashTags;
		this.nickname = nickname;
		this.description = description;
		this.userToken = userToken;
		this.picture = picture;
		//this.myFriendsList = new ArrayList<User>();
		this.messagesArrayAdapter = null;
		this.userID = userID;
		this.password = password;

		this.isAcquaintance = isAcquaintance;
		this.isBlocked = isBlocked;
		this.isInvited = isInvited;
		this.incomingInvitationMessage = incomingInvitationMessage;
		this.isSyncedWithServer = isSyncedWithServer;

		this.setSendButtonEnabled(true);
		this.setChatFocused(false);
		this.setSendButton(null);

		if (userToken == null) {
			localDAO = UsersDAO.getInstance(userToken);
		} else {
			localDAO = AccountsDAO.getInstance(null);
		}

	}
	
	/**
	 * Method comparing user credentials with given ones.
	 * @param nickname user nickname
	 * @param password user password
	 * @return true if these are user credentials, false otherwise 
	 */
	public boolean isCredentialRight(String nickname, String password) {
		return this.nickname != null && this.password != null && 
				this.nickname.equals(nickname) && this.password.equals(password);
	}

	public void setInvited(boolean isInvited) {
		this.isInvited = isInvited;
		saveifRequired();
	}

	public boolean isAcquaintance() {
		return isAcquaintance;
	}

	public void setAcquaintance(boolean isAcquaintance) {
		this.isAcquaintance = isAcquaintance;
		this.isBlocked = false;
		this.isInvited = false;
		this.incomingInvitationMessage = null;
		isSyncedWithServer = false;
		saveifRequired();
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
		saveifRequired();
	}

	public boolean isInvitationReceived() {
		return incomingInvitationMessage != null;
	}

	public String getInvitationMessage() {
		return incomingInvitationMessage;
	}

	public void setInvitationMessage(String invitationMessage) {
		this.incomingInvitationMessage = invitationMessage;
		saveifRequired();
	}

	public boolean isInvited() {
		return isInvited;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		saveifRequired();
	}

	public List<Tag> getHashTags() {
		return hashTags;
	}

	public void setHashTags(List<Tag> hashTags) {
		this.hashTags = hashTags;
		saveifRequired();
	}

	@Override
	public boolean equals(Object o1) {
		if (o1 != null && o1 instanceof User) {
			if (((User) o1).getUserID().equals(userID)) {
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
		this.picture = convertBitmapToByteArray(picture);
		saveifRequired();
	}

	private static byte[] convertBitmapToByteArray(Bitmap picture) {
		if (picture == null) {
			return null;
		} else {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
			return stream.toByteArray();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		saveifRequired();
	}

	public String getUserToken() {
		return userToken;
	}

	public String getUserID() {
		return userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		saveifRequired();
	}

	public void changeInvited() {
		if (isInvited) {
			isInvited = false;
		} else {
			isInvited = true;
		}
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
		serializedUser.put("userID", user.getUserID());

		List<Tag> hashTags = user.getHashTags();

		JSONArray hashTagsArray = new JSONArray();
		for (Tag t : hashTags) {
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

	public static User deserializeObjectFromJSON(String jsonUser)
			throws JSONException {
		JSONObject serializedUser = new JSONObject(jsonUser);

		String nickname = serializedUser.getString("nickname");
		String userID = (String) serializedUser.get("userID");

		String description = null;
		if (!serializedUser.get("description").equals(JSONObject.NULL)) {
			description = (String) serializedUser.get("description");
		}
		Bitmap picture = null;
		if (!serializedUser.get("picture").equals(JSONObject.NULL)) {
			Log.i("BT", "odebrano zdjêcie");
			picture = (Bitmap) serializedUser.get("picture");
		}

		ArrayList<Tag> hashTags = new ArrayList<Tag>();
		JSONArray hashTagsArray = serializedUser.getJSONArray("hashTags");
		for (int i = 0; i < hashTagsArray.length(); i++) {
			hashTags.add(new Tag((String) hashTagsArray.get(i), userID));
		}

		// TODO: zmodyfikowaæ przeys³ane komunikaty ze wzglêdu na 4 flagi
		// "stanu znajomoœci" i flagê synchronizacji z serwerem
		User user = UsersAbstractFactory.createOrGetUser(nickname, hashTags,
				description, null, User.convertBitmapToByteArray(picture),
				userID, null, false, false, false, null, false);
		return user;
	}

	public ArrayAdapter<TextMessage> getMessagesArrayAdapter() {
		return messagesArrayAdapter;
	}

	public void setMessagesArrayAdapter(
			ArrayAdapter<TextMessage> messagesArrayAdapter) {
		this.messagesArrayAdapter = messagesArrayAdapter;
	}

	public boolean isSendButtonEnabled() {
		return isSendButtonEnabled;
	}

	public void setSendButtonEnabled(boolean isSendButtonEnabled) {
		this.isSendButtonEnabled = isSendButtonEnabled;
	}

	public boolean isChatFocused() {
		return isChatFocused;
	}

	public void setChatFocused(boolean isChatFocused) {
		this.isChatFocused = isChatFocused;
	}

	public void setSendButton(Button sendButton) {
		this.sendButton = sendButton;
	}

	public void sendMessage(final TextMessage message) {
		final Handler handler = BluetoothDispatcher.getInstance().getHandler();
		sendButton.setEnabled(false);
		(new Thread(new Runnable() {
			public void run() {
				try {
					bluetoothConnection.addMessage(message);
				} catch (Exception e) {
					if (!DAO.sendMessage(message)) {
						handler.post(new Runnable() {

							public void run() {

								Toast.makeText(
										BluetoothDispatcher.getInstance()
												.getTempServiceContextHolder(),
										"Wiadomoœæ nie zosta³a wys³ana!",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
				handler.post(new Runnable() {
					public void run() {

						isSendButtonEnabled = true;
						if (isChatFocused) {
							sendButton.setEnabled(true);
						}
					}
				});

			}
		})).start();

	}

	public void sendInvitation(final InvitationMessage message,
			final LinearLayout layout, final EditText inviteMessage,
			final ImageView image, final User user) {
		final Handler handler = BluetoothDispatcher.getInstance().getHandler();

		layout.setVisibility(LinearLayout.VISIBLE);
		inviteMessage.setVisibility(EditText.GONE);
		image.setVisibility(EditText.GONE);

		(new Thread(new Runnable() {
			public void run() {
				try {
					bluetoothConnection.addMessage(message);
				} catch (Exception e) {
					if (!DAO.sendInvite(message, userID)) {
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(
										BluetoothDispatcher.getInstance()
												.getTempServiceContextHolder(),
										"Wiadomoœæ nie zosta³a wys³ana!",
										Toast.LENGTH_SHORT).show();
								layout.setVisibility(LinearLayout.GONE);
							}
						});
					}
				}
				handler.post(new Runnable() {
					public void run() {
						user.changeInvited();

						isSendButtonEnabled = true;
						if (isChatFocused) {
							sendButton.setEnabled(true);
						}
					}
				});

			}
		})).start();

	}

	public byte[] getPictureAsByteArray() {
		return picture;
	}

	public int compareTo(User another) {
		return CompatibilityAlgorithm.computePercentageValue(
				this.getHashTags(), another.getHashTags());

	}

	public boolean isSyncedWithServer() {
		return isSyncedWithServer;
	}

	public void setSyncedWithServer(boolean syncedWithServer) {
		this.isSyncedWithServer = syncedWithServer;
	}

	public boolean saveInDB() {
		return localDAO.saveUser(this);
	}

	public boolean removeFromDB() {
		return localDAO.removeUser(this);
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
		saveifRequired();
	}

	private boolean saveifRequired() {
		if (config.getAllKnownUsers().contains(this)) {
			return localDAO.saveUser(this);
		} else {
			if (isAcquaintance || isBlocked || isInvited
					|| (incomingInvitationMessage != null)) {
				config.getAllKnownUsers().add(this);
				if (!config.getAllKnownUsers().add(this)) {
					return localDAO.saveUser(this);
				}
			} else {
				if (userToken != null) {
					return localDAO.saveUser(this);
				}
			}
			return false;
		}
	}

}
