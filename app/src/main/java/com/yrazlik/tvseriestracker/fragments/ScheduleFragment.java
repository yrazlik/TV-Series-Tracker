package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity;
import com.yrazlik.tvseriestracker.adapters.ScheduleListAdapter;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ScheduleTask;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.data.ShowSchedule;
import com.yrazlik.tvseriestracker.util.AdUtils;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_DATE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_IMAGE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_NAME;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_SUMMARY;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SEASON;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SHOW_ID;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SHOW_NAME;

/**
 * Created by yrazlik on 17.11.2017.
 */

public class ScheduleFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private List<ShowSchedule> schedule = new ArrayList<>();
    private ScheduleListAdapter scheduleListAdapter;

    private ListView scheduleList;
    private RelativeLayout noScheduleRL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    private void initUI() {
        showProgressWithWhiteBG();
        scheduleList = rootView.findViewById(R.id.scheduleList);
        noScheduleRL = rootView.findViewById(R.id.noScheduleRL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scheduleList.setOnItemClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestSchedule();
            }
        }, 100);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdUtils.showInterstitial();
        ShowSchedule showSchedule = (ShowSchedule) scheduleList.getAdapter().getItem(i);

        Intent intent = new Intent(getContext(), EpisodeDetailActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showSchedule.getShow().getId());
        intent.putExtra(EXTRA_SEASON, showSchedule.getEpisode().getSeason());
        intent.putExtra(EXTRA_EPISODE, showSchedule.getEpisode().getNumber());
        intent.putExtra(EXTRA_SHOW_NAME, showSchedule.getShow().getName());
        intent.putExtra(EXTRA_EPISODE_NAME, showSchedule.getEpisode().getName());
        intent.putExtra(EXTRA_EPISODE_DATE, showSchedule.getEpisode().getAirStamp());
        intent.putExtra(EXTRA_EPISODE_SUMMARY, showSchedule.getEpisode().getSummary());
        intent.putExtra(EXTRA_EPISODE_IMAGE, showSchedule.getEpisode().getImage() != null ? showSchedule.getEpisode().getImage().getOriginal() : "");

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.fadeout);
    }

    private void requestSchedule() {
        schedule = new ArrayList<>();
        showProgressWithWhiteBG();
        ExecutorService executorService = TvSeriesTrackerApp.executorService;
        List<ScheduleTask> taskList = new ArrayList<>();
        List<Future<List<EpisodeDto>>> invokedList = new ArrayList<>();
        List<List<EpisodeDto>> showScheduleList = new ArrayList<>();

        for (Map.Entry<Long, ShowDto> show : TvSeriesTrackerApp.favoritesList.entrySet()) {
            taskList.add(new ScheduleTask(show.getKey(), getContext()));
        }

        if (taskList != null && taskList.size() > 0) {
            try {
                invokedList = executorService.invokeAll(taskList, 3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (invokedList != null && invokedList.size() > 0) {
            for (int i = 0; i < invokedList.size(); i++) {
                try {
                    List<EpisodeDto> episodes = invokedList.get(i).get();
                    showScheduleList.add(episodes);
                } catch (InterruptedException e) {
                    showScheduleList.add(null);
                } catch (ExecutionException e) {
                    showScheduleList.add(null);
                } catch (CancellationException e) {
                    showScheduleList.add(null);
                }
            }
        }

        if (showScheduleList != null && showScheduleList.size() > 0) {
            for (int i = 0; i < showScheduleList.size(); i++) {
                if (showScheduleList != null) {
                    List<EpisodeDto> episodes = showScheduleList.get(i);
                    if (episodes != null && episodes.size() > 0) {
                        for (int j = episodes.size() - 1; j >= 0; j--) {
                            EpisodeDto episode = episodes.get(j);
                            if (Utils.episodePassed(episode)) {
                                break;
                            }
                            if (Utils.episodeAirsInUpcomingWeek(episode)) {
                                ShowSchedule showSchedule = new ShowSchedule(TvSeriesTrackerApp.favoritesList.get(taskList.get(i).getShowId()), episode);
                                schedule.add(showSchedule);
                                break;
                            }
                        }
                    }
                }
            }
        }

        dismissProgress();
        setScheduleListAdapter();
        setNoScheduleRLVisibility();
    }

    private void setScheduleListAdapter() {
        scheduleListAdapter = new ScheduleListAdapter(getContext(), R.layout.list_row_schedule, schedule);
        scheduleList.setAdapter(scheduleListAdapter);
    }

    private void setNoScheduleRLVisibility() {
        if (schedule == null || schedule.size() == 0) {
            noScheduleRL.setVisibility(View.VISIBLE);
        } else {
            noScheduleRL.setVisibility(View.GONE);
        }
    }

    @Override
    protected void retry() {
        requestSchedule();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected int getFragmentTitle() {
        return R.string.title_schedule;
    }

    @Override
    public void setActionBar() {
        setDefaultActionBar(getResources().getString(getFragmentTitle()), null);
    }
}
