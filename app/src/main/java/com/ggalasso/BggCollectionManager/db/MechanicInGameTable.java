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
        String colBgId = MechanicInGameHelper.mg_bg_Id;
        String colMeId = MechanicInGameHelper.mg_me_Id;

        String insertSQL = "INSERT OR IGNORE INTO " + mechanicInGameTable + " (" + colBgId + ", " + colMeId + ") VALUES";

        for (Map.Entry<String, ArrayList<String>> game : bgMecMap.entrySet()) {
            String bgId = game.getKey();
            for(String meId: game.getValue()) {
                insertSQL += "('" + bgId + "', '" + meId + "'),";
            }
        }
        insertSQL = insertSQL.substring(0, insertSQL.length()-1);
        insertSQL += ";";

        Log.d("BGCM-CT", "Bulk insert into " + mechanicInGameTable + "\nSQL statement: " + insertSQL);
        //Do the insert
        open();
        database.execSQL(insertSQL);
        close();
        Log.d("BGCM-CT", "Bulk insert into " + mechanicInGameTable + "\nSQL statement: \n" + insertSQL);
    }

}
