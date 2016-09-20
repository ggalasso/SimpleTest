package com.ggalasso.BggCollectionManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.BggCollectionManager.api.ImageService;
import com.ggalasso.BggCollectionManager.db.Schema.BoardGameHelper;
import com.ggalasso.BggCollectionManager.model.BoardGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by truthd on 9/20/2015.
 */
public class BoardGameTable extends SQLController {

    //Used empty constructor for unit tests. Calling it otherwise will cause the application to crash
    //since a context is needed in the super class (SQLController).
    public BoardGameTable(){

    }

    public BoardGameTable(Context c) {
        super(c);
    }

    public int fetchBoardGameCount(){
        return fetchTableCount(BoardGameHelper.getTableName());
    }

    public void deleteAllRowsFromTable(){
        deleteAllRowsFromTable(BoardGameHelper.getTableName());
    }

    public void insert(BoardGame bg) {
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
        cv.put(BoardGameHelper.bg_ThumbnailPath, bg.getThumbnailPath());

        super.insertToDatabase(BoardGameHelper.getTableName(), cv);
        Log.d("BGCM-BGT", "Successfully added " + bg.getId());
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

        String filter = id == null ? null : new String(BoardGameHelper.bg_Id + " = " + id);

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
                BoardGameHelper.bg_MinAge,
                BoardGameHelper.bg_ThumbnailPath
        };

        open();
        Cursor cursor = super.executeDBQuery(BoardGameHelper.getTableName(), columns, filter, null, null, null, null);

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
                        , cursor.getString(14)
                );
                results.add(bg);
           }

            Log.d("BGCM-BGT", "Board game list size: " + results.size());
        }
        close();
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

    public void delete(String id) {
        super.open();
        Integer result = database.delete(BoardGameHelper.getTableName(), BoardGameHelper.bg_Id + " = " + id, null);
        if (result > 0) {
            Log.d("BGCM-BGT", "Successfully deleted boardgame " + id + " as STRING");
        } else {
            Log.d("BGCM-BGT", "Unable to delete boardgame, STRING id: " + id + "; Result = " + result);
        }
        super.close();
    }

    public void delete(BoardGame bg) {
        super.open();
        Integer result = database.delete(BoardGameHelper.getTableName(), BoardGameHelper.bg_Id + " = " + bg.getId(), null);
        if (result > 0) {
            Log.d("BGCM-BGT", "Successfully deleted boardgame " + bg.getId() + " as OBJECT");
        } else {
            Log.d("BGCM-BGT", "Unable to delete boardgame, OBJECT id: " + bg.getId());
        }
        super.close();
    }

    public ArrayList<String> fetchAllGameIds() {
        open();
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
        close();
        return results;
    }
}
