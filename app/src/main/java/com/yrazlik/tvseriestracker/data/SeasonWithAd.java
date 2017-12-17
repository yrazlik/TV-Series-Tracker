package com.yrazlik.tvseriestracker.data;

import com.google.android.gms.ads.AdView;

/**
 * Created by yrazlik on 17.12.2017.
 */

public class SeasonWithAd {

    private long id;
    private boolean isAd = false;

    public SeasonWithAd(boolean isAd) {
        this.isAd = isAd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }
}
