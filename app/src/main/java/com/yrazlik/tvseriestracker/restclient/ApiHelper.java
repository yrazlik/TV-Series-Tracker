package com.yrazlik.tvseriestracker.restclient;

import android.content.Context;

import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.SeasonDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.util.Utils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class ApiHelper {

    private Context mContext;

    private static ApiHelper mInstance;

    public static ApiHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiHelper();
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private ApiHelper() {

    }

    private void onSuccessResponse(IApiResponseHandler responseHandler, Response response) {
        if(responseHandler != null) {
            try {
                responseHandler.onResponse(response.code(), response.errorBody().string(), response.body());
            } catch (IOException e) {
                responseHandler.onResponse(response.code(), null, response.body());
            } catch (Exception e) {
                responseHandler.onResponse(response.code(), null, response.body());
            }
        }
    }

    private void onFailResponse(final IApiResponseHandler responseHandler, Throwable t) {
        if(responseHandler != null) {
            responseHandler.onFail(t);
        }
    }

    public void searchShows(final String showName, final ApiResponseListener responseListener) {

        if(!Utils.isNullOrEmpty(showName)) {
            Call<List<SearchResultDto>> call = TvSeriesApiClient.getApiInterface().getSearchResults(showName);
            call.enqueue(new Callback<List<SearchResultDto>>() {
                @Override
                public void onResponse(Call<List<SearchResultDto>> call, Response<List<SearchResultDto>> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<List<SearchResultDto>> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void searchShow(final String showName, final ApiResponseListener responseListener) {

        if(!Utils.isNullOrEmpty(showName)) {
            Call<ShowDto> call = TvSeriesApiClient.getApiInterface().getSingleSearchResult(showName);
            call.enqueue(new Callback<ShowDto>() {
                @Override
                public void onResponse(Call<ShowDto> call, Response<ShowDto> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<ShowDto> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void searchShowById(final long id, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<ShowDto> call = TvSeriesApiClient.getApiInterface().getShowInfoById(id);
            call.enqueue(new Callback<ShowDto>() {
                @Override
                public void onResponse(Call<ShowDto> call, Response<ShowDto> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<ShowDto> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void searchShowByIdWithCast(final long id, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<ShowDto> call = TvSeriesApiClient.getApiInterface().getShowInfoByIdWithCast(id, "cast");
            call.enqueue(new Callback<ShowDto>() {
                @Override
                public void onResponse(Call<ShowDto> call, Response<ShowDto> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<ShowDto> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void getAllEpisodes(final long id, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<List<EpisodeDto>> call = TvSeriesApiClient.getApiInterface().getAllEpisodes(id);
            call.enqueue(new Callback<List<EpisodeDto>>() {
                @Override
                public void onResponse(Call<List<EpisodeDto>> call, Response<List<EpisodeDto>> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<List<EpisodeDto>> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void getSeasonEpisodes(final long id, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<List<EpisodeDto>> call = TvSeriesApiClient.getApiInterface().getSeasonEpisodes(id);
            call.enqueue(new Callback<List<EpisodeDto>>() {
                @Override
                public void onResponse(Call<List<EpisodeDto>> call, Response<List<EpisodeDto>> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<List<EpisodeDto>> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void getEpisode(final long id, final long season, final long number, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<EpisodeDto> call = TvSeriesApiClient.getApiInterface().getEpisode(id, season, number);
            call.enqueue(new Callback<EpisodeDto>() {
                @Override
                public void onResponse(Call<EpisodeDto> call, Response<EpisodeDto> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<EpisodeDto> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }

    public void getSeasons(final long id, final ApiResponseListener responseListener) {

        if(id > 0) {
            Call<List<SeasonDto>> call = TvSeriesApiClient.getApiInterface().getSeasons(id);
            call.enqueue(new Callback<List<SeasonDto>>() {
                @Override
                public void onResponse(Call<List<SeasonDto>> call, Response<List<SeasonDto>> response) {
                    onSuccessResponse(new TvSeriesTrackerResponseHandler(responseListener), response);
                }

                @Override
                public void onFailure(Call<List<SeasonDto>> call, Throwable t) {
                    onFailResponse(new TvSeriesTrackerResponseHandler(responseListener), t);
                }
            });
        }
    }
}
