package com.example.admat.rpgini;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admat on 12/11/2016.
 */

public class MyDB {
    private DbHelper dbHelper;

    private SQLiteDatabase database;

    public MyDB(Context context){
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public String getCharName(String username, String table){
        String[] projection = {"cname"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        String charname = "";
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            charname = c.getString(c.getColumnIndexOrThrow("cname"));
        }
        c.close();
        return charname;
    }

    public void setCharName(String username, String charname, String table){
        String sql = "UPDATE " + table + " SET cname='" + charname + "' WHERE username='" + username + "'";
        database.execSQL(sql);
    }

    public int getPhysical(String username, String table){
        String[] projection = {"physical"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        int phys = 0;
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            phys = c.getInt(c.getColumnIndexOrThrow("physical"));
        }
        c.close();
        return phys;
    }

    public void setPhysical(String username, int value, String table){
        String sql = "UPDATE " + table + " SET physical=" + value + " WHERE username='" + username + "'";
        database.execSQL(sql);
    }

    public int getMagical(String username, String table){
        String[] projection = {"magical"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        int magi = 0;
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            magi = c.getInt(c.getColumnIndexOrThrow("magical"));
        }
        c.close();
        return magi;
    }

    public void setMagical(String username, int value, String table){
        String sql = "UPDATE " + table + " SET magical=" + value + " WHERE username='" + username + "'";
        database.execSQL(sql);
    }

    public int getHealth(String username, String table){
        String[] projection = {"health"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        int health = 0;
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            health = c.getInt(c.getColumnIndexOrThrow("health"));
        }
        c.close();
        return health;
    }

    public void setHealth(String username, int value, String table){
        String sql = "UPDATE " + table + " SET health=" + value + " WHERE username='" + username + "'";
        database.execSQL(sql);
    }

    public int getLevel(String username, String table){
        String[] projection = {"level"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        int level = 0;
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            level = c.getInt(c.getColumnIndexOrThrow("level"));
        }
        c.close();
        return level;
    }

    public void setLevel(String username, int value, String table){
        String sql = "UPDATE " + table + " SET level=" + value + " WHERE username='" + username + "'";
        database.execSQL(sql);
    }

    public int getXP(String username, String table){
        String[] projection = {"xp"};
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};
        int xp = 0;
        Cursor c = database.query(table, projection, selection, selectionArgs, null, null, null);
        if(c.moveToFirst()){
            xp = c.getInt(c.getColumnIndexOrThrow("xp"));
        }
        c.close();
        return xp;
    }

    public void setXP(String username,int value, String table){
        String sql = "UPDATE " + table + " SET xp=" + value + " WHERE username='" + username + "'";
        database.execSQL(sql);
    }
}
