package com.neaniesoft.concurrency.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface CurrenciesDataSource {
    interface LoadCurrenciesCallback {
        void onCurrenciesLoaded(List<Currency> currencies);

        void onDataNotAvailable();
    }

    void getCurrencies(@NonNull LoadCurrenciesCallback callback);

    void saveCurrency(@NonNull Currency currency);
}
