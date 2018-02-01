// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import android.util.Log;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import android.graphics.DashPathEffect;

public abstract class AxisBase extends ComponentBase
{
    private int mGridColor;
    private float mGridLineWidth;
    private int mAxisLineColor;
    private float mAxisLineWidth;
    protected boolean mDrawGridLines;
    protected boolean mDrawAxisLine;
    protected boolean mDrawLabels;
    private DashPathEffect mGridDashPathEffect;
    protected List<LimitLine> mLimitLines;
    protected boolean mDrawLimitLineBehindData;
    protected boolean mCustomAxisMin;
    protected boolean mCustomAxisMax;
    public float mAxisMaximum;
    public float mAxisMinimum;
    public float mAxisRange;
    
    public AxisBase() {
        this.mGridColor = -7829368;
        this.mGridLineWidth = 1.0f;
        this.mAxisLineColor = -7829368;
        this.mAxisLineWidth = 1.0f;
        this.mDrawGridLines = true;
        this.mDrawAxisLine = true;
        this.mDrawLabels = true;
        this.mGridDashPathEffect = null;
        this.mDrawLimitLineBehindData = false;
        this.mCustomAxisMin = false;
        this.mCustomAxisMax = false;
        this.mAxisMaximum = 0.0f;
        this.mAxisMinimum = 0.0f;
        this.mAxisRange = 0.0f;
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(5.0f);
        this.mLimitLines = new ArrayList<LimitLine>();
    }
    
    public void setDrawGridLines(final boolean enabled) {
        this.mDrawGridLines = enabled;
    }
    
    public boolean isDrawGridLinesEnabled() {
        return this.mDrawGridLines;
    }
    
    public void setDrawAxisLine(final boolean enabled) {
        this.mDrawAxisLine = enabled;
    }
    
    public boolean isDrawAxisLineEnabled() {
        return this.mDrawAxisLine;
    }
    
    public void setGridColor(final int color) {
        this.mGridColor = color;
    }
    
    public int getGridColor() {
        return this.mGridColor;
    }
    
    public void setAxisLineWidth(final float width) {
        this.mAxisLineWidth = Utils.convertDpToPixel(width);
    }
    
    public float getAxisLineWidth() {
        return this.mAxisLineWidth;
    }
    
    public void setGridLineWidth(final float width) {
        this.mGridLineWidth = Utils.convertDpToPixel(width);
    }
    
    public float getGridLineWidth() {
        return this.mGridLineWidth;
    }
    
    public void setAxisLineColor(final int color) {
        this.mAxisLineColor = color;
    }
    
    public int getAxisLineColor() {
        return this.mAxisLineColor;
    }
    
    public void setDrawLabels(final boolean enabled) {
        this.mDrawLabels = enabled;
    }
    
    public boolean isDrawLabelsEnabled() {
        return this.mDrawLabels;
    }
    
    public void addLimitLine(final LimitLine l) {
        this.mLimitLines.add(l);
        if (this.mLimitLines.size() > 6) {
            Log.e("MPAndroiChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
        }
    }
    
    public void removeLimitLine(final LimitLine l) {
        this.mLimitLines.remove(l);
    }
    
    public void removeAllLimitLines() {
        this.mLimitLines.clear();
    }
    
    public List<LimitLine> getLimitLines() {
        return this.mLimitLines;
    }
    
    public void setDrawLimitLinesBehindData(final boolean enabled) {
        this.mDrawLimitLineBehindData = enabled;
    }
    
    public boolean isDrawLimitLinesBehindDataEnabled() {
        return this.mDrawLimitLineBehindData;
    }
    
    public abstract String getLongestLabel();
    
    public void enableGridDashedLine(final float lineLength, final float spaceLength, final float phase) {
        this.mGridDashPathEffect = new DashPathEffect(new float[] { lineLength, spaceLength }, phase);
    }
    
    public void disableGridDashedLine() {
        this.mGridDashPathEffect = null;
    }
    
    public boolean isGridDashedLineEnabled() {
        return this.mGridDashPathEffect != null;
    }
    
    public DashPathEffect getGridDashPathEffect() {
        return this.mGridDashPathEffect;
    }
    
    public float getAxisMaximum() {
        return this.mAxisMaximum;
    }
    
    public float getAxisMinimum() {
        return this.mAxisMinimum;
    }
    
    public void resetAxisMaxValue() {
        this.mCustomAxisMax = false;
    }
    
    public boolean isAxisMaxCustom() {
        return this.mCustomAxisMax;
    }
    
    public void resetAxisMinValue() {
        this.mCustomAxisMin = false;
    }
    
    public boolean isAxisMinCustom() {
        return this.mCustomAxisMin;
    }
    
    public void setAxisMinValue(final float min) {
        this.mCustomAxisMin = true;
        this.mAxisMinimum = min;
    }
    
    public void setAxisMaxValue(final float max) {
        this.mCustomAxisMax = true;
        this.mAxisMaximum = max;
    }
}
