package com.yrazlik.tvseriestracker.restclient;

import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.SeasonDto;
import com.yrazlik.tvseriestracker.data.ShowDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yrazlik on 29/10/17.
 */

public interface TvSeriesApiInterface {

    @GET("search/shows")
    Call<List<SearchResultDto>> getSearchResults(@Query("q") String showName);

    @GET("singlesearch/shows")
    Call<ShowDto> getSingleSearchResult(@Query("q") String showName);

    @GET("shows/{id}")
    Call<ShowDto> getShowInfoById(@Path("id") long id);

    @GET("shows/{id}")
    Call<ShowDto> getShowInfoByIdWithCast(@Path("id") long id, @Query("embed") String cast);

    @GET("shows/{id}/episodes")
    Call<List<EpisodeDto>> getAllEpisodes(@Path("id") long id);

    @GET("seasons/{id}/episodes")
    Call<List<EpisodeDto>> getSeasonEpisodes(@Path("id") long id);

    @GET("shows/{id}/episodebynumber")
    Call<EpisodeDto> getEpisode(@Path("id") long id, @Query("season") long season, @Query("number") long number);

    @GET("shows/{id}/seasons")
    Call<List<SeasonDto>> getSeasons(@Path("id") long id);

}
