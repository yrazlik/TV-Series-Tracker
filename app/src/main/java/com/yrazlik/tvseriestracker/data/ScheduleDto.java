package com.yrazlik.tvseriestracker.data;

import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class ScheduleDto {

    private String time;
    private List<String> days;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
