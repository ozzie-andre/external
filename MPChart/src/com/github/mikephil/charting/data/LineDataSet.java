// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import android.content.Context;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import java.util.ArrayList;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.FillFormatter;
import android.graphics.DashPathEffect;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class LineDataSet extends LineRadarDataSet<Entry> implements ILineDataSet
{
    private List<Integer> mCircleColors;
    private int mCircleColorHole;
    private float mCircleRadius;
    private float mCubicIntensity;
    private DashPathEffect mDashPathEffect;
    private FillFormatter mFillFormatter;
    private boolean mDrawCircles;
    private boolean mDrawCubic;
    private boolean mDrawStepped;
    private boolean mDrawCircleHole;
    public static final int INDICATOR_MODE_CIRCLE = 0;
    public static final int INDICATOR_MODE_TRIANGLE = 1;
    public static final int INDICATOR_MODE_RECTANGLE = 2;
    private int indicatorMode;
    
    public LineDataSet(final List<Entry> yVals, final String label) {
        super(yVals, label);
        this.mCircleColors = null;
        this.mCircleColorHole = -1;
        this.mCircleRadius = 8.0f;
        this.mCubicIntensity = 0.2f;
        this.mDashPathEffect = null;
        this.mFillFormatter = new DefaultFillFormatter();
        this.mDrawCircles = true;
        this.mDrawCubic = false;
        this.mDrawStepped = false;
        this.mDrawCircleHole = true;
        this.indicatorMode = 0;
        (this.mCircleColors = new ArrayList<Integer>()).add(Color.rgb(140, 234, 255));
    }
    
    @Override
    public DataSet<Entry> copy() {
        final List<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(this.mYVals.get(i).copy());
        }
        final LineDataSet copied = new LineDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mCircleRadius = this.mCircleRadius;
        copied.mCircleColors = this.mCircleColors;
        copied.mDashPathEffect = this.mDashPathEffect;
        copied.mDrawCircles = this.mDrawCircles;
        copied.mDrawCubic = this.mDrawCubic;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
    }
    
    public void setCubicIntensity(float intensity) {
        if (intensity > 1.0f) {
            intensity = 1.0f;
        }
        if (intensity < 0.05f) {
            intensity = 0.05f;
        }
        this.mCubicIntensity = intensity;
    }
    
    @Override
    public float getCubicIntensity() {
        return this.mCubicIntensity;
    }
    
    public void setCircleRadius(final float radius) {
        this.mCircleRadius = Utils.convertDpToPixel(radius);
    }
    
    @Override
    public float getCircleRadius() {
        return this.mCircleRadius;
    }
    
    @Deprecated
    public void setCircleSize(final float size) {
        this.setCircleRadius(size);
    }
    
    @Deprecated
    public float getCircleSize() {
        return this.getCircleRadius();
    }
    
    public void enableDashedLine(final float lineLength, final float spaceLength, final float phase) {
        this.mDashPathEffect = new DashPathEffect(new float[] { lineLength, spaceLength }, phase);
    }
    
    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }
    
    @Override
    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }
    
    @Override
    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }
    
    public void setDrawCircles(final boolean enabled) {
        this.mDrawCircles = enabled;
    }
    
    @Override
    public boolean isDrawCirclesEnabled() {
        return this.mDrawCircles;
    }
    
    public void setDrawCubic(final boolean enabled) {
        this.mDrawCubic = enabled;
    }
    
    @Override
    public boolean isDrawCubicEnabled() {
        return this.mDrawCubic;
    }
    
    public void setDrawStepped(final boolean enabled) {
        this.mDrawStepped = enabled;
    }
    
    public void setIndicatorMode(final int mode) {
        this.indicatorMode = mode;
    }
    
    @Override
    public boolean isDrawSteppedEnabled() {
        return this.mDrawStepped;
    }
    
    public List<Integer> getCircleColors() {
        return this.mCircleColors;
    }
    
    @Override
    public int getCircleColor(final int index) {
        return this.mCircleColors.get(index % this.mCircleColors.size());
    }
    
    public void setCircleColors(final List<Integer> colors) {
        this.mCircleColors = colors;
    }
    
    public void setCircleColors(final int[] colors) {
        this.mCircleColors = ColorTemplate.createColors(colors);
    }
    
    public void setCircleColors(final int[] colors, final Context c) {
        final List<Integer> clrs = new ArrayList<Integer>();
        for (final int color : colors) {
            clrs.add(c.getResources().getColor(color));
        }
        this.mCircleColors = clrs;
    }
    
    public void setCircleColor(final int color) {
        this.resetCircleColors();
        this.mCircleColors.add(color);
    }
    
    public void resetCircleColors() {
        this.mCircleColors = new ArrayList<Integer>();
    }
    
    public void setCircleColorHole(final int color) {
        this.mCircleColorHole = color;
    }
    
    @Override
    public int getCircleHoleColor() {
        return this.mCircleColorHole;
    }
    
    public void setDrawCircleHole(final boolean enabled) {
        this.mDrawCircleHole = enabled;
    }
    
    @Override
    public boolean isDrawCircleHoleEnabled() {
        return this.mDrawCircleHole;
    }
    
    public void setFillFormatter(final FillFormatter formatter) {
        if (formatter == null) {
            this.mFillFormatter = new DefaultFillFormatter();
        }
        else {
            this.mFillFormatter = formatter;
        }
    }
    
    @Override
    public FillFormatter getFillFormatter() {
        return this.mFillFormatter;
    }
    
    @Override
    public int getIndicatorMode() {
        return this.indicatorMode;
    }
}
