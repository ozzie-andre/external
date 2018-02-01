// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.ScatterChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.data.ScatterData;

public class ScatterChart extends BarLineChartBase<ScatterData> implements ScatterDataProvider
{
    public ScatterChart(final Context context) {
        super(context);
    }
    
    public ScatterChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ScatterChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new ScatterChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis.mAxisMinimum = -0.5f;
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        if (this.mXAxis.mAxisRange == 0.0f && ((ScatterData)this.mData).getYValCount() > 0) {
            this.mXAxis.mAxisRange = 1.0f;
        }
        final XAxis mxAxis = this.mXAxis;
        mxAxis.mAxisMaximum += 0.5f;
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
    }
    
    public static ScatterShape[] getAllPossibleShapes() {
        return new ScatterShape[] { ScatterShape.SQUARE, ScatterShape.CIRCLE, ScatterShape.TRIANGLE, ScatterShape.CROSS };
    }
    
    @Override
    public ScatterData getScatterData() {
        return (ScatterData)this.mData;
    }
    
    public enum ScatterShape
    {
        SQUARE("SQUARE", 0), 
        CIRCLE("CIRCLE", 1), 
        TRIANGLE("TRIANGLE", 2), 
        CROSS("CROSS", 3), 
        X("X", 4);
        
        private ScatterShape(final String s, final int n) {
        }
    }
}
