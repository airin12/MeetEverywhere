package com.meetEverywhere;

import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.messages.InvitationMessage;
import com.meetEverywhere.common.messages.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.meetEverywhere.common.User;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Klasa stanowi Data Access Object do komunikacji z serwerem.
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

    public static boolean sendMessage(Message message) {
        //TODO: Zaimplementowaæ wysy³anie danych do serwera.

        //true: wiadomoœæ dotar³a do serwera, false wpp
        return false;
    }

    public synchronized void updateLocationOnServer(double latitude,
                                                    double longtitude, boolean isPositionFromGPS) {
        String result;
        try {
            result = new ApiService.UpdateLocationCoordinatesQuery(latitude, longtitude).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateMyHashtags(List<String> hashtags) {
        String result;

        try {
            result = new ApiService.UpdateMyTagsQuery(new HashSet<String>(hashtags)).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

	public synchronized User register(String nickname, String password, String description){
		String jsonString, token = null, id = null;
		User user = null;
		try {
			if((jsonString = new ApiService.CreateUserQuery(nickname,password,description).execute().get()) != null) {
				JSONObject jsonObject = new JSONObject(jsonString);
				if(jsonObject.has("auth_token") && jsonObject.has("id") && jsonObject.getJSONObject("id").has("$oid")) {
					token = jsonObject.getString("auth_token");
					id = jsonObject.getJSONObject("id").getString("$oid");;
				} else {
					return null;
				}

                    /* tworzenie Usera przez new - w tym przypadku nie korzystamy z fabryki. */
                user = new User(nickname, new ArrayList<Tag>(), description, token, null, id, password, false, false, false, null, true);
			}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        } catch (ExecutionException e) {
        	e.printStackTrace();
        } catch (JSONException e) {
			e.printStackTrace();
		}

        /* mock: */
        Random rand = new Random();
        user = new User(nickname, new ArrayList<Tag>(), description, "token" + rand.nextInt(100000), null, "userd" + rand.nextInt(1000000), password, false, false, false, null, false);

        return user;
	}
	
	/**
	 * Method checking if user is registered on server
	 * @param nickname user nickname
	 * @param password user password
	 * @return true if is registered, false otherwise
	 */
	public synchronized User loginOnExternalServer(String nickname, String password) {
		User user = null;
		String jsonString, token = null, description = null, id = null;
		
		//useful only if problem occurred between server registration and local DAO registration

		try {
			if(!"error".equals(jsonString = new ApiService.LoginUserQuery(nickname,password).execute().get())) {
				JSONObject jsonObject = new JSONObject(jsonString);
				
				if(jsonObject.has("auth_token") && jsonObject.has("id") && jsonObject.getJSONObject("id").has("$oid")) {
					token = jsonObject.getString("auth_token");
					id = jsonObject.getJSONObject("id").getString("$oid");;
				} else {
					return null;
				}
				
				description = jsonObject.getString("description");

                    /* tworzenie Usera przez new - w tym przypadku nie korzystamy z fabryki. */
                user = new User(nickname, new ArrayList<Tag>(), description, token, null, id, password, false, false, false, null, true);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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

    public void stopListening() {

    }

}
