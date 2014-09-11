package com.meetEverywhere;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;
import com.meetEverywhere.common.UsersAbstractFactory;

/**
 * Created by Krzysiu on 04.06.14.
 */
public class ApiService {
	
	public enum HttpType {
		GET,
		POST,
		PATCH,
		DELETE
	}
	
	private static String token = "";
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_VALUE = "Token token=";
	private static final String SERVER_HTTP = "http://serene-plateau-6907.herokuapp.com";
    private static final String USER_HTTP = SERVER_HTTP + "/user";
    private static final String MATCHES_HTTP = SERVER_HTTP + "/matches";
    private static final String TAGS_HTTP = SERVER_HTTP + "/tags";
    private static final String TAG_HTTP = SERVER_HTTP + "/tag";
    private static final String LOGIN_HTTP = SERVER_HTTP + "/sessions";
    private static final String INVITATIONS_MODULE_HTTP = SERVER_HTTP + "/invitations";
    private static final String INVITE_HTTP = INVITATIONS_MODULE_HTTP + "/invite";
    private static final String ACCEPT_INVITATION_HTTP = INVITATIONS_MODULE_HTTP + "/accept";
    private static final String REJECT_INVITATION_HTTP = INVITATIONS_MODULE_HTTP + "/reject";
    private static final String INCOMING_INVITATIONS_HTTP = INVITATIONS_MODULE_HTTP + "/incoming_invitations";
    private static final String OUTCOMING_INVITATIONS_HTTP = INVITATIONS_MODULE_HTTP + "/outcoming_invitations";
    private static final String BLOCK_USER_HTTP = SERVER_HTTP + "/block_user";

    private static final String USER_NAME = "user[name]";
    private static final String USER_PASSWORD = "user[password]";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String USER_ID = "user_id";
    private static final String USER_DESC = "user[description]";
    private static final String USER_LOCATION = "user[coordinate_attributes][location][]";
    
    
    
    public static class GetMyUserData extends AsyncTask<Void, Void, String> {
    	@Override
		protected String doInBackground(Void... voids) {
			JSONObject jsonObject = performQuery(ApiService.USER_HTTP, new ArrayList<NameValuePair>(), HttpType.GET);
			return jsonObject.toString();
    	}
    }
    
    public static class CreateUserQuery extends AsyncTask<Void, Void, String> {

		private String userName;
    	private String password;
    	private String description;
    	
		public CreateUserQuery(String nick, String password, String description) {
			this.userName = nick;
			this.password = password;
			this.description = description;
		}
    	
    	@Override
		protected String doInBackground(Void... voids) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(USER_NAME, userName));
            nameValuePairs.add(new BasicNameValuePair(USER_PASSWORD, password));
            nameValuePairs.add(new BasicNameValuePair(USER_DESC, description));
			JSONObject jsonObject = performQuery(ApiService.USER_HTTP, nameValuePairs, HttpType.POST);
			
			if(jsonObject.has("auth_token")) {
				return jsonObject.toString();
			}
			
