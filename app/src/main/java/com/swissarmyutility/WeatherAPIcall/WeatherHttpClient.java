package com.swissarmyutility.WeatherAPIcall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.swissarmyutility.Constant.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vinod.sakala on 7/23/2014.
 */
public class WeatherHttpClient {

    public String getWeatherData(int lat, int lon) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            String resultURL = Constants.WEATHER_BASE_URL + "lat=" + lat + "&lon=" + lon + "&APPID=" + Constants.WEATHER_API_KEY;
            con = (HttpURLConnection) (new URL(resultURL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {

            String url = Constants.WEATHER_IMG_URL + code + ".png";
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[40000];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }
}
