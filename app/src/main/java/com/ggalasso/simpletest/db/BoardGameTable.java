package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ggalasso.simpletest.model.BoardGame;

import java.sql.SQLException;

/**
 * Created by truthd on 9/20/2015.
 */
public class BoardGameTable extends SQLController {

    public BoardGameTable(Context c) {
        super(c);
    }

    public boolean check(BoardGame bg) {

        String ID = bg.getId();
        String name = bg.getPrimaryName();

        Cursor C = this.fetch(bg);
        if (C == null)   { return false; } /* This means that the BoardGame was not in the database */

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

    public void insert(BoardGame bg) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper._Id, bg.getId());
        cv.put(DBhelper.Name, bg.getPrimaryName());
        database.insert(DBhelper.Table_Name, null, cv);
    }

    public void deleteAll(){
        database.delete(DBhelper.Table_Name, null, null);
    }

    public Cursor fetchAllGameIDs() {
        super.open();
        String[] columns = new String[]{
                DBhelper._Id,
        };

        Cursor cursor = database.query(
                DBhelper.Table_Name,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        //if (cursor != null) {cursor.moveToFirst();}
        return cursor;
    }

}
