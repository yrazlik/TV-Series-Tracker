package com.yrazlik.tvseriestracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.yrazlik.tvseriestracker.activities.MainActivity;

/**
 * Created by yrazlik on 25/03/17.
 */

public class DeeplinkingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = null;

        if(intent != null){
            data = intent.getData();
        }

        if (TvSeriesTrackerApp.appIsRunning) {
            Intent i = new Intent(DeeplinkingActivity.this, MainActivity.class);

            if(data != null){
                i.setData(data);
            }

            startActivity(i);

        } else {
            TvSeriesTrackerApp.appIsRunning = true;
            Intent i = new Intent(DeeplinkingActivity.this, MainActivity.class);

            if(data != null){
                i.setData(data);
            }

            startActivity(i);
        }

        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
