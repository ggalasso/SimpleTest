package com.ggalasso.simpletest.db;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class BoardGameHelper {

    // Table name
    private static final String tableName = "board_game";
    private ArrayList<String> columns = new ArrayList<String>();


    // Table Columns
    public static final String Id = "_id";
    public static final String Name = "Name";
    // other columns here



    public BoardGameHelper() {
        //columns.clear();
        columns.add(Id + " TEXT primary key");
        columns.add(Name + " TEXT");

    }


    public static String getTableName() {
        return tableName;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }
}
