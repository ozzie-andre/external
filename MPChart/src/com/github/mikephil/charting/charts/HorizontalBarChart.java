// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.PointF;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.RectF;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart;
//import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
//import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart;
import android.util.AttributeSet;
import android.content.Context;

public class HorizontalBarChart extends BarChart
{
    public HorizontalBarChart(final Context context) {
        super(context);
    }
    
    public HorizontalBarChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public HorizontalBarChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mLeftAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRightAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRenderer = new HorizontalBarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.setHighlighter(new HorizontalBarHighlighter(this));
        this.mAxisRendererLeft = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
    }
    
    @Override
    public void calculateOffsets() {
        float offsetLeft = 0.0f;
        float offsetRight = 0.0f;
        float offsetTop = 0.0f;
        float offsetBottom = 0.0f;
        if (this.mLegend != null && this.mLegend.isEnabled()) {
            if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                offsetRight += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset() * 2.0f;
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                offsetLeft += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset() * 2.0f;
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                final float yOffset = this.mLegend.mTextHeightMax;
                offsetBottom += Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                final float yOffset = this.mLegend.mTextHeightMax * 2.0f;
                offsetTop += Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            }
        }
        if (this.mAxisLeft.needsOffset()) {
            offsetTop += this.mAxisLeft.getRequiredHeightSpace(this.mAxisRendererLeft.getPaintAxisLabels());
        }
        if (this.mAxisRight.needsOffset()) {
            offsetBottom += this.mAxisRight.getRequiredHeightSpace(this.mAxisRendererRight.getPaintAxisLabels());
        }
        final float xlabelwidth = this.mXAxis.mLabelRotatedWidth;
        if (this.mXAxis.isEnabled()) {
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                offsetLeft += xlabelwidth;
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                offsetRight += xlabelwidth;
            }
            else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                offsetLeft += xlabelwidth;
                offsetRight += xlabelwidth;
            }
        }
        offsetTop += this.getExtraTopOffset();
        offsetRight += this.getExtraRightOffset();
        offsetBottom += this.getExtraBottomOffset();
        offsetLeft += this.getExtraLeftOffset();
        final float minOffset = Utils.convertDpToPixel(this.mMinOffset);
        this.mViewPortHandler.restrainViewPort(Math.max(minOffset, offsetLeft), Math.max(minOffset, offsetTop), Math.max(minOffset, offsetRight), Math.max(minOffset, offsetBottom));
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
            Log.i("MPAndroidChart", "Content: " + this.mViewPortHandler.getContentRect().toString());
        }
        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }
    
    @Override
    protected void prepareValuePxMatrix() {
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
    }
    
    @Override
    protected void calcModulus() {
        final float[] values = new float[9];
        this.mViewPortHandler.getMatrixTouch().getValues(values);
        this.mXAxis.mAxisLabelModulus = (int)Math.ceil(((BarData)this.mData).getXValCount() * this.mXAxis.mLabelRotatedHeight / (this.mViewPortHandler.contentHeight() * values[4]));
        if (this.mXAxis.mAxisLabelModulus < 1) {
            this.mXAxis.mAxisLabelModulus = 1;
        }
    }
    
    @Override
    public RectF getBarBounds(final BarEntry e) {
        final IBarDataSet set = ((BarData)this.mData).getDataSetForEntry(e);
        if (set == null) {
            return null;
        }
        final float barspace = set.getBarSpace();
        final float y = e.getVal();
        final float x = e.getXIndex();
        final float spaceHalf = barspace / 2.0f;
        final float top = x - 0.5f + spaceHalf;
        final float bottom = x + 0.5f - spaceHalf;
        final float left = (y >= 0.0f) ? y : 0.0f;
        final float right = (y <= 0.0f) ? y : 0.0f;
        final RectF bounds = new RectF(left, top, right, bottom);
        this.getTransformer(set.getAxisDependency()).rectValueToPixel(bounds);
        return bounds;
    }
    
    @Override
    public PointF getPosition(final Entry e, final YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        }
        final float[] vals = { e.getVal(), e.getXIndex() };
        this.getTransformer(axis).pointValuesToPixel(vals);
        return new PointF(vals[0], vals[1]);
    }
    
    @Override
    public Highlight getHighlightByTouchPoint(final float x, final float y) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        }
        return this.getHighlighter().getHighlight(y, x);
    }
    
    @Override
    public int getLowestVisibleXIndex() {
        final float step = ((BarData)this.mData).getDataSetCount();
        final float div = (step <= 1.0f) ? 1.0f : (step + ((BarData)this.mData).getGroupSpace());
        final float[] pts = { this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int)(((pts[1] <= 0.0f) ? 0.0f : (pts[1] / div)) + 1.0f);
    }
    
    @Override
    public int getHighestVisibleXIndex() {
        final float step = ((BarData)this.mData).getDataSetCount();
        final float div = (step <= 1.0f) ? 1.0f : (step + ((BarData)this.mData).getGroupSpace());
        final float[] pts = { this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (int)((pts[1] >= this.getXChartMax()) ? (this.getXChartMax() / div) : (pts[1] / div));
    }
}
