package com.yrazlik.tvseriestracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.ShowDetailPagerAdapter;
import com.yrazlik.tvseriestracker.data.ImageDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.fragments.EpisodesFragment;
import com.yrazlik.tvseriestracker.fragments.ShowCastInfoFragment;
import com.yrazlik.tvseriestracker.fragments.ShowSummaryInfoFragment;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.view.PagerSlidingTabStrip;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowDetailActivity extends BaseActivity implements ApiResponseListener{

    public static final String EXTRA_SHOW_ID = "extraShowId";

    private ViewPager showDetailPager;
    private ShowDetailPagerAdapter showDetailPagerAdapter;
    private ImageView showIV;
    private TabLayout tabs;

    private long showId;
    private ShowDto showData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterAnim = R.anim.fadein;
        exitAnim =  R.anim.slide_top_out;
        showProgressWithWhiteBG();
        getExtras();
        initUI();
        requestShowDetail();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        showId = extras.getLong(EXTRA_SHOW_ID);
    }

    private void initUI() {
        showDetailPager = (ViewPager) findViewById(R.id.showDetailPager);
        showDetailPager.setOffscreenPageLimit(3);
        showIV = (ImageView) findViewById(R.id.showIV);
    }

    private void setupShowsPager() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        showDetailPagerAdapter = new ShowDetailPagerAdapter(getSupportFragmentManager());
        ShowSummaryInfoFragment showSummaryInfoFragment = ShowSummaryInfoFragment.newInstance(showData);
        ShowCastInfoFragment showCastInfoFragment = ShowCastInfoFragment.newInstance(showData);
        EpisodesFragment episodesFragment = EpisodesFragment.newInstance(showData);
        showDetailPagerAdapter.addFragment(showSummaryInfoFragment, getResources().getString(showSummaryInfoFragment.getTitle()));
        showDetailPagerAdapter.addFragment(episodesFragment, getResources().getString(episodesFragment.getTitle()));
        showDetailPagerAdapter.addFragment(showCastInfoFragment, getResources().getString(showCastInfoFragment.getTitle()));
        showDetailPager.setAdapter(showDetailPagerAdapter);
        tabs.setupWithViewPager(showDetailPager);
    }

    private void requestShowDetail() {
        ApiHelper.getInstance(this).getShowInfoByIdWithCast(showId, this);
    }

    private void populatePage() {
        setupShowsPager();
        if(showData != null) {
            ImageDto showImage = showData.getImage();
            if(showImage != null) {
                PicassoImageLoader.getInstance(this).loadImage(showImage.getOriginal(), showIV);
            }
        }
    }

    @Override
    protected void retry() {
        requestShowDetail();
    }

    @Override
    public void onResponse(Object response) {
        this.showData = (ShowDto) response;
        populatePage();
        dismissProgress();
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {
        showRetryView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_show_detail;
    }
}
