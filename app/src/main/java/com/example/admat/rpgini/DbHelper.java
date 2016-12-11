package com.example.admat.rpgini;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by admat on 12/9/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static class EmailUserEntry implements BaseColumns {//stores a password for authentication
        public static final String TABLE_NAME = "emailusers";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PHYSICAL = "physical";
        public static final String COLUMN_NAME_MAGICAL = "magical";
        public static final String COLUMN_NAME_HEALTH = "health";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_XP = "xp";
        public static final String COLUMN_NAME_CHARNAME = "cname";
    }

    public static class FBUserEntry implements BaseColumns {//allows facebook to do authentication so doesn't store password
        public static final String TABLE_NAME = "fbusers";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PHYSICAL = "physical";
        public static final String COLUMN_NAME_MAGICAL = "magical";
        public static final String COLUMN_NAME_HEALTH = "health";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_XP = "xp";
        public static final String COLUMN_NAME_CHARNAME = "cname";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_EMAILUSERS =
            "CREATE TABLE " + EmailUserEntry.TABLE_NAME + " (" +
                    EmailUserEntry._ID + " INTEGER PRIMARY KEY," +
                    EmailUserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_PHYSICAL + INT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_MAGICAL + INT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_HEALTH + INT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_LEVEL + INT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_XP + INT_TYPE + COMMA_SEP +
                    EmailUserEntry.COLUMN_NAME_CHARNAME + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES_FBUSERS =
            "DROP TABLE IF EXISTS " + EmailUserEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_FBUSERS =
            "CREATE TABLE " + FBUserEntry.TABLE_NAME + " (" +
                    FBUserEntry._ID + " INTEGER PRIMARY KEY," +
                    FBUserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_PHYSICAL + INT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_MAGICAL + INT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_HEALTH + INT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_LEVEL + INT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_XP + INT_TYPE + COMMA_SEP +
                    FBUserEntry.COLUMN_NAME_CHARNAME + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES_EMAILUSERS =
            "DROP TABLE IF EXISTS " + FBUserEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Users.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_EMAILUSERS);
        db.execSQL(SQL_CREATE_ENTRIES_FBUSERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_EMAILUSERS);
        db.execSQL(SQL_DELETE_ENTRIES_FBUSERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
