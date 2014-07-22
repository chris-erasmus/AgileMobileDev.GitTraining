package com.swissarmyutility.globalnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.swissarmyutility.Calculator.CalculatorFragment;
import com.app.swissarmyutility.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Naresh.Kaushik on 15-07-2014.
 * this is the only activity in application and need to change fragments for view
 */
public class SlidingMenuFragmentActivity extends SlidingFragmentActivity{
    //current view exclude menu
    Fragment mContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the main view as content view
        mContent = new CalculatorFragment();
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();

        // and set sliding menu as the Behind View
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SlidingMenuFragment()).commit();

        //customize sliding menu as per our requirements
        getSlidingMenu().setFadeEnabled(false);
        getSlidingMenu().setBehindScrollScale(0.0f);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);


    }

    /**
     *  replace the current fragment by the fragment we get
     */

    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    /**
     * toggle the sliding menu
     */
    public void toggleMenu(){
        getSlidingMenu().toggle();
    }
}
