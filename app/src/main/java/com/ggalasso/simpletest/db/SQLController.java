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
        database.insert(DBhelper.Table_Name,null,cv);
    }

    public Cursor fetch() {
        String[] columns = new String[]{
                DBhelper._Id,
                DBhelper.Name,
        };

        Cursor cursor = database.query(
                DBhelper.Table_Name,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    // this is more of an example at this point
    // since we haven't defined what an update looks like yet
    public int update(String _id, BoardGame bg){
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.Name, bg.getPrimaryName());

        int i = database.update(
                DBhelper.Table_Name,
                cv,
                DBhelper._Id + " = " + _id,
                null);

        return i;
    }

    public void delete(String _id){
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + _id, null);
    }
}
