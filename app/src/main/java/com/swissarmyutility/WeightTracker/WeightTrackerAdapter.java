package com.swissarmyutility.WeightTracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.swissarmyutility.R;
import com.swissarmyutility.dataModel.WeightTrackModel;

import java.util.List;


public class WeightTrackerAdapter extends BaseAdapter {

    private Context mContext;
    List<WeightTrackModel> allWishLists;

    public WeightTrackerAdapter(Context c,  List<WeightTrackModel> allWishLists) {
        mContext = c;
        this.allWishLists = allWishLists;
    }
    @Override
    public int getCount() {
        return allWishLists.size();
    }
    @Override
    public Object getItem(int position) {
        return allWishLists.get(position);
    }
    @Override
    public long getItemId(int position) {
       return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view= new View(mContext);
            view = inflater.inflate(R.layout.weight_tracker_adapter, null);
            holder= new ViewHolder();
            holder.btnSerNo = (TextView)view.findViewById(R.id.weight_tracker_serial_no);
            holder.btnWeight = (TextView)view.findViewById(R.id.weight_tracker_weidht);
            holder.btnWeightReduce = (TextView)view.findViewById(R.id.weight_tracker_weight_reduce);
            holder.btnWeightTime = (TextView)view.findViewById(R.id.weight_tracker_weight_time);

            holder.btnSerNo.setText(""+(position+1));
            holder.btnWeight.setText(allWishLists.get(position).getWeight());

            String weig = "0";
            try {
                if((position != 0)){
                    if(!allWishLists.get(position-1).getWeight().toString().equals(allWishLists.get(position).getWeight().toString())) {

                        int WeightInteger = 0;
                        WeightInteger = Integer.parseInt(allWishLists.get(position).getWeight()) - Integer.parseInt(allWishLists.get(position-1).getWeight());
                        if (WeightInteger > 0) {
                            weig = "+" + WeightInteger;
                        } else {
                            weig = "" + WeightInteger;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            holder.btnWeightReduce.setText(weig);

            holder.btnWeightTime.setText(allWishLists.get(position).getTime());

        } else {
            view = (View) convertView;
        }
        return view;
    }

    private class ViewHolder {
       private  TextView btnSerNo,btnWeight,btnWeightReduce,btnWeightTime;

    }
}
