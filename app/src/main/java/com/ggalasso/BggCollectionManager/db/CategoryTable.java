package com.ggalasso.BggCollectionManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.CategoryHelper;
import com.ggalasso.BggCollectionManager.model.Link;
import com.ggalasso.BggCollectionManager.model.UtilityConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CategoryTable extends SQLController {

    public CategoryTable() {
    }

    public CategoryTable(Context c) {
        super(c);
    }

    public void deleteAllRowsFromTable(){
        deleteAllRowsFromTable(CategoryHelper.getTableName());
    }

    public void syncCategories(Map<String, String> categoryMap) {
        String categoryTable = CategoryHelper.getTableName();
        Integer rowCount = fetchTableCount(categoryTable);
        List<String> columns = Arrays.asList(CategoryHelper.ca_Id, CategoryHelper.ca_Name);

        String insertSQL = getInsertSQL(categoryMap, categoryTable, columns);

         super.insertToDatabase(insertSQL);
        Log.d("BGCM-CT", "Bulk insert into " + categoryTable + "\nSQL statement: " + insertSQL);

        fetchTableCount(categoryTable);
    }

    public String getInsertSQL(Map<String, String> categoryMap, String tableName, List<String> columns) {
        String insertSQL = "INSERT OR IGNORE INTO " + tableName + " (";
        insertSQL += super.getColumns(columns);
        insertSQL += " VALUES ";
        Map<String, String> treeMap = new TreeMap<>(categoryMap);
        for (Map.Entry<String, String> elem : treeMap.entrySet()) {
            insertSQL += getRowValue(elem.getKey(), elem.getValue());
        }
        insertSQL = insertSQL.substring(0, insertSQL.length() - UtilityConstants.TRIM_COMMA.getValue());
        insertSQL += ";";
        return insertSQL;
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
        ArrayList<Link> results = new ArrayList<Link>();

        String filter = id == null ? null : new String(CategoryHelper.ca_Id + " = " + id);

        String[] columns = new String[]{
                CategoryHelper.ca_Id,
                CategoryHelper.ca_Name,
        };

        Cursor cursor = super.executeDBQuery(CategoryHelper.getTableName(), columns, filter, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Link ca = new Link(cursor.getString(0), cursor.getString(1), "Category");
                results.add(ca);
            }
            Log.d("BGCM-CAT", "Category list size: " + results.size());
        }
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

    public ArrayList<String> fetchAllCategoryIds() {
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

        if (cursor != null) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
            Log.d("BGCM-CAT", "All category Id list size: " + results.size());
        }
        close();
        return results;
    }

    public ArrayList<String> getOrphanedCategories() {
        ArrayList<String> results = new ArrayList<>();
        String categoryTable = CategoryHelper.getTableName();

        open();
        String query = "SELECT ca_id FROM " + categoryTable + " EXCEPT SELECT DISTINCT cg_ca_id FROM category_in_game;";
        Log.d("BGCM-CAT", "Attempting to find orphaned categories");
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        }
        Log.d("BGCM-CAT", "Number of orphaned categories: " + results.size());
        close();

        return results;
    }
}



