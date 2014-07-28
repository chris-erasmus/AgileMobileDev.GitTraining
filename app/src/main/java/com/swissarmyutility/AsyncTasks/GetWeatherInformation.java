package com.swissarmyutility.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.swissarmyutility.Parser.JsonWeatherParser;
import com.swissarmyutility.WeatherAPIcall.WeatherHttpClient;
import com.swissarmyutility.dataModel.WeatherData;


import org.json.JSONException;

/**
 * Created by vinod.sakala on 7/28/2014.
 */

public class GetWeatherInformation extends AsyncTask<Integer, Void, WeatherData> {
    WeatherInformationListener weatherInformationListener;
    String response;
    Context mContext;
    ProgressDialog progressDialog;

    public GetWeatherInformation(Context mContext){
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);

    }

    @Override
    protected WeatherData doInBackground(Integer... params) {
        showProgressDialog();
        WeatherData weather = new WeatherData();

        response = ((new WeatherHttpClient()).getWeatherData(params[0], params[1]));
        try {
            weather = JsonWeatherParser.getWeather(response);
            // Let's retrieve the icon
            weather.iconData = ((new WeatherHttpClient())
                    .getImage(weather.currentCondition.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }

    @Override
    protected void onPostExecute(WeatherData weather) {
        super.onPostExecute(weather);
        hideProgressDialog();
        if (response != null && response.length() > 0) {
            weatherInformationListener.dataDownloadedSuccessfully(weather);
        } else {
            weatherInformationListener.dataDownloadFailed();
        }
    }

    public void showProgressDialog()
    {
         if(!progressDialog.isShowing()){
             progressDialog.setMessage("Loading...");
             progressDialog.show();
         }
    }

    public void hideProgressDialog()
    {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void setWeatherInformationListener(WeatherInformationListener weatherInformationListener) {
        this.weatherInformationListener = weatherInformationListener;
    }

    public static interface WeatherInformationListener {
        void dataDownloadedSuccessfully(WeatherData weather);
        void dataDownloadFailed();
    }
}
