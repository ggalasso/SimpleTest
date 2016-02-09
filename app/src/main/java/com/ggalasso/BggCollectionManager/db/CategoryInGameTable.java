package com.ggalasso.BggCollectionManager.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.CategoryInGameHelper;

import java.util.ArrayList;
import java.util.Map;

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

        String insertSQL = super.getSQLInsertString(bgCatMap, categoryInGameTable, columns);

        Log.d("BGCM-CT", "Bulk insert into " + categoryInGameTable + "\nSQL statement: " + insertSQL);
        //Do the insert
        open();
        database.execSQL(insertSQL);
        close();
        Log.d("BGCM-CT", "Bulk insert into " + categoryInGameTable + "\nSQL statement: \n" + insertSQL);
    }

//    @NonNull
//    protected String getSQLInsertString1(Map<String, ArrayList<String>> bgCatMap, String categoryInGameTable, String colBgId, String colCaId) {
//        String insertSQL = "INSERT OR IGNORE INTO " + categoryInGameTable + " (" + colBgId + ", " + colCaId + ") VALUES ";
//
//        for (Map.Entry<String, ArrayList<String>> game : bgCatMap.entrySet()) {
//            String bgId = game.getKey();
//            for(String caId: game.getValue()) {
//                insertSQL += "('" + bgId + "', '" + caId + "'),";
//            }
//        }
//        insertSQL = insertSQL.substring(0, insertSQL.length()-1);
//        insertSQL += ";";
//        return insertSQL;
//    }

}
