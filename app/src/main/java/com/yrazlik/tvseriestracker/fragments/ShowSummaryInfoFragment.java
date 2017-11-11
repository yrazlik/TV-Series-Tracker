package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.tvseriestracker.R;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowSummaryInfoFragment extends BaseFragment {

    public ShowSummaryInfoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_summary_info;
    }

    public int getTitle() {
        return R.string.fragment_title_show_summary_info;
    }
}
