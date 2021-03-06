package com.swissarmyutility.WeatherAPIcall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.swissarmyutility.R;
import com.swissarmyutility.dataModel.WeatherData;
import java.util.ArrayList;

/**
 * Created by Vinod.sakala on 7/24/2014.
 */
public class ListAdapterEarlierDates extends BaseAdapter {

    ArrayList<WeatherData> weathersList;
    Context mContext;
    LayoutInflater layoutInflater;

    public ListAdapterEarlierDates(Context mContext, ArrayList<WeatherData> weatherList) {

        this.mContext = mContext;
        this.weathersList = weatherList;
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return weathersList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView textViewCity;
        TextView textViewTemparature;
        TextView textViewHumidity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.weatherlayout_list_item, null);
            holder = new ViewHolder();
            holder.textViewCity = (TextView) convertView.findViewById(R.id.tvCity);
            holder.textViewHumidity = (TextView) convertView.findViewById(R.id.tvHumidity);
            holder.textViewTemparature = (TextView) convertView.findViewById(R.id.tvTemp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WeatherData mWeather = weathersList.get(position);

        holder.textViewCity.setText(mWeather.location.getCity() + ", " + mWeather.location.getCountry());
        holder.textViewTemparature.setText(Math.round((mWeather.temperature.getTemp()
                - 273.15)) + "\u2103");  // 'u2103' to display the centigrade symbol

        holder.textViewHumidity.setText(mWeather.currentCondition.getHumidity() + "%");

        return convertView;
    }
}

