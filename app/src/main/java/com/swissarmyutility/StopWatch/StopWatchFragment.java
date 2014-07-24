package com.swissarmyutility.StopWatch;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;

import java.util.ArrayList;


/**
 * Created by Naresh.Kaushik on 16-07-2014.
 *
 */
public class StopWatchFragment extends AppFragment {

    Chronometer mChronometer;
    private long timeWhenStopped = 0;
    private Button btnStart, btnStop, btnReset, btnLap;
    String time="";
    ListView timer_list;
    CustomAdapter arrayAdpter;
    protected ArrayList<String> timeList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return the view here
        View view =  inflater.inflate(R.layout.stop_watch,null);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"digitaL.ttf");

        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mChronometer.setTypeface(tf);
        // Watch for button clicks.
        btnStart = (Button) view.findViewById(R.id.btn_start);
        btnStop = (Button) view.findViewById(R.id.btn_stop);
        btnReset = (Button) view.findViewById(R.id.btn_reset);
        btnLap = (Button) view.findViewById(R.id.btn_lap);
        timer_list=(ListView) view.findViewById(R.id.list);
        timeList=new ArrayList<String>();
        arrayAdpter=new CustomAdapter(getActivity(),timeList);
        timer_list.setAdapter(arrayAdpter);

        btnStart.setOnClickListener(mStartListener);
        btnStop.setOnClickListener(mStopListener);
        btnReset.setOnClickListener(mResetListener);
        btnLap.setOnClickListener(mLapListener);

        return view;

    }

    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(btnStart.getText().toString()=="START")
            {
                mChronometer.start();
            }
            else {
                mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                mChronometer.start();
            }
            btnStart.setText("RESTART");
        }
    };

    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {

            timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
        }
    };

    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeList.clear();
            arrayAdpter.notifyDataSetChanged();
            btnStart.setText("START");
        }
    };

    View.OnClickListener mLapListener = new View.OnClickListener() {
        public void onClick(View v) {
            timeList.add(showElapsedTime());
            arrayAdpter.notifyDataSetChanged();
        }
    };

    public String showElapsedTime(){
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        long totalSecs = elapsedMillis/1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        return mins+":"+secs;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Stop Watch");
        super.onActivityCreated(savedInstanceState);
    }
}