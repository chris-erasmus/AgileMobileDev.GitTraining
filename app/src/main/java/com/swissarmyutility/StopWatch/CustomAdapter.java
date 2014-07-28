    package com.swissarmyutility.StopWatch;

    import android.app.Activity;
    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.TextView;

    import com.app.swissarmyutility.R;

    import java.util.ArrayList;

    public class CustomAdapter extends BaseAdapter {

     Context context;
     ArrayList<String> rowItems;

     CustomAdapter(Context context, ArrayList<String> rowItems) {
      this.context = context;
      this.rowItems = rowItems;
     }
     @Override
     public int getCount() {
      return rowItems.size();
     }

     @Override
     public Object getItem(int position) {
      return rowItems.get(position);
     }

     @Override
     public long getItemId(int position) {
      return rowItems.indexOf(getItem(position));
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

      LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.stop_watch_row_layout, null);
        TextView timer_textview = (TextView)convertView.findViewById(R.id.timer_text);
        timer_textview.setText(rowItems.get(position));

      return convertView;
     }

    }