			return null;
    	}
    }
    
    /**
     * Query for checking if user is registered on server
     * @author Bartosz
     */
    public static class LoginUserQuery extends AsyncTask<Void, Void, String> {
    	
    	private String userName;
    	private String password;
    	
		public LoginUserQuery(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}
    	
    	@Override
    	protected String doInBackground(Void... voids) {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair(NAME, userName));
    		nameValuePairs.add(new BasicNameValuePair(PASSWORD, password));
    		JSONObject jsonObject = performQuery(ApiService.LOGIN_HTTP , nameValuePairs, HttpType.POST);
    		
			if(!jsonObject.has("error")) {
				return jsonObject.toString();
			}
    		
    		return "error";
    	}
    	
    }
    
    public static class UpdateNickQuery extends AsyncTask<Void, Void, String> {

    	private String nick;
    	
		public UpdateNickQuery(String nick) {
			super();
			this.nick = nick;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(USER_NAME, nick));
            JSONObject jsonObject = performQuery(ApiService.USER_HTTP, nameValuePairs, HttpType.PATCH);
            return jsonObject.toString();
		}
    }
    
    public static class UpdateLocationCoordinatesQuery extends AsyncTask<Void, Void, String> {

    	private double latitude;
    	private double longitude;
    	
		public UpdateLocationCoordinatesQuery(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(USER_LOCATION, Double.toString(longitude)));
            nameValuePairs.add(new BasicNameValuePair(USER_LOCATION, Double.toString(latitude)));
			JSONObject jsonObject = performQuery(ApiService.USER_HTTP, nameValuePairs, HttpType.PATCH);
			return jsonObject.toString();
		}
    }
    
    public static class GetMatchesQuery extends AsyncTask<Void, Void, List<User>> {	
    	
    	private Set<String> tags;
    	private String percentage;
    	
    	public GetMatchesQuery(Set<String> tags, String percentage) {
			this.tags = tags;
			this.percentage = percentage;
		}
    	
		@Override
		protected List<User> doInBackground(Void... params) {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for(String tag : tags) {
				nameValuePairs.add(new BasicNameValuePair("tags[]", tag));
			}	
			nameValuePairs.add(new BasicNameValuePair(":percentage[]", percentage));
			JSONObject jsonObject = performQuery(ApiService.MATCHES_HTTP, nameValuePairs, HttpType.GET);
			List<User> users = new LinkedList<User>();
			
			
			try {
				JSONArray array = jsonObject.getJSONArray("users");
				for(int i = 0 ; i < array.length() ; i++){
					JSONObject juser = array.getJSONObject(i);
					String name = juser.getString("name");
					JSONObject id = juser.getJSONObject("id");
					String userID = id.getString("$oid");
					User user = UsersAbstractFactory.createOrGetUser(name, new LinkedList<Tag>(), "", null, null, userID, null, false, false, false, null, false);
				    users.add(user);
				    
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return users;
		}
    }
    
    public static class GetMyTagsQuery extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... voids) {
			JSONObject jsonObject = performQuery(ApiService.TAGS_HTTP, new ArrayList<NameValuePair>(), HttpType.GET);
			return jsonObject.toString();
		}
    }
    
    public static class UpdateMyTagsQuery extends AsyncTask<Void, Void, String> {

    	private Set<String> tags;
    	
		public UpdateMyTagsQuery(Set<String> tags) {
			this.tags = tags;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for(String tag : tags) {
				nameValuePairs.add(new BasicNameValuePair("tags[]", tag));
			}
			JSONObject jsonObject = performQuery(ApiService.TAG_HTTP, nameValuePairs, HttpType.PATCH);
			return jsonObject.toString();
		}
    	
    }
    
    public static class InviteUserQuery extends AsyncTask<Void, Void, String> {
    	
    	private String userId;
    	private String message;
    	
    	public InviteUserQuery(String userId, String message) {
			this.userId = userId;
			this.message = message;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
            nameValuePairs.add(new BasicNameValuePair("message", message));
			JSONObject jsonObject = performQuery(ApiService.INVITE_HTTP, nameValuePairs, HttpType.POST);
			return jsonObject.toString();
		}
    }

    public static class GetIncomingInvitationsQuery extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... voids) {
			JSONObject jsonObject = performQuery(ApiService.INCOMING_INVITATIONS_HTTP, new ArrayList<NameValuePair>(), HttpType.GET);
			return jsonObject.toString();
		}
    }
    
    public static class GetOutcomingInvitationsQuery extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... voids) {
			JSONObject jsonObject = performQuery(ApiService.OUTCOMING_INVITATIONS_HTTP, new ArrayList<NameValuePair>(), HttpType.GET);
			return jsonObject.toString();
		}
    }
    
    public static class AcceptInvitationQuery extends AsyncTask<Void, Void, String> {
		
    	private String invitationId;
    	
    	public AcceptInvitationQuery(String invitationId) {
			this.invitationId = invitationId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("invitation_id", invitationId));
			JSONObject jsonObject = performQuery(ApiService.ACCEPT_INVITATION_HTTP, nameValuePairs, HttpType.PATCH);
			return jsonObject.toString();
		}
    }
    
    public static class RejectInvitationQuery extends AsyncTask<Void, Void, String> {
    	
    	private String invitationId;
    	
    	public RejectInvitationQuery(String invitationId) {
			this.invitationId = invitationId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("invitation_id", invitationId));
			JSONObject jsonObject = performQuery(ApiService.REJECT_INVITATION_HTTP, nameValuePairs, HttpType.PATCH);
			return jsonObject.toString();
		}
    }
    
    /**
     * If error return null; -- For now {"nojson"=";("} (todo in future alteration)
     * 
     * @param httpAddress
     * @param nameValuePairs
     * @return
     */
    private static JSONObject performQuery(String httpAddress, List<NameValuePair> nameValuePairs, HttpType type) {
        JSONObject jsonObject = null;
        try {
        	HttpRequestBase base = getHttpRequestBase(type, httpAddress);
        	if(type != HttpType.GET) {
        		((HttpPost)base).setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	}
        	if(token != null && !token.isEmpty()) {
        		base.addHeader(AUTHORIZATION_HEADER_KEY, getAutorizationHeaderValue());
        	}
            HttpResponse response = new DefaultHttpClient().execute(base);
            if(response.getEntity() != null) { //TODO: Local Domain Model or backend alterations
                InputStream stream = response.getEntity().getContent();
                StringBuilder builder = new StringBuilder();
                int code;
                while((code = stream.read()) != -1) {
                    builder.append((char)code);
                }
                jsonObject = new JSONObject(builder.toString());
            } else {
            	jsonObject = new JSONObject("{\"nojson\"=\";(\"}");
            }
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
    
    private static HttpRequestBase getHttpRequestBase(HttpType type, String httpAddress) {
    	switch(type) {
    		case GET: return new HttpGet(httpAddress);
    		case POST: return new HttpPost(httpAddress);
    		case PATCH: return new HttpPatch(httpAddress);
    		case DELETE: return new HttpDelete(httpAddress);
    	}
    	return null;
    }
    
    private static String getAutorizationHeaderValue() {
    	return AUTHORIZATION_HEADER_VALUE + token;
    }
    
    public static class AcceptInvitatinoByUserIdQuery extends AsyncTask<Void, Void, String> {

    	private String userId;
    	
		public AcceptInvitatinoByUserIdQuery(String userId) {
			super();
			this.userId = userId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(USER_ID, userId));
            JSONObject jsonObject = performQuery(ApiService.ACCEPT_INVITATION_HTTP, nameValuePairs, HttpType.PATCH);
            return jsonObject.toString();
		}
    }
    
    public static class RejectInvitatinoByUserIdQuery extends AsyncTask<Void, Void, String> {

    	private String userId;
    	
		public RejectInvitatinoByUserIdQuery(String userId) {
			super();
			this.userId = userId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(USER_ID, userId));
            JSONObject jsonObject = performQuery(ApiService.REJECT_INVITATION_HTTP, nameValuePairs, HttpType.PATCH);
            return jsonObject.toString();
		}
    }
    
    public static class BlockUserQuery extends AsyncTask<Void, Void, String> {

    	private String userId;
    	
		public BlockUserQuery(String userId) {
			super();
			this.userId = userId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(USER_ID, userId));
            JSONObject jsonObject = performQuery(ApiService.BLOCK_USER_HTTP, nameValuePairs, HttpType.POST);
            return jsonObject.toString();
		}
    }
    
    public static class UnblockUserQuery extends AsyncTask<Void, Void, String> {

    	private String userId;
    	
		public UnblockUserQuery(String userId) {
			super();
			this.userId = userId;
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(USER_ID, userId));
            JSONObject jsonObject = performQuery(ApiService.BLOCK_USER_HTTP, nameValuePairs, HttpType.DELETE);
            return jsonObject.toString();
		}
    }
}
