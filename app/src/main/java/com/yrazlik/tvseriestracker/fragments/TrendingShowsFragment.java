package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.activities.MainActivity;
import com.yrazlik.tvseriestracker.activities.ShowDetailActivity;
import com.yrazlik.tvseriestracker.adapters.TrendingShowsListAdapter;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.AdUtils;
import com.yrazlik.tvseriestracker.util.Utils;
import java.util.List;

import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_ID;
import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_NAME;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class TrendingShowsFragment extends BaseFragment implements ApiResponseListener, AdapterView.OnItemClickListener {

    private ListView trendingShowsList;
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
        trendingShowsList.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(trendingShowsListAdapter == null) {
            showProgressWithWhiteBG();
            requestTrendingShows();
        } else {
            trendingShowsListAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        if(trendingShowsListAdapter != null) {
            trendingShowsListAdapter.notifyDataSetChanged();
        }
    }

    private void requestTrendingShows() {
        ApiHelper.getInstance(getContext()).getShows(0, this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdUtils.showInterstitial(false);
        ShowDto show = trendingShowsListAdapter.getItem(i);
        Intent intent = new Intent(getContext(), ShowDetailActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, show.getId());
        intent.putExtra(EXTRA_SHOW_NAME, show.getName());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.fadeout);
    }

    @Override
    public void onResponse(Object response) {
        dismissProgress();
        List<ShowDto> trendingShows = (List<ShowDto>) response;
        trendingShows = Utils.sortShowsByWeightedRating(trendingShows);
        for(int i = 0; i < trendingShows.size(); i++) {
            ShowDto showDto = trendingShows.get(i);
            showDto.setOrder(i + 1);
        }
        addAdsView(trendingShows);
        trendingShowsListAdapter = new TrendingShowsListAdapter(getContext(), R.layout.list_row_trending_shows, trendingShows);
        trendingShowsList.setAdapter(trendingShowsListAdapter);
    }

    private void addAdsView(List<ShowDto> trendingShows) {
        ShowDto ad = new ShowDto();
        ad.setAd(true);
        try {
            if(trendingShows.size() > 5) {
                trendingShows.add(5, ad);
            }
            if(trendingShows.size() > 15) {
                trendingShows.add(15, ad);
            }
            if(trendingShows.size() > 25) {
                trendingShows.add(25, ad);
            }
            if(trendingShows.size() > 35) {
                trendingShows.add(35, ad);
            }
            if(trendingShows.size() > 45) {
                trendingShows.add(45, ad);
            }
            if(trendingShows.size() > 55) {
                trendingShows.add(55, ad);
            }
            if(trendingShows.size() > 65) {
                trendingShows.add(65, ad);
            }

        } catch (Exception ignored) {}
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {
        if(apiError != null) {
            Toast.makeText(getContext(), apiError.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
        showRetryView();
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
    protected int getFragmentTitle() {
        return R.string.title_trending;
    }

    @Override
    public void setActionBar() {
        ((MainActivity) getActivity()).initSearchBar(getResources().getString(getFragmentTitle()));
    }
}
