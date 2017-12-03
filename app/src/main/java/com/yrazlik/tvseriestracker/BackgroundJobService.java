package com.yrazlik.tvseriestracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.yrazlik.tvseriestracker.activities.MainActivity;
import com.yrazlik.tvseriestracker.data.SeasonDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 19.11.2017.
 */

public class BackgroundJobService extends JobService {

    public static final String BG_JOB_SERVICE_TAG = "com.yrazlik.tvseriestracker.bgjobservice";

    @Override
    public boolean onStartJob(final JobParameters job) {

       Map<Long, ShowDto> favoriteShows =  Utils.getFavoritesList(this);
        if(favoriteShows != null && favoriteShows.size() > 0) {

            for (Map.Entry<Long, ShowDto> entry : favoriteShows.entrySet())
            {

            }

        }

        Log.d("BACKGROUND", "onStopJob()");
        ApiHelper.getInstance(this).getSeasons(1, new ApiResponseListener() {
            @Override
            public void onResponse(Object response) {
                List<SeasonDto> seasons = (List<SeasonDto>) response;
                if(seasons != null && seasons.size() > 0) {
                    for(int i = 0; i < seasons.size(); i++) {
                        Log.d("BACKGROUND",  seasons.get(i).getUrl());
                    }
                }
                jobFinished(job, true);
            }

            @Override
            public void onFail(TVSeriesApiError apiError) {
                jobFinished(job, true);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("BACKGROUND", "onStopJob()");
        return true;
    }
}
