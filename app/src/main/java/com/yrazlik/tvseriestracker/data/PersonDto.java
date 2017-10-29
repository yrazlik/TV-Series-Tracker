package com.yrazlik.tvseriestracker.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class PersonDto {

    private long id;
    private String url;
    private String name;
    private ImageDto image;
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

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public LinksDto getLinks() {
        return links;
    }

    public void setLinks(LinksDto links) {
        this.links = links;
    }
}
