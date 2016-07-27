package com.neaniesoft.concurrency;

import android.app.Application;

/**
 * Created by mdpearce on 27/07/2016.
 */

public class ConCurrencyApplication extends Application {
    private static ConCurrencyApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static ConCurrencyApplication getInstance() {
        return sApplication;
    }
}
