package com.swissarmyutility.TimeZone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kapil.gupta on 23-07-2014.
 */
public class TimeZoneFragment extends AppFragment {
    Button btn_us,btn_inda,btn_sa;
    JSONObject signalr;
    TextView txt_time,txt_time_us,txt_time_sa;
    private static String url_us="http://api.timezonedb.com/?zone=America/Toronto&format=json&key=PKHNUPT9GY3L";
    private static String url_india="http://api.timezonedb.com/?zone=Asia/Kolkata&format=json&key=PKHNUPT9GY3L";
    private static String url_sa="http://api.timezonedb.com/?zone=Africa/Johannesburg&format=json&key=PKHNUPT9GY3L";
    String str_zone_name,str_abbreviation,str_gmt_offset,str_time_stamp,str_dst;
    private static final String TAG_ZONE_NAME = "zoneName";
    private static final String TAG_ABBREVIATION = "abbreviation";
    private static final String TAG_GMT_OFF_SET_TIME = "gmtOffset";
    private static final String TAG_TIME_STAMP = "timestamp";
    private static final String TAG_DST = "dst";
    public int getHttpStatus = 0;
    public int getHttpStatus_india = 1;
    public int getHttpStatus_us = 2;
    public int getHttpStatus_sa = 3;
    SimpleDateFormat formatter;
    Calendar cal;
    boolean bol_india_time=false;
    boolean bol_us_time=false;
    boolean bol_sa_time=false;

