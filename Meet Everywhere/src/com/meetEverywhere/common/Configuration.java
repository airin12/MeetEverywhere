package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Klasa jest Singletonem i stanowi kontener na dane m. in. na objekt klasy User.
 * Rozwa¿yæ czy bêdzie nam potrzebna lista ulubionych.
 * 
 * @author marekmagik
 *
 */
public class Configuration implements Runnable{
	private static Configuration instance;
	private final int SECOND_FROM_MILLIS= 1000;
	private long bluetoothMillisToReconnectAttempt = 10 * SECOND_FROM_MILLIS;
	private List<User> favourites;
	private User user;
	private long sessionToken;
	
	private Configuration(){
		loadConfiguration();
		(new Thread(this)).start();
	}
	
	private void loadConfiguration() {
		// TODO metoda do wczytywania danych z przestrzeni lokalnej
		// MOCK:
		List<String> hashtags = new ArrayList<String>();
		hashtags.add("pi³ka no¿na");
		hashtags.add("strzelectwo");
		user = new User("marek" + (new Random().nextInt(100000)), hashtags);
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

	public long getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(long sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	
}
