package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.activities.CastDetailActivity;
import com.yrazlik.tvseriestracker.adapters.CastInfoListAdapter;
import com.yrazlik.tvseriestracker.data.CastDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.AdUtils;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowCastInfoFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private ListView castLV;
    private CastInfoListAdapter castInfoListAdapter;

    private ShowDto showDto;

    public static ShowCastInfoFragment newInstance(ShowDto showDto) {
        ShowCastInfoFragment showCastInfoFragment = new ShowCastInfoFragment();
        showCastInfoFragment.setActionBar = false;
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
        castLV.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        AdUtils.showInterstitial();
        CastDto cast = castInfoListAdapter.getItem(position);
        if(cast.getPerson() != null) {
            Intent i = new Intent(getActivity(), CastDetailActivity.class);
            i.putExtra(CastDetailActivity.EXTRA_CAST_NAME, cast.getPerson().getName());
            i.putExtra(CastDetailActivity.EXTRA_CAST_ID, cast.getPerson().getId());
            i.putExtra(CastDetailActivity.EXTRA_CAST_IMG, cast.getPerson().getImage() != null ? cast.getPerson().getImage().getMedium() : null);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.slide_bottom_in, R.anim.fadeout);
        }
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

    @Override
    protected int getFragmentTitle() {
        return R.string.fragment_title_show_cast_info;
    }

    public int getTitle() {
        return R.string.fragment_title_show_cast_info;
    }

    @Override
    public void setActionBar() {
        setDefaultActionBar(showDto.getName(), getResources().getString(getFragmentTitle()));
    }
}
