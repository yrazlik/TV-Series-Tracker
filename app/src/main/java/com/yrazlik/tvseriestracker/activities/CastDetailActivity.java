package com.yrazlik.tvseriestracker.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.TrendingShowsListAdapter;
import com.yrazlik.tvseriestracker.data.CastCreditDto;
import com.yrazlik.tvseriestracker.data.CastEmbeddedDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.AdUtils;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 18.11.2017.
 */

public class CastDetailActivity extends BaseActivity implements ApiResponseListener{

    public static final String EXTRA_CAST_NAME = "EXTRA_CAST_NAME";
    public static final String EXTRA_CAST_ID = "EXTRA_CAST_ID";
    public static final String EXTRA_CAST_IMG = "EXTRA_CAST_IMG";

    private AdView adView;
    private ImageView castIV;
    private RobotoTextView castNameTV;
    private ListView castShowsLV;

    private long castId;
    private String castName;
    private String castImg;
    private TrendingShowsListAdapter trendingShowsListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getExtras();
        enterAnim = R.anim.fadein;
        exitAnim =  R.anim.slide_top_out;
        super.onCreate(savedInstanceState);
        initUI();
        showProgressWithWhiteBG();
        getCastInfo();
    }

    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        castId = bundle.getLong(EXTRA_CAST_ID);
        castName = bundle.getString(EXTRA_CAST_NAME);
        castImg = bundle.getString(EXTRA_CAST_IMG);
    }

    private void initUI() {
        adView = (AdView) findViewById(R.id.bannerAdView);
        castIV = (ImageView) findViewById(R.id.castIV);
        castNameTV = (RobotoTextView) findViewById(R.id.castName);
        castShowsLV = (ListView) findViewById(R.id.castShowsLV);

        PicassoImageLoader.getInstance(this).loadCircleImage(castImg, castIV);
        castNameTV.setText(castName);
        AdUtils.loadBannerAd(adView, new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });
    }

    private void getCastInfo() {
        ApiHelper.getInstance(this).getCastDetail(castId, this);
    }

    @Override
    protected void retry() {
        getCastInfo();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_cast_detail;
    }

    @Override
    protected String getToobarTitle() {
        return castName == null ? getString(R.string.app_name) : castName;
    }

    @Override
    public void onResponse(Object response) {
        List<ShowDto> shows = new ArrayList<>();
        List<CastCreditDto> creditDtos = (List<CastCreditDto>) response;
        if(creditDtos != null) {
            for(int i = 0; i < creditDtos.size(); i++) {
                CastEmbeddedDto embeddedDto = creditDtos.get(i).getEmbedded();
                if(embeddedDto != null) {
                    ShowDto showDto = embeddedDto.getShow();
                    if(showDto != null) {
                        shows.add(showDto);
                    }
                }
            }
        }
        TrendingShowsListAdapter trendingShowsListAdapter = new TrendingShowsListAdapter(this, R.layout.list_row_trending_shows, shows);
        castShowsLV.setAdapter(trendingShowsListAdapter);
        dismissProgress();
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {
        dismissProgress();
    }
}
