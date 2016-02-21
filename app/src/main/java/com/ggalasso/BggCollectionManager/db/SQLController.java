package com.ggalasso.BggCollectionManager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.Schema.DBhelper;
import com.ggalasso.BggCollectionManager.model.UtilityConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Edward on 9/8/2015.
 */
public class SQLController {


    Context context;
    SQLiteDatabase database;

    private DBhelper dbHelper;

    public SQLController() {

    }

    public SQLController(Context ctx) {
        context = ctx;
        //dbHelper = new DBhelper(context);
    }

    private SQLController openConnection() throws SQLException {
        //TODO: Learn more about passing the context and whether we instantiate multiple DB Helpers and by default multiple Board Game Helpers.
        dbHelper = DBhelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void open() {
        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public void destroyEverything() {
        open();
        dbHelper.deleteDatabase(context);
        close();
    }

    public Cursor fetchFromDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = database.query(
                table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy,
                limit
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Integer fetchTableCount(String tableName) {
        open();
        Integer count = 0;
        String query = "SELECT COUNT(*) FROM " + tableName;

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        Log.d("BGCM-SQL", "Fetch Table Count = " + count);
        close();
        return count;
    }

    public void dropTable(String tableName) {
        open();
        dbHelper.dropTable(database, tableName);
        close();
    }

    public void deleteAllRowsFromTable(String tableName) {
        open();
        database.delete(tableName, null, null);
        Log.d("BGCM-SQL", "Deleted all rows from " + tableName);
        close();
    }


    protected String getRowValues(String key, ArrayList<String> values) {
        String sql_rows = "";
        for (String value : values) {
            sql_rows += "('" + key + "', '" + value + "'),";
        }
        return sql_rows;
    }

    protected String getRowValue(String key, String value) {
        return "('" + key + "', '" + value + "'),";
    }


    @NonNull
    protected String getColumns(ArrayList<String> columns) {
        String sql_columns = "";
        for (String column : columns) {
            sql_columns += column + ", ";
        }
        sql_columns = sql_columns.substring(0, sql_columns.length() - UtilityConstants.TRIM_COMMA_AND_SPACE.getValue());
        sql_columns += ")";
        return sql_columns;
    }

    protected void insertToDatabase(String insertSQL) {
        open();
        database.execSQL(insertSQL);
        close();
    }
}
