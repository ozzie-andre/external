// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.data.CandleData;

public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider
{
    public CandleStickChart(final Context context) {
        super(context);
    }
    
    public CandleStickChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CandleStickChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new CandleStickChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis.mAxisMinimum = -0.5f;
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        final XAxis mxAxis = this.mXAxis;
        mxAxis.mAxisMaximum += 0.5f;
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
    }
    
    @Override
    public CandleData getCandleData() {
        return (CandleData)this.mData;
    }
}
