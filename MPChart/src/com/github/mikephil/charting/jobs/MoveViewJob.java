// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MoveViewJob extends ViewPortJob
{
    public MoveViewJob(final ViewPortHandler viewPortHandler, final float xValue, final float yValue, final Transformer trans, final View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
    }
    
    @Override
    public void run() {
        this.pts[0] = this.xValue;
        this.pts[1] = this.yValue;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
    }
}
