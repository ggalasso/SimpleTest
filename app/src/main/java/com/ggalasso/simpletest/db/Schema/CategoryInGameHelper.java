package com.ggalasso.simpletest.db.Schema;

import android.util.Log;

import com.ggalasso.simpletest.model.BoardGame;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class CategoryInGameHelper {

    // Table Columns
    public static final String cg_bg_Id = "cg_bg_Id";
    public static final String cg_ca_Id = "cg_ca_Id";

    // Table name
    private static final String tableName = "category_in_game";
    private static CategoryInGameHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private CategoryInGameHelper() {
        String bg_table_name = BoardGameHelper.getTableName();
        String bg_Id = BoardGameHelper.bg_Id;
        String ca_table_name = CategoryHelper.getTableName();
        String ca_Id = CategoryHelper.ca_Id;

        columns.add(cg_bg_Id + " TEXT");
        columns.add(cg_ca_Id + " TEXT");
        columns.add("FOREIGN KEY(cg_bg_Id) REFERENCES " + bg_table_name + "(" + bg_Id + ")");
        columns.add("FOREIGN KEY(cg_ca_Id) REFERENCES " + ca_table_name + "(" + ca_Id + ")");

        Log.i("BGCM-CIG", "Instantiated CategoryHelper");
    }

    public static CategoryInGameHelper getInstance() {
        if (ourInstance == null) {
            synchronized (CategoryInGameHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new CategoryInGameHelper();
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
