package com.swissarmyutility.WeatherAPIcall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.swissarmyutility.R;
import com.swissarmyutility.weatherModel.Weather;

import java.util.ArrayList;

/**
 * Created by Vinod.sakala on 7/24/2014.
 */
public class ListAdapterEarlierDates extends BaseAdapter{

    ArrayList<Weather> weathersList;
    Context mContext;
    LayoutInflater layoutInflater;

    public ListAdapterEarlierDates(Context mContext ,ArrayList<Weather> weatherList)
    {

        this.mContext = mContext ;
        this.weathersList = weatherList;
        layoutInflater= (LayoutInflater)mContext
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

    private class ViewHolder
    {
        TextView tvCity;
        TextView tvTemp;
        TextView tvHumidity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vholder = null;
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.weatherlayout_list_item, null);
            vholder = new ViewHolder();
            vholder.tvCity = (TextView)convertView.findViewById(R.id.tvCity);
            vholder.tvHumidity= (TextView)convertView.findViewById(R.id.tvHumidity);
            vholder.tvTemp = (TextView)convertView.findViewById(R.id.tvTemp);
            convertView.setTag(vholder);
        }
        else
        {
            vholder = (ViewHolder)convertView.getTag();
        }


        Weather mWeather = weathersList.get(position);

        vholder.tvCity .setText(mWeather.location.getCity() + ", " + mWeather.location.getCountry());
        vholder.tvTemp . setText(Math.round((mWeather.temperature.getTemp() - 273.15)) + "\u2103");
        vholder.tvHumidity.setText(mWeather.currentCondition.getHumidity() + "%");

        return convertView;
    }
}

