package com.yrazlik.tvseriestracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.ExternalsDto;
import com.yrazlik.tvseriestracker.data.NetworkDto;
import com.yrazlik.tvseriestracker.data.RatingDto;
import com.yrazlik.tvseriestracker.data.ScheduleDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.AdUtils;
import com.yrazlik.tvseriestracker.util.Utils;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowSummaryInfoFragment extends BaseFragment {

    private AdView mAdView;
    private RelativeLayout imdbRL;
    private RobotoTextView showTitle, airsOnTV, scheduledTV, premieredTV, genresTV, statusTV, ratingTV, showSummaryTV, imdbTV;

    private ShowDto showDto;

    public static ShowSummaryInfoFragment newInstance(ShowDto showDto) {
        ShowSummaryInfoFragment showSummaryInfoFragment = new ShowSummaryInfoFragment();
        showSummaryInfoFragment.showDto = showDto;
        return showSummaryInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        initAds();
        return rootView;
    }

    private void initAds() {
        mAdView = rootView.findViewById(R.id.bannerAdViewBig);
        AdUtils.loadBigBannerAd(mAdView, null);
    }

    private void initUI() {
        showTitle = rootView.findViewById(R.id.showTitle);
        airsOnTV = rootView.findViewById(R.id.airsOnTV);
        scheduledTV = rootView.findViewById(R.id.scheduledTV);
        premieredTV = rootView.findViewById(R.id.premieredTV);
        genresTV = rootView.findViewById(R.id.genresTV);
        statusTV = rootView.findViewById(R.id.statusTV);
        ratingTV = rootView.findViewById(R.id.ratingTV);
        showSummaryTV = rootView.findViewById(R.id.showSummaryTV);
        imdbRL = rootView.findViewById(R.id.imdbRL);
        imdbTV = rootView.findViewById(R.id.imdbTV);

        if(showDto != null) {
            RatingDto ratingDto = showDto.getRating();
            NetworkDto network = showDto.getNetwork();
            ScheduleDto schedule = showDto.getSchedule();
            String premiered = showDto.getPremiered();
            String genres = showDto.getGenresText();
            String status = showDto.getStatus();
            final ExternalsDto externals = showDto.getExternals();

            showTitle.setText(showDto.getName());
            ratingTV.setText(ratingDto != null ? ratingDto.getAverage() + "" : "-");
            airsOnTV.setText((network != null && network.getName() != null && !network.getName().equalsIgnoreCase("")) ? network.getName() : "-");

            String scheduledText = "-";
            if(schedule != null) {
                List<String> days = schedule.getDays();
                if(days != null && days.size() > 0 && schedule.getTime() != null) {
                    scheduledText = "";
                    for(String day : days) {
                        scheduledText += day + ",";
                    }
                    if(schedule.getTime() != null && schedule.getTime().length() > 0) {
                        scheduledText += " at " + schedule.getTime();
                    }
                }
            }

            scheduledTV.setText(scheduledText);
            premieredTV.setText(premiered != null && !premiered.equalsIgnoreCase("") ? premiered : "-");
            genresTV.setText(genres != null && !genres.equalsIgnoreCase("") ? genres : "-");
            statusTV.setText(status != null && !status.equalsIgnoreCase("") ? status : "-");
            showSummaryTV.setText(showDto.getSummary() != null ? android.text.Html.fromHtml(showDto.getSummary()).toString() : "...");

            if(externals != null && externals.getImdb() != null && externals.getImdb().length() > 0) {
                imdbTV.setText(externals.getImdb());
                Utils.underline(imdbTV);
                imdbRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AdUtils.showInterstitial(false);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(getResources().getString(R.string.imdb_title_url) + externals.getImdb()));
                        startActivity(i);
                    }
                });
            } else {
                imdbTV.setText("-");
            }
        }
    }

    @Override
    protected void retry() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_summary_info;
    }

    @Override
    protected int getFragmentTitle() {
        return R.string.fragment_title_show_summary_info;
    }

    public int getTitle() {
        return R.string.fragment_title_show_summary_info;
    }

    @Override
    public void setActionBar() {
        setDefaultActionBar(showDto.getName(), getResources().getString(getFragmentTitle()));
    }
}
