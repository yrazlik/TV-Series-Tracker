package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class CountryDto {

    private String name;
    private String code;
    private String timezone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
