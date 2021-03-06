package com.yrazlik.tvseriestracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yrazlik.tvseriestracker.activities.MainActivity;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.AdUtils;
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

    public static boolean appIsRunning = false;
    public static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static Context mAppContext;
    public static Map<Long, Map<Long, EpisodeDto>> watchedList; //long showId, long episodeId
    public static Map<Long, ShowDto> favoritesList;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        List<String> testDevices = new ArrayList<>();
        //testDevices.add("9D9D289B3C0209123947B9B256CD2AB0");
        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                /*.setTestDeviceIds(testDevices)*/
                .build();

        // val deviceIds = arrayListOf(AdRequest.DEVICE_ID_EMULATOR)
        // getDeviceIdForAdMobTestAds(context)?.let { deviceIds.add(it.toUpperCase(Locale.ROOT)) }
        // MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(deviceIds).build())

        MobileAds.setRequestConfiguration(requestConfiguration);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseMessaging", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                       // String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d("FirebaseMessaging", msg);
                        //Toast.makeText(getAppContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

        MobileAds.initialize(getApplicationContext());
        watchedList = Utils.getWatchedList(mAppContext);
        favoritesList = Utils.getFavoritesList(mAppContext);
        AdUtils.initInterstitialAds(this);
        AdUtils.getInstance().getCachedAd(AdUtils.NATIVE_ADUNIT_ID);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(TvSeriesTrackerFirebaseMessagingService.CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        } catch (Exception ignored) {}
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
