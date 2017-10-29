package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.TrendingShowsListAdapter;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TrendingShowsFragment extends BaseFragment implements ApiResponseListener {

    private ListViewCompat trendingShowsList;
    private TrendingShowsListAdapter trendingShowsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    private void initUI() {
        trendingShowsList = rootView.findViewById(R.id.trendingShowsList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(trendingShowsListAdapter == null) {
            showProgressWithWhiteBG();
            requestTrendingShows();
        }
    }

    private void requestTrendingShows() {
        ApiHelper.getInstance(getContext()).getShows(0, this);
    }

    @Override
    protected void retry() {
        requestTrendingShows();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_trending_shows;
    }

    @Override
    public void onResponse(Object response) {
        dismissProgress();
        List<ShowDto> trendingShows = (List<ShowDto>) response;
        trendingShows = Utils.sortShowsByWeightedRating(trendingShows);
        trendingShowsListAdapter = new TrendingShowsListAdapter(getContext(), R.layout.list_row_trending_shows, trendingShows);
        trendingShowsList.setAdapter(trendingShowsListAdapter);
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {
        if(apiError != null) {
            Toast.makeText(getContext(), apiError.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
