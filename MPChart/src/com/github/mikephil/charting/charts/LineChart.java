// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.renderer.LineChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.data.LineData;

public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider
{
    public LineChart(final Context context) {
        super(context);
    }
    
    public LineChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LineChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new LineChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        if (this.mXAxis.mAxisRange == 0.0f && ((LineData)this.mData).getYValCount() > 0) {
            this.mXAxis.mAxisRange = 1.0f;
        }
    }
    
    @Override
    public LineData getLineData() {
        return (LineData)this.mData;
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && this.mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer)this.mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
