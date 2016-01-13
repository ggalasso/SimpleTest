package com.ggalasso.BggCollectionManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.MechanicHelper;
import com.ggalasso.BggCollectionManager.model.Link;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by truthd on 9/20/2015.
 */
public class MechanicTable extends SQLController {

    public MechanicTable(Context c) {
        super(c);
    }

    public void syncMechanics(Map<String, String> mechanicMap) {
        String mechanicTable = MechanicHelper.getTableName();
        Integer rowCount = fetchTableCount(mechanicTable);
        String colId = MechanicHelper.me_Id;
        String colName = MechanicHelper.me_Name;

            String insertSQL = "INSERT OR IGNORE INTO " + mechanicTable + " (" + colId + ", " + colName + ") VALUES";

            for (Map.Entry<String, String> mechanic : mechanicMap.entrySet()) {


                String id = mechanic.getKey();
                String name = mechanic.getValue();
                insertSQL += "('" + id + "', '" + name + "'),";
            }
            insertSQL = insertSQL.substring(0, insertSQL.length()-1);
            insertSQL += ";";

            Log.d("BGCM-MT", "Bulk insert into " + mechanicTable + "\nSQL statement: " + insertSQL);
            //Do the insert
            open();
            database.execSQL(insertSQL);
            close();
            Log.d("BGCM-MT", "Bulk insert into " + mechanicTable + "\nSQL statement: \n" + insertSQL);
//        }
       fetchTableCount(mechanicTable);
    }

    private void insert(Link me) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(MechanicHelper.me_Id, me.getId());
        cv.put(MechanicHelper.me_Name, me.getValue());
        database.insert(MechanicHelper.getTableName(), null, cv);
        Log.d("BGCM-MET", "Successfully added " + me.getId());
        close();
    }

    public ArrayList<Link> fetchAllCategories() {
        return fetch_impl(null);
    }

    public Link fetchMechanic(String id) {
        ArrayList<Link> item = fetch_impl(id);
        if (item.size() > 0) {
            Link result = item.get(0);
            return result;
        }
        return null;
    }

    public ArrayList<Link> fetch_impl(String id) {
        open();
        ArrayList<Link> results = new ArrayList<Link>();
        String filter;

        if (id == null) {
            filter = null;
        } else {
            filter = new String(MechanicHelper.me_Id + " = " + id);
        }

        String[] columns = new String[]{
                MechanicHelper.me_Id,
                MechanicHelper.me_Name,
        };

        Cursor cursor = database.query(
                MechanicHelper.getTableName(),
                columns,
                filter,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Link ca = new Link(cursor.getString(0), cursor.getString(1), "Mechanic");
                results.add(ca);
            }
            Log.d("BGCM-MET", "Mechanic list size: " + results.size());
        }
        close();
        return results;
    }

    public int update(Link me) {
        super.open();
        ContentValues cv = new ContentValues();
        //cv.put(MechanicHelper.Id, me.getId());     /* Not necessary to include the Id. Everything else is necessary though */
        cv.put(MechanicHelper.me_Name, me.getValue());

        int i = database.update(
                MechanicHelper.getTableName(),
                cv,
                MechanicHelper.me_Id + " = " + me.getId(),
                null
        );
        super.close();
        return i;
    }

    public void delete(String id) {
        super.open();
        Integer result = database.delete(MechanicHelper.getTableName(), MechanicHelper.me_Id + " = " + id, null);
        if (result > 0) {
            Log.d("BGCM-MET", "Successfully deleted mechanic " + id + " as STRING");
        } else {
            Log.d("BGCM-MET", "Unable to delete mechanic, STRING id: " + id);
        }
        super.close();
    }

    public void delete(Link ca) {
        super.open();
        Integer result = database.delete(MechanicHelper.getTableName(), MechanicHelper.me_Id + " = " + ca.getId(), null);
        if (result > 0) {
            Log.d("BGCM-MET", "Successfully deleted mechanic " + ca.getId() + " as OBJECT");
        } else {
            Log.d("BGCM-MET", "Unable to delete mechanic, OBJECT id: " + ca.getId());
        }
        super.close();
    }

    public ArrayList<String> fetchAllGameIds() {
        open();
        ArrayList<String> results = new ArrayList<String>();
        String[] columns = new String[]{
                MechanicHelper.me_Id,
        };

        Cursor cursor = database.query(
                MechanicHelper.getTableName(),
                columns,
                null,
                null,
                null,
                null,
                null
        );
        //if (cursor != null) {cursor.moveToFirst();}
        if (cursor != null) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
            Log.d("BGCM-MET", "All game Id list size: " + results.size());
        }
        close();
        return results;
    }
}
