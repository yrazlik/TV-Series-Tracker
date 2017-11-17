package com.yrazlik.tvseriestracker.data;

import java.util.List;

/**
 * Created by yrazlik on 17.11.2017.
 */

public class ShowSchedule {

    private ShowDto show;
    private EpisodeDto episode;

    public ShowSchedule(ShowDto show, EpisodeDto episode) {
        this.show = show;
        this.episode = episode;
    }

    public ShowDto getShow() {
        return show;
    }

    public void setShow(ShowDto show) {
        this.show = show;
    }

    public EpisodeDto getEpisode() {
        return episode;
    }

    public void setEpisode(EpisodeDto episode) {
        this.episode = episode;
    }
}
