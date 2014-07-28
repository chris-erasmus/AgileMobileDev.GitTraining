package com.swissarmyutility.Parser;

import com.swissarmyutility.Constant.Constants;
import com.swissarmyutility.Entity.Time;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hemant.bareja on 28-07-2014.
 */
public class TimeDataParser {
   JSONObject signalr;
    Time time;
    public  Time parseTimeData(String result)
    {
        try {
            time =new Time();
            signalr= new JSONObject(result);
            time.str_zone_name=signalr.getString(Constants.TAG_ZONE_NAME);
            time.str_abbreviation=signalr.getString(Constants.TAG_ABBREVIATION);
            time.str_gmt_offset=signalr.getString(Constants.TAG_GMT_OFF_SET_TIME);
            time. str_time_stamp=signalr.getString(Constants.TAG_TIME_STAMP);
            time.str_dst=signalr.getString(Constants.TAG_DST);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  time;
    }

}
