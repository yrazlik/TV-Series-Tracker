package com.yrazlik.tvseriestracker.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.yrazlik.tvseriestracker.R;

/**
 * Created by yrazlik on 17.12.2017.
 */

public class AdUtils {

    public static boolean ADS_ENABLED = true;
    private static final String TAG_ADS = "ADS_TAG";
    private static int mClickCount = 0;

    private static InterstitialAd mInterstitialAd;

    public static void disableAds() {
        ADS_ENABLED = false;
    }

    public static void initAds(Context context) {
        if(ADS_ENABLED) {
            MobileAds.initialize(context, context.getResources().getString(R.string.ads_app_id));
        }
    }

    public static void loadBannerAd(AdView adView, AdListener adListener) {
        if(ADS_ENABLED) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            if(adListener != null) {
                adView.setAdListener(adListener);
            }
        }
    }

    public static void loadBigBannerAd(AdView adView, AdListener adListener) {
        if(ADS_ENABLED) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            if(adListener != null) {
                adView.setAdListener(adListener);
            }
        }
    }

    public static void initInterstitialAds(Context context) {
        if(ADS_ENABLED) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-3219973945608696/7557702300");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });
        }
    }

    public static void showInterstitial() {
        mClickCount++;
        if(ADS_ENABLED) {
            if(mClickCount == 5 || mClickCount % 30 == 0) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG_ADS, "The interstitial not loaded yet.");
                }
            }
        }
    }
}
