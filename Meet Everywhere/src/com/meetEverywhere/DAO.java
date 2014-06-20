package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.ServUser;
import com.meetEverywhere.common.Tag;
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

	public synchronized void updateLocationOnServer(double latitude,
			double longtitude, boolean isPositionFromGPS) {
		// TODO
	}
	
	public synchronized void updateMyHashtags(List<String> hashtags){
		// TODO
	}
	
	public List<User> getUsersFromServer(List<String> tags2, int percentage) {
		List<User> list = new ArrayList<User>();
		
		Random random = new Random();
		int count = random.nextInt(5)+1;
		
		for(int i=0;i<count;i++){
			String nick = "marek"+random.nextInt(100);
			String desc = " jestem marek z Wloszczowy, pozdrawiam Tarnow";
			List<Tag> tags = new ArrayList<Tag>();
			tags.add(new Tag("siatkowka"));
			tags.add(new Tag("szachy"));
			int perc = random.nextInt(100);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			//Bitmap bitmap = BitmapFactory.decodeFile("/ic_launcher-web.png", options);
			//Bitmap bitmap = null;				
			
			User user = new User(nick, tags, desc, null, "dfsfdsf");
			int nr = random.nextInt(100);
			if(nr<30)
				Configuration.getInstance().getUser().getMyFriendsList().add(user);
				//user.setFriend(true);
			
			if(perc>=percentage){
				list.add(user);
			}
		}
		Log.i("DAO", "list returned " + list.size() + " elements");
		return list;
	}
		
	
	public static boolean sendInvite(InvitationMessage message){
		
		return false;
	}
	
//<<<<<<< HEAD
	public static boolean sendMessage(TextMessage message){
		
		return false;
	}
	
//=======
	public void stopListening(){
		
	}
/*	
	public void listenIncomingMessages(Chat chat){
		listener = new MessagesListener(chat);
		listener.start();
		
	}
	
	public class MessagesListener extends Thread{
		
		private boolean shouldRun;
		private Chat chat;
		
		public MessagesListener(Chat chat){
			shouldRun=true;
			this.chat=chat;
		}
		
		public void run(){
			while(shouldRun){
				Log.d("dao thread", "running");
				chat.messageReceived("hej co tam");
				try {
					Thread.currentThread().sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void stopThread(){
			shouldRun=false;
		}
		
	}
>>>>>>> branch 'master' of https://github.com/airin12/MeetEverywhere
*/
	
}
