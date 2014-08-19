package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;

import com.meetEverywhere.MyUsersListAdapter;


/**
 * Klasa jest Singletonem i stanowi kontener na dane m. in. na objekt klasy User.
 * Rozwa¿yæ czy bêdzie nam potrzebna lista ulubionych.
 * 
 * @author marekmagik
 *
 */
public class Configuration implements Runnable{
	private static Configuration instance;
	private int bluetoothSecsTimeBetweenRefreshing = 60;
	private double gpsScanningRadiusInKilometres = 1;
	private int serverSecsTimeBetweenRefreshing = 60;
	private List<Tag> tagsToSearchApartFromUserTags;
	private MyUsersListAdapter usersFoundByOwnTags;
	private MyUsersListAdapter usersFoundBySpecifiedTags;
	private User user;
	private boolean isApplicationOnline;
	private int desiredTagsCompatibility = 50;
	private List<User> blocked = new LinkedList<User>();
	private List<InvitationMessage> invitesSent = new LinkedList<InvitationMessage>();
	private List<InvitationMessage> invitesReceived = new LinkedList<InvitationMessage>();
	private boolean isBluetoothUsed;
	private boolean isGPSUsed;

	private Configuration(){
		instance = this;
		//loadConfiguration();
		(new Thread(this)).start();
	}
	
	private void loadConfiguration() {
		// TODO metoda do wczytywania danych z przestrzeni lokalnej
		// MOCK:
		List<Tag> hashtags = new ArrayList<Tag>();
		hashtags.add(new Tag("pi³ka no¿na"));
		hashtags.add(new Tag("strzelectwo"));
		String description = "Mistrz wszechœwiata i okolic. Pozdrawiam!";
		//Bitmap picture = BitmapFactory.decodeFile("ic_launcher-web.png");
		Bitmap picture = null;
		desiredTagsCompatibility = 60;
		user = new User("marek" + (new Random().nextInt(100000)), hashtags, description, picture, "jsdjfkskjf");
		blocked = new LinkedList<User>();
		blocked.add(new User("marek" + (new Random().nextInt(100000)), hashtags, description, picture, "jsdjfkskjf"));
		
		invitesReceived = new LinkedList<InvitationMessage>();
		invitesSent = new LinkedList<InvitationMessage>();
			
	}	
			
	public static Configuration getInstance(){
		if(instance == null){
			instance = new Configuration();
		}
		return instance;
	}

	public void run() {
		
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}

	public boolean isApplicationOnline() {
		return isApplicationOnline;
	}

	public void setApplicationOnline(boolean isApplicationOnline) {
		this.isApplicationOnline = isApplicationOnline;
	}

	public int getBluetoothSecsTimeBetweenRefreshing() {
		return bluetoothSecsTimeBetweenRefreshing;
	}

	public void setBluetoothSecsTimeBetweenRefreshing(
			int bluetoothSecsTimeBetweenRefreshing) {
		this.bluetoothSecsTimeBetweenRefreshing = bluetoothSecsTimeBetweenRefreshing;
	}

	public int getDesiredTagsCompatibility() {
		return desiredTagsCompatibility;
	}

	public void setDesiredTagsCompatibility(int desiredTagsCompatibility) {
		this.desiredTagsCompatibility = desiredTagsCompatibility;
	}

	public long getServerSecsTimeBetweenRefreshing() {
		return serverSecsTimeBetweenRefreshing;
	}

	public void setServerSecsTimeBetweenRefreshing(
			int serverSecsTimeBetweenRefreshing) {
		this.serverSecsTimeBetweenRefreshing = serverSecsTimeBetweenRefreshing;
	}

	public List<Tag> getTagsToSearchApartFromUserTags() {
		return tagsToSearchApartFromUserTags;
	}

	public void setTagsToSearchApartFromUserTags(
			List<Tag> tagsToSearchApartFromUserTags) {
		this.tagsToSearchApartFromUserTags = tagsToSearchApartFromUserTags;
	}
	
	public MyUsersListAdapter getUsersFoundByOwnTagsAdapter() {
		return usersFoundByOwnTags;
	}

	public void setUsersFoundByOwnTagsAdapter(MyUsersListAdapter usersFoundByOwnTags) {
		this.usersFoundByOwnTags = usersFoundByOwnTags;
	}

	public MyUsersListAdapter getUsersFoundBySpecifiedTagsAdapter() {
		return usersFoundBySpecifiedTags;
	}

	public void setUsersFoundBySpecifiedTagsAdapter(MyUsersListAdapter usersFoundBySpecifiedTags) {
		this.usersFoundBySpecifiedTags = usersFoundBySpecifiedTags;
	}

	public List<User> getBlocked() {
		return blocked;
	}

	public void setBlocked(List<User> blocked) {
		this.blocked = blocked;
	}

	public List<InvitationMessage> getInvitationMessagesSent() {
		return invitesSent;
	}

	public void setInvitationMessagesSent(List<InvitationMessage> invitesSent) {
		this.invitesSent = invitesSent;
	}

	public List<InvitationMessage> getInvitationMessagesReceived() {
		return invitesReceived;
	}

	public void setInvitationMessagesReceived(List<InvitationMessage> invitesReceived) {
		this.invitesReceived = invitesReceived;
	}
	
	public double getGpsScanningRadiusInKilometres() {
		return gpsScanningRadiusInKilometres;
	}

	public void setGpsScanningRadiusInKilometres(double minGpsRadius) {
		this.gpsScanningRadiusInKilometres = minGpsRadius;
	}

	public boolean isBluetoothUsed() {
		return isBluetoothUsed;
	}

	public void setBluetoothUsed(boolean isBluetoothUsed) {
		this.isBluetoothUsed = isBluetoothUsed;
	}

	public boolean isGPSUsed() {
		return isGPSUsed;
	}

	public void setGPSUsed(boolean isGPSUsed) {
		this.isGPSUsed = isGPSUsed;
	}

}
