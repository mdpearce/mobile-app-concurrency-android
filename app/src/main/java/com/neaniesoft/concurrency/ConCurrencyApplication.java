package com.neaniesoft.concurrency;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mdpearce on 27/07/2016.
 */

public class ConCurrencyApplication extends Application {
    private static ConCurrencyApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sApplication = this;
    }

    public static ConCurrencyApplication getInstance() {
        return sApplication;
    }
}
