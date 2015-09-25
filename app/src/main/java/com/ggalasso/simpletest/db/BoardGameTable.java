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
        BoardGame boardGame = this.fetchBoardGame(id);
        if (boardGame == null) {
            return false;
        } else {
            return true;
        }
    }

    public void insert(BoardGame bg) {
        super.open();
        ContentValues cv = new ContentValues();
        cv.put(BoardGameHelper.Id, bg.getId());
        cv.put(BoardGameHelper.Name, bg.getPrimaryName());
        database.insert(BoardGameHelper.getTableName(), null, cv);
        Log.d("BGCM-BGT", "Successfully added " + bg.getId());
        super.close();
    }

    public ArrayList<BoardGame> fetchAllBoardGames() {
        return fetch_impl(null);
    }

    public BoardGame fetchBoardGame(String id) {
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

        if (id == null) { filter = null;}
        else {filter = new String(BoardGameHelper.Id + " = " + id);}

        String[] columns = new String[]{
                BoardGameHelper.Id,
                BoardGameHelper.Name,
        };
        super.open();
        Cursor cursor = database.query(
                BoardGameHelper.getTableName(),
                columns,
                filter,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                BoardGame bg = new BoardGame(cursor.getString(0), cursor.getString(1));
                results.add(bg);
            }
            Log.d("BGCM-BGT", "Board game list size: " + results.size());
        }
        super.close();
        return results;
    }

    public int update(BoardGame bg) {
        super.open();
        ContentValues cv = new ContentValues();
        //cv.put(BoardGameHelper.Id, bg.getId());     /* Not necessary to include the Id. Everything else is necessary though */
        cv.put(BoardGameHelper.Name, bg.getPrimaryName());

        int i = database.update(
                BoardGameHelper.getTableName(),
                cv,
                BoardGameHelper.Id + " = " + bg.getId(),
                null
        );
        super.close();
        return i;
    }

    public void deleteAll() {
        database.delete(BoardGameHelper.getTableName(), null, null);
        Log.d("BGCM-BGT", "Successfully deleted all items from Board Game Table");
    }

    public void delete(String id) {
        super.open();
        database.delete(BoardGameHelper.getTableName(), BoardGameHelper.Id + " = " + id, null);
        Log.d("BGCM-BGT", "Successfully deleted " + id + " as STRING");
        super.close();
    }

    public void delete(BoardGame bg) {
        super.open();
        database.delete(BoardGameHelper.getTableName(), BoardGameHelper.Id + " = " + bg.getId(), null);
        Log.d("BGCM-BGT", "Successfully deleted " + bg.getId() + " as OBJECT");
        super.close();
    }

    public ArrayList<String> fetchAllGameIds() {
        super.open();
        ArrayList<String> results = new ArrayList<String>();
        String[] columns = new String[]{
                BoardGameHelper.Id,
        };

        Cursor cursor = database.query(
                BoardGameHelper.getTableName(),
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
            Log.d("BGCM-BGT", "All game Id list size: " + results.size());
        }
        super.close();
        return results;
    }
}
