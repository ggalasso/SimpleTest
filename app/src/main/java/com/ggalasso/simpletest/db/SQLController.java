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
    private DBhelper dbHelper;
    Context context;
    SQLiteDatabase database;

    public SQLController(Context c){
        context = c;
    }

    private SQLController openConnection() throws SQLException {
        dbHelper = new DBhelper(context);
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

    public Cursor fetch() {
        return fetch_impl(null);
    }

    public Cursor fetch(BoardGame bg) {
        return fetch_impl(bg);
    }

    public Cursor fetch_impl(BoardGame bg) {

        String filter;

        if (bg == null)     { filter = null; }
        else                { filter = new String(DBhelper._Id + " = " + bg.getId()); }

        String[] columns = new String[]{
                DBhelper._Id,
                DBhelper.Name,
        };

        Cursor cursor = database.query(
                DBhelper.Table_Name,
                columns,
                filter,
                null,
                null,
                null,
                null);

        //if (cursor != null) { cursor.moveToFirst(); }


        return cursor;
    }

    public void delete(String _id){
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + _id, null);
    }

    public void delete(BoardGame bg){
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + bg.getId(), null);
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
