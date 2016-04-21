package com.neaniesoft.currentlyconvert;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by mdpearce on 21/04/16.
 */
public class CurrentlyConvertApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
