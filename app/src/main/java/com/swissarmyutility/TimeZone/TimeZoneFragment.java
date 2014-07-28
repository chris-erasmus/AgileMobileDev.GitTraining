package com.swissarmyutility.TimeZone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.swissarmyutility.AsyncTasks.GetIndiaTime;
import com.swissarmyutility.AsyncTasks.GetSATime;
import com.swissarmyutility.AsyncTasks.GetUSTime;
import com.swissarmyutility.Constant.Constants;
import com.swissarmyutility.Entity.Time;
import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Modified by hemant.gupta on 28-07-2014.
 */
public class TimeZoneFragment extends AppFragment {
    Button btn_us,btn_inda,btn_sa;

    TextView txt_time,txt_time_us,txt_time_sa;
    public int http_status = 0;
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
                http_status=Constants.HTTP_STATUS_US;
                GetUSTime gd = new GetUSTime(
                        getActivity());
                gd.setDataDownloadListener(new GetUSTime.DataDownloadListener() {
                    @Override
                    public void dataDownloadedSuccessfully(Time data) {
                        long longmillis = Long.parseLong(data.str_time_stamp);

                        final Calendar cal_us;
                        cal_us = Calendar.getInstance();
                        long longmillistimezone = Long.parseLong(data.str_gmt_offset);
                        long longmilis_india = 19800;
                        long gt_value_us = longmillistimezone - longmilis_india;
                        cal_us.setTimeInMillis((longmillis + gt_value_us) * 1000L);
                        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        txt_time_us.setText("US Time " + formatter.format(cal_us.getTime()));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                GetUSTime gd = new GetUSTime(
                                        getActivity());
                                gd.execute(Constants.url_us);
                                txt_time_us.setText("US Time " + formatter.format(cal_us.getTime()));

                                handler.postDelayed(this, 5000); //now is every 5 seconds
                            }
                        }, 5000); //Every  (5 seconds)
                    }
                    @Override
                    public void dataDownloadFailed() {

                    }
                });
                gd.execute(Constants.url_us);
            }
        });
        btn_inda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol_india_time=true;
                http_status=Constants.HTTP_STATUS_INDIA;
                GetIndiaTime gd = new GetIndiaTime(
                        getActivity());

                  gd.setDataDownloadListener(new GetIndiaTime.DataDownloadListener() {
                    @Override
                    public void dataDownloadedSuccessfully(Time data) {
                        long longmillis=Long.parseLong(data.str_time_stamp);
                        cal = Calendar.getInstance();
                        long longmillistimezone=Long.parseLong(data.str_gmt_offset);
                        cal.setTimeInMillis((longmillis*1000L-longmillistimezone*1000L));
                        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        txt_time.setText("India Time "+formatter.format(cal.getTime()));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                GetIndiaTime gd = new GetIndiaTime(
                                        getActivity());
                                gd.execute(Constants.url_india);
                                txt_time.setText("India Time "+formatter.format(cal.getTime()));
                                handler.postDelayed(this, 5000); //now is every 5 seconds
                            }
                        }, 5000); //Every  (5 seconds)
                    }
                    @Override
                    public void dataDownloadFailed() {

                    }
                });

                gd.execute(Constants.url_india);
            }
        });

        btn_sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bol_sa_time=true;
                http_status=Constants.HTTP_STATUS_SA;
                GetSATime gd = new GetSATime(
                        getActivity());

                gd.setDataDownloadListener(new GetSATime.DataDownloadListener() {
                    @Override
                    public void dataDownloadedSuccessfully(Time data) {
                        long longmillis = Long.parseLong(data.str_time_stamp);
                        final Calendar cal_sa;
                        cal_sa = Calendar.getInstance();
                        long longmillistimezone = Long.parseLong(data.str_gmt_offset);
                        long longmillisgmt_india = 19800;
                        long gtvalue = longmillistimezone - longmillisgmt_india;
                        cal_sa.setTimeInMillis((longmillis + gtvalue) * 1000L);
                        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        txt_time_sa.setText("SA Time " + formatter.format(cal_sa.getTime()));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                GetSATime gd = new GetSATime(
                                        getActivity());
                                gd.execute(Constants.url_sa);
                                txt_time_sa.setText("SA Time " + formatter.format(cal_sa.getTime()));
                                handler.postDelayed(this, 5000); //now is every 5 seconds
                            }
                        }, 5000); //Every  (5 seconds)

                    }
                    @Override
                    public void dataDownloadFailed() {

                    }
                });
                gd.execute(Constants.url_sa);
            }
        });
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("TimeZone");
        super.onActivityCreated(savedInstanceState);
    }
}