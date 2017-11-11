package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.CastInfoListAdapter;
import com.yrazlik.tvseriestracker.data.ShowDto;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowCastInfoFragment extends BaseFragment{

    private ListViewCompat castLV;
    private CastInfoListAdapter castInfoListAdapter;

    private ShowDto showDto;

    public static ShowCastInfoFragment newInstance(ShowDto showDto) {
        ShowCastInfoFragment showCastInfoFragment = new ShowCastInfoFragment();
        showCastInfoFragment.showDto = showDto;
        return showCastInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return rootView;
    }

    private void initUI() {
        castLV = rootView.findViewById(R.id.castLV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(showDto.get_embedded() != null && showDto.get_embedded().getCast() != null) {
            castInfoListAdapter = new CastInfoListAdapter(getContext(), R.layout.list_row_cast, showDto.get_embedded().getCast());
            castLV.setAdapter(castInfoListAdapter);
        }
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_cast_info;
    }

    public int getTitle() {
        return R.string.fragment_title_show_cast_info;
    }
}
