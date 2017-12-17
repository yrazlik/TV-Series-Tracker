package com.yrazlik.tvseriestracker.util;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.yrazlik.tvseriestracker.R;

/**
 * Created by yrazlik on 17.12.2017.
 */

public class AdUtils {

    public static boolean ADS_ENABLED = true;

    public static void disableAds() {
        ADS_ENABLED = false;
    }

    public static void initAds(Context context) {
        if(ADS_ENABLED) {
            MobileAds.initialize(context, context.getResources().getString(R.string.ads_app_id));
        }
    }

    public static void loadBannerAd(AdView adView) {
        if(ADS_ENABLED) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }
}
