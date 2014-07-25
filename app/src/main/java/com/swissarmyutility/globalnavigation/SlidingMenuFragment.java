package com.swissarmyutility.globalnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.swissarmyutility.Calculator.CalculatorFragment;
import com.swissarmyutility.Dictionary.WordsmithDictionaryFragment;
import com.swissarmyutility.Expenditure.ExpenditureFragment;
import com.swissarmyutility.HeadTails.HeadTailsFragment;
import com.swissarmyutility.MetricConvertor.MetricConvertorFragment;
import com.swissarmyutility.ReminderSetter.RemiderSetterFragment;
import com.swissarmyutility.ShoppingList.ShoppingListFragment;
import com.swissarmyutility.StopWatch.StopWatchFragment;
import com.swissarmyutility.TimeZone.TimeZoneFragment;
import com.swissarmyutility.WeatherAPIcall.WeatherFragment;
import com.swissarmyutility.WeightTracker.WeightTrackerFragment;
import com.app.swissarmyutility.R;

import java.util.ArrayList;

public class SlidingMenuFragment extends Fragment {
    //view of sliding menu
    private View view = null;
    //array list of all fragments of app
    private ArrayList<AppFragment> appFragments = null;
    //listview of menu items
    private ListView menuList = null;
    //flag indicating whether the comment dialog is open or not
    private boolean isCommentDialogOpened;
    //sliding down animation implemented on comment dialog
    private Animation slideDownAnim;
    //layout of comment dialog
    private LinearLayout commentDialogLinearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sliding_menu, null);
        // set the functionality of comment button in downside
        ((Button)view.findViewById(R.id.comment_button)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isCommentDialogOpened)
                    commentDialogLinearLayout.startAnimation(slideDownAnim);
                else
                    showCommentDialog();
            }
        });

        //set functionality of menu list
        menuList = (ListView)view.findViewById(R.id.menu_list);
        menuList.setAdapter(new MenuListAdapter());
        menuList.setOnItemClickListener(onItemClickListener);

        //init the list of app fragments
        appFragments = new ArrayList<AppFragment>();
        appFragments.add(new CalculatorFragment());
        appFragments.add(new StopWatchFragment());

        appFragments.add(new TimeZoneFragment());
        appFragments.add(new HeadTailsFragment());
        appFragments.add(new RemiderSetterFragment());
        appFragments.add(new WeatherFragment());
        appFragments.add(new WeightTrackerFragment());
        appFragments.add(new WordsmithDictionaryFragment());
        return view;
	}

    /**
     * show comment dialog with animation
     */
    private void showCommentDialog(){
        commentDialogLinearLayout = (LinearLayout)view.findViewById(R.id.comment_dialog_ll);
        commentDialogLinearLayout.setVisibility(View.VISIBLE);
        menuList.setVisibility(View.GONE);
        Animation slideUp = AnimationUtils.loadAnimation(getActivity(),R.anim.up_translate);
        slideDownAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.down_translate);
        slideDownAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                commentDialogLinearLayout.setVisibility(View.GONE);
                menuList.setVisibility(View.VISIBLE);
                isCommentDialogOpened = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        ((Button)view.findViewById(R.id.button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialogLinearLayout.startAnimation(slideDownAnim);
            }
        });
        commentDialogLinearLayout.startAnimation(slideUp);
        isCommentDialogOpened = true;
    }

    /**
     * open the particular fragment for list item
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment newContent = appFragments.get(position);

		if (newContent != null)
			switchFragment(newContent);
        }
    };

	// switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof SlidingMenuFragmentActivity) {
            SlidingMenuFragmentActivity activity = (SlidingMenuFragmentActivity) getActivity();
            activity.switchContent(fragment);
		}
	}


    class MenuListAdapter extends BaseAdapter{
        String listItemNames[] = SlidingMenuFragment.this.getActivity().getResources().getStringArray(R.array.sliding_menu_list_items);
        int iconImages[] = {R.drawable.calculator_ic,R.drawable.stopwatch_ic, R.drawable.timezone_ic, R.drawable.coin, R.drawable.calendar_ic,R.drawable.icon_slider_weather, R.drawable.scale_ic, R.drawable.book_ic};
        LayoutInflater layoutInflater = null;

        public MenuListAdapter(){
            layoutInflater = SlidingMenuFragment.this.getActivity().getLayoutInflater();
        }
        @Override
        public int getCount() {
            return listItemNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.menu_list_item, null);
            ((ImageView)convertView.findViewById(R.id.menu_list_item_icon)).setImageResource(iconImages[position]);
            ((TextView)convertView.findViewById(R.id.menu_list_item_text)).setText(listItemNames[position]);
            return convertView;
        }

    }
}
