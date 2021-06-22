package com.example.covid_19tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MySqliteDatabaseSSN extends SQLiteOpenHelper {
    public static final String dbName="SSN_db";
    public static final int dbVersion=2;
    private static final String tableName="SSN_table";


    public MySqliteDatabaseSSN(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tableName+" (ssn INTEGER PRIMARY KEY)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(db);
    }

    public boolean insertSSN(SSNModel model){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ssn",model.getssn());
        long result = db.insert(tableName,null,values);
        return result !=-1;
    }

    public long getSSNCount(){
            SQLiteDatabase db = getReadableDatabase();
            return DatabaseUtils.queryNumEntries(db,tableName);
    }
}
