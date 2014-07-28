package com.swissarmyutility.WeightTracker;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.app.swissarmyutility.R;
import com.swissarmyutility.data.DatabaseManager;
import com.swissarmyutility.dataModel.WeightTrackModel;
import com.swissarmyutility.globalnavigation.AppFragment;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
public class WeightTrackerFragment extends AppFragment {
    private EditText etWeight;
    private Button btnTrade;
    private ListView list;
    private WeightTrackerAdapter weightTrackerAdapter;
    private SimpleDateFormat sdfTime;
    List<WeightTrackModel> allWishLists;
    private LinearLayout user_graph;
    private WeightTrackModel weightTrackModel;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return the view here
        View view = inflater.inflate(R.layout.weight_tracker, null);
        etWeight = (EditText) view.findViewById(R.id.weight_tracker_et_weight);
        btnTrade = (Button) view.findViewById(R.id.weight_tracker_btn_trade);
        list = (ListView) view.findViewById(R.id.weight_tracker_list_view);
        sdfTime = new SimpleDateFormat("dd-MM-yyyy HH-mm");
        user_graph = (LinearLayout) view.findViewById(R.id.chart_container1);
        DatabaseManager.init(getActivity());
        AddingAdapter();
    btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etWeight.getText().toString().equals("")){
                    String time = sdfTime.format(Calendar.getInstance().getTime());
                    weightTrackModel = new WeightTrackModel();
                    weightTrackModel.setTime(time);
                    weightTrackModel.setWeight(etWeight.getText().toString());
                    DatabaseManager.getInstance().addWeightTrackList(weightTrackModel);
                    allWishLists.clear();
                    for(WeightTrackModel wishListItem: allWishLists)
                    {
                        Toast.makeText(getActivity(),""+wishListItem.getTime(),Toast.LENGTH_SHORT).show();
                    }
                    weightTrackerAdapter.notifyDataSetChanged();
                    AddingAdapter();

                }else{
                    Toast.makeText(getActivity(),"Please Enter Your Weight",Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    /**
     *  Entering data into listview
     */
    private void AddingAdapter(){
        user_graph.removeAllViews();

        allWishLists = DatabaseManager.getInstance().getAllWeightTrackerLists();
        weightTrackerAdapter = new WeightTrackerAdapter(getActivity(),allWishLists);
        list.setAdapter(weightTrackerAdapter);

        etWeight.setText("");

        Opengraph_activity(allWishLists,user_graph);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Weight Tracker");
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Contaning Chart functionality
     * @param valuesList
     * @param user_graph
     */
    public void Opengraph_activity( List<WeightTrackModel> valuesList,LinearLayout user_graph) {

        double[] valueArray1 = new double[valuesList.size()];
        double[] valueArray_time = new double[valuesList.size()];
        for(int i=0;i<valuesList.size();i++)
        {
            String weightVal = valuesList.get(i).getWeight();
            valueArray1[i] = Double.parseDouble(weightVal);
            String timeArray = valuesList.get(i).getTime();
            valueArray_time[i] = Double.parseDouble(""+i);
        }
        String[] titles = new String[] {"Weight Graph"};
        List<double[]> x = new ArrayList<double[]>();
        List<double[]> values = new ArrayList<double[]>();
        x.add( valueArray_time );
        values.add(valueArray1);
        int[] colors = new int[] { Color.GREEN };
        PointStyle[] styles = new PointStyle[] { PointStyle.X };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        renderer.setPointSize(0f);  // to  increase the size of the Circle
        renderer.setPanLimits(valueArray1);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = (XYSeriesRenderer) renderer
                    .getSeriesRendererAt(i);
            r.setLineWidth(7);
            r.setFillPoints(true);
        }
        setChartSettings(renderer, "Weight Tracking", "Number", "Weight", 0.5,
                valuesList.size(),0,valuesList.size(), Color.LTGRAY, Color.LTGRAY);

        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Paint.Align.RIGHT);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanEnabled(true, false);
        renderer.setZoomEnabled(true, false);
        renderer.setClickEnabled(false);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setMarginsColor(Color.argb(0, 250, 250, 250));
        renderer.setBarSpacing(0.5);
        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        String[] types = new String[] {  CubicLineChart.TYPE };
        final GraphicalView grfv = ChartFactory.getCombinedXYChartView(
                getActivity(), dataset, renderer, types);
        user_graph.addView(grfv);
    }
    // Set only the names of the lines
    protected XYMultipleSeriesDataset buildDataset(String[] titles,
                                                   List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }
    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
                            List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }
    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
                                                     PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
                               PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }
    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
                                    String title, String xTitle, String yTitle, double xMin,
                                    double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        for (int i=0;i<xMax;i++){
            renderer.addYTextLabel(i, "10%");
        }
        for (int i=0;i<yMax;i++){
            renderer.addYTextLabel(i, "20%");
        }
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }
}