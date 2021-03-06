package com.yrazlik.tvseriestracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.data.EpisodeDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.view.RobotoTextView;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class Utils {

    public static final String TV_SERIES_TRACKER_SHARED_PREFS = "TV_SERIES_TRACKER_SHARED_PREFS";
    public static final String WATCHED_LIST = "com.yrazlik.tvseriestracker.watchedlist";
    public static final String FAVORITES_LIST = "com.yrazlik.tvseriestracker.favoriteslist";

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    public static List<ShowDto> sortShowsByWeightedRating(List<ShowDto> shows) {
        if(shows != null) {
            try {
                Collections.sort(shows, new Comparator<ShowDto>() {
                    public int compare(ShowDto s1, ShowDto s2) {
                        if (s1.getWeightedRating() == s2.getWeightedRating())
                            return 0;
                        return s1.getWeightedRating() < s2.getWeightedRating() ? 1 : -1;
                    }
                });
            } catch (Exception e) {
                return shows;
            }
        }

        return shows;
    }

    public static String makeTwoDigits(long number) {
        if(number < 10) {
            return "0" + number;
        }
        return number + "";
    }

    public static String getEpisodesText(long season, long number) {
        return "Season " + season + ", Episode " + number;
    }

    public static  Map<Long, Map<Long, EpisodeDto>> getWatchedList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TV_SERIES_TRACKER_SHARED_PREFS, 0);
        String watchedEpisodes = prefs.getString(WATCHED_LIST, null);
        Map<Long, Map<Long, EpisodeDto>> watchedList = null;
        try {
            Type type = new TypeToken<Map<Long, Map<Long, EpisodeDto>>>(){}.getType();
            watchedList = new Gson().fromJson(watchedEpisodes, type);
        } catch (Exception e) {
            watchedList = null;
        }

        if(watchedList == null) {
            watchedList = new HashMap<>();
        }

        return watchedList;
    }

    public static Map<Long, ShowDto> getFavoritesList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TV_SERIES_TRACKER_SHARED_PREFS, 0);
        String watchedEpisodes = prefs.getString(FAVORITES_LIST, null);
        Map<Long, ShowDto> favoritesList = null;
        try {
            Type type = new TypeToken<Map<Long, ShowDto>>(){}.getType();
            favoritesList = new Gson().fromJson(watchedEpisodes, type);
        } catch (Exception e) {
            favoritesList = null;
        }

        if(favoritesList == null) {
            favoritesList = new HashMap<>();
        }

        return favoritesList;
    }

    public static void saveToFavoritesList(Context context, ShowDto showDto) {
        long showId = showDto.getId();
        Map<Long, ShowDto> favoritesList = TvSeriesTrackerApp.favoritesList;

        if(favoritesList == null) {
            favoritesList = new HashMap<>();
        }

        ShowDto show = favoritesList.get(showId);

        if(show == null) {
            favoritesList.put(showId, showDto);
        }

        updateFavoritesList(context, favoritesList);
    }

    public static void updateFavoritesList(Context context, Map<Long, ShowDto> favoritesList) {
        SharedPreferences prefs = context.getSharedPreferences(TV_SERIES_TRACKER_SHARED_PREFS, 0);
        try {
            Type type = new TypeToken<Map<Long, ShowDto>>(){}.getType();
            prefs.edit().putString(FAVORITES_LIST, new Gson().toJson(favoritesList, type)).commit();
        } catch (Exception e) {

        }
    }

    public static void removeFromFavoritesList(Context context, ShowDto showDto) {
        long showId = showDto.getId();
        Map<Long, ShowDto> favoritesList = TvSeriesTrackerApp.favoritesList;
        ShowDto show = favoritesList.get(showId);

        if(show != null) {
            favoritesList.remove(showId);
            updateFavoritesList(context, favoritesList);
        }
    }

    public static boolean isFavoriteShow(long showId) {
        Map<Long, ShowDto> favoritesList = TvSeriesTrackerApp.favoritesList;

        if(favoritesList != null) {
            ShowDto show = favoritesList.get(showId);

            if(show != null) {
               return true;
            }
        }
        return false;
    }

    public static boolean isEpisodeWatched(long showId, EpisodeDto episodeDto) {
        Map<Long, Map<Long, EpisodeDto>> watchedList = TvSeriesTrackerApp.watchedList;
        if(episodeDto != null && watchedList != null) {
            long episodeId = episodeDto.getId();
            Map<Long, EpisodeDto> show = watchedList.get(showId);
            if(show != null) {
                EpisodeDto episode = show.get(episodeId);
                if(episode != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateWatchedList(Context context, Map<Long, Map<Long, EpisodeDto>> watchedList) {
        SharedPreferences prefs = context.getSharedPreferences(TV_SERIES_TRACKER_SHARED_PREFS, 0);
        try {
            Type type = new TypeToken<Map<Long, Map<Long, EpisodeDto>>>(){}.getType();
            prefs.edit().putString(WATCHED_LIST, new Gson().toJson(watchedList, type)).commit();
        } catch (Exception e) {

        }
    }

    public static void saveToWatchedList(Context context, long showId, EpisodeDto episodeDto) {
        long episodeId = episodeDto.getId();
        Map<Long, Map<Long, EpisodeDto>> watchedList = TvSeriesTrackerApp.watchedList;

        Map<Long, EpisodeDto> show = watchedList.get(showId);
        if(show == null) {
            Map<Long, EpisodeDto> watchedEpisodesList = new HashMap<>();
            watchedEpisodesList.put(episodeDto.getId(), episodeDto);
            watchedList.put(showId, watchedEpisodesList);
        } else {
            EpisodeDto episode = show.get(episodeId);
            if(episode == null) {
                show.put(episodeDto.getId(), episodeDto);
            }
        }
        updateWatchedList(context, watchedList);
    }

    public static void removeFromWatchedList(Context context, long showId, EpisodeDto episodeDto) {
        long episodeId = episodeDto.getId();
        Map<Long, Map<Long, EpisodeDto>> watchedList = TvSeriesTrackerApp.watchedList;
        Map<Long, EpisodeDto> show = watchedList.get(showId);
        if(show != null) {
            EpisodeDto episode = show.get(episodeId);
            if(episode != null) {
                show.remove(episodeId);
                if(show.size() == 0) {
                    watchedList.remove(showId);
                }
            }
            updateWatchedList(context, watchedList);
        }
    }

    public static boolean episodeAirsInUpcomingWeek(EpisodeDto episode){
        String airDate = episode.getAirDate();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar nextWeekToday = Calendar.getInstance();
        nextWeekToday.add(Calendar.DATE, 7);
        try {
            if(airDate != null) {
                String splitted[] = airDate.split("-");
                if (splitted != null && splitted.length >= 2) {
                    int year = Integer.parseInt(splitted[0]);
                    int month = Integer.parseInt(splitted[1]);
                    int day = Integer.parseInt(splitted[2]);

                    Calendar episodeDay = Calendar.getInstance();
                    episodeDay.set(year, month - 1, day);

                    if (episodeDay.after(yesterday) && episodeDay.before(nextWeekToday)) {
                        return true;
                    }
                }
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    public static boolean episodePassed(EpisodeDto episode){
        String airDate = episode.getAirDate();
        Calendar today = Calendar.getInstance();
        try {
            if(airDate != null) {
                String splitted[] = airDate.split("-");
                if (splitted != null && splitted.length >= 2) {
                    int year = Integer.parseInt(splitted[0]);
                    int month = Integer.parseInt(splitted[1]);
                    int day = Integer.parseInt(splitted[2]);

                    Calendar episodeDay = Calendar.getInstance();
                    episodeDay.set(year, month - 1, day);

                    if (episodeDay.before(today)) {
                        return true;
                    }
                }
            }
        }catch (Exception e){
            return true;
        }

        return false;
    }

    public static void underline(RobotoTextView tv) {
        if(tv != null && tv.getText() != null) {
            String text = tv.getText().toString();
            SpannableString spanString = new SpannableString(text);
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            tv.setText(spanString);
        }
    }

    public static String getEpisodeDateText(String airDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(airDate);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            String monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            int year = c.get(Calendar.YEAR);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return monthName + " " + day + ", " + year;
        } catch (Exception e) {
            return "-";
        }
    }
}
