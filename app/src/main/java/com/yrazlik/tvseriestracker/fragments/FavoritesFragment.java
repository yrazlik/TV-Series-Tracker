package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.activities.MainActivity;
import com.yrazlik.tvseriestracker.activities.ShowDetailActivity;
import com.yrazlik.tvseriestracker.adapters.FavoritesListAdapter;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.AdUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_ID;
import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_NAME;

/**
 * Created by yrazlik on 12.11.2017.
 */

public class FavoritesFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private RelativeLayout noShowsRL;
    private ListView favoritesList;
    private FavoritesListAdapter favoritesListAdapter;
    private List<ShowDto> favoriteShowsList = new ArrayList<>();

    public static FavoritesFragment newInstance() {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        return favoritesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    private void initUI() {
        noShowsRL = rootView.findViewById(R.id.noShowsRL);
        favoritesList = rootView.findViewById(R.id.favoritesList);
        favoritesList.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setNoFavoritesTextVisibility();
        setFavoritesAdapter();
    }

    private void setFavoritesAdapter() {
        favoriteShowsList.clear();

        for (Map.Entry<Long, ShowDto> entry : TvSeriesTrackerApp.favoritesList.entrySet()) {
            favoriteShowsList.add(entry.getValue());
        }

        //addAdsView(favoriteShowsList);

        if (favoritesListAdapter == null) {
            if (TvSeriesTrackerApp.favoritesList != null || TvSeriesTrackerApp.favoritesList.size() > 0) {
                favoritesListAdapter = new FavoritesListAdapter(getContext(), R.layout.list_row_favorites, favoriteShowsList, new FavoritesListAdapter.FavoritesEmptyListener() {
                    @Override
                    public void onFavoritesEmpty() {
                        setNoFavoritesTextVisibility();
                    }
                });
                favoritesList.setAdapter(favoritesListAdapter);
            }
        } else {
            favoritesListAdapter.notifyDataSetChanged();
        }
    }

    private void addAdsView(List<ShowDto> shows) {
        ShowDto ad = new ShowDto();
        ad.setAd(true);
        try {
            if(shows.size() > 3) {
                shows.add(3, ad);
            }
        } catch (Exception ignored) {}
    }

    public void notifyDataSetChanged() {
        setFavoritesAdapter();
        setNoFavoritesTextVisibility();
    }

    private void setNoFavoritesTextVisibility() {
        if (TvSeriesTrackerApp.favoritesList == null || TvSeriesTrackerApp.favoritesList.size() == 0) {
            favoritesList.setVisibility(View.GONE);
            noShowsRL.setVisibility(View.VISIBLE);
        } else {
            favoritesList.setVisibility(View.VISIBLE);
            noShowsRL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdUtils.showInterstitial(false);
        ShowDto show = favoritesListAdapter.getItem(i);
        Intent intent = new Intent(getContext(), ShowDetailActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, show.getId());
        intent.putExtra(EXTRA_SHOW_NAME, show.getName());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.fadeout);
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_favorites;
    }

    @Override
    protected int getFragmentTitle() {
        return R.string.title_favorites;
    }

    @Override
    public void setActionBar() {
        ((MainActivity) getActivity()).initSearchBar(getResources().getString(getFragmentTitle()));
    }
}
