package com.ggalasso.simpletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.simpletest.db.Schema.CategoryHelper;
import com.ggalasso.simpletest.model.Link;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by truthd on 9/20/2015.
 */
public class CategoryTable extends SQLController {

    public CategoryTable(Context c) {
        super(c);
    }

    //10-11-15 - GAG - Not using this method currently but may come in handy later
    public boolean isCategoryInTable(String id) {
        Link category = this.fetchCategory(id);
        if (category == null) {
            return false;
        } else {
            return true;
        }
    }

    public void syncCategories(Map<String, String> categoryMap) {
        String categoryTable = CategoryHelper.getTableName();
        Integer rowCount = fetchTableCount(categoryTable);
        String colId = CategoryHelper.ca_Id;
        String colName = CategoryHelper.ca_Name;


//        INSERT INTO EVENTTYPE (EventTypeName)
//        SELECT 'ANI Received'
//        WHERE NOT EXISTS (SELECT 1 FROM EVENTTYPE WHERE EventTypeName = 'ANI Received');


//        String insertSQL = ""; // = "INSERT INTO " + categoryTable + " (" + colId + ", " + colName + ") VALUES";
//        Integer loopCnt = 0;

//        for (Map.Entry<String, String> category : categoryMap.entrySet()) {
//            String id = category.getKey();
//            String name1 = category.getValue();
//            String name = name1.replace("&", "a");
//            loopCnt++;
//            Log.d("BGCM-CT", loopCnt + "out of " + categoryMap.size() + " : " + id + " | " + name);
//           // if (loopCnt < 25) {
//                insertSQL = "INSERT OR IGNORE INTO " + categoryTable+ " (" + colId + ", " + colName + ") VALUES ('"+ id + "', '" + name + "'); ";
//            open();
//            database.execSQL(insertSQL);
//            close();
////                insertSQL += "INSERT INTO " + categoryTable + " (" + colId + ", " + colName + ") ";
////                insertSQL += "SELECT '" + id + "', '" + name + "' ";
////                insertSQL += "WHERE NOT EXISTS (SELECT 1 FROM " + categoryTable + " WHERE " + colId + " = " + id + ");\n";
//            //}
//        }

        //Do the insert


//        if (rowCount > 0) {
//            //(boardGames);
//        } else {
            String insertSQL = "INSERT OR IGNORE INTO " + categoryTable + " (" + colId + ", " + colName + ") VALUES";

            for (Map.Entry<String, String> category : categoryMap.entrySet()) {


                String id = category.getKey();
                String name = category.getValue();
                insertSQL += "('" + id + "', '" + name + "'),";
            }
            insertSQL = insertSQL.substring(0, insertSQL.length()-1);
            insertSQL += ";";

            Log.d("BGCM-CT", "Bulk insert into " + categoryTable + "\nSQL statement: " + insertSQL);
            //Do the insert
            open();
            database.execSQL(insertSQL);
            close();
            Log.d("BGCM-CT", "Bulk insert into " + categoryTable + "\nSQL statement: \n" + insertSQL);
//        }
       fetchTableCount(categoryTable);
    }

    private void insert(Link ca) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(CategoryHelper.ca_Id, ca.getId());
        cv.put(CategoryHelper.ca_Name, ca.getValue());
        database.insert(CategoryHelper.getTableName(), null, cv);
        Log.d("BGCM-CAT", "Successfully added " + ca.getId());
        close();
    }

    public ArrayList<Link> fetchAllCategories() {
        return fetch_impl(null);
    }

    public Link fetchCategory(String id) {
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
            filter = new String(CategoryHelper.ca_Id + " = " + id);
        }

        String[] columns = new String[]{
                CategoryHelper.ca_Id,
                CategoryHelper.ca_Name,
        };

        Cursor cursor = database.query(
                CategoryHelper.getTableName(),
                columns,
                filter,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Link ca = new Link(cursor.getString(0), cursor.getString(1), "Category");
                results.add(ca);
            }
            Log.d("BGCM-CAT", "Category list size: " + results.size());
        }
        close();
        return results;
    }

    public int update(Link ca) {
        super.open();
        ContentValues cv = new ContentValues();
        //cv.put(CategoryHelper.Id, ca.getId());     /* Not necessary to include the Id. Everything else is necessary though */
        cv.put(CategoryHelper.ca_Name, ca.getValue());

        int i = database.update(
                CategoryHelper.getTableName(),
                cv,
                CategoryHelper.ca_Id + " = " + ca.getId(),
                null
        );
        super.close();
        return i;
    }

    public void delete(String id) {
        super.open();
        Integer result = database.delete(CategoryHelper.getTableName(), CategoryHelper.ca_Id + " = " + id, null);
        if (result > 0) {
            Log.d("BGCM-CAT", "Successfully deleted category " + id + " as STRING");
        } else {
            Log.d("BGCM-CAT", "Unable to delete category, STRING id: " + id);
        }
        super.close();
    }

    public void delete(Link ca) {
        super.open();
        Integer result = database.delete(CategoryHelper.getTableName(), CategoryHelper.ca_Id + " = " + ca.getId(), null);
        if (result > 0) {
            Log.d("BGCM-CAT", "Successfully deleted category " + ca.getId() + " as OBJECT");
        } else {
            Log.d("BGCM-CAT", "Unable to delete category, OBJECT id: " + ca.getId());
        }
        super.close();
    }

    public ArrayList<String> fetchAllGameIds() {
        open();
        ArrayList<String> results = new ArrayList<String>();
        String[] columns = new String[]{
                CategoryHelper.ca_Id,
        };

        Cursor cursor = database.query(
                CategoryHelper.getTableName(),
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
            Log.d("BGCM-CAT", "All game Id list size: " + results.size());
        }
        close();
        return results;
    }
}
