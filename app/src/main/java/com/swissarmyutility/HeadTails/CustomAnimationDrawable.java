
package com.swissarmyutility.HeadTails;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

// AnimationDrawable does not provide a callback after the animation is
// finished.  This abstract class raises an onAnimationFinish() method
// after the final frame of the animation is rendered.

public abstract class CustomAnimationDrawable extends AnimationDrawable {

    Handler mAnimationHandler;
    Runnable mAnimationRunable;

    public CustomAnimationDrawable(AnimationDrawable aniDraw) {
        // Add each frame to this CustomAnimationDrawable
        for (int i = 0; i < aniDraw.getNumberOfFrames(); i++) {
            this.addFrame(aniDraw.getFrame(i), aniDraw.getDuration(i));
        }
    }

    @Override
    public void start() {
        super.start();

        // Call super.start() to call the base class start animation method.
        // Then add a handler to call onAnimationFinish() when the total
        // duration for the animation has passed.

        mAnimationRunable = new Runnable() {
            @Override public void run() {
                onAnimationFinish();
            }
        };

        mAnimationHandler = new Handler();
        mAnimationHandler.postDelayed(mAnimationRunable, 3000);

    }

    public int getTotalDuration() {


        int iDuration = 0;

        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }

        return iDuration;
    }

    // called when the animation finishes
    protected abstract void onAnimationFinish();
}
