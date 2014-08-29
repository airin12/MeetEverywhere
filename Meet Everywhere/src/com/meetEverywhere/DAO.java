package com.meetEverywhere;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.meetEverywhere.ApiService.CreateUserQuery;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.TextMessage;
import com.meetEverywhere.common.User;

/**
 * Klasa stanowi Data Access Object do komunikacji z serwerem.
 * 
 */
public class DAO {
/*
	private Chat chat;
	private MessagesListener listener;
*/
	
	/*
	 * private static int timeout = 5000;
	 * 
	 * public void connectGet(String method, String params){ try { URL url = new
	 * URL("http://10.0.2.2:8080/" + method + "?" + params); HttpURLConnection
	 * connection = (HttpURLConnection)url.openConnection();
	 * connection.setConnectTimeout(timeout);
	 * connection.setReadTimeout(timeout); connection.setRequestMethod("GET");
	 * 
	 * int code = connection.getResponseCode(); if(code == 200){ InputStream
	 * inStream = connection.getInputStream(); if(inStream != null){
	 * BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
	 * String line; while((line = in.readLine()) != null)
	 * System.out.println(line);
	 * 
	 * connection.disconnect(); } else { System.out.println("Error for " +
	 * method + ", params " + params + ": connection stream is null");
	 * connection.disconnect(); } } else { System.out.println("Error for " +
	 * method + ", params " + params + ": response code: " + code);
	 * connection.disconnect(); } } catch (Exception e) {
	 * System.out.println("Error for " + method + ", params " + params + ": " +
	 * e); } }
	 */

	
	//private static TextView token;
	
	
	public synchronized void updateLocationOnServer(double latitude,
			double longtitude, boolean isPositionFromGPS) {
		 String result;
	        try {
	        	result =  new ApiService.UpdateLocationCoordinatesQuery(latitude, longtitude).execute().get();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } catch (ExecutionException e) {
	            e.printStackTrace();
	        }
	}
	
	public synchronized void updateMyHashtags(List<String> hashtags){
		String result;
				
        try {
        	result =  new ApiService.UpdateMyTagsQuery(new HashSet<String>(hashtags)).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
	}

	static List<User> alreadyRegistered = new ArrayList<User>();

	public synchronized User register(String nickname, String password, String description){
		User user = null;
		String token;
		try {
			if((token = new CreateUserQuery(nickname,password,description).execute().get()) != null) {
				user = new User(nickname, password, description, token, true);
				alreadyRegistered.add(user);
			}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        } catch (ExecutionException e) {
        	e.printStackTrace();
        }
		return user;	
	}
	
	public List<User> getUsersFromServer(List<String> tags2, int percentage) {

		List<User> result = new LinkedList<User>();
/*
		try {
        	result =  new ApiService.GetMatchesQuery(new HashSet<String>(tags2), Integer.toString(percentage)).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/		
        return result;
	}
	
	
		
	
	public static boolean sendInvite( InvitationMessage message, String userID){
		String result;
        try {
        	result =  new ApiService.InviteUserQuery(userID, message.toString()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public static boolean sendMessage(TextMessage message){
		
		return false;
	}
	
	
	
	public void stopListening(){
		
	}
	
}
