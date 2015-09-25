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
        //dbHelper = new DBhelper(context);
    }

    private SQLController openConnection() throws SQLException {
        //TODO: Learn more about passing the context and whether we instantiate multiple DB Helpers and by default multiple Board Game Helpers.
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

    public void dropAllTables() {
        open();
        dbHelper.dropAllTables(database);
        close();
    }

    public void createAllTables() {
        open();
        dbHelper.onCreate(database);
        close();
    }

    public void testTables() {
        open();
        dbHelper.testTables();
        close();
    }

//    public Cursor fetch() {
//        return fetch_impl(null);
//    }
//
//    public Cursor fetch(BoardGame bg) {
//        return fetch_impl(bg);
//    }
//
//    public Cursor fetch_impl(BoardGame bg) {
//
//        String filter;
//
//        if (bg == null)     { filter = null; }
//        else                { filter = new String(DBhelper._Id + " = " + bg.getId()); }
//
//        String[] columns = new String[]{
//                DBhelper._Id,
//                DBhelper.Name,
//        };
//
//        Cursor cursor = database.query(
//                DBhelper.Table_Name,
//                columns,
//                filter,
//                null,
//                null,
//                null,
//                null);
//
//        //if (cursor != null) { cursor.moveToFirst(); }
//
//
//        return cursor;
//    }
//    TODO: Make this generic by passing table and primary key of specified table
//    public void delete(String id){
//        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + id, null);
//    }
//

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
