package com.yrazlik.tvseriestracker.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class EpisodeDto {

    private long id;
    private String url;
    private String name;
    private long season;
    private long number;
    @SerializedName("airdate")
    private String airDate;
    @SerializedName("airtime")
    private String airTime;
    @SerializedName("airstamp")
    private String airStamp;
    private long runtime;
    private ImageDto image;
    private String summary;
    @SerializedName("_links")
    private LinksDto links;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSeason() {
        return season;
    }

    public void setSeason(long season) {
        this.season = season;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getAirStamp() {
        return airStamp;
    }

    public void setAirStamp(String airStamp) {
        this.airStamp = airStamp;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LinksDto getLinks() {
        return links;
    }

    public void setLinks(LinksDto links) {
        this.links = links;
    }
}
