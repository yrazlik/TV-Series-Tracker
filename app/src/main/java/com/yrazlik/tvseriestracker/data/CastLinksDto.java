package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 18.11.2017.
 */

public class CastLinksDto {

    private CastShowDto show;
    private CastCharacterDto character;

    public CastShowDto getShow() {
        return show;
    }

    public void setShow(CastShowDto show) {
        this.show = show;
    }

    public CastCharacterDto getCharacter() {
        return character;
    }

    public void setCharacter(CastCharacterDto character) {
        this.character = character;
    }
}
