package com.yrazlik.tvseriestracker.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

/**
 * Created by yrazlik on 29.10.2017.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected boolean setActionBar = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null) {
            super.onCreateView(inflater, container, savedInstanceState);
            rootView = inflater.inflate(getLayoutResourceId(), container, false);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(setActionBar) {
            setActionBar();
        }
    }

    protected void showProgressWithWhiteBG() {
        if(rootView != null) {
            CardView loadingView = rootView.findViewById(R.id.loadingView);
            if (loadingView != null) {
                loadingView.findViewById(R.id.retryContainer).setOnClickListener(null);
                loadingView.findViewById(R.id.imgRetry).setVisibility(View.GONE);
                loadingView.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                ((RobotoTextView) loadingView.findViewById(R.id.loadingText)).setText(getString(R.string.loading));
                loadingView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void dismissProgress() {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void showRetryView() {
        if(rootView != null) {
            try {
                CardView loadingView = rootView.findViewById(R.id.loadingView);
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
    }

    protected void setDefaultActionBar(String title, String subtitle) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(getFragmentTitle());
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME);
        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout searchBar = (RelativeLayout) inf.inflate(R.layout.actionbar_default, null);
        RobotoTextView actionBarTitle = searchBar.findViewById(R.id.action_bar_title);
        RobotoTextView actionBarSubTitle = searchBar.findViewById(R.id.action_bar_subtitle);
        actionBarTitle.setText(title == null ? "" : title);
        actionBarSubTitle.setText(subtitle == null ? "" : subtitle);
        actionBarSubTitle.setVisibility(subtitle == null || subtitle.length() == 0 ? View.GONE : View.VISIBLE);
        actionBar.setCustomView(searchBar);
    }

    protected abstract void retry();
    protected abstract int getLayoutResourceId();
    protected abstract int getFragmentTitle();
    public abstract void setActionBar();

}
