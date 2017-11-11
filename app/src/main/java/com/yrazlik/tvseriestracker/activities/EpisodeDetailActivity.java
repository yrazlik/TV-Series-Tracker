package com.yrazlik.tvseriestracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.util.PicassoImageLoader;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class EpisodeDetailActivity extends BaseActivity {

    public static final String EXTRA_SHOW_ID = "showId";
    public static final String EXTRA_SEASON = "season";
    public static final String EXTRA_EPISODE = "episode";
    public static final String EXTRA_SHOW_NAME = "showName";
    public static final String EXTRA_EPISODE_IMAGE = "episodeImage";
    public static final String EXTRA_EPISODE_NAME = "episodeName";
    public static final String EXTRA_EPISODE_SUMMARY = "episodeSummary";
    public static final String EXTRA_EPISODE_DATE = "episodeDate";

    private ImageView episodeIV;
    private RobotoTextView seriesNameTV, episodeNameTV, episodeCountTV, episodeDateTV, episodeSummaryTV;

    private long showId;
    private long season, episode;
    private String showName, episodeName, date, summary, imageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        getExtras();
        initUI();
    }

    private void initUI() {
        episodeIV = (ImageView) findViewById(R.id.episodeIV);
        seriesNameTV = (RobotoTextView) findViewById(R.id.seriesNameTV);
        episodeNameTV = (RobotoTextView) findViewById(R.id.episodeNameTV);
        episodeCountTV = (RobotoTextView) findViewById(R.id.episodeCountTV);
        episodeDateTV = (RobotoTextView) findViewById(R.id.episodeDateTV);
        episodeSummaryTV = (RobotoTextView) findViewById(R.id.episodeSummaryTV);

        PicassoImageLoader.getInstance(this).loadImage(imageUrl, episodeIV);
        seriesNameTV.setText(showName);
        episodeNameTV.setText(episodeName);
        episodeCountTV.setText(getResources().getString(R.string.season) + " " + season + " " + getResources().getString(R.string.episode) + " " + episode);
        episodeDateTV.setText(date);
        episodeSummaryTV.setText(android.text.Html.fromHtml(summary == null ? "-" : summary));
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        showId = extras.getLong(EXTRA_SHOW_ID);
        season = extras.getLong(EXTRA_SEASON);
        episode = extras.getLong(EXTRA_EPISODE);
        showName = extras.getString(EXTRA_SHOW_NAME);
        episodeName = extras.getString(EXTRA_EPISODE_NAME);
        date = extras.getString(EXTRA_EPISODE_DATE);
        summary = extras.getString(EXTRA_EPISODE_SUMMARY);
        imageUrl = extras.getString(EXTRA_EPISODE_IMAGE);
    }
}
