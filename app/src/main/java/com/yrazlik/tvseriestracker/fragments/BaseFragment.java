package com.yrazlik.tvseriestracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

/**
 * Created by yrazlik on 29.10.2017.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    private int fragmentStartTransaction = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null) {
            super.onCreateView(inflater, container, savedInstanceState);
            rootView = inflater.inflate(getLayoutResourceId(), container, false);
        }
        return rootView;
    }

    public void setFragmentStartTransaction(int fragmentStartTransaction) {
        this.fragmentStartTransaction = fragmentStartTransaction;
    }

    protected void showProgressWithWhiteBG() {
        if(rootView != null) {
            CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
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
                CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
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

    protected abstract void retry();
    protected abstract int getLayoutResourceId();

}
