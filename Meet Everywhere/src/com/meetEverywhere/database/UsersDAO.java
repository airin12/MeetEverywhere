package com.meetEverywhere.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;
import com.meetEverywhere.common.UsersAbstractFactory;
import com.meetEverywhere.common.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class UsersDAO extends SQLiteOpenHelper implements LocalDAO {

	/*
     * TODO: 5 tabel: Accounts <<-- osobna baza danych? <done> Users <done> Tags
	 * <done> Messages <done> Invitations <not necessary, merged with Users> <-
	 * undo merging
	 */

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "Users";
    private static final String COL_USERS_USER_ID = "UserId";
    private static final String COL_USERS_NICKNAME = "Nickname";
    private static final String COL_USERS_DESCRIPTION = "Description";
    private static final String COL_USERS_PICTURE = "Picture";
    private static final String COL_USERS_IS_ACQUAINTANCE = "Acquaintance";
    private static final String COL_USERS_IS_INVITED = "IsInvited";
    private static final String COL_USERS_IS_BLOCKED = "IsBlocked";
    private static final String COL_USERS_INCOMING_INVITATION_MESSAGE = "IncomingInvitationMessage";
    private static final String COL_USERS_SYNCED_WITH_SERVER = "SyncedWithServer";
    private static final String TABLE_MESSAGES = "Messages";
    private static final String COL_MESSAGES_ID = "Id";
    private static final String COL_MESSAGES_TEXT = "Text";
    private static final String COL_MESSAGES_FROM_USER_ID = "FromUserID";
    private static final String COL_MESSAGES_RECIPIENT_USER_ID = "RecipientUserID";
    private static final String COL_MESSAGES_TYPE = "Type";
    private static final String TABLE_TAGS = "Tags";
    private static final String COL_TAG_ID = "Id";
    private static final String COL_TAG_TEXT = "Text";
    private static final String COL_TAG_USER_ID = "UserId";
    private static SQLiteDatabase database = null;
    private static UsersDAO instance = null;
    private List<String> usersIdSavedInDBInRuntime;
    private String userID;

    private UsersDAO() {
        super(AccountsDAO.getInstance(null).getContext(), AccountsDAO
                        .getInstance(null).getPickedAccountToken() + ".db", null,
                DATABASE_VERSION);
        if (database == null) {
            userID = AccountsDAO.getInstance(null).getPickedAccountID();
            usersIdSavedInDBInRuntime = new ArrayList<String>();
            database = getWritableDatabase();
        }
    }

    public static UsersDAO getInstance(String userToken) {
        if (instance == null) {
            instance = new UsersDAO();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

// @formatter:off
        String createUsersTableQuery = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USERS_USER_ID + " TEXT PRIMARY KEY, "
                + COL_USERS_NICKNAME + " TEXT NOT NULL, "
                + COL_USERS_DESCRIPTION + " TEXT NOT NULL, "
                + COL_USERS_PICTURE + " BLOB, "
                + COL_USERS_IS_ACQUAINTANCE + " INT NOT NULL, "
                + COL_USERS_IS_INVITED + " INT NOT NULL, "
                + COL_USERS_IS_BLOCKED + " INT NOT NULL, "
                + COL_USERS_INCOMING_INVITATION_MESSAGE + " TEXT, "
                + COL_USERS_SYNCED_WITH_SERVER + " INT NOT NULL "
                + ")";

        String createMessagesTableQuery = "CREATE TABLE " + TABLE_MESSAGES + " ("
                + COL_MESSAGES_ID + " INT AUTO_INCREMENT, "
                + COL_MESSAGES_TEXT + " TEXT NOT NULL, "
                + COL_MESSAGES_FROM_USER_ID + " TEXT NOT NULL, "
                + COL_MESSAGES_RECIPIENT_USER_ID + " TEXT NOT NULL, "
                + COL_MESSAGES_TYPE + " TEXT NOT NULL "
                + ")";

        String createTagsTableQuery = "CREATE TABLE " + TABLE_TAGS + " ("
                + COL_TAG_ID + " INT AUTO_INCREMENT, "
                + COL_TAG_TEXT + " TEXT NOT NULL, "
                + COL_TAG_USER_ID + " TEXT NOT NULL "
                + ")";
// @formatter:on

        db.execSQL(createTagsTableQuery);
        db.execSQL(createMessagesTableQuery);
        db.execSQL(createUsersTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean saveUser(User user) {

        ContentValues userValues = new ContentValues();

        userValues.put(COL_USERS_NICKNAME, user.getNickname());
        userValues.put(COL_USERS_DESCRIPTION, user.getDescription());
        userValues.put(COL_USERS_USER_ID, user.getUserID());
        userValues.put(COL_USERS_PICTURE, user.getPictureAsByteArray());
        userValues
                .put(COL_USERS_IS_ACQUAINTANCE, user.isAcquaintance() ? 1 : 0);
        userValues.put(COL_USERS_IS_BLOCKED, user.isBlocked() ? 1 : 0);
        userValues.put(COL_USERS_INCOMING_INVITATION_MESSAGE,
                user.getInvitationMessage());
        userValues.put(COL_USERS_IS_INVITED, user.isInvited() ? 1 : 0);
        userValues.put(COL_USERS_SYNCED_WITH_SERVER, 0);

        try {
            database.beginTransaction();
            database.delete(TABLE_USERS,
                    COL_USERS_USER_ID + "='" + user.getUserID() + "'", null);
            if (database.insert(TABLE_USERS, null, userValues) == -1 && database.replace(TABLE_USERS, null, userValues) == -1) {
                Log.i("DB", "inserting user row failed.");
                return false;
            }

            if (!saveHashtags(user.getHashTags(), user)) {
                Log.i("DB", "saving hashtags failed.");
                return false;
            }
            database.setTransactionSuccessful();
            return true;
        } finally {
            database.endTransaction();
        }
    }

    public User getUserById(String recipientUserID) {
//@formatter:off
        String accountQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COL_USERS_USER_ID + "='" + recipientUserID + "'";
//@formatter:on	

        Cursor usersResult = database.rawQuery(accountQuery, null);

        if (usersResult.getCount() == 0) {
            return null;
        } else {
            usersResult.moveToFirst();

            int indexDescription = usersResult
                    .getColumnIndex(COL_USERS_DESCRIPTION);
            int indexPicture = usersResult.getColumnIndex(COL_USERS_PICTURE);
            int indexNickname = usersResult.getColumnIndex(COL_USERS_NICKNAME);
            int indexIsSyncedWithServer = usersResult
                    .getColumnIndex(COL_USERS_SYNCED_WITH_SERVER);
            int indexIsAcquaintance = usersResult
                    .getColumnIndex(COL_USERS_IS_ACQUAINTANCE);
            int indexIsBlocked = usersResult
                    .getColumnIndex(COL_USERS_IS_BLOCKED);
            int indexIsInvited = usersResult
                    .getColumnIndex(COL_USERS_IS_INVITED);
            int indexIncomingInvitationMessage = usersResult
                    .getColumnIndex(COL_USERS_INCOMING_INVITATION_MESSAGE);

            String nickname = usersResult.getString(indexNickname);
            String description = usersResult.getString(indexDescription);
            byte[] picture = usersResult.getBlob(indexPicture);
            boolean isAcquaintance = usersResult.getInt(indexIsAcquaintance) > 0;
            boolean isBlocked = usersResult.getInt(indexIsBlocked) > 0;
            boolean isInvited = usersResult.getInt(indexIsInvited) > 0;
            String incomingInvitationMessage = usersResult
                    .getString(indexIncomingInvitationMessage);
            boolean isSyncedWithServer = usersResult
                    .getInt(indexIsSyncedWithServer) > 0;

            List<Tag> hashTags = getHashtagsByID(userID);

            User user = UsersAbstractFactory.createOrGetUser(nickname, hashTags, description, null,
                    picture, recipientUserID, null, isAcquaintance, isBlocked,
                    isInvited, incomingInvitationMessage, isSyncedWithServer);

            return user;
        }
    }

    public List<User> getAllAcquaintances() {
        return genericAllUsersWithFlagGetter(COL_USERS_IS_ACQUAINTANCE);
    }

    public List<User> getAllBlocked() {
        return genericAllUsersWithFlagGetter(COL_USERS_IS_BLOCKED);
    }

    public List<User> getAllInvited() {
        return genericAllUsersWithFlagGetter(COL_USERS_IS_INVITED);
    }

    public List<User> getAllInvitationReceived() {
//@formatter:off		
        String usersQuery = "SELECT " + COL_USERS_USER_ID + " FROM " + TABLE_USERS + " WHERE "
                + COL_USERS_INCOMING_INVITATION_MESSAGE + " IN NOT NULL";
//@formatter:on		
        Cursor usersResult = null;

        try {
            List<User> users = new ArrayList<User>();

            usersResult = database.rawQuery(usersQuery, null);

            int indexUserId = usersResult.getColumnIndex(COL_USERS_USER_ID);
            while (usersResult.moveToNext()) {
                String userId = usersResult.getString(indexUserId);
                User user = getUserById(userId);
                users.add(user);
            }
            return users;
        } finally {
            if (usersResult != null) {
                usersResult.close();
            }
        }

    }

	/*
	 * TODO: zamienieæ wyra¿enie na
	 * !genericAllUsersWithFlagGetter(COL_USERS_SYNCED_WITH_SERVER) public
	 * List<User> getAllNotSyncedWithServer(){ return
	 * genericAllUsersWithFlagGetter(COL_USERS_SYNCED_WITH_SERVER); }
	 */

    private List<User> genericAllUsersWithFlagGetter(String columnName) {
//@formatter:off		
        String usersQuery = "SELECT " + COL_USERS_USER_ID + " FROM " + TABLE_USERS + " WHERE "
                + columnName + "='1'";
//@formatter:on		
        Cursor usersResult = null;

        try {
            List<User> users = new ArrayList<User>();

            usersResult = database.rawQuery(usersQuery, null);

            int indexUserId = usersResult.getColumnIndex(COL_USERS_USER_ID);
            while (usersResult.moveToNext()) {
                String userId = usersResult.getString(indexUserId);
                User user = getUserById(userId);
                users.add(user);
            }
            return users;
        } finally {
            if (usersResult != null) {
                usersResult.close();
            }
        }
    }

    public List<Tag> getHashtagsByID(String recipientUserID) {

//@formatter:off
        String tagsQuery = "SELECT * FROM " + TABLE_TAGS + " WHERE "
                + COL_TAG_USER_ID + "='" + recipientUserID + "'";
//@formatter:on
        Cursor accountResult = null;

        try {
            List<Tag> tagsList = new ArrayList<Tag>();

            accountResult = database.rawQuery(tagsQuery, null);

            int indexHashTags = accountResult.getColumnIndex(COL_TAG_TEXT);
            while (accountResult.moveToNext()) {
                String hashtag = accountResult.getString(indexHashTags);
                Tag tag = new Tag(hashtag, recipientUserID);
                tagsList.add(tag);
            }

            return tagsList;
        } finally {
            if (accountResult != null) {
                accountResult.close();
            }
        }
    }

    public boolean saveHashtags(List<Tag> hashtags, User user) {
        // List<Tag> hashtags = user.getHashTags();
        String recipientUserID = user.getUserID();

        try {
            database.beginTransaction();
            database.delete(TABLE_TAGS, COL_TAG_USER_ID + "='"
                    + recipientUserID + "'", null);

            for (Tag tag : hashtags) {
                ContentValues tags = new ContentValues();
                tags.put(COL_TAG_TEXT, tag.getName());
                tags.put(COL_TAG_USER_ID, recipientUserID);
                if (database.insert(TABLE_TAGS, null, tags) == -1) {
                    return false;
                }
            }
            database.setTransactionSuccessful();
            return true;
        } finally {
            database.endTransaction();
        }
    }

    public boolean saveMessage(Message message, User user) {

        if (!isUserIdAlreadyInFilterList(user)) {
            saveUser(user);
        }

        try {
            database.beginTransaction();

            ContentValues messageValues = new ContentValues();
            messageValues.put(COL_MESSAGES_TEXT, message.getText());
            messageValues.put(COL_MESSAGES_FROM_USER_ID, message.getFrom());
            messageValues.put(COL_MESSAGES_RECIPIENT_USER_ID,
                    message.getRecipient());
            messageValues.put(COL_MESSAGES_TYPE, message.getType());
            if (database.insert(TABLE_MESSAGES, null, messageValues) == -1) {
                return false;
            }
            database.setTransactionSuccessful();
            return true;
        } finally {
            database.endTransaction();
        }
    }

    // Metoda sprawdzaj¹ca cachuj¹ca zapisywane userID, aby nie zapisywaæ Usera
    // przy ka¿dej wiadomoœci.
    private boolean isUserIdAlreadyInFilterList(User user) {
        if (usersIdSavedInDBInRuntime.contains(user.getUserID())) {
            return true;
        } else {
            usersIdSavedInDBInRuntime.add(user.getUserID());
            return false;
        }

    }

    public boolean removeUser(User user) {
        try {
            database.beginTransaction();
            if (database.delete(TABLE_USERS,
                    COL_USERS_USER_ID + "='" + user.getUserID() + "'", null) == 1) {
                database.setTransactionSuccessful();
                return true;
            }
        } finally {
            database.endTransaction();
        }
        return false;
    }

    public List<User> getAllUsers() {
//@formatter:off		
        String usersQuery = "SELECT " + COL_USERS_USER_ID + " FROM " + TABLE_USERS;
//@formatter:on		
        Cursor usersResult = null;

        try {
            List<User> users = new ArrayList<User>();

            usersResult = database.rawQuery(usersQuery, null);

            int indexUserId = usersResult.getColumnIndex(COL_USERS_USER_ID);
            while (usersResult.moveToNext()) {
                String userId = usersResult.getString(indexUserId);
                User user = getUserById(userId);
                users.add(user);
            }
            return users;
        } finally {
            if (usersResult != null) {
                usersResult.close();
            }
        }
    }

}