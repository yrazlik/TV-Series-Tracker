package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.FavoritesListAdapter;

/**
 * Created by yrazlik on 12.11.2017.
 */

public class FavoritesFragment extends BaseFragment {

    private ListView favoritesList;
    private FavoritesListAdapter favoritesListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    private void initUI() {
        favoritesList = rootView.findViewById(R.id.favoritesList);

    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_favorites;
    }
}
