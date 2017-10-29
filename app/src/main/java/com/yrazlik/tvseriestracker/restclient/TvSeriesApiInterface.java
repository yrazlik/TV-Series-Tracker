package com.yrazlik.tvseriestracker.restclient;

import com.yrazlik.tvseriestracker.data.SearchResultDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yrazlik on 29/10/17.
 */

public interface TvSeriesApiInterface {

    @GET("search/shows")
    Call<List<SearchResultDto>> getSearchResult(@Query("q") String showName);

}
