package com.ggalasso.simpletest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Edward on 9/8/2015.
 */
public class DBhelper extends SQLiteOpenHelper{
    // Table name
    public static final String Table_Name = "board_game";

    // Table Columns
    public static final String _Id = "_id";
    public static final String Name = "Name";
    // other columns here

    // Database Information
    public static final String DB_Name = "bgm_db";

    // Database Version
    static final int DB_Version = 1;

    // Create Table Query
    public String CREATE_QUERY = "CREATE TABLE " + Table_Name + " ("
            + _Id + " TEXT primary key, "
            + Name + " TEXT );";

    public DBhelper(Context context){
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
}
