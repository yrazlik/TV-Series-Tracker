package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class WebChannelDto {

    private long id;
    private String name;
    private CountryDto country;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto country) {
        this.country = country;
    }
}
