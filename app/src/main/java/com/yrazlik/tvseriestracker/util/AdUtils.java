package com.yrazlik.tvseriestracker.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.yrazlik.tvseriestracker.AdCache;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 17.12.2017.
 */

public class AdUtils {

    public static final String NATIVE_ADUNIT_ID = "ca-app-pub-3219973945608696/1480878764";
    public static boolean ADS_ENABLED = true;
    private static final String TAG_ADS = "ADS_TAG";
    private static int mClickCount = 0;

    private static final long AD_CACHE_TIMEOUT = 45 * 1000;
    private LayoutInflater mLayoutInflater = LayoutInflater.from(TvSeriesTrackerApp.getAppContext());;


    private static Map<String, AdCache> cachedAds = new HashMap<>();
    private UnifiedNativeAdView emptyAd = new UnifiedNativeAdView(TvSeriesTrackerApp.getAppContext());
    private static AdUtils mInstance;


    private static InterstitialAd mInterstitialAd;

    public static void disableAds() {
        ADS_ENABLED = false;
    }

    public static void initAds(Context context) {
        if(ADS_ENABLED) {
            //MobileAds.initialize(context, context.getResources().getString(R.string.ads_app_id));
        }
    }

    public static AdUtils getInstance() {
        if(mInstance == null) {
            mInstance = new AdUtils();
            cachedAds.put(NATIVE_ADUNIT_ID, null);
        }
        return mInstance;
    }

    private UnifiedNativeAdView getEmptyAd() {
        if(emptyAd == null) {
            emptyAd = new UnifiedNativeAdView(TvSeriesTrackerApp.getAppContext());
        }
        return emptyAd;
    }

    public void loadNativeAd(Context context, final String adUnitId) {
        if(ADS_ENABLED) {
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions)/*.setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)*/.build();
            AdLoader adLoader = new AdLoader.Builder(context, adUnitId)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            AdUtils.this.updateAds(adUnitId, unifiedNativeAd);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            if(adError != null) {
                                try {
                                    Log.d(TAG_ADS,"Failed to load ad: " + adError.getMessage());
                                } catch (Exception ignored) {
                                    Log.d(TAG_ADS,"Failed to load ad exception");
                                }
                            } else {
                                Log.d(TAG_ADS,"Failed to load ad");
                            }
                        }
                    })
                    .withNativeAdOptions(adOptions)
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            makeAllAdsNull();
        }
    }

    private void updateAds(String adUnitId, UnifiedNativeAd nativeAd) {
        synchronized (nativeAd) {
            AdCache cachedAd = cachedAds.containsKey(adUnitId) ? cachedAds.get(adUnitId) : null;
            if(cachedAd != null) {
                UnifiedNativeAd oldAd = cachedAd.getNativeAd();
                if(oldAd != null) {
                    oldAd.destroy();
                }
            }
            cachedAds.remove(adUnitId);
            AdCache adCache = new AdCache();
            adCache.setAdUnitId(adUnitId);
            adCache.setLastRequestTime(new Date().getTime());
            adCache.setNativeAd(nativeAd);
            cachedAds.put(adUnitId, adCache);
        }
    }

    private void makeAllAdsNull() {
        synchronized (cachedAds) {
            cachedAds.clear();
        }
    }

    public UnifiedNativeAd getCachedAd(String adUnitId) {
        if(ADS_ENABLED) {
            if(cachedAds.containsKey(adUnitId)) {
                AdCache adCache = cachedAds.get(adUnitId);
                if(adCache != null) {
                    if (shouldRequestNewAd(adCache.getLastRequestTime())) {
                        loadNativeAd(TvSeriesTrackerApp.getAppContext(), adUnitId);
                    }
                    return adCache.getNativeAd();
                } else {
                    loadNativeAd(TvSeriesTrackerApp.getAppContext(), adUnitId);
                }
            }
        }
        return null;
    }

    private boolean shouldRequestNewAd(long lastRequestTime) {
        long now = new Date().getTime();
        if(now - lastRequestTime > AD_CACHE_TIMEOUT) {
            return true;
        }
        return false;
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

    public View createSmallAdView(String adUnitId) {
        if(cachedAds.containsKey(adUnitId)) {
            UnifiedNativeAd nativeAd = getCachedAd(adUnitId);
            if(nativeAd != null) {
                return createSmallNativeAdView(nativeAd);
            }
        }
        return getEmptyAd();
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

    public static void showInterstitial(boolean forceShow) {
        mClickCount++;
        if(ADS_ENABLED) {
            if((mClickCount == 8 || mClickCount % 40 == 0) || forceShow) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d(TAG_ADS, "The interstitial not loaded yet.");
                        }
                    }
                }, 2000);

            }
        }
    }

    private UnifiedNativeAdView createSmallNativeAdView(UnifiedNativeAd ad) {
        try {
            UnifiedNativeAdView adView = (UnifiedNativeAdView) mLayoutInflater.inflate(R.layout.list_row_small_nativeadview, null, false);
            ImageView adIV = adView.findViewById(R.id.adImage);
            RobotoTextView headlineTV = adView.findViewById(R.id.adHeadline);
            RobotoTextView bodyTV = adView.findViewById(R.id.adBody);
            RobotoTextView ratingTv = adView.findViewById(R.id.ratingTv);
            RelativeLayout ratingContainer = adView.findViewById(R.id.ratingContainer);
            RelativeLayout ctaContainer = adView.findViewById(R.id.ctaContainer);
            RobotoTextView ctaTv = adView.findViewById(R.id.callToActionTV);

            if(ad.getIcon() != null && ad.getIcon().getDrawable() != null) {
                adIV.setVisibility(View.VISIBLE);
                adIV.setImageDrawable(ad.getIcon().getDrawable());
            } else {
                adIV.setVisibility(View.INVISIBLE);
            }
            adView.setIconView(adIV);

            headlineTV.setText(ad.getHeadline());
            adView.setHeadlineView(headlineTV);

            bodyTV.setText(ad.getBody());
            adView.setBodyView(bodyTV);

            if(ad.getStarRating() != null) {
                ratingContainer.setVisibility(View.VISIBLE);
                ratingTv.setText(ad.getStarRating() + "");
            } else {
                ratingContainer.setVisibility(View.GONE);
            }

            adView.setStarRatingView(ratingContainer);

            ctaTv.setText(ad.getCallToAction());
            adView.setCallToActionView(ctaContainer);

            adView.setNativeAd(ad);

            return adView;
        } catch (Exception e) {
            return getEmptyAd();
        }
    }
}
