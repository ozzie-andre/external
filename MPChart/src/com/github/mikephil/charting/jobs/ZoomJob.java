// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.jobs;

import android.graphics.Matrix;
import com.github.mikephil.charting.charts.BarLineChartBase;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.components.YAxis;
@SuppressWarnings("rawtypes")

public class ZoomJob extends ViewPortJob
{
    protected float scaleX;
    protected float scaleY;
    protected YAxis.AxisDependency axisDependency;
    
    public ZoomJob(final ViewPortHandler viewPortHandler, final float scaleX, final float scaleY, final float xValue, final float yValue, final Transformer trans, final YAxis.AxisDependency axis, final View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.axisDependency = axis;
    }
    
	@Override
    public void run() {
        Matrix save = this.mViewPortHandler.zoom(this.scaleX, this.scaleY);
        this.mViewPortHandler.refresh(save, this.view, false);
        final float valsInView = ((BarLineChartBase)this.view).getDeltaY(this.axisDependency) / this.mViewPortHandler.getScaleY();
        final float xsInView = ((BarLineChartBase)this.view).getXAxis().getValues().size() / this.mViewPortHandler.getScaleX();
        this.pts[0] = this.xValue - xsInView / 2.0f;
        this.pts[1] = this.yValue + valsInView / 2.0f;
        this.mTrans.pointValuesToPixel(this.pts);
        save = this.mViewPortHandler.translate(this.pts);
        this.mViewPortHandler.refresh(save, this.view, false);
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
    }
}
