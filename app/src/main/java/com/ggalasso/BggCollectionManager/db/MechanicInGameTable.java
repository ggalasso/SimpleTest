package com.ggalasso.BggCollectionManager.db;

import android.content.Context;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.MechanicInGameHelper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by truthd on 9/20/2015.
 */
public class MechanicInGameTable extends SQLController {

    public MechanicInGameTable(){

    }

    public MechanicInGameTable(Context c) {
        super(c);
    }

    public void insertAllMechanicsInGame(Map<String, ArrayList<String>> bgMecMap ) {
        String mechanicInGameTable = MechanicInGameHelper.getTableName();
        ArrayList<String> columns = new ArrayList<>();
        columns.add(MechanicInGameHelper.mg_bg_Id);
        columns.add(MechanicInGameHelper.mg_me_Id);

        //String insertSQL = "INSERT OR IGNORE INTO " + mechanicInGameTable + " (" + colBgId + ", " + colMeId + ") VALUES";

        String insertSQL = super.getSQLInsertString(bgMecMap, mechanicInGameTable, columns);

//        for (Map.Entry<String, ArrayList<String>> game : bgMecMap.entrySet()) {
//            String bgId = game.getKey();
//            for(String meId: game.getValue()) {
//                insertSQL += "('" + bgId + "', '" + meId + "'),";
//            }
//        }
//        insertSQL = insertSQL.substring(0, insertSQL.length()-1);
//        insertSQL += ";";

        Log.d("BGCM-CT", "Bulk insert into " + mechanicInGameTable + "\nSQL statement: " + insertSQL);
        //Do the insert
        open();
        database.execSQL(insertSQL);
        close();
        Log.d("BGCM-CT", "Bulk insert into " + mechanicInGameTable + "\nSQL statement: \n" + insertSQL);
    }

}
