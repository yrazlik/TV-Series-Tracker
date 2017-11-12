package com.yrazlik.tvseriestracker;

import android.app.Application;
import android.content.Context;

import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.Map;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TvSeriesTrackerApp extends Application {

    private static Context mAppContext;
    public static Map<Long, Map<Long, EpisodeDto>> watchedList; //long showId, long episodeId

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        watchedList = Utils.getWatchedList(mAppContext);
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
