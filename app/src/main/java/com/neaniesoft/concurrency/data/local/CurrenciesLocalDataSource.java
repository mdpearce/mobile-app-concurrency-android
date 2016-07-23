package com.neaniesoft.concurrency.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.Currency;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesLocalDataSource implements CurrenciesDataSource {

    private static CurrenciesLocalDataSource sInstance;

    private CurrenciesDbHelper mDbHelper;

    private CurrenciesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new CurrenciesDbHelper(context);
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

        if (currencies.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onCurrenciesLoaded(currencies);
        }
    }

    @Override
    public void saveCurrency(@NonNull Currency currency) {
        checkNotNull(currency);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CurrenciesPersistenceContract.CurrencyEntry._ID, currency.getCode());
        values.put(CurrenciesPersistenceContract.CurrencyEntry.COLUMN_RATE, currency.getRate());

        db.replace(CurrenciesPersistenceContract.CurrencyEntry.TABLE_NAME, null, values);

        db.close();
    }
}
