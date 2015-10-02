package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ggalasso.simpletest.model.BoardGame;

import java.sql.SQLException;

/**
 * Created by Edward on 9/8/2015.
 */
public class SQLController{
    Context context;
    SQLiteDatabase database;
    private DBhelper dbHelper;

    public SQLController(Context ctx){
        context = ctx;
        //dbHelper = new DBhelper(context);
    }

    private SQLController openConnection() throws SQLException {
        //TODO: Learn more about passing the context and whether we instantiate multiple DB Helpers and by default multiple Board Game Helpers.
        dbHelper = DBhelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void open()  {
        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        dbHelper.close();
    }


    public void destroyEverything() {
        open();
        dbHelper.deleteDatabase(context);
        close();
    }

    public Cursor fetchFromDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = database.query(
                table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
        );
        if (cursor != null) {cursor.moveToFirst();}
        return cursor;
    }
}
