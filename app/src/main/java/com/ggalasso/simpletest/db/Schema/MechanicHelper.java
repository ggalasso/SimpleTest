package com.ggalasso.simpletest.db.Schema;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class MechanicHelper {

    // Table Columns
    public static final String me_Id = "me_Id";
    public static final String me_Name = "me_Name";

    // Table name
    private static final String tableName = "mechanic";
    private static MechanicHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private MechanicHelper() {
        columns.add(me_Id + " TEXT primary key");
        columns.add(me_Name + " TEXT");

        Log.i("BGCM-MEH", "Instantiated MechanicHelper");
    }

    public static MechanicHelper getInstance() {
        if (ourInstance == null) {
            synchronized (MechanicHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new MechanicHelper();
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
