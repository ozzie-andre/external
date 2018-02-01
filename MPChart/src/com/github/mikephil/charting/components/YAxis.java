// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

public class YAxis extends AxisBase
{
    protected YAxisValueFormatter mYAxisValueFormatter;
    public float[] mEntries;
    public int mEntryCount;
    public int mDecimals;
    private int mLabelCount;
    private boolean mDrawTopYLabelEntry;
    protected boolean mShowOnlyMinMax;
    protected boolean mInverted;
    protected boolean mForceLabels;
    protected boolean mDrawZeroLine;
    protected int mZeroLineColor;
    protected float mZeroLineWidth;
    protected float mSpacePercentTop;
    protected float mSpacePercentBottom;
    private YAxisLabelPosition mPosition;
    private AxisDependency mAxisDependency;
    protected float mMinWidth;
    protected float mMaxWidth;
    protected boolean mGranularityEnabled;
    protected float mGranularity;
    
    public YAxis() {
        this.mEntries = new float[0];
        this.mLabelCount = 6;
        this.mDrawTopYLabelEntry = true;
        this.mShowOnlyMinMax = false;
        this.mInverted = false;
        this.mForceLabels = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mGranularityEnabled = true;
        this.mGranularity = 1.0f;
        this.mAxisDependency = AxisDependency.LEFT;
        this.mYOffset = 0.0f;
    }
    
    public YAxis(final AxisDependency position) {
        this.mEntries = new float[0];
        this.mLabelCount = 6;
        this.mDrawTopYLabelEntry = true;
        this.mShowOnlyMinMax = false;
        this.mInverted = false;
        this.mForceLabels = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mGranularityEnabled = true;
        this.mGranularity = 1.0f;
        this.mAxisDependency = position;
        this.mYOffset = 0.0f;
    }
    
