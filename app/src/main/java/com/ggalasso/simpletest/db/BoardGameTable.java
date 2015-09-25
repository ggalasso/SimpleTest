package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.simpletest.model.BoardGame;
import com.ggalasso.simpletest.model.Name;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by truthd on 9/20/2015.
 */
public class BoardGameTable extends SQLController {

    public BoardGameTable(Context c) {
        super(c);
    }

    public boolean isBoardGameInTable(String id) {
        String ID = "Filler";//bg.getId();
//        String name = bg.getPrimaryName();

        BoardGame boardGame = this.fetchBoardGame(id);
        if (boardGame == null) {
            return true;
        } else {
            return false;
        }
    }

    public void insert(BoardGame bg) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper._Id, bg.getId());
        cv.put(DBhelper.Name, bg.getPrimaryName());
        database.insert(DBhelper.Table_Name, null, cv);
        Log.d("BGCM", "Successfully added " + bg.getId());
    }

    public ArrayList<BoardGame> fetchAllBoardGames() {
        return fetch_impl(null);
    }

    public BoardGame fetchBoardGame(String id) {
        String blah = "filler";
        ArrayList<BoardGame> item = fetch_impl(id);
        if (item.size() > 0) {
            BoardGame result = item.get(0);
            return result;
        }
        return null;
    }

    public ArrayList<BoardGame> fetch_impl(String id) {

        ArrayList<BoardGame> results = new ArrayList<BoardGame>();

        String filter;

        if (id == null) {
            filter = null;
        } else {
            filter = new String(DBhelper._Id + " = " + id);
        }

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
        boolean test = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("BGCM-BGT", "Cursor 0: " + cursor.getString(0));
                Log.d("BGCM-BGT", "Cursor 1: " + cursor.getString(1));
                BoardGame bg = new BoardGame(cursor.getString(0), cursor.getString(1));
                Log.d("BGCM-BGT", "BG is now: " + bg.getId() + ";" + bg.getPrimaryName());
                //
                //  STOPPED HERE
                //  Figure out why it crashes when constructing the name in the
                //         BoardGame Constructor
                //         It was the null ArrayList in BG object.
                //
                test = results.add(bg);
                Log.d("BGCM-BGT", "Array List size: " + results.size());
            }
        }

        return results;
    }

    public int update(BoardGame bg) {
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

    public void deleteAll() {
        database.delete(DBhelper.Table_Name, null, null);
        Log.d("BGCM", "Successfully deleted all items from Board Game Table");
    }

    public void delete(String _id) {
        super.open();
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + _id, null);
        Log.d("BGCM", "Successfully deleted " + _id + " as STRING");
        super.close();
    }

    public void delete(BoardGame bg) {
        super.open();
        database.delete(DBhelper.Table_Name, DBhelper._Id + " = " + bg.getId(), null);
        Log.d("BGCM", "Successfully deleted " + bg.getId() + " as OBJECT");
        super.close();
    }

    public ArrayList<String> fetchAllGameIDs() {
        super.open();
        ArrayList<String> results = new ArrayList<String>();
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
        if (cursor != null) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
            Log.d("BGCM", "TOTAL: " + results.size());
        }
        super.close();
        return results;
    }
}
