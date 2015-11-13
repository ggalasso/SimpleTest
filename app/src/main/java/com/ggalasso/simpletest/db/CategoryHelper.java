package com.ggalasso.simpletest.db;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class CategoryHelper {

    // Table Columns
    public static final String ca_Id = "ca_Id";
    public static final String ca_Name = "ca_Name";

    // Table name
    private static final String tableName = "category";
    private static CategoryHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private CategoryHelper() {
        columns.add(ca_Id + " TEXT primary key");
        columns.add(ca_Name + " TEXT");

        Log.i("BGCM-CAH", "Instantiated CategoryHelper");
    }

    public static CategoryHelper getInstance() {
        if (ourInstance == null) {
            synchronized (CategoryHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new CategoryHelper();
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
