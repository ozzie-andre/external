// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.animation.ValueAnimator;

@SuppressLint({ "NewApi" })
public abstract class AnimatedViewPortJob extends ViewPortJob implements ValueAnimator.AnimatorUpdateListener
{
    protected ObjectAnimator animator;
    protected float phase;
    protected float xOrigin;
    protected float yOrigin;
    
    public AnimatedViewPortJob(final ViewPortHandler viewPortHandler, final float xValue, final float yValue, final Transformer trans, final View v, final float xOrigin, final float yOrigin, final long duration) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        (this.animator = ObjectAnimator.ofFloat((Object)this, "phase", new float[] { 0.0f, 1.0f })).setDuration(duration);
        this.animator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)this);
    }
    
    @SuppressLint({ "NewApi" })
    public void run() {
        this.animator.start();
    }
    
    public float getPhase() {
        return this.phase;
    }
    
    public void setPhase(final float phase) {
        this.phase = phase;
    }
    
    public float getXOrigin() {
        return this.xOrigin;
    }
    
    public float getYOrigin() {
        return this.yOrigin;
    }
}
