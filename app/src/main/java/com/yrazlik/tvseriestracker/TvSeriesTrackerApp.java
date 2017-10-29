package com.yrazlik.tvseriestracker;

import android.app.Application;
import android.content.Context;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TvSeriesTrackerApp extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
