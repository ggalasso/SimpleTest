package com.ggalasso.BggCollectionManager.db.Schema;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class BoardGameHelper {

    // Table Columns
    public static final String bg_Id = "bg_Id";
    public static final String bg_PrimaryName = "bg_PrimaryName";
    public static final String bg_YearPub = "bg_YearPub";
    public static final String bg_Description = "bg_Description";
    public static final String bg_Thumbnail = "bg_Thumbnail";
    public static final String bg_Image = "bg_Image";
    public static final String bg_Rating = "bg_Rating";
    public static final String bg_Rank = "bg_Rank";
    public static final String bg_MinPlayers = "bg_MinPlayers";
    public static final String bg_MaxPlayers = "bg_MaxPlayers";
    public static final String bg_PlayTime = "bg_PlayTime";
    public static final String bg_MinTime = "bg_MinTime";
    public static final String bg_MaxTime = "bg_MaxTime";
    public static final String bg_MinAge = "bg_MinAge";

    // Table name
    private static final String tableName = "board_game";
    private static BoardGameHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private BoardGameHelper() {
        columns.add(bg_Id + " TEXT primary key");
        columns.add(bg_PrimaryName + " TEXT");
        columns.add(bg_YearPub + " TEXT");
        columns.add(bg_Description + " TEXT");
        columns.add(bg_Thumbnail + " TEXT");
        columns.add(bg_Image + " TEXT");
        columns.add(bg_Rating + " REAL");
        columns.add(bg_Rank + " TEXT");
        columns.add(bg_MinPlayers + " INT");
        columns.add(bg_MaxPlayers + " INT");
        columns.add(bg_PlayTime + " INT");
        columns.add(bg_MinTime + " INT");
        columns.add(bg_MaxTime + " INT");
        columns.add(bg_MinAge + " INT");

        Log.i("BGCM-BGH", "Instantiated BoardGameHelper");
    }

    public static BoardGameHelper getInstance() {
        if (ourInstance == null) {
            synchronized (BoardGameHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new BoardGameHelper();
                }
            }
        }
        return ourInstance;
    }


    public static String getTableName() {
        return tableName;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }
}
