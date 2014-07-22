package com.swissarmyutility.globalnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.swissarmyutility.R;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 *  this is parent fragment of our application fragments and responsible to set title and menu button functionality
 */
public class AppFragment extends Fragment{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button)getView().findViewById(R.id.button_menu_header)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() == null)
                    return;

                if(getActivity() instanceof SlidingMenuFragmentActivity)
                    ((SlidingMenuFragmentActivity )getActivity()).toggleMenu();
            }
        });
    }

    public void setTitle(String title){
        ((TextView)getView().findViewById(R.id.title)).setText(title);
    }
}
