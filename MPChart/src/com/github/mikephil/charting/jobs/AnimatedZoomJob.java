// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import com.github.mikephil.charting.charts.BarLineChartBase;
import android.graphics.Matrix;
import android.animation.ValueAnimator;
import com.github.mikephil.charting.utils.Transformer;
import android.view.View;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.components.YAxis;
import android.annotation.SuppressLint;
import android.animation.Animator;

@SuppressLint({ "NewApi" })
@SuppressWarnings("rawtypes")
public class AnimatedZoomJob extends AnimatedViewPortJob implements Animator.AnimatorListener
{
    protected float zoomOriginX;
    protected float zoomOriginY;
    protected float zoomCenterX;
    protected float zoomCenterY;
    protected YAxis yAxis;
    protected float xValCount;
    
    public AnimatedZoomJob(final ViewPortHandler viewPortHandler, final View v, final Transformer trans, final YAxis axis, final float xValCount, final float scaleX, final float scaleY, final float xOrigin, final float yOrigin, final float zoomCenterX, final float zoomCenterY, final float zoomOriginX, final float zoomOriginY, final long duration) {
        super(viewPortHandler, scaleX, scaleY, trans, v, xOrigin, yOrigin, duration);
        this.zoomCenterX = zoomCenterX;
        this.zoomCenterY = zoomCenterY;
        this.zoomOriginX = zoomOriginX;
        this.zoomOriginY = zoomOriginY;
        this.animator.addListener((Animator.AnimatorListener)this);
        this.yAxis = axis;
        this.xValCount = xValCount;
    }
    
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float scaleX = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
        final float scaleY = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
        Matrix save = this.mViewPortHandler.setZoom(scaleX, scaleY);
        this.mViewPortHandler.refresh(save, this.view, false);
        final float valsInView = this.yAxis.mAxisRange / this.mViewPortHandler.getScaleY();
        final float xsInView = this.xValCount / this.mViewPortHandler.getScaleX();
        this.pts[0] = this.zoomOriginX + (this.zoomCenterX - xsInView / 2.0f - this.zoomOriginX) * this.phase;
        this.pts[1] = this.zoomOriginY + (this.zoomCenterY + valsInView / 2.0f - this.zoomOriginY) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        save = this.mViewPortHandler.translate(this.pts);
        this.mViewPortHandler.refresh(save, this.view, true);
    }
    

	public void onAnimationEnd(final Animator animation) {
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
    }
    
    public void onAnimationCancel(final Animator animation) {
    }
    
    public void onAnimationRepeat(final Animator animation) {
    }
    
    public void onAnimationStart(final Animator animation) {
    }
}
