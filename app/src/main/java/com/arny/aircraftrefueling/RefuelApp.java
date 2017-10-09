package com.arny.aircraftrefueling;

import android.app.Application;
import android.support.multidex.MultiDex;
public class RefuelApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
