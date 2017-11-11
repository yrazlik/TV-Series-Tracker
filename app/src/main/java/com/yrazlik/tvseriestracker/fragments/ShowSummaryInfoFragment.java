package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.data.NetworkDto;
import com.yrazlik.tvseriestracker.data.RatingDto;
import com.yrazlik.tvseriestracker.data.ScheduleDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class ShowSummaryInfoFragment extends BaseFragment {

    private RobotoTextView showTitle, airsOnTV, scheduledTV, premieredTV, genresTV, statusTV, ratingTV, showSummaryTV;

    private ShowDto showDto;

    public ShowSummaryInfoFragment() {}

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
        return rootView;
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

        if(showDto != null) {
            RatingDto ratingDto = showDto.getRating();
            NetworkDto network = showDto.getNetwork();
            ScheduleDto schedule = showDto.getSchedule();
            String premiered = showDto.getPremiered();
            String genres = showDto.getGenresText();
            String status = showDto.getStatus();

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
                    scheduledText += " at " + schedule.getTime();
                }
            }

            scheduledTV.setText(scheduledText);
            premieredTV.setText(premiered != null && !premiered.equalsIgnoreCase("") ? premiered : "-");
            genresTV.setText(genres != null && !genres.equalsIgnoreCase("") ? genres : "-");
            statusTV.setText(status != null && !status.equalsIgnoreCase("") ? status : "-");
            showSummaryTV.setText(showDto.getSummary() != null ? android.text.Html.fromHtml(showDto.getSummary()).toString() : "...");

        }
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
