// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.animation.ValueAnimator;

public class ChartAnimator
{
    private ValueAnimator.AnimatorUpdateListener mListener;
    protected float mPhaseY;
    protected float mPhaseX;
    
    public ChartAnimator() {
        this.mPhaseY = 1.0f;
        this.mPhaseX = 1.0f;
    }
    
    public ChartAnimator(final ValueAnimator.AnimatorUpdateListener listener) {
        this.mPhaseY = 1.0f;
        this.mPhaseX = 1.0f;
        this.mListener = listener;
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY, final EasingFunction easingX, final EasingFunction easingY) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setInterpolator((TimeInterpolator)easingY);
        animatorY.setDuration((long)durationMillisY);
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setInterpolator((TimeInterpolator)easingX);
        animatorX.setDuration((long)durationMillisX);
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(this.mListener);
        }
        else {
            animatorY.addUpdateListener(this.mListener);
        }
        animatorX.start();
        animatorY.start();
    }
    
    public void animateX(final int durationMillis, final EasingFunction easing) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setInterpolator((TimeInterpolator)easing);
        animatorX.setDuration((long)durationMillis);
        animatorX.addUpdateListener(this.mListener);
        animatorX.start();
    }
    
    public void animateY(final int durationMillis, final EasingFunction easing) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setInterpolator((TimeInterpolator)easing);
        animatorY.setDuration((long)durationMillis);
        animatorY.addUpdateListener(this.mListener);
        animatorY.start();
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY, final Easing.EasingOption easingX, final Easing.EasingOption easingY) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingY));
        animatorY.setDuration((long)durationMillisY);
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingX));
        animatorX.setDuration((long)durationMillisX);
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(this.mListener);
        }
        else {
            animatorY.addUpdateListener(this.mListener);
        }
        animatorX.start();
        animatorY.start();
    }
    
    public void animateX(final int durationMillis, final Easing.EasingOption easing) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easing));
        animatorX.setDuration((long)durationMillis);
        animatorX.addUpdateListener(this.mListener);
        animatorX.start();
    }
    
    public void animateY(final int durationMillis, final Easing.EasingOption easing) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easing));
        animatorY.setDuration((long)durationMillis);
        animatorY.addUpdateListener(this.mListener);
        animatorY.start();
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setDuration((long)durationMillisY);
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setDuration((long)durationMillisX);
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(this.mListener);
        }
        else {
            animatorY.addUpdateListener(this.mListener);
        }
        animatorX.start();
        animatorY.start();
    }
    
    public void animateX(final int durationMillis) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        animatorX.setDuration((long)durationMillis);
        animatorX.addUpdateListener(this.mListener);
        animatorX.start();
    }
    
    public void animateY(final int durationMillis) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        animatorY.setDuration((long)durationMillis);
        animatorY.addUpdateListener(this.mListener);
        animatorY.start();
    }
    
    public float getPhaseY() {
        return this.mPhaseY;
    }
    
    public void setPhaseY(final float phase) {
        this.mPhaseY = phase;
    }
    
    public float getPhaseX() {
        return this.mPhaseX;
    }
    
    public void setPhaseX(final float phase) {
        this.mPhaseX = phase;
    }
}
