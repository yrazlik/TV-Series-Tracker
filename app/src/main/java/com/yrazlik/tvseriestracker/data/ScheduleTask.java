package com.yrazlik.tvseriestracker.data;

import android.content.Context;

import com.yrazlik.tvseriestracker.restclient.ApiHelper;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by yrazlik on 17.11.2017.
 */

public class ScheduleTask implements Callable<List<EpisodeDto>>{

    private long showId;
    private Context mContext;

    public ScheduleTask(long showId, Context context) {
        this.showId = showId;
        this.mContext = context;
    }

    public long getShowId() {
        return showId;
    }

    @Override
    public List<EpisodeDto> call() throws Exception {
        try {
            return ApiHelper.getInstance(mContext).getAllEpisodesSynchronously(showId);
        } catch (Exception e) {
            return null;
        }
    }
}
