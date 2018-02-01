// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class AnimatedMoveViewJob extends AnimatedViewPortJob
{
    public AnimatedMoveViewJob(final ViewPortHandler viewPortHandler, final float xValue, final float yValue, final Transformer trans, final View v, final float xOrigin, final float yOrigin, final long duration) {
        super(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration);
    }
    
    public void onAnimationUpdate(final ValueAnimator animation) {
        this.pts[0] = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
        this.pts[1] = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
    }
}
