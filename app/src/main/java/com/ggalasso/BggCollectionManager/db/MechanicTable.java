package com.ggalasso.BggCollectionManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.CategoryHelper;
import com.ggalasso.BggCollectionManager.db.Schema.MechanicHelper;
import com.ggalasso.BggCollectionManager.model.Link;
import com.ggalasso.BggCollectionManager.model.UtilityConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by truthd on 9/20/2015.
 * making a difference - e2thed 3/3/2016
 */
public class MechanicTable extends SQLController {
    public MechanicTable() {
    }

    public MechanicTable(Context c) {
        super(c);
    }

    public void syncMechanics(Map<String, String> mechanicMap) {
        String mechanicTable = MechanicHelper.getTableName();
        Integer rowCount = fetchTableCount(mechanicTable);
        List<String> columns = Arrays.asList(MechanicHelper.me_Id, MechanicHelper.me_Name);
        String insertSQL = getInsertSQL(mechanicMap, mechanicTable, columns);

        super.insertToDatabase(getInsertSQL(mechanicMap, mechanicTable, columns));
        Log.d("BGCM-MT", "Bulk insert into " + mechanicTable + "\nSQL statement: \n" + insertSQL);

        fetchTableCount(mechanicTable);
    }

    public String getInsertSQL(Map<String, String> mechanicMap, String tableName, List<String> columns) {
        String insertSQL = "INSERT OR IGNORE INTO " + tableName + " (";
        insertSQL += super.getColumns(columns);
        insertSQL += " VALUES ";
        Map<String, String> treeMap = new TreeMap<>(mechanicMap);
        for (Map.Entry<String, String> elem : treeMap.entrySet()) {
            insertSQL += getRowValue(elem.getKey(), elem.getValue());
        }
        insertSQL = insertSQL.substring(0, insertSQL.length() - UtilityConstants.TRIM_COMMA.getValue());
        insertSQL += ";";
        return insertSQL;
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

        String filter = id == null ? null : new String(CategoryHelper.ca_Id + " = " + id);
        if (id == null) {
            filter = null;
        } else {
            filter = new String(MechanicHelper.me_Id + " = " + id);
        }

        String[] columns = new String[]{
                MechanicHelper.me_Id,
                MechanicHelper.me_Name,
        };

        Cursor cursor = super.executeDBQuery(CategoryHelper.getTableName(), columns, filter, null, null, null, null);

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