    public AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }
    
    public float getMinWidth() {
        return this.mMinWidth;
    }
    
    public void setMinWidth(final float minWidth) {
        this.mMinWidth = minWidth;
    }
    
    public float getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public void setMaxWidth(final float maxWidth) {
        this.mMaxWidth = maxWidth;
    }
    
    public boolean isGranularityEnabled() {
        return this.mGranularityEnabled;
    }
    
    public void setGranularityEnabled(final boolean enabled) {
        this.mGranularityEnabled = true;
    }
    
    public float getGranularity() {
        return this.mGranularity;
    }
    
    public void setGranularity(final float granularity) {
        this.mGranularity = granularity;
    }
    
    public YAxisLabelPosition getLabelPosition() {
        return this.mPosition;
    }
    
    public void setPosition(final YAxisLabelPosition pos) {
        this.mPosition = pos;
    }
    
    public boolean isDrawTopYLabelEntryEnabled() {
        return this.mDrawTopYLabelEntry;
    }
    
    public void setDrawTopYLabelEntry(final boolean enabled) {
        this.mDrawTopYLabelEntry = enabled;
    }
    
    public void setLabelCount(int count, final boolean force) {
        if (count > 25) {
            count = 25;
        }
        if (count < 2) {
            count = 2;
        }
        this.mLabelCount = count;
        this.mForceLabels = force;
    }
    
    public int getLabelCount() {
        return this.mLabelCount;
    }
    
    public boolean isForceLabelsEnabled() {
        return this.mForceLabels;
    }
    
    public void setShowOnlyMinMax(final boolean enabled) {
        this.mShowOnlyMinMax = enabled;
    }
    
    public boolean isShowOnlyMinMaxEnabled() {
        return this.mShowOnlyMinMax;
    }
    
    public void setInverted(final boolean enabled) {
        this.mInverted = enabled;
    }
    
    public boolean isInverted() {
        return this.mInverted;
    }
    
    @Deprecated
    public void setStartAtZero(final boolean startAtZero) {
        if (startAtZero) {
            this.setAxisMinValue(0.0f);
        }
        else {
            this.resetAxisMinValue();
        }
    }
    
    public void setSpaceTop(final float percent) {
        this.mSpacePercentTop = percent;
    }
    
    public float getSpaceTop() {
        return this.mSpacePercentTop;
    }
    
    public void setSpaceBottom(final float percent) {
        this.mSpacePercentBottom = percent;
    }
    
    public float getSpaceBottom() {
        return this.mSpacePercentBottom;
    }
    
    public boolean isDrawZeroLineEnabled() {
        return this.mDrawZeroLine;
    }
    
    public void setDrawZeroLine(final boolean mDrawZeroLine) {
        this.mDrawZeroLine = mDrawZeroLine;
    }
    
    public int getZeroLineColor() {
        return this.mZeroLineColor;
    }
    
    public void setZeroLineColor(final int color) {
        this.mZeroLineColor = color;
    }
    
    public float getZeroLineWidth() {
        return this.mZeroLineWidth;
    }
    
    public void setZeroLineWidth(final float width) {
        this.mZeroLineWidth = Utils.convertDpToPixel(width);
    }
    
    public float getRequiredWidthSpace(final Paint p) {
        p.setTextSize(this.mTextSize);
        final String label = this.getLongestLabel();
        float width = Utils.calcTextWidth(p, label) + this.getXOffset() * 2.0f;
        float minWidth = this.getMinWidth();
        float maxWidth = this.getMaxWidth();
        if (minWidth > 0.0f) {
            minWidth = Utils.convertDpToPixel(minWidth);
        }
        if (maxWidth > 0.0f && maxWidth != Float.POSITIVE_INFINITY) {
            maxWidth = Utils.convertDpToPixel(maxWidth);
        }
        width = Math.max(minWidth, Math.min(width, (maxWidth > 0.0) ? maxWidth : width));
        return width;
    }
    
    public float getRequiredHeightSpace(final Paint p) {
        p.setTextSize(this.mTextSize);
        final String label = this.getLongestLabel();
        return Utils.calcTextHeight(p, label) + this.getYOffset() * 2.0f;
    }
    
    @Override
    public String getLongestLabel() {
        String longest = "";
        for (int i = 0; i < this.mEntries.length; ++i) {
            final String text = this.getFormattedLabel(i);
            if (longest.length() < text.length()) {
                longest = text;
            }
        }
        return longest;
    }
    
    public String getFormattedLabel(final int index) {
        if (index < 0 || index >= this.mEntries.length) {
            return "";
        }
        return this.getValueFormatter().getFormattedValue(this.mEntries[index], this);
    }
    
    public void setValueFormatter(final YAxisValueFormatter f) {
        if (f == null) {
            this.mYAxisValueFormatter = new DefaultYAxisValueFormatter(this.mDecimals);
        }
        else {
            this.mYAxisValueFormatter = f;
        }
    }
    
    public YAxisValueFormatter getValueFormatter() {
        if (this.mYAxisValueFormatter == null) {
            this.mYAxisValueFormatter = new DefaultYAxisValueFormatter(this.mDecimals);
        }
        return this.mYAxisValueFormatter;
    }
    
    public boolean needsDefaultFormatter() {
        return this.mYAxisValueFormatter == null || this.mYAxisValueFormatter instanceof DefaultValueFormatter;
    }
    
    public boolean needsOffset() {
        return this.isEnabled() && this.isDrawLabelsEnabled() && this.getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART;
    }
    
    public void calcMinMax(final float dataMin, final float dataMax) {
        float min = this.mCustomAxisMin ? this.mAxisMinimum : dataMin;
        float max = this.mCustomAxisMax ? this.mAxisMaximum : dataMax;
        final float range = Math.abs(max - min);
        if (range == 0.0f) {
            ++max;
            --min;
        }
        if (!this.mCustomAxisMin) {
            final float bottomSpace = range / 100.0f * this.getSpaceBottom();
            this.mAxisMinimum = min - bottomSpace;
        }
        if (!this.mCustomAxisMax) {
            final float topSpace = range / 100.0f * this.getSpaceTop();
            this.mAxisMaximum = max + topSpace;
        }
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }
    
    public enum AxisDependency
    {
        LEFT("LEFT", 0), 
        RIGHT("RIGHT", 1);
        
        private AxisDependency(final String s, final int n) {
        }
    }
    
    public enum YAxisLabelPosition
    {
        OUTSIDE_CHART("OUTSIDE_CHART", 0), 
        INSIDE_CHART("INSIDE_CHART", 1);
        
        private YAxisLabelPosition(final String s, final int n) {
        }
    }
}
