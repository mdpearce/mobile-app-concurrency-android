package com.neaniesoft.concurrency.data;

import android.support.annotation.NonNull;

import com.neaniesoft.concurrency.data.model.Currency;

import java.util.Date;
import java.util.List;

/**
 * Created by mdpearce on 23/07/2016.
 */

public interface CurrenciesDataSource {
    interface LoadCurrenciesCallback {
        void onCurrenciesLoaded(List<Currency> currencies, Date date);

        void onDataNotAvailable();
    }

    void getCurrencies(@NonNull LoadCurrenciesCallback callback);

    void saveCurrencies(@NonNull List<Currency> currency);

    void refreshCurrencies();
}
