package com.neaniesoft.concurrency.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "currencies.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CurrenciesPersistenceContract.CurrencyEntry.TABLE_NAME + " (" +
                    CurrenciesPersistenceContract.CurrencyEntry._ID + " TEXT PRIMARY KEY," +
                    CurrenciesPersistenceContract.CurrencyEntry.COLUMN_RATE + " REAL" +
                    " )";

    public CurrenciesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // not required for version 1 schema
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not required for version 1 schema
    }
}
