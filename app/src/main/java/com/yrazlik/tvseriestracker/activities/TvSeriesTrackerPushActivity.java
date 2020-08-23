package com.yrazlik.tvseriestracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.TvSeriesTrackerNotification;

/**
 * Created by yrazlik on 25/03/17.
 */

public class TvSeriesTrackerPushActivity extends Activity{

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent pIntent = getIntent();

        if(pIntent != null) {

            Uri data = pIntent.getData();
            Bundle extras = pIntent.getExtras();

            if (TvSeriesTrackerApp.appIsRunning) {
                Intent i = new Intent(TvSeriesTrackerPushActivity.this, MainActivity.class);
                i.setData(data);
                i.putExtras(extras);
                i.putExtra(TvSeriesTrackerNotification.PARAMETER_IS_PUSH, true);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(this, MainActivity.class);
                i.setData(data);
                i.putExtras(extras);
                i.putExtra(TvSeriesTrackerNotification.PARAMETER_IS_PUSH, true);
                startActivity(i);
            }
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
