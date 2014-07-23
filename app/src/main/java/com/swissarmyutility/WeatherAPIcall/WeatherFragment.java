package com.swissarmyutility.WeatherAPIcall;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.swissarmyutility.R;
import com.swissarmyutility.globalnavigation.AppFragment;


/**
 * Created by Vinod.sakala on 7/23/2014.
 */

public class WeatherFragment extends AppFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return the view here
        return inflater.inflate(R.layout.fragment_weather_api,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Weather API");
        super.onActivityCreated(savedInstanceState);
    }
}
