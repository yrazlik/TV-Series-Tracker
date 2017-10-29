package com.yrazlik.tvseriestracker.data;

import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class EmbeddedDto {

    private List<CastDto> cast;

    public List<CastDto> getCast() {
        return cast;
    }

    public void setCast(List<CastDto> cast) {
        this.cast = cast;
    }
}
