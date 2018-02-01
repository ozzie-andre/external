// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class ViewPortJob implements Runnable
{
    protected float[] pts;
    protected ViewPortHandler mViewPortHandler;
    protected float xValue;
    protected float yValue;
    protected Transformer mTrans;
    protected View view;
    
    public ViewPortJob(final ViewPortHandler viewPortHandler, final float xValue, final float yValue, final Transformer trans, final View v) {
        this.pts = new float[2];
        this.xValue = 0.0f;
        this.yValue = 0.0f;
        this.mViewPortHandler = viewPortHandler;
        this.xValue = xValue;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;
    }
    
    public float getXValue() {
        return this.xValue;
    }
    
    public float getYValue() {
        return this.yValue;
    }
}
