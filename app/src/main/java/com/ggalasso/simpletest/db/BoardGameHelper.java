package com.ggalasso.simpletest.db;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class BoardGameHelper {

    // Table Columns
    public static final String Id = "_id";
    public static final String Name = "Name";
    // Table name
    private static final String tableName = "board_game";
    private static BoardGameHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private BoardGameHelper() {
        columns.add(Id + " TEXT primary key");
        columns.add(Name + " TEXT");

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
