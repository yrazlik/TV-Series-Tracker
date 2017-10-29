package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class LinksDto {

    private SelfDto self;
    private PreviousEpisodeDto previousEpisode;

    public SelfDto getSelf() {
        return self;
    }

    public void setSelf(SelfDto self) {
        this.self = self;
    }

    public PreviousEpisodeDto getPreviousEpisode() {
        return previousEpisode;
    }

    public void setPreviousEpisode(PreviousEpisodeDto previousEpisode) {
        this.previousEpisode = previousEpisode;
    }
}
