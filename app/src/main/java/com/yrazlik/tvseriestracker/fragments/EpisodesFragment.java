package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity;
import com.yrazlik.tvseriestracker.adapters.SeasonsListAdapter;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_DATE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_IMAGE;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_NAME;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_EPISODE_SUMMARY;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SEASON;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SHOW_ID;
import static com.yrazlik.tvseriestracker.activities.EpisodeDetailActivity.EXTRA_SHOW_NAME;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class EpisodesFragment extends BaseFragment implements ApiResponseListener, ExpandableListView.OnChildClickListener{

    private ExpandableListView episodesLV;

    private ShowDto showDto;
    private List<EpisodeDto> episodes;

    public static EpisodesFragment newInstance(ShowDto showDto) {
        EpisodesFragment episodesFragment = new EpisodesFragment();
        episodesFragment.showDto = showDto;
        return episodesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestEpisodes();
    }

    private void initUI() {
        episodesLV = rootView.findViewById(R.id.episodesLV);
        episodesLV.setOnChildClickListener(this);
    }

    private void requestEpisodes() {
        ApiHelper.getInstance(getContext()).getAllEpisodes(showDto.getId(), this);
    }

    private void populateEpisodes() {
        if(episodes != null && episodes.size() > 0) {
            long firstSeasonNumber = episodes.get(0).getSeason();
            long lastSeasonNumber = episodes.get(episodes.size() - 1).getSeason();
            List<Long> seasons = new ArrayList<>();
            for(long i = firstSeasonNumber; i <= lastSeasonNumber; i++) {
                seasons.add(i);
            }

            Map<Long, List<EpisodeDto>> episodesMap = new HashMap<>();


            for(int i = 0; i < seasons.size(); i++) {
                List<EpisodeDto> episodeDtos = new ArrayList<>();
                long season = seasons.get(i);
                for (int j = 0; j < episodes.size(); j++) {
                    if(season == episodes.get(j).getSeason()) {
                        episodeDtos.add(episodes.get(j));
                    }
                }
                episodesMap.put(season, episodeDtos);
            }

            SeasonsListAdapter seasonsListAdapter = new SeasonsListAdapter(getContext(), seasons, episodesMap);
            episodesLV.setAdapter(seasonsListAdapter);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        EpisodeDto episodeDto = (EpisodeDto) expandableListView.getExpandableListAdapter().getChild(groupPosition, childPosition);

        Intent i = new Intent(getContext(), EpisodeDetailActivity.class);
        i.putExtra(EXTRA_SHOW_ID, showDto.getId());
        i.putExtra(EXTRA_SEASON, episodeDto.getSeason());
        i.putExtra(EXTRA_EPISODE, episodeDto.getNumber());
        i.putExtra(EXTRA_SHOW_NAME, showDto.getName());
        i.putExtra(EXTRA_EPISODE_NAME, episodeDto.getName());
        i.putExtra(EXTRA_EPISODE_DATE, episodeDto.getAirStamp());
        i.putExtra(EXTRA_EPISODE_SUMMARY, episodeDto.getSummary());
        i.putExtra(EXTRA_EPISODE_IMAGE, episodeDto.getImage() != null ? episodeDto.getImage().getOriginal() : "");

        startActivity(i);

        return true;
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_episodes;
    }

    public int getTitle() {
        return R.string.fragment_title_episodes;
    }

    @Override
    public void onResponse(Object response) {
        this.episodes = (List<EpisodeDto>) response;
        populateEpisodes();
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {

    }
}
