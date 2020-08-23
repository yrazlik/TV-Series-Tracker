package com.yrazlik.tvseriestracker;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.ArrayList;
import java.util.List;
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
        List<String> testDevices = new ArrayList<>();
        testDevices.add("9D9D289B3C0209123947B9B256CD2AB0");
        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();

        // val deviceIds = arrayListOf(AdRequest.DEVICE_ID_EMULATOR)
        // getDeviceIdForAdMobTestAds(context)?.let { deviceIds.add(it.toUpperCase(Locale.ROOT)) }
        // MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(deviceIds).build())

        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(getApplicationContext());
        mAppContext = getApplicationContext();
        watchedList = Utils.getWatchedList(mAppContext);
        favoritesList = Utils.getFavoritesList(mAppContext);
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
