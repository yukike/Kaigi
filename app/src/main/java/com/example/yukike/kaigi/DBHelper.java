package com.example.yukike.kaigi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.File;

public class DBHelper {
    public static final String TAG = "DBHelper";

    public SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    public DBHelper(final Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    /**
     * Databaseが削除できればtrue。できなければfalse
     * @param context
     * @return
     */
    public boolean isDatabaseDelete(final Context context) {
        boolean result = false;
        if (this.db != null) {
            File file = context.getDatabasePath(dbOpenHelper.getDatabaseName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result = this.db.deleteDatabase(file);
            }
        }
        return result;
    }

    // insert
    void insert(String  table, String  nullColumnHack, ContentValues  values) {
        db.insert(table, nullColumnHack, values);
    }
    // update

    // select
    Cursor query(boolean distinct, String  table, String[]  columns, String  selection, String[]  selectionArgs, String  groupBy, String  having, String  orderBy, String  limit) {
        return db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
