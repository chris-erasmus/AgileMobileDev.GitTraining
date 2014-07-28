package com.swissarmyutility.Constant;

public class Constants {
	public static final String keyHeadTailListId = "watchListId";

    //constants for TimeZoneFragment
    public static final String url_us="http://api.timezonedb.com/?zone=America/Toronto&format=json&key=PKHNUPT9GY3L";
    public static final String url_india="http://api.timezonedb.com/?zone=Asia/Kolkata&format=json&key=PKHNUPT9GY3L";
    public static  String url_sa="http://api.timezonedb.com/?zone=Africa/Johannesburg&format=json&key=PKHNUPT9GY3L";
    public static final String TAG_ZONE_NAME = "zoneName";
    public static final String TAG_ABBREVIATION = "abbreviation";
    public static final String TAG_GMT_OFF_SET_TIME = "gmtOffset";
    public static final String TAG_TIME_STAMP = "timestamp";
    public static final String TAG_DST = "dst";
    public static final int HTTP_STATUS_INDIA = 1;
    public static final int HTTP_STATUS_US = 2;
    public static final int HTTP_STATUS_SA = 3;

    // Url's for weather api calls
    public static String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static String WEATHER_IMG_URL = "http://openweathermap.org/img/w/";
    public static String WEATHER_API_KEY = "a67ef4410f337698c7d4feeaa5fcbf01";

}
