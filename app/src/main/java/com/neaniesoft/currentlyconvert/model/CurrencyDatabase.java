package com.neaniesoft.currentlyconvert.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by mdpearce on 21/04/16.
 */

@Database(name = CurrencyDatabase.NAME, version = CurrencyDatabase.VERSION)
public class CurrencyDatabase {

    public static final String NAME = "Currencies";
    public static final int VERSION = 1;

}
