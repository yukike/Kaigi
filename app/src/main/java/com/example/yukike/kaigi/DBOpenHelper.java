package com.example.yukike.kaigi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    public Context m_context;
    public static final String TAG = "excute";
    public static final String DB_NAME = "useridRegist";
    public static final int DB_VERSION = 1;
    public static final String CREATE_TABLE = "create table userdata ( userid text not null);";

    public DBOpenHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.m_context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.d(TAG, "onCreate version : " + db.getVersion());
        //this.execFileSQL(db, "create_table.sql");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.d(TAG, "onUpgrade version : " + db.getVersion());
        Log.d(TAG, "onUpgrade oldVersion : " + oldVersion);
        Log.d(TAG, "onUpgrade newVersion : " + newVersion);
    }
}
