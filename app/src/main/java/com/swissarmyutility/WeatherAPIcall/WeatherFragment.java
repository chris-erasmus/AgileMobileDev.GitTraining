package com.swissarmyutility.WeatherAPIcall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.swissarmyutility.R;
import com.swissarmyutility.globalnavigation.AppFragment;

import org.json.JSONException;

import com.swissarmyutility.weatherModel.Weather;

import java.util.ArrayList;


/**
 * Created by Vinod.sakala on 7/23/2014.
 */
public class WeatherFragment extends AppFragment {

    TextView cityText;
    TextView contentDescription;
    TextView temparature;
    TextView humidity;
    TextView pressure;
    TextView windSpeed;
    TextView windDegrees;
    ImageView imageWeather;
    ListView list_ListOfEarlierDates;
    LinearLayout listHeader;
    private static Criteria searchProviderCriteria = new Criteria();
    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    ListAdapterEarlierDates listAdapter;
    ArrayList<Weather> weatherList;
    Context mContext;

    static {
        searchProviderCriteria.setPowerRequirement(Criteria.POWER_LOW);
        searchProviderCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        searchProviderCriteria.setCostAllowed(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_weather, null);

        weatherList = new ArrayList<Weather>();
        mContext = getActivity();
        list_ListOfEarlierDates = (ListView) fragmentView.findViewById(R.id.list_ListOfEarlierDates);
        cityText = (TextView) fragmentView.findViewById(R.id.cityText);
        contentDescription = (TextView) fragmentView.findViewById(R.id.condDescr);
        temparature = (TextView) fragmentView.findViewById(R.id.temp);
        humidity = (TextView) fragmentView.findViewById(R.id.hum);
        pressure = (TextView) fragmentView.findViewById(R.id.press);
        windSpeed = (TextView) fragmentView.findViewById(R.id.windSpeed);
        windDegrees = (TextView) fragmentView.findViewById(R.id.windDeg);
        imageWeather = (ImageView) fragmentView.findViewById(R.id.condIcon);
        listHeader = (LinearLayout) fragmentView.findViewById(R.id.header_list);
        listHeader.setVisibility(View.GONE);

        String provider = locationManager.getBestProvider(searchProviderCriteria, true);
        locationManager.requestSingleUpdate(provider, locListener, null);

        return fragmentView;
    }

    private LocationListener locListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("WeatherActivity", "Location changed!");
            double sLat = location.getLatitude();
            double sLon = location.getLongitude();

            int iLat = (int) sLat;
            int iLon = (int) sLon;
            Log.d("New Changes", "Lat [" + sLat + "] - sLong [" + sLon + "]");

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new Integer[]{iLat, iLon});
        }
    };

    private class JSONWeatherTask extends AsyncTask<Integer, Void, Weather> {
        @Override
        protected Weather doInBackground(Integer... params) {
            Weather weather = new Weather();

            String data = ((new WeatherHttpClient()).getWeatherData(params[0], params[1]));
            try {
                weather = JsonWeatherParser.getWeather(data);
                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient())
                        .getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0,
                        weather.iconData.length);
                imageWeather.setImageBitmap(img);
            }

            weatherList.add(weather);
            listHeader.setVisibility(View.VISIBLE);
            cityText.setText(weather.location.getCity() + ", " + weather.location.getCountry());
            contentDescription.setText(" " + weather.currentCondition.getCondition()
                    + "(" + weather.currentCondition.getDescr() + ")");
            temparature.setText("Temp: \t " + Math.round((weather.temperature.getTemp() - 273.15))
                    + "\u2103");
            humidity.setText("Humidity: \t " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: \t" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("Wind: \t" + weather.wind.getSpeed() + " mps");
            windDegrees.setText("" + weather.wind.getDeg());

            listAdapter = new ListAdapterEarlierDates(getActivity(), weatherList);
            list_ListOfEarlierDates.setAdapter(listAdapter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Weather API");
        super.onActivityCreated(savedInstanceState);
    }
}
