package com.ggalasso.BggCollectionManager.db.Schema;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by truthd on 9/24/2015.
 */
public class MechanicInGameHelper {

    // Table Columns
    public static final String mg_bg_Id = "mg_bg_Id";
    public static final String mg_me_Id = "mg_me_Id";

    // Table name
    private static final String tableName = "mechanic_in_game";
    private static MechanicInGameHelper ourInstance = null;
    // other columns here
    private ArrayList<String> columns = new ArrayList<String>();

    private MechanicInGameHelper() {
        String bg_table_name = BoardGameHelper.getTableName();
        String bg_Id = BoardGameHelper.bg_Id;
        String me_table_name = MechanicHelper.getTableName();
        String me_Id = MechanicHelper.me_Id;

        columns.add(mg_bg_Id + " TEXT");
        columns.add(mg_me_Id + " TEXT");
        columns.add("FOREIGN KEY(mg_bg_Id) REFERENCES " + bg_table_name + "(" + bg_Id + ") ON DELETE CASCADE");
        columns.add("FOREIGN KEY(mg_me_Id) REFERENCES " + me_table_name + "(" + me_Id + ") ON DELETE CASCADE");

        Log.i("BGCM-MIG", "Instantiated MechanicHelper");
    }

    public static MechanicInGameHelper getInstance() {
        if (ourInstance == null) {
            synchronized (MechanicInGameHelper.class) {
                if (ourInstance == null) {
                    ourInstance = new MechanicInGameHelper();
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
