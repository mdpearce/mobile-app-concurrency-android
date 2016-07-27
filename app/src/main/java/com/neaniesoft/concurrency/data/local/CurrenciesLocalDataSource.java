package com.neaniesoft.concurrency.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.Currency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesLocalDataSource implements CurrenciesDataSource {

    private static final String PREF_FETCHED_DATE = "fetched_date";
    private static CurrenciesLocalDataSource sInstance;

    private CurrenciesDbHelper mDbHelper;

    private SharedPreferences mPreferences;

    private CurrenciesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new CurrenciesDbHelper(context);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CurrenciesLocalDataSource getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new CurrenciesLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void getCurrencies(@NonNull LoadCurrenciesCallback callback) {
        List<Currency> currencies = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                CurrenciesPersistenceContract.CurrencyEntry._ID,
                CurrenciesPersistenceContract.CurrencyEntry.COLUMN_RATE
        };

        Cursor c = db.query(
                CurrenciesPersistenceContract.CurrencyEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String code = c.getString(c.getColumnIndexOrThrow(CurrenciesPersistenceContract.CurrencyEntry._ID));
                double rate = c.getDouble(c.getColumnIndexOrThrow(CurrenciesPersistenceContract.CurrencyEntry.COLUMN_RATE));
                Currency currency = new Currency(code, rate);
                currencies.add(currency);
            }
        }

        if (c != null) {
            c.close();
        }

        db.close();

        Date fetchedDate = new Date(mPreferences.getLong(PREF_FETCHED_DATE, 0));

        if (currencies.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onCurrenciesLoaded(currencies, fetchedDate);
        }
    }

    @Override
    public void saveCurrencies(@NonNull List<Currency> currencies) {
        checkNotNull(currencies);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (Currency currency : currencies) {
                ContentValues values = new ContentValues();
                values.put(CurrenciesPersistenceContract.CurrencyEntry._ID, currency.getCode());
                values.put(CurrenciesPersistenceContract.CurrencyEntry.COLUMN_RATE, currency.getRate());

                db.replace(CurrenciesPersistenceContract.CurrencyEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(PREF_FETCHED_DATE, System.currentTimeMillis()).apply();
    }

    @Override
    public void refreshCurrencies() {
        // Not required
    }
}
