package com.yrazlik.tvseriestracker.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

/**
 * Created by yrazlik on 11.11.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected int enterAnim;
    protected int exitAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setToolbarTitle();
    }

    private void setToolbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getToobarTitle());
        }
    }

    protected void showProgressWithWhiteBG() {
        CardView loadingView = (CardView) findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.findViewById(R.id.retryContainer).setOnClickListener(null);
            loadingView.findViewById(R.id.imgRetry).setVisibility(View.GONE);
            loadingView.findViewById(R.id.progress).setVisibility(View.VISIBLE);
            ((RobotoTextView) loadingView.findViewById(R.id.loadingText)).setText(getString(R.string.loading));
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void dismissProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    CardView loadingView = (CardView) findViewById(R.id.loadingView);
                    if (loadingView != null) {
                        loadingView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void showRetryView() {
        try {
            CardView loadingView = (CardView) findViewById(R.id.loadingView);
            if (loadingView != null) {
                loadingView.findViewById(R.id.imgRetry).setVisibility(View.VISIBLE);
                loadingView.findViewById(R.id.progress).setVisibility(View.GONE);
                ((RobotoTextView) loadingView.findViewById(R.id.loadingText)).setText(getString(R.string.retry));
                loadingView.findViewById(R.id.retryContainer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retry();
                    }
                });
                loadingView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(enterAnim, exitAnim);

    }

    protected abstract void retry();
    protected abstract int getLayoutResourceId();
    protected abstract String getToobarTitle();
}
