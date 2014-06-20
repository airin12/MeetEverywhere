package com.meetEverywhere.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "Main.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_USER = "User";
	public static final String COLUMN_NAME_USER_ID = "Id";
	public static final String COLUMN_NAME_USER_NICKNAME = "Nickname";
	public static final String COLUMN_NAME_USER_PASSWORD = "Password";
	public static final String COLUMN_NAME_USER_TOKEN = "Token";
	public static final String COLUMN_NAME_USER_DESCRIPTION = "Description";
	public static final String COLUMN_NAME_USER_PROFILE_PICTURE = "ProfilePicture";
	public static final String COLUMN_NAME_USER_IS_INVITED = "IsInvited";
	
	public static final String TABLE_TAG = "Tag";
	public static final String COLUMN_NAME_TAG_ID = "Id";
	public static final String COLUMN_NAME_TAG_NAME = "Name";
	public static final String COLUMN_NAME_TAG_USER_ID = "UserId";
	
	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_USER + "(" + COLUMN_NAME_USER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ COLUMN_NAME_USER_NICKNAME + " TEXT NOT NULL, " + COLUMN_NAME_USER_PASSWORD + " TEXT NOT NULL, "
				+ COLUMN_NAME_USER_TOKEN + " TEXT NOT NULL, " + COLUMN_NAME_USER_DESCRIPTION + " TEXT, " 
				+ COLUMN_NAME_USER_IS_INVITED + " INTEGER NOT NULL, " + COLUMN_NAME_USER_PROFILE_PICTURE + " BLOB)");
		
		db.execSQL("CREATE TABLE " + TABLE_TAG + "(" + COLUMN_NAME_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ COLUMN_NAME_TAG_NAME + " TEXT NOT NULL, " + COLUMN_NAME_TAG_USER_ID + " INTEGER NOT NULL, FOREIGN KEY "
				+ "(" + COLUMN_NAME_TAG_USER_ID + ") REFERENCES " + TABLE_USER + " (" + COLUMN_NAME_USER_ID + "));");
		
		
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(db);
	}

}