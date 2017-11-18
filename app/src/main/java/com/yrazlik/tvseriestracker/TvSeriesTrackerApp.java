package com.yrazlik.tvseriestracker;

import android.app.Application;
import android.content.Context;

import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TvSeriesTrackerApp extends Application {

    public static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static Context mAppContext;
    public static Map<Long, Map<Long, EpisodeDto>> watchedList; //long showId, long episodeId
    public static Map<Long, ShowDto> favoritesList;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        watchedList = Utils.getWatchedList(mAppContext);
        favoritesList = Utils.getFavoritesList(mAppContext);
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
