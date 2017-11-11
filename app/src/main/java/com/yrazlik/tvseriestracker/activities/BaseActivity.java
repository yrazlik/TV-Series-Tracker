package com.yrazlik.tvseriestracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yrazlik on 11.11.2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startActivity(Context currentActivity, Class nextActivity) {
        Intent i = new Intent(currentActivity, nextActivity);
        startActivity(i);
    }

}
