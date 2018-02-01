// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

public class RadarDataSet extends LineRadarDataSet<Entry> implements IRadarDataSet
{
    protected boolean mDrawHighlightCircleEnabled;
    protected int mHighlightCircleFillColor;
    protected int mHighlightCircleStrokeColor;
    protected int mHighlightCircleStrokeAlpha;
    protected float mHighlightCircleInnerRadius;
    protected float mHighlightCircleOuterRadius;
    protected float mHighlightCircleStrokeWidth;
    
    public RadarDataSet(final List<Entry> yVals, final String label) {
        super(yVals, label);
        this.mDrawHighlightCircleEnabled = false;
        this.mHighlightCircleFillColor = -1;
        this.mHighlightCircleStrokeColor = 1122867;
        this.mHighlightCircleStrokeAlpha = 76;
        this.mHighlightCircleInnerRadius = 3.0f;
        this.mHighlightCircleOuterRadius = 4.0f;
        this.mHighlightCircleStrokeWidth = 2.0f;
    }
    
    @Override
    public boolean isDrawHighlightCircleEnabled() {
        return this.mDrawHighlightCircleEnabled;
    }
    
    @Override
    public void setDrawHighlightCircleEnabled(final boolean enabled) {
        this.mDrawHighlightCircleEnabled = enabled;
    }
    
    @Override
    public int getHighlightCircleFillColor() {
        return this.mHighlightCircleFillColor;
    }
    
    public void setHighlightCircleFillColor(final int color) {
        this.mHighlightCircleFillColor = color;
    }
    
    @Override
    public int getHighlightCircleStrokeColor() {
        return this.mHighlightCircleStrokeColor;
    }
    
    public void setHighlightCircleStrokeColor(final int color) {
        this.mHighlightCircleStrokeColor = color;
    }
    
    @Override
    public int getHighlightCircleStrokeAlpha() {
        return this.mHighlightCircleStrokeAlpha;
    }
    
    public void setHighlightCircleStrokeAlpha(final int alpha) {
        this.mHighlightCircleStrokeAlpha = alpha;
    }
    
    @Override
    public float getHighlightCircleInnerRadius() {
        return this.mHighlightCircleInnerRadius;
    }
    
    public void setHighlightCircleInnerRadius(final float radius) {
        this.mHighlightCircleInnerRadius = radius;
    }
    
    @Override
    public float getHighlightCircleOuterRadius() {
        return this.mHighlightCircleOuterRadius;
    }
    
    public void setHighlightCircleOuterRadius(final float radius) {
        this.mHighlightCircleOuterRadius = radius;
    }
    
    @Override
    public float getHighlightCircleStrokeWidth() {
        return this.mHighlightCircleStrokeWidth;
    }
    
    public void setHighlightCircleStrokeWidth(final float strokeWidth) {
        this.mHighlightCircleStrokeWidth = strokeWidth;
    }
    
    @Override
    public DataSet<Entry> copy() {
        final List<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(this.mYVals.get(i).copy());
        }
        final RadarDataSet copied = new RadarDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
    }
}
