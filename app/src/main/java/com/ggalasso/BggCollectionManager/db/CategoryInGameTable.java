package com.ggalasso.BggCollectionManager.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.CategoryInGameHelper;
import com.ggalasso.BggCollectionManager.model.UtilityConstants;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by truthd on 9/20/2015.
 */
public class CategoryInGameTable extends SQLController {

    public CategoryInGameTable(){

    }

    public CategoryInGameTable(Context c) {
        super(c);
    }

    public void insertAllCatergoriesInGame(Map<String, ArrayList<String>> bgCatMap ) {
        String categoryInGameTable = CategoryInGameHelper.getTableName();

        ArrayList<String> columns = new ArrayList<>();
        columns.add(CategoryInGameHelper.cg_bg_Id);
        columns.add(CategoryInGameHelper.cg_ca_Id);

        String insertSQL = getInsertSQL(bgCatMap, categoryInGameTable, columns);
        Log.d("BGCM-CT", "Bulk insert into " + categoryInGameTable + "\nSQL statement: " + insertSQL);
        //Do the insert
        super.insertToDatabase(insertSQL);
        Log.d("BGCM-CT", "Bulk insert into " + categoryInGameTable + "\nSQL statement: \n" + insertSQL);
    }

    public String getInsertSQL(Map<String, ArrayList<String>> bgCatMap, String tableName, ArrayList<String> columns) {
        String insertSQL = "INSERT OR IGNORE INTO " + tableName + " (";
        insertSQL += super.getColumns(columns);
        insertSQL += " VALUES ";
        Map<String, ArrayList<String>> treeMap = new TreeMap<>(bgCatMap);
        for (Map.Entry<String, ArrayList<String>> elem : treeMap.entrySet()) {
            String key = elem.getKey();
            insertSQL += super.getRowValues(key, elem.getValue());
        }
        insertSQL = insertSQL.substring(0, insertSQL.length() - UtilityConstants.TRIM_COMMA.getValue());
        insertSQL += ";";
        return insertSQL;
    }

}
