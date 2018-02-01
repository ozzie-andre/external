// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.RectF;
import com.github.mikephil.charting.data.BarEntry;
import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.github.mikephil.charting.renderer.XAxisRendererBarChart;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.data.BarData;
public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider
{
    private boolean mDrawHighlightArrow;
    private boolean mDrawValueAboveBar;
    private boolean mDrawBarShadow;
    
    public BarChart(final Context context) {
        super(context);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
    }
    
    public BarChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
    }
    
    public BarChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new BarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxisRenderer = new XAxisRendererBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
        this.setHighlighter(new BarHighlighter(this));
        this.mXAxis.mAxisMinimum = -0.5f;
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        final XAxis mxAxis = this.mXAxis;
        mxAxis.mAxisRange += 0.5f;
        final XAxis mxAxis2 = this.mXAxis;
        mxAxis2.mAxisRange *= ((BarData)this.mData).getDataSetCount();
        final float groupSpace = ((BarData)this.mData).getGroupSpace();
        final XAxis mxAxis3 = this.mXAxis;
        mxAxis3.mAxisRange += ((BarData)this.mData).getXValCount() * groupSpace;
        this.mXAxis.mAxisMaximum = this.mXAxis.mAxisRange - this.mXAxis.mAxisMinimum;
    }
    
    @Override
    public Highlight getHighlightByTouchPoint(final float x, final float y) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        }
        return this.getHighlighter().getHighlight(x, y);
    }
    
    public RectF getBarBounds(final BarEntry e) {
        final IBarDataSet set = ((BarData)this.mData).getDataSetForEntry(e);
        if (set == null) {
            return null;
        }
        final float barspace = set.getBarSpace();
        final float y = e.getVal();
        final float x = e.getXIndex();
        final float barWidth = 0.5f;
        final float spaceHalf = barspace / 2.0f;
        final float left = x - barWidth + spaceHalf;
        final float right = x + barWidth - spaceHalf;
        final float top = (y >= 0.0f) ? y : 0.0f;
        final float bottom = (y <= 0.0f) ? y : 0.0f;
        final RectF bounds = new RectF(left, top, right, bottom);
        this.getTransformer(set.getAxisDependency()).rectValueToPixel(bounds);
        return bounds;
    }
    
    public void setDrawHighlightArrow(final boolean enabled) {
        this.mDrawHighlightArrow = enabled;
    }
    
    @Override
    public boolean isDrawHighlightArrowEnabled() {
        return this.mDrawHighlightArrow;
    }
    
    public void setDrawValueAboveBar(final boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }
    
    @Override
    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }
    
    public void setDrawBarShadow(final boolean enabled) {
        this.mDrawBarShadow = enabled;
    }
    
    @Override
    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }
    
    @Override
    public BarData getBarData() {
        return (BarData)this.mData;
    }
    
    @Override
    public int getLowestVisibleXIndex() {
        final float step = ((BarData)this.mData).getDataSetCount();
        final float div = (step <= 1.0f) ? 1.0f : (step + ((BarData)this.mData).getGroupSpace());
        final float[] pts = { this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int)((pts[0] <= this.getXChartMin()) ? 0.0f : (pts[0] / div + 1.0f));
    }
    
    @Override
    public int getHighestVisibleXIndex() {
        final float step = ((BarData)this.mData).getDataSetCount();
        final float div = (step <= 1.0f) ? 1.0f : (step + ((BarData)this.mData).getGroupSpace());
        final float[] pts = { this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int)((pts[0] >= this.getXChartMax()) ? (this.getXChartMax() / div) : (pts[0] / div));
    }
}
