package com.ggalasso.BggCollectionManager.db.Schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Edward on 9/8/2015.
 */
public class DBhelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DB_Name = "bgm_db";
    // Database Version
    private static final int DB_Version = 1;
    private static DBhelper ourInstance = null;
    private BoardGameHelper _bgh = BoardGameHelper.getInstance();
    private CategoryHelper _cah = CategoryHelper.getInstance();
    private CategoryInGameHelper _cigh = CategoryInGameHelper.getInstance();
    private MechanicHelper _meh = MechanicHelper.getInstance();
    private MechanicInGameHelper _migh = MechanicInGameHelper.getInstance();

    private DBhelper(Context context) {
        super(context, DB_Name, null, DB_Version);
        Log.i("BGCM-DBH", "Instantiated DBhelper");
    }

    public static DBhelper getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (DBhelper.class) {
                if (ourInstance == null) {
                    ourInstance = new DBhelper(context);
                }
            }
        }
        return ourInstance;
    }

    // Create Table Query
    public String createTable(ArrayList<String> columns, String tableName) {
        StringBuilder results = new StringBuilder();
        int colSize = columns.size();

        results.append("CREATE TABLE " + tableName + " (");
        //Prepend the first column to the results query
        results.append(columns.get(0));
        //Loop through starting at the second column
        for (int i = 1; i < colSize; i++) {
            results.append(", " + columns.get(i));
        }
        results.append(");");
        Log.d("BGCM-DBH", "Create table query string is: " + results.toString());

        return results.toString();
    }

    public void deleteDatabase(Context ctx) {
        Log.d("BGCM-DBH", "Attempting to Delete Database");
        ctx.deleteDatabase(DB_Name);
        Log.d("BGCM-DBH", "Database Deleted");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Board Game Table
        db.execSQL(createTable(_bgh.getColumns(), _bgh.getTableName()));
        Log.d("BGCM-DBH", "Table " + _bgh.getTableName() + " was created.");
        //Create Category Table
        db.execSQL(createTable(_cah.getColumns(), _cah.getTableName()));
        Log.d("BGCM-DBH", "Table " + _cah.getTableName() + " was created.");
        //Create Category In Game Table
        db.execSQL(createTable(_cigh.getColumns(), _cigh.getTableName()));
        Log.d("BGCM-DBH", "Table " + _cigh.getTableName() + " was created.");
        //Create Mechanic Table
        db.execSQL(createTable(_meh.getColumns(), _meh.getTableName()));
        Log.d("BGCM-DBH", "Table " + _meh.getTableName() + " was created.");
        //Create Mechanic In Game Table
        db.execSQL(createTable(_migh.getColumns(), _migh.getTableName()));
        Log.d("BGCM-DBH", "Table " + _migh.getTableName() + " was created.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + _bgh.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + _cah.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + _cigh.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + _meh.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + _migh.getTableName());
        onCreate(db);
        Log.d("BGCM-DBH", "Table " + _bgh.getTableName() + " was upgraded, from " + oldVersion + " to " + newVersion);
    }

    public void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        Log.d("BGCM-DBH", "Table " + tableName + " was dropped.");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Log.d("BGCM-DBH","Set Foreign Key Constraint By onConfigure");
        db.setForeignKeyConstraintsEnabled(true);
    }

//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//        if(!db.isReadOnly()){
//            Log.d("BGCM-DBH","Foreign Keys Constraints Activated!!");
//            db.execSQL("PRAGMA foreign_keys=ON;");
//        }
//    }
}
