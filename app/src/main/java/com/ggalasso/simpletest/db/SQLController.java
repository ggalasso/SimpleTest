package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ggalasso.simpletest.controller.BoardGameManager;
import com.ggalasso.simpletest.model.BoardGame;

import java.sql.SQLException;

/**
 * Created by Edward on 9/8/2015.
 */
public class SQLController{
    private DBhelper dbHelper;
    private Context outContext;
    private SQLiteDatabase database;

    public SQLController(Context c){
        outContext = c;
    }

    public SQLController open() throws SQLException {
        dbHelper = new DBhelper(outContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(BoardGame bg) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper._Id, bg.getId());
        cv.put(DBhelper.Name, bg.getPrimaryName());
        database.insert(DBhelper.Table_Name, null, cv);
    }

    public Cursor fetch() {
        return fetch_impl(null);
    }

    public Cursor fetch(BoardGame bg) {
        return fetch_impl(bg);
    }

    public Cursor fetch_impl(BoardGame bg) {

        String filter;

        if (bg == null)
            filter = null;
        else
            filter = new String(DBhelper._Id + " = " + bg.getId());

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

        if (cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean check(BoardGame bg) {

        String ID = bg.getId();
        String name = bg.getPrimaryName();

        Cursor C = this.fetch(bg);

        if (C == null)      /* This means that the BoardGame was not in the database */
            return false;

        return C.moveToFirst();
    }

    // this is more of an example at this point
    // since we haven't defined what an update looks like yet
    public int update(BoardGame bg){
        ContentValues cv = new ContentValues();
        //cv.put(DBhelper._Id, bg.getId());     /* Not necessary to include the _Id. Everything else is necessary though */
        cv.put(DBhelper.Name, bg.getPrimaryName());

        int i = database.update(
                DBhelper.Table_Name,
                cv,
                DBhelper._Id + " = " + bg.getId(),
                null);

        return i;
    }


    public void delete(String _id){
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + _id, null);
    }

    public void delete(BoardGame bg){
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + bg.getId(), null);
    }
}
