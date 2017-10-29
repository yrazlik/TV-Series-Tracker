package com.yrazlik.tvseriestracker.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class SeasonDto {

    private long id;
    private String url;
    private String number;
    private String name;
    private long episodeOrder;
    private String premiereDate;
    private String endDate;
    private NetworkDto network;
    private WebChannelDto webChannel;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEpisodeOrder() {
        return episodeOrder;
    }

    public void setEpisodeOrder(long episodeOrder) {
        this.episodeOrder = episodeOrder;
    }

    public String getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(String premiereDate) {
        this.premiereDate = premiereDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public NetworkDto getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDto network) {
        this.network = network;
    }

    public WebChannelDto getWebChannel() {
        return webChannel;
    }

    public void setWebChannel(WebChannelDto webChannel) {
        this.webChannel = webChannel;
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
