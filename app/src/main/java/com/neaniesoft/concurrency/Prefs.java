package com.neaniesoft.concurrency;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.neaniesoft.concurrency.data.model.Currency;

/**
 * Created by mdpearce on 27/07/2016.
 */

public class Prefs {
    private static final String KEY_FROM_CURRENCY = "from_currency";
    private static final String KEY_TO_CURRENCY = "to_currency";

    public Prefs() {
    }

    private SharedPreferences prefs() {
        return PreferenceManager.getDefaultSharedPreferences(ConCurrencyApplication.getInstance().getApplicationContext());
    }

    private SharedPreferences.Editor edit() {
        return prefs().edit();
    }

    public void setFromCurrency(Currency currency) {
        edit().putString(KEY_FROM_CURRENCY, currency.getCode()).apply();
    }

    public void setToCurrency(Currency currency) {
        edit().putString(KEY_TO_CURRENCY, currency.getCode()).apply();
    }

    public String getFromCurrencyCode() {
        return prefs().getString(KEY_FROM_CURRENCY, null);
    }

    public String getToCurrencyCode() {
        return prefs().getString(KEY_TO_CURRENCY, null);
    }
}
