package com.neaniesoft.concurrency.data.remote;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.CurrenciesDataSource;
import com.neaniesoft.concurrency.data.Currency;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesRemoteDataSource implements CurrenciesDataSource {

    private static CurrenciesRemoteDataSource sInstance;

    public static CurrenciesRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new CurrenciesRemoteDataSource();
        }
        return sInstance;
    }

    private CurrenciesRemoteDataSource() {}

    @Override
    public void getCurrencies(@NonNull LoadCurrenciesCallback callback) {
        checkNotNull(callback);
    }

    @Override
    public void saveCurrency(@NonNull Currency currency) {

    }
}
