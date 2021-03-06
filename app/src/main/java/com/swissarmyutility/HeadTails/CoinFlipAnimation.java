
package com.swissarmyutility.HeadTails;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.EnumMap;

public class CoinFlipAnimation {

    // enumerator of all possible transition states
    public enum ResultState {
        HEADS_HEADS,
        HEADS_TAILS,
        TAILS_HEADS,
        TAILS_TAILS,
        UNKNOWN
    }

    private static EnumMap<ResultState, AnimationDrawable> coinAnimationsMap;

    private CoinFlipAnimation() {
        //singleton
    }

    public static AnimationDrawable getAnimation(ResultState state) {
        return coinAnimationsMap.get(state);
    }

    public static EnumMap<ResultState, AnimationDrawable> generateAnimations(final Drawable imageA, final Drawable imageB,final Drawable edge, final Drawable background) {
        CoinFlipAnimation.ResultState resultState;
        AnimationDrawable coinAnimation;
        coinAnimationsMap = new EnumMap<ResultState, AnimationDrawable>(CoinFlipAnimation.ResultState.class);
        final int widthA = ((BitmapDrawable) imageA).getBitmap().getWidth();
        final int heightA = ((BitmapDrawable) imageA).getBitmap().getHeight();
        final int widthB = ((BitmapDrawable) imageB).getBitmap().getWidth();
        final int heightB = ((BitmapDrawable) imageB).getBitmap().getHeight();

        BitmapDrawable bg = (BitmapDrawable) background;

        // create the individual animation frames for the head side
        final BitmapDrawable imageA_8 = (BitmapDrawable) imageA;
        final BitmapDrawable imageA_6 = resizeBitmapDrawable(imageA_8, (int) (widthA * 0.75), heightA, bg);
        final BitmapDrawable imageA_4 = resizeBitmapDrawable(imageA_8, (int) (widthA * 0.50), heightA, bg);
        final BitmapDrawable imageA_2 = resizeBitmapDrawable(imageA_8, (int) (widthA * 0.25), heightA, bg);

        // create the individual animation frames for the tail side
        final BitmapDrawable imageB_8 = (BitmapDrawable) imageB;
        final BitmapDrawable imageB_6 = resizeBitmapDrawable(imageB_8, (int) (widthB * 0.75), heightB, bg);
        final BitmapDrawable imageB_4 = resizeBitmapDrawable(imageB_8, (int) (widthB * 0.50), heightB, bg);
        final BitmapDrawable imageB_2 = resizeBitmapDrawable(imageB_8, (int) (widthB * 0.25), heightB, bg);

        // the temporary bitmaps have already been cleared, this might be a good place for a garbage collection
        System.gc();

        // create the appropriate animation depending on the result state
        resultState = CoinFlipAnimation.ResultState.HEADS_HEADS;
        coinAnimation = generateAnimatedDrawable(imageA_8, imageA_6, imageA_4, imageA_2, imageB_8, imageB_6, imageB_4, imageB_2, (BitmapDrawable) edge, resultState);
        coinAnimationsMap.put(resultState, coinAnimation);

        resultState = CoinFlipAnimation.ResultState.HEADS_TAILS;
        coinAnimation = generateAnimatedDrawable(imageA_8, imageA_6, imageA_4, imageA_2, imageB_8, imageB_6, imageB_4, imageB_2, (BitmapDrawable) edge, resultState);
        coinAnimationsMap.put(resultState, coinAnimation);

        resultState = CoinFlipAnimation.ResultState.TAILS_HEADS;
        coinAnimation = generateAnimatedDrawable(imageA_8, imageA_6, imageA_4, imageA_2, imageB_8, imageB_6, imageB_4, imageB_2, (BitmapDrawable) edge, resultState);
        coinAnimationsMap.put(resultState, coinAnimation);

        resultState = CoinFlipAnimation.ResultState.TAILS_TAILS;
        coinAnimation = generateAnimatedDrawable(imageA_8, imageA_6, imageA_4, imageA_2, imageB_8, imageB_6, imageB_4, imageB_2, (BitmapDrawable) edge, resultState);
        coinAnimationsMap.put(resultState, coinAnimation);

        return coinAnimationsMap;

    }

    private static BitmapDrawable resizeBitmapDrawable(final BitmapDrawable image, final int width, final int height, final BitmapDrawable background) {

        // load the transparent background and convert to a bitmap
        Bitmap background_bm = background.getBitmap();

        // convert the passed in image to a bitmap and resize according to parameters
        Bitmap image_bm = Bitmap.createScaledBitmap(image.getBitmap(), width, height, true);

        // create a new canvas to combine the two images on
        Bitmap comboImage_bm = Bitmap.createBitmap(background_bm.getWidth(),background_bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(comboImage_bm);

        // add the background as well as the new image to the horizontal center
        // of the image
        comboImage.drawBitmap(background_bm, 0f, 0f, null);
        comboImage.drawBitmap(image_bm, (background_bm.getWidth() - image_bm.getWidth()) / 2, 0f, null);

        // convert the new combo image bitmap to a BitmapDrawable
        final BitmapDrawable comboImage_bmd = new BitmapDrawable(comboImage_bm);

        background_bm = null;
        image_bm = null;
        comboImage_bm = null;
        comboImage = null;

        return comboImage_bmd;
    }

    private static AnimationDrawable generateAnimatedDrawable(final BitmapDrawable imageA_8, final BitmapDrawable imageA_6, final BitmapDrawable imageA_4, final BitmapDrawable imageA_2, final BitmapDrawable imageB_8, final BitmapDrawable imageB_6, final BitmapDrawable imageB_4, final BitmapDrawable imageB_2, final BitmapDrawable edge, final CoinFlipAnimation.ResultState resultState) {

        final int duration = 20;
        final AnimationDrawable animation = new AnimationDrawable();

        // create the appropriate animation depending on the result state
        switch (resultState) {
            case HEADS_HEADS:
                // Begin Flip 1
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                // Begin Flip 2
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                // Begin Flip 3
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                break;
            case HEADS_TAILS:
                // Begin Flip 1
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                // Begin Flip 2
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                // Begin Flip 3 (half flip)
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                break;
            case TAILS_HEADS:
                // Begin Flip 1
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                // Begin Flip 2
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                // Begin Flip 3 (half flip)
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                break;
            case TAILS_TAILS:
                // Begin Flip 1
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                // Begin Flip 2
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                // Begin Flip 3
                animation.addFrame(imageB_8, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_8, duration);
                animation.addFrame(imageA_6, duration);
                animation.addFrame(imageA_4, duration);
                animation.addFrame(imageA_2, duration);
                animation.addFrame(edge, duration);
                animation.addFrame(imageB_2, duration);
                animation.addFrame(imageB_4, duration);
                animation.addFrame(imageB_6, duration);
                animation.addFrame(imageB_8, duration);
                break;
            default:
                break;
        }

        animation.setOneShot(true);

        return animation;
    }
}
