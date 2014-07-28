package com.swissarmyutility.StopWatch;

/**
 * Created by navneet.srivastava on 7/28/2014.
 */
public class SetElapsedTimeForLapping {

    public static String showElapsedTime(long elapsedMillis) {
        long totalSecs = elapsedMillis/1000;
        long hours = (totalSecs / 3600);
        long minutes = (totalSecs / 60) % 60;
        long seconds = totalSecs % 60;
        return minutes + ":" + seconds;
    }
}
