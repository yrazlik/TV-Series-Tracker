package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.tvseriestracker.R;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class NullFragment extends BaseFragment {

    public static NullFragment instantiateFragment() {
        NullFragment nullFragment = new NullFragment();
        nullFragment.setFragmentStartTransaction(-1);
      //  nullFragment.startFragment(true);
        return nullFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return rootView;
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_null;
    }
}