    @SuppressLint("Override")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.time_conversion,null);
        btn_us=(Button)v.findViewById(R.id.btn_us_time);
        btn_inda=(Button)v.findViewById(R.id.btn_india_time);
        btn_sa=(Button)v.findViewById(R.id.btn_sa_time);
        txt_time=(TextView)v.findViewById(R.id.txt_time);
        txt_time_sa=(TextView)v.findViewById(R.id.txt_time_sa);
        txt_time_us=(TextView)v.findViewById(R.id.txt_time_us);

        btn_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol_us_time=true;
                getHttpStatus=getHttpStatus_us;
                Get_Data_for_us_time gd = new Get_Data_for_us_time(
                        getActivity());

                gd.execute(url_us);
            }
        });
        btn_inda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol_india_time=true;
                getHttpStatus=getHttpStatus_india;
                Get_Data gd = new Get_Data(
                        getActivity());

                gd.execute(url_india);
            }
        });

        btn_sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol_sa_time=true;
                getHttpStatus=getHttpStatus_sa;
                Get_Data_for_sa_time gd = new Get_Data_for_sa_time(
                        getActivity());

                gd.execute(url_sa);
            }
        });
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("TimeZone");
        super.onActivityCreated(savedInstanceState);
    }
    public class Get_Data extends AsyncTask<String, Void, String> {

        private Context context;
        String responseStr = null;
        InputStream is = null;
        HttpEntity responseEntity;

        public Get_Data(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Instantiate the custom HttpClient
                System.out.println("URL=" + params[0]);
                DefaultHttpClient client = new Client(
                        getActivity());
                HttpGet get = new HttpGet(params[0]);
                HttpResponse getResponse = client.execute(get);
                HttpEntity responseEntity = getResponse.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(responseEntity.getContent(),
                                "UTF-8")
                );
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                int cp;
                while ((cp = reader.read()) != -1) {
                    sb.append((char) cp);
                }
                reader.close();
                String result = sb.toString();
                responseStr = result;
                if (responseStr != null) {
                    responseStr = result;
                    System.out.println("response in if" + responseStr);
                } else {
                    System.out.println("happy coding");
                }
                try {
                    signalr= new JSONObject(responseStr);
                    str_zone_name=signalr.getString(TAG_ZONE_NAME);
                    str_abbreviation=signalr.getString(TAG_ABBREVIATION);
                    str_gmt_offset=signalr.getString(TAG_GMT_OFF_SET_TIME);
                    str_time_stamp=signalr.getString(TAG_TIME_STAMP);
                    str_dst=signalr.getString(TAG_DST);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Thread", "" + e.getMessage());
            }

            return responseStr;
        }

        @Override
        protected void onPostExecute(String result) {

                super.onPostExecute(result);
                long longmillis=Long.parseLong(str_time_stamp);
                cal = Calendar.getInstance();
                long longmillistimezone=Long.parseLong(str_gmt_offset);
                cal.setTimeInMillis((longmillis*1000L-longmillistimezone*1000L));
                formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                txt_time.setText("India Time "+formatter.format(cal.getTime()));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Get_Data gd = new Get_Data(
                                getActivity());
                        gd.execute(url_india);
                        txt_time.setText("India Time "+formatter.format(cal.getTime()));
                        handler.postDelayed(this, 5000); //now is every 5 seconds
                    }
                }, 5000); //Every  (5 seconds)


        }
    }


    public class Get_Data_for_us_time extends AsyncTask<String, Void, String> {

        private Context context;
        String responseStr = null;
        InputStream is = null;
        HttpEntity responseEntity;

        public Get_Data_for_us_time(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("URL=" + params[0]);
                DefaultHttpClient client = new Client(
                        getActivity());
                HttpGet get = new HttpGet(params[0]);
                HttpResponse getResponse = client.execute(get);
                HttpEntity responseEntity = getResponse.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(responseEntity.getContent(),
                                "UTF-8")
                );
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                int cp;
                while ((cp = reader.read()) != -1) {
                    sb.append((char) cp);
                }
                reader.close();
                String result = sb.toString();
                Log.v("RESULT = ", result);
                responseStr = result;
                if (responseStr != null) {
                    responseStr = result;
                    System.out.println("response in if" + responseStr);
                } else {
                    System.out.println("happy coding");
                }
                try {
                    signalr= new JSONObject(responseStr);
                    str_zone_name=signalr.getString(TAG_ZONE_NAME);
                    str_abbreviation=signalr.getString(TAG_ABBREVIATION);
                    str_gmt_offset=signalr.getString(TAG_GMT_OFF_SET_TIME);
                    str_time_stamp=signalr.getString(TAG_TIME_STAMP);
                    str_dst=signalr.getString(TAG_DST);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Thread", "" + e.getMessage());
            }

            return responseStr;
        }

        @Override
        protected void onPostExecute(String result) {


                super.onPostExecute(result);
                long longmillis = Long.parseLong(str_time_stamp);
                System.out.println("helooo" + longmillis);
                final Calendar cal_us;
                cal_us = Calendar.getInstance();
                long longmillistimezone = Long.parseLong(str_gmt_offset);
                long longmilis_india = 19800;
                long gt_value_us = longmillistimezone - longmilis_india;
                cal_us.setTimeInMillis((longmillis + gt_value_us) * 1000L);
                formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                txt_time_us.setText("US Time " + formatter.format(cal_us.getTime()));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Get_Data_for_us_time gd = new Get_Data_for_us_time(
                                getActivity());
                        gd.execute(url_us);
                        txt_time_us.setText("US Time " + formatter.format(cal_us.getTime()));
                        System.out.println("thread working");
                        handler.postDelayed(this, 5000); //now is every 5 seconds
                    }
                }, 5000); //Every  (5 seconds)

        }
    }


    public class Get_Data_for_sa_time extends AsyncTask<String, Void, String> {

        private Context context;
        String responseStr = null;
        InputStream is = null;
        HttpEntity responseEntity;

        public Get_Data_for_sa_time(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                // Instantiate the custom HttpClient
                System.out.println("URL=" + params[0]);
                DefaultHttpClient client = new Client(
                        getActivity());
                HttpGet get = new HttpGet(params[0]);
                HttpResponse getResponse = client.execute(get);
                HttpEntity responseEntity = getResponse.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(responseEntity.getContent(),
                                "UTF-8")
                );
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                int cp;
                while ((cp = reader.read()) != -1) {
                    sb.append((char) cp);
                }
                reader.close();
                String result = sb.toString();
                Log.v("RESULT = ", result);
                responseStr = result;
                if (responseStr != null) {
                    responseStr = result;
                    System.out.println("response in if" + responseStr);
                } else {
                    System.out.println("happy coding");
                }
                try {
                    signalr= new JSONObject(responseStr);
                    str_zone_name=signalr.getString(TAG_ZONE_NAME);
                    str_abbreviation=signalr.getString(TAG_ABBREVIATION);
                    str_gmt_offset=signalr.getString(TAG_GMT_OFF_SET_TIME);
                    str_time_stamp=signalr.getString(TAG_TIME_STAMP);
                    str_dst=signalr.getString(TAG_DST);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Thread", "" + e.getMessage());
            }

            return responseStr;
        }

        @Override
        protected void onPostExecute(String result) {

                super.onPostExecute(result);
                long longmillis = Long.parseLong(str_time_stamp);
                final Calendar cal_sa;
                cal_sa = Calendar.getInstance();
                long longmillistimezone = Long.parseLong(str_gmt_offset);
                long longmillisgmt_india = 19800;
                long gtvalue = longmillistimezone - longmillisgmt_india;
                cal_sa.setTimeInMillis((longmillis + gtvalue) * 1000L);
                formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                txt_time_sa.setText("SA Time " + formatter.format(cal_sa.getTime()));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Get_Data_for_sa_time gd = new Get_Data_for_sa_time(
                                getActivity());
                        gd.execute(url_sa);
                        txt_time_sa.setText("SA Time " + formatter.format(cal_sa.getTime()));
                        handler.postDelayed(this, 5000); //now is every 5 seconds
                    }
                }, 5000); //Every  (5 seconds)

        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else{
            showMessage("Alert","Connection Error");
        }
        return false;


    }
    @SuppressWarnings("deprecation")
    private void showMessage(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        final AlertDialog ad = alertDialog.create();
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                ad.dismiss();

            }
        });
        ad.show();
    }
}