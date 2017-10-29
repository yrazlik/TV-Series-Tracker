package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class SearchResultDto {

    private double score;
    private ShowDto show;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public ShowDto getShow() {
        return show;
    }

    public void setShow(ShowDto show) {
        this.show = show;
    }
}

