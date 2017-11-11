package com.yrazlik.tvseriestracker.util;

import com.yrazlik.tvseriestracker.data.ShowDto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class Utils {

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
        return "S" + makeTwoDigits(season) + " E" + makeTwoDigits(number);
    }
}
