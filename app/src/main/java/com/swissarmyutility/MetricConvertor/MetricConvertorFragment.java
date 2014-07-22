package com.swissarmyutility.MetricConvertor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
public class MetricConvertorFragment extends AppFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return the view here
        return inflater.inflate(R.layout.library_fragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Metric Convertor");
        super.onActivityCreated(savedInstanceState);
    }
}