package com.yrazlik.tvseriestracker.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 18.11.2017.
 */

public class CastCreditDto {

    @SerializedName("_links")
    private CastLinksDto links;
    @SerializedName("_embedded")
    private CastEmbeddedDto embedded;

    public CastLinksDto getLinks() {
        return links;
    }

    public void setLinks(CastLinksDto links) {
        this.links = links;
    }

    public CastEmbeddedDto getEmbedded() {
        return embedded;
    }

    public void setEmbedded(CastEmbeddedDto embedded) {
        this.embedded = embedded;
    }
}
