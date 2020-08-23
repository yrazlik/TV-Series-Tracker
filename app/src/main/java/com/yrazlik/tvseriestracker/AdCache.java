package com.yrazlik.tvseriestracker;

import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class AdCache {

    private long lastRequestTime;
    private String adUnitId;
    private UnifiedNativeAd nativeAd;

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(long lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public UnifiedNativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(UnifiedNativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
