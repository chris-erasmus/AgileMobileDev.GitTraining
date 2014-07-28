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
import com.swissarmyutility.HeadTails.CoinFlipAnimation.ResultState;
import com.swissarmyutility.data.DatabaseManager;
import com.swissarmyutility.globalnavigation.AppFragment;

import java.util.List;
import java.util.Random;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
public class HeadTailsFragment extends AppFragment {
    //View of the fragment
    private View fragmentView;

    private CoinDao coinDao;
    private Animation scaleAnimation;
    private Boolean currentResult = true;
    private Boolean previousResult = true;
    private ImageView coinImage;
    private CustomAnimationDrawable coinAnimationCustom;
    private int headsCounter;
    private int tailsCounter;
    private boolean flipResult;
    TextView headResultTextView,tailResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.head_tails,null);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Head/Tails");

        coinDao = new CoinDao();
        tailsCounter=0;
        headsCounter=0;
        List<CoinDao> coinDaos = DatabaseManager.getInstance(getActivity()).getAllFlipResults();
        for(int i = 0;i<coinDaos.size();i++){
            if(coinDaos.get(i).getFlipResult())
                headsCounter++;
            else
                tailsCounter++;
        }
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


        headResultTextView = (TextView)fragmentView.findViewById(R.id.head_count);
        tailResultTextView = (TextView)fragmentView.findViewById(R.id.tail_count);
        headResultTextView.setText(String.valueOf(tailsCounter));
        tailResultTextView.setText(String.valueOf(headsCounter));

        loadResources();
    }

    // load resources internal to the CoinFlip package
    private void loadResources() {
        // load the images
        final Drawable heads = getResources().getDrawable(R.drawable.head);
        final Drawable tails = getResources().getDrawable(R.drawable.tail);
        final Drawable edge = getResources().getDrawable(R.drawable.edge);
        final Drawable background = getResources().getDrawable(R.drawable.background);

        // render the animation for each result state and store it in the
        // animations map

        CoinFlipAnimation.generateAnimations(heads, tails, edge, background);
    }


    private void flipCoin() {
        Random randomGenerator = new Random();

        // flip the coin and update the state with the result
        flipResult = randomGenerator.nextBoolean();
        if (flipResult) {
            headsCounter++;
        } else {
            tailsCounter++;
        }
        ResultState resultState = updateState(flipResult);

        // update the screen with the result of the flip
        animateCoin(resultState);

    }


    private ResultState updateState(boolean flipResult) {
        // Analyze the current coin state and the new coin state and determine
        // the proper transition between the two.

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

    private void animateCoin(final ResultState resultState) {
        AnimationDrawable coinAnimation;

        // hide the static image and clear the text
        displayCoinImage(false);
        displayCoinAnimation(false);

            // load the appropriate coin animation based on the state
            coinAnimation = CoinFlipAnimation.getAnimation(resultState);
            coinAnimationCustom = new CustomAnimationDrawable(coinAnimation) {
                @Override
                protected void onAnimationFinish() {
                    showTossResult();
                }
            };

            // hide the static image and render the animation
            displayCoinImage(false);
            displayCoinAnimation(true);
            coinImage.setBackgroundDrawable(coinAnimationCustom);
            coinAnimationCustom.start();

    }

    public void showTossResult(){
        if(flipResult) {
            headResultTextView.setText(String.valueOf(headsCounter));
            headResultTextView.startAnimation(scaleAnimation);
            coinDao.setFlipResult(true);
        }else {
            tailResultTextView.setText(String.valueOf(tailsCounter));
            tailResultTextView.startAnimation(scaleAnimation);
            coinDao.setFlipResult(false);
        }
        DatabaseManager.getInstance(getActivity()).addFlipResult(coinDao);
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