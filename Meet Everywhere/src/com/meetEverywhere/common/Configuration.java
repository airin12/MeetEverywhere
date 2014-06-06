package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * Klasa jest Singletonem i stanowi kontener na dane m. in. na objekt klasy User.
 * Rozwa�y� czy b�dzie nam potrzebna lista ulubionych.
 * 
 * @author marekmagik
 *
 */
public class Configuration implements Runnable{
	private static Configuration instance;
	private final int SECOND_FROM_MILLIS= 1000;
	private long bluetoothMillisToReconnectAttempt = 2 * SECOND_FROM_MILLIS;
	private long bluetoothMillisTimeBetweenRefreshing = 20 * SECOND_FROM_MILLIS;
	private long serverMillisTimeBetweenRefreshing = 30 * SECOND_FROM_MILLIS;
	private List<Tag> tagsToSearchApartFromUserTags;
	private List<User> favourites;
	private User user;
	private boolean isApplicationOnline;
	private int desiredTagsCompatibility;
	
	
	private Configuration(){
		loadConfiguration();
		(new Thread(this)).start();
	}
	
	private void loadConfiguration() {
		// TODO metoda do wczytywania danych z przestrzeni lokalnej
		// MOCK:
		List<Tag> hashtags = new ArrayList<Tag>();
		hashtags.add(new Tag("pi�ka no�na"));
		hashtags.add(new Tag("strzelectwo"));
		String description = "Mistrz wszech�wiata i okolic. Pozdrawiam!";
		Bitmap picture = BitmapFactory.decodeFile("ic_launcher-web.png");
		desiredTagsCompatibility = 60;
		user = new User("marek" + (new Random().nextInt(100000)), hashtags, description, picture);
	}

	private void storageConfiguration() {
		// TODO metoda do zapisywania danych w przestrzeni lokalnej 
	}
	
	public static Configuration getInstance(){
		if(instance == null){
			instance = new Configuration();
		}
		return instance;
	}

	public void run() {
		
	}
	
	public User getUser(){
		return user;
	}

	public long getBluetoothMillisToReconnectAttempt() {
		return bluetoothMillisToReconnectAttempt;
	}

	public void setBluetoothMillisToReconnectAttempt(
			long bluetoothMillisToReconnectAttempt) {
		this.bluetoothMillisToReconnectAttempt = bluetoothMillisToReconnectAttempt;
	}

	public boolean isApplicationOnline() {
		return isApplicationOnline;
	}

	public void setApplicationOnline(boolean isApplicationOnline) {
		this.isApplicationOnline = isApplicationOnline;
	}

	public long getBluetoothMillisTimeBetweenRefreshing() {
		return bluetoothMillisTimeBetweenRefreshing;
	}

	public void setBluetoothMillisTimeBetweenRefreshing(
			long bluetoothMillisTimeBetweenRefreshing) {
		this.bluetoothMillisTimeBetweenRefreshing = bluetoothMillisTimeBetweenRefreshing;
	}

	public int getDesiredTagsCompatibility() {
		return desiredTagsCompatibility;
	}

	public void setDesiredTagsCompatibility(int desiredTagsCompatibility) {
		this.desiredTagsCompatibility = desiredTagsCompatibility;
	}

	public long getServerMillisTimeBetweenRefreshing() {
		return serverMillisTimeBetweenRefreshing;
	}

	public void setServerMillisTimeBetweenRefreshing(
			long serverMillisTimeBetweenRefreshing) {
		this.serverMillisTimeBetweenRefreshing = serverMillisTimeBetweenRefreshing;
	}

	public List<Tag> getTagsToSearchApartFromUserTags() {
		return tagsToSearchApartFromUserTags;
	}

	public void setTagsToSearchApartFromUserTags(
			List<Tag> tagsToSearchApartFromUserTags) {
		this.tagsToSearchApartFromUserTags = tagsToSearchApartFromUserTags;
	}
	
	
	
	
}
