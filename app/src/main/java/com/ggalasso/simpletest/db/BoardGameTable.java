package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.ggalasso.simpletest.controller.BoardGameManager;
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

    public Integer getTableRowCount() {

        return 0;
    }

    public boolean isBoardGameInTable(String id) {
        BoardGame boardGame = this.fetchBoardGame(id);
        if (boardGame == null) {
            return false;
        } else {
            return true;
        }
    }

    public void syncBoardGameCollection(ArrayList<BoardGame> boardGames){

        Integer testCount = fetchTableCount(BoardGameHelper.getTableName());

        if (testCount > 0){
            syncShallow(boardGames);
        } else {
            //syncDeep(boardGames); put this back after testing the shallow
            syncShallow(boardGames);
        }

        testCount = fetchTableCount(BoardGameHelper.getTableName());
    }

    private void syncDeep(ArrayList<BoardGame> boardGames) {
        // TODO : Fill this in syncDeep
    }


    private void syncShallow(ArrayList<BoardGame> boardGames) {
        boolean result = false;

        for (BoardGame game : boardGames){
            try {
                result = isBoardGameInTable(game.getId());
                if (result) {
                    Log.i("BGCM-BGT", "found the boardgame to already exist");
                    //update(game);
                } else {
                    Log.i("BGCM-BGT", "found NEW boardgame to insert in the database");
                    insert(game);
                }
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(BoardGame bg) {
        super.open();
        ContentValues cv = new ContentValues();
        cv.put(BoardGameHelper.bg_Id, bg.getId());
        cv.put(BoardGameHelper.bg_PrimaryName, bg.getPrimaryName());
        cv.put(BoardGameHelper.bg_YearPub, bg.getYearPub());
        cv.put(BoardGameHelper.bg_Description, bg.getDescription());
        cv.put(BoardGameHelper.bg_Rating, bg.getRating());
        cv.put(BoardGameHelper.bg_MinAge, bg.getMinAge());
        cv.put(BoardGameHelper.bg_MaxTime, bg.getMaxTime());
        cv.put(BoardGameHelper.bg_MinTime, bg.getMinTime());
        cv.put(BoardGameHelper.bg_PlayTime, bg.getPlayTime());
        cv.put(BoardGameHelper.bg_MaxPlayers, bg.getMaxPlayers());
        cv.put(BoardGameHelper.bg_MinPlayers, bg.getMinPlayers());
        cv.put(BoardGameHelper.bg_Rank, bg.getRank());
        cv.put(BoardGameHelper.bg_Image, bg.getImage());
        cv.put(BoardGameHelper.bg_Thumbnail, bg.getThumbnail());
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
        else {filter = new String(BoardGameHelper.bg_Id + " = " + id);}

        String[] columns = new String[]{
                BoardGameHelper.bg_Id,
                BoardGameHelper.bg_PrimaryName,
                BoardGameHelper.bg_YearPub,
                BoardGameHelper.bg_Description,
                BoardGameHelper.bg_Thumbnail,
                BoardGameHelper.bg_Image,
                BoardGameHelper.bg_Rating,
                BoardGameHelper.bg_Rank,
                BoardGameHelper.bg_MinPlayers,
                BoardGameHelper.bg_MaxPlayers,
                BoardGameHelper.bg_PlayTime,
                BoardGameHelper.bg_MinTime,
                BoardGameHelper.bg_MaxTime,
                BoardGameHelper.bg_MinAge
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
                BoardGame bg = new BoardGame(
                        cursor.getString(0)
                        , cursor.getString(1)
                        , cursor.getString(2)
                        , cursor.getString(3)
                        , cursor.getString(4)
                        , cursor.getString(5)
                        , cursor.getDouble(6)
                        , cursor.getString(7)
                        , cursor.getInt(8)
                        , cursor.getInt(9)
                        , cursor.getInt(10)
                        , cursor.getInt(11)
                        , cursor.getInt(12)
                        , cursor.getInt(13)
                );
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
        cv.put(BoardGameHelper.bg_PrimaryName, bg.getPrimaryName());

        int i = database.update(
                BoardGameHelper.getTableName(),
                cv,
                BoardGameHelper.bg_Id + " = " + bg.getId(),
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
        database.delete(BoardGameHelper.getTableName(), BoardGameHelper.bg_Id + " = " + id, null);
        Log.d("BGCM-BGT", "Successfully deleted " + id + " as STRING");
        super.close();
    }

    public void delete(BoardGame bg) {
        super.open();
        database.delete(BoardGameHelper.getTableName(), BoardGameHelper.bg_Id + " = " + bg.getId(), null);
        Log.d("BGCM-BGT", "Successfully deleted " + bg.getId() + " as OBJECT");
        super.close();
    }

    public ArrayList<String> fetchAllGameIds() {
        super.open();
        ArrayList<String> results = new ArrayList<String>();
        String[] columns = new String[]{
                BoardGameHelper.bg_Id,
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
