package com.swissarmyutility.HeadTails;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.swissarmyutility.R;
import com.swissarmyutility.HeadTails.AnimationLib.Animation.ResultState;
import com.swissarmyutility.HeadTails.AnimationLib.Coin;
import com.swissarmyutility.HeadTails.AnimationLib.CustomAnimationDrawable;
import com.swissarmyutility.data.DatabaseManager;
import com.swissarmyutility.dataModel.HeadTailModel;
import com.swissarmyutility.globalnavigation.AppFragment;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
public class HeadTailsFragment extends AppFragment {
    View fragmentView;
    HeadTailModel headTailModel;
    Animation scaleAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.head_tails,null);
        //return the view here
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Head/Tails");
        DatabaseManager.init(getActivity());
        headTailModel = new HeadTailModel();
        int t=0,h=0;
        List<HeadTailModel> headTailModels = DatabaseManager.getInstance().getAllWatchLists();
        for(int i = 0;i<headTailModels.size();i++){
            if(headTailModels.get(i).getToss() == 0)
                t++;
            else
                h++;
        }
        tailsCounter = t;
        headsCounter = h;
        initView();

        scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(){
        coinImage = (ImageView)fragmentView.findViewById(R.id.head_tail_coin_image);

        ((Button)fragmentView.findViewById(R.id.head_tail_flip_coin_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               flipCoin();
            }
        });


        ((TextView)fragmentView.findViewById(R.id.tail_count)).setText(String.valueOf(tailsCounter));
        ((TextView)fragmentView.findViewById(R.id.head_count)).setText(String.valueOf(headsCounter));
        com.swissarmyutility.HeadTails.AnimationLib.Animation.init();
        coinImagesMap = new EnumMap<ResultState, Drawable>(ResultState.class);
        loadResources();
    }

    public void onAnimationEnd(){
        if(flipResult) {
            ((TextView)fragmentView.findViewById(R.id.head_count)).setText(String.valueOf(headsCounter));
            ((TextView)fragmentView.findViewById(R.id.head_count)).startAnimation(scaleAnimation);
            headTailModel.setToss(1);
        }else {
            ((TextView)fragmentView.findViewById(R.id.tail_count)).setText(String.valueOf(tailsCounter));
            ((TextView)fragmentView.findViewById(R.id.tail_count)).startAnimation(scaleAnimation);
            headTailModel.setToss(0);
        }
        DatabaseManager.getInstance().addWishLi(headTailModel);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    //=======//
    EnumMap<ResultState, Drawable> coinImagesMap;

    private final Coin theCoin = new Coin();

    private Boolean currentResult = true;

    private Boolean previousResult = true;

    private ImageView coinImage;

    private CustomAnimationDrawable coinAnimationCustom;

    private int flipCounter = 0;

    private int headsCounter = 0;

    private int tailsCounter = 0;
    boolean flipResult;

    private void flipCoin() {
        flipCounter++;
         // we're in the process of flipping the coin
        ResultState resultState = ResultState.UNKNOWN;


        // flip the coin and update the state with the result
        flipResult = theCoin.flip();
        if (flipResult) {
            headsCounter++;
        } else {
            tailsCounter++;
        }
        resultState = updateState(flipResult);

        // update the screen with the result of the flip
        renderResult(resultState);

    }

    private void resetCoin() {
        // hide the animation and draw the reset image
        displayCoinAnimation(false);
        displayCoinImage(true);
        coinImage.setImageDrawable(getResources().getDrawable(R.drawable.head));

        com.swissarmyutility.HeadTails.AnimationLib.Animation.clearAnimations();
        coinImagesMap.clear();
    }


    private ResultState updateState(final boolean flipResult) {
        // Analyze the current coin state and the new coin state and determine
        // the proper transition between the two.
        // true = HEADS | false = TAILS
        ResultState resultState = ResultState.UNKNOWN;
        currentResult = flipResult;

        // this is easier to read than the old code
        if (previousResult == true && currentResult == true) {
            resultState = ResultState.HEADS_HEADS;
        }
        if (previousResult == true && currentResult == false) {
            resultState = ResultState.HEADS_TAILS;
        }
        if (previousResult == false && currentResult == true) {
            resultState = ResultState.TAILS_HEADS;
        }
        if (previousResult == false && currentResult == false) {
            resultState = ResultState.TAILS_TAILS;
        }

        // update the previousResult for the next flip
        previousResult = currentResult;

        return resultState;
    }

    // check the coin preference and determine how to load its resources
    private void loadResources() {
        loadInternalResources();

    }

    // load resources internal to the CoinFlip package
    private void loadInternalResources() {
       // load the images
        final Drawable heads = getResources().getDrawable(R.drawable.head);
        final Drawable tails = getResources().getDrawable(R.drawable.tail);
        final Drawable edge = getResources().getDrawable(R.drawable.edge);
        final Drawable background = getResources().getDrawable(R.drawable.background);

            // render the animation for each result state and store it in the
            // animations map
        com.swissarmyutility.HeadTails.AnimationLib.Animation.generateAnimations(heads, tails, edge, background);


        // add the appropriate image for each result state to the images map
        // WTF? There's some kind of rendering bug if you use the "head" or
        // "tail" variables here...
        coinImagesMap.put(com.swissarmyutility.HeadTails.AnimationLib.Animation.ResultState.HEADS_HEADS, getResources().getDrawable(R.drawable.head));
        coinImagesMap.put(com.swissarmyutility.HeadTails.AnimationLib.Animation.ResultState.HEADS_TAILS, getResources().getDrawable(R.drawable.tail));
        coinImagesMap.put(com.swissarmyutility.HeadTails.AnimationLib.Animation.ResultState.TAILS_HEADS, getResources().getDrawable(R.drawable.head));
        coinImagesMap.put(com.swissarmyutility.HeadTails.AnimationLib.Animation.ResultState.TAILS_TAILS, getResources().getDrawable(R.drawable.tail));
    }

    private void renderResult(final ResultState resultState) {
        AnimationDrawable coinAnimation;

        // hide the static image and clear the text
        displayCoinImage(false);
        displayCoinAnimation(false);


        // display the result

            // load the appropriate coin animation based on the state
            coinAnimation = com.swissarmyutility.HeadTails.AnimationLib.Animation.getAnimation(resultState);
            coinAnimationCustom = new CustomAnimationDrawable(coinAnimation) {
                @Override
                protected void onAnimationFinish() {
                    onAnimationEnd();
                }
            };

            // hide the static image and render the animation
            displayCoinImage(false);
            displayCoinAnimation(true);
            coinImage.setBackgroundDrawable(coinAnimationCustom);
            coinAnimationCustom.start();
            // handled by animation callback
            // playCoinSound();
            // updateResultText(resultState, resultText);

    }

    private void displayCoinAnimation(final boolean flag) {
        // safety first!
        if (coinAnimationCustom != null) {
            if (flag) {
                coinAnimationCustom.setAlpha(255);
            } else {
                coinAnimationCustom.setAlpha(0);
            }
        }
    }

    private void displayCoinImage(final boolean flag) {
        // safety first!
        if (coinImage != null) {
            if (flag) {
                // get rid of the animation background
                coinImage.setBackgroundDrawable(null);
                coinImage.setAlpha(255);
            } else {
                coinImage.setAlpha(0);
            }
        }
    }

}