package com.meetEverywhere.database;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccountsDAO extends SQLiteOpenHelper implements LocalDAO{

	private static SQLiteDatabase database = null;
	
	private static final String DATABASE_FILENAME = "Accounts.db";
	private static final int DATABASE_VERSION = 1;
	private final Context context;
	private String pickedAccountToken;
	private String pickedAccountID;
	
	private static final String TABLE_ACCOUNTS = "Accounts";
	private static final String COL_ACCOUNTS_TOKEN = "Token";
	private static final String COL_ACCOUNTS_USERID = "Id";
	private static final String COL_ACCOUNTS_NICKNAME = "Nickname";
	private static final String COL_ACCOUNTS_PASSWORD = "Password";
	private static final String COL_ACCOUNTS_DESCRIPTION = "Description";
	private static final String COL_ACCOUNTS_PICTURE = "Picture";
	private static final String COL_ACCOUNTS_SYNCED_WITH_SERVER = "SyncedWithServer";
	
	private static final String TABLE_CONFIG = "Configuration";
	private static final String COL_CONFIG_TOKEN = "Token";
	private static final String COL_CONFIG_USE_BLUETOOTH = "UseBluetooth";
	private static final String COL_CONFIG_BLUETOOTH_SEARCH_FREQ = "BtSearchFreq";
	private static final String COL_CONFIG_USE_GPS = "UseGPS";
	private static final String COL_CONFIG_MAX_GPS_RADIUS = "MaxGpsRadius";
	private static final String COL_CONFIG_SERVER_POOL_FREQ = "SvPoolFreq";
	private static final String COL_CONFIG_MIN_COMPATIBILITY_PERCENT = "CompatibilityPercent";

	private static final String TABLE_TAGS = "Tags";
	private static final String COL_TAG_ID = "Id";
	private static final String COL_TAG_TEXT = "Text";
	private static final String COL_TAG_TOKEN = "Token";
	private static final String COL_TAG_SYNCED_WITH_SERVER = "SyncedWithServer";
	
	private static AccountsDAO instance = null;
	
	private AccountsDAO(Context context) {
		super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
		this.context = context;
		if (database == null) {
			database = getWritableDatabase();
		}
	}

	public static AccountsDAO getInstance(Context context){
		if(instance == null){
			instance = new AccountsDAO(context);
		}
		return instance;
	}
	
	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

//@formatter:off
		String createAccountsTableQuery = "CREATE TABLE " + TABLE_ACCOUNTS + " ("
				+ COL_ACCOUNTS_TOKEN + " TEXT NOT NULL, " 
				+ COL_ACCOUNTS_USERID + " TEXT NOT NULL, "
				+ COL_ACCOUNTS_DESCRIPTION + " TEXT, "
				+ COL_ACCOUNTS_PICTURE + " BLOB, "
				+ COL_ACCOUNTS_NICKNAME + " INT NOT NULL, "
				+ COL_ACCOUNTS_PASSWORD + " TEXT NOT NULL, "
				+ COL_ACCOUNTS_SYNCED_WITH_SERVER + " INT NOT NULL, "
				+ "UNIQUE("+ COL_ACCOUNTS_TOKEN +") ON CONFLICT FAIL "
				+ ")";
		
		String createConfigurationTableQuery = "CREATE TABLE " + TABLE_CONFIG + " (" 
				+ COL_CONFIG_TOKEN + " TEXT NOT NULL, "
				+ COL_CONFIG_USE_BLUETOOTH + " INT NOT NULL, "					//BOOLEAN 
				+ COL_CONFIG_BLUETOOTH_SEARCH_FREQ + " INT NOT NULL, "
				+ COL_CONFIG_USE_GPS + " INT NOT NULL, "						//BOOLEAN
				+ COL_CONFIG_MAX_GPS_RADIUS + " DOUBLE PRECISION NOT NULL, "
				+ COL_CONFIG_SERVER_POOL_FREQ + " INT NOT NULL, "
				+ COL_CONFIG_MIN_COMPATIBILITY_PERCENT + " INT NOT NULL, "
				+ "UNIQUE("+ COL_CONFIG_TOKEN +") ON CONFLICT FAIL "
				+ ")";

		String createTagsTableQuery = "CREATE TABLE " + TABLE_TAGS + " ("
				+ COL_TAG_ID + " INT AUTO_INCREMENT, "
				+ COL_TAG_TEXT + " TEXT NOT NULL, "
				+ COL_TAG_TOKEN + " TEXT NOT NULL, "
				+ COL_TAG_SYNCED_WITH_SERVER + " INT NOT NULL"
				+ ")";
// @formatter:on

		db.execSQL(createTagsTableQuery);
		db.execSQL(createConfigurationTableQuery);
		db.execSQL(createAccountsTableQuery);
	}

	public boolean logIn(String nickname, String password) {

		Cursor accountResult = null;
		Cursor configResult = null;
		Cursor debugResult = null;
		try {
			database.beginTransaction();
//@formatter:off		
			String accountQuery = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " 
						+ COL_ACCOUNTS_NICKNAME + "='" + nickname + "' AND "
						+ COL_ACCOUNTS_PASSWORD + "='" + password + "'";
			
			String resultQuery = "SELECT * FROM " + TABLE_ACCOUNTS;
	
/* debug, TO REMOVE */			
			debugResult = database.rawQuery(resultQuery, null);
			
			for(int i = 0; i < debugResult.getCount(); i++){
				debugResult.moveToNext();
				for(int k = 0; k < debugResult.getColumnCount(); k++){
					try{
						Log.i(debugResult.getColumnName(k), debugResult.getString(k));
					}catch(Exception e){
					}
				}
			}
/*/debug, TO REMOVE*/		

//@formatter:on

			accountResult = database.rawQuery(accountQuery, null);

			if (accountResult.getCount() == 0) {
				return false;
			} else {
				accountResult.moveToFirst();

				int indexDescription = accountResult
						.getColumnIndex(COL_ACCOUNTS_DESCRIPTION);
				int indexPicture = accountResult
						.getColumnIndex(COL_ACCOUNTS_PICTURE);
				int indexToken = accountResult
						.getColumnIndex(COL_ACCOUNTS_TOKEN);
				int indexUserID = accountResult
						.getColumnIndex(COL_ACCOUNTS_USERID);				
				
				String description = accountResult.getString(indexDescription);
				byte[] picture = accountResult.getBlob(indexPicture);
				String userToken = accountResult.getString(indexToken);
				String userID = accountResult.getString(indexUserID);
				List<Tag> hashTags = getHashtagsByToken(userToken);

				pickedAccountToken = userToken;
				pickedAccountID = userID;
				User user = new User(nickname, hashTags, description,
						userToken, picture, userID, password, false, false, false, null, false);

				Configuration config = Configuration.getInstance();
				config.setUser(user);

//@formatter:off			
			String configQuery = "SELECT * FROM " + TABLE_CONFIG + " WHERE "
					+ COL_CONFIG_TOKEN + "='" + userToken + "'";
//@formatter:on			

				configResult = database.rawQuery(configQuery, null);
				configResult.moveToFirst();

				int indexBtSearchFreq = configResult
						.getColumnIndex(COL_CONFIG_BLUETOOTH_SEARCH_FREQ);
				int indexMinCompatibilityPercent = configResult
						.getColumnIndex(COL_CONFIG_MIN_COMPATIBILITY_PERCENT);
				int indexMaxGpsRadius = configResult
						.getColumnIndex(COL_CONFIG_MAX_GPS_RADIUS);
				int indexServerPoolFreq = configResult
						.getColumnIndex(COL_CONFIG_SERVER_POOL_FREQ);
				int indexUseBt = configResult
						.getColumnIndex(COL_CONFIG_USE_BLUETOOTH);
				int indexUseGPS = configResult
						.getColumnIndex(COL_CONFIG_USE_GPS);

				boolean useGPS = configResult.getInt(indexUseGPS) > 0;
				boolean useBT = configResult.getInt(indexUseBt) > 0;
				int btSearchFreq = configResult.getInt(indexBtSearchFreq);
				double maxGpsRadius = configResult.getDouble(indexMaxGpsRadius);
				int serverPoolFreq = configResult.getInt(indexServerPoolFreq);
				int minCompatibilityPercent = configResult
						.getInt(indexMinCompatibilityPercent);

				config.setGPSUsed(useGPS);
				config.setBluetoothUsed(useBT);
				config.setBluetoothSecsTimeBetweenRefreshing(btSearchFreq);
				config.setGpsScanningRadiusInKilometres(maxGpsRadius);
				config.setServerSecsTimeBetweenRefreshing(serverPoolFreq);
				config.setDesiredTagsCompatibility(minCompatibilityPercent);

				return true;
			}
		} finally {
			if (accountResult != null) {
				accountResult.close();
			}
			if (configResult != null) {
				configResult.close();
			}
			if (debugResult != null) {
				debugResult.close();
			}

			database.endTransaction();
			
		}
	}

	public boolean register(User user) {

		ContentValues account = new ContentValues();
		ContentValues config = new ContentValues();

		account.put(COL_ACCOUNTS_NICKNAME, user.getNickname());
		account.put(COL_ACCOUNTS_PASSWORD, user.getPassword());
		account.put(COL_ACCOUNTS_DESCRIPTION, user.getDescription());
		account.put(COL_ACCOUNTS_TOKEN, user.getUserToken());
		account.put(COL_ACCOUNTS_USERID, user.getUserID());
		account.put(COL_ACCOUNTS_PICTURE, user.getPictureAsByteArray());
		account.put(COL_ACCOUNTS_SYNCED_WITH_SERVER, 0);
		
		config.put(COL_CONFIG_BLUETOOTH_SEARCH_FREQ, 60);
		config.put(COL_CONFIG_SERVER_POOL_FREQ, 60);
		config.put(COL_CONFIG_MIN_COMPATIBILITY_PERCENT, 50);
		config.put(COL_CONFIG_MAX_GPS_RADIUS, 10);
		config.put(COL_CONFIG_TOKEN, user.getUserToken());
		config.put(COL_CONFIG_USE_BLUETOOTH, 1);
		config.put(COL_CONFIG_USE_GPS, 1);

		try {
			database.beginTransaction();
			if (database.insert(TABLE_ACCOUNTS, null, account) == -1
					|| database.insert(TABLE_CONFIG, null, config) == -1) {
				return false;
			}
			database.setTransactionSuccessful();
			return true;
		} finally {
			database.endTransaction();
		}

	}

	public boolean saveConfiguration(Configuration configuration){
		
		ContentValues config = new ContentValues();
		
		config.put(COL_CONFIG_BLUETOOTH_SEARCH_FREQ, configuration.getBluetoothSecsTimeBetweenRefreshing());
		config.put(COL_CONFIG_SERVER_POOL_FREQ, configuration.getServerSecsTimeBetweenRefreshing());
		config.put(COL_CONFIG_MIN_COMPATIBILITY_PERCENT, configuration.getDesiredTagsCompatibility());
		config.put(COL_CONFIG_MAX_GPS_RADIUS, configuration.getGpsScanningRadiusInKilometres());
		config.put(COL_CONFIG_TOKEN, configuration.getUser().getUserToken());
		config.put(COL_CONFIG_USE_BLUETOOTH, configuration.isBluetoothUsed() ? 1 : 0);
		config.put(COL_CONFIG_USE_GPS, configuration.isGPSUsed() ? 1 : 0);

		try {
			database.beginTransaction();
			database.delete(TABLE_CONFIG,
					COL_CONFIG_TOKEN + "='" + configuration.getUser().getUserToken() + "'", null);
			if (database.insert(TABLE_CONFIG, null, config) == -1) {
				return false;
			}
			database.setTransactionSuccessful();
			return true;
		} finally {
			database.endTransaction();
		}		
	}
	
	
	public boolean saveUser(User user) {

		if(user.getUserToken() == null){
			return false;
		}
		
		ContentValues userValues = new ContentValues();

		userValues.put(COL_ACCOUNTS_NICKNAME, user.getNickname());
		userValues.put(COL_ACCOUNTS_PASSWORD, user.getPassword());
		userValues.put(COL_ACCOUNTS_DESCRIPTION, user.getDescription());
		userValues.put(COL_ACCOUNTS_TOKEN, user.getUserToken());
		userValues.put(COL_ACCOUNTS_USERID, user.getUserID());
		userValues.put(COL_ACCOUNTS_PICTURE, user.getPictureAsByteArray());
		userValues.put(COL_ACCOUNTS_SYNCED_WITH_SERVER, 0);
		
		try {
			database.beginTransaction();
			database.delete(TABLE_ACCOUNTS,
					COL_ACCOUNTS_TOKEN + "='" + user.getUserToken() + "'", null);
			if (database.insert(TABLE_ACCOUNTS, null, userValues) == -1) {
				Log.i("DB", "inserting user row failed.");
				return false;
			}
			if(!saveHashtags(user)){
				Log.i("DB", "saving hashtags failed.");
				return false;
			}
			database.setTransactionSuccessful();
			return true;
		} finally {
			database.endTransaction();
		}

	}

	private List<Tag> getHashtagsByToken(String token) {

//@formatter:off		
		String tagsQuery = "SELECT * FROM " + TABLE_TAGS + " WHERE " 
							+ COL_TAG_TOKEN + "='" + token + "'";
//@formatter:on		
		Cursor accountResult = null;

		try {
			List<Tag> tagsList = new ArrayList<Tag>();

			accountResult = database.rawQuery(tagsQuery, null);

			int indexHashTags = accountResult.getColumnIndex(COL_TAG_TEXT);
			while (accountResult.moveToNext()) {
				String hashtag = accountResult.getString(indexHashTags);
				Tag tag = new Tag(hashtag, null);
				tagsList.add(tag);
			}

			return tagsList;
		} finally {
			if (accountResult != null) {
				accountResult.close();
			}
		}
	}

	private boolean saveHashtags(User user) {
		List<Tag> hashtags = user.getHashTags();
		String token = user.getUserToken();

		try {
			database.beginTransaction();
			database.delete(TABLE_TAGS, COL_TAG_TOKEN + "='" + token + "'",
					null);

			for (Tag tag : hashtags) {
				ContentValues tags = new ContentValues();
				tags.put(COL_TAG_TEXT, tag.getName());
				tags.put(COL_TAG_TOKEN, token);
				tags.put(COL_TAG_SYNCED_WITH_SERVER, 0);
				if(database.insert(TABLE_TAGS, null, tags) == -1){
					return false;
				}
			}
			database.setTransactionSuccessful();
			return true;
		} finally {
			database.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Implementacja nie jest konieczna.
	}

	public boolean removeUser(User user) {
		// TODO To be implemented. One day...
		return false;
	}

	public String getPickedAccountToken() {
		return pickedAccountToken;
	}

	public String getPickedAccountID() {
		return pickedAccountID;
	}

}
