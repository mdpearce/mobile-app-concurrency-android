package com.neaniesoft.concurrency.data.local;

import android.provider.BaseColumns;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class CurrenciesPersistenceContract {
    public CurrenciesPersistenceContract() {}

    public static abstract class CurrencyEntry implements BaseColumns {
        public static final String TABLE_NAME = "currency";
        public static final String COLUMN_RATE = "rate";
    }
}
