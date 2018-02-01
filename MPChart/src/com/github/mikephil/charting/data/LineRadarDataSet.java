// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import android.annotation.TargetApi;
import android.graphics.Color;
import java.util.List;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;

public abstract class LineRadarDataSet<T extends Entry> extends LineScatterCandleRadarDataSet<T> implements ILineRadarDataSet<T>
{
    private int mFillColor;
    protected Drawable mFillDrawable;
    private int mFillAlpha;
    private float mLineWidth;
    private boolean mDrawFilled;
    
    public LineRadarDataSet(final List<T> yVals, final String label) {
        super(yVals, label);
        this.mFillColor = Color.rgb(140, 234, 255);
        this.mFillAlpha = 85;
        this.mLineWidth = 2.5f;
        this.mDrawFilled = false;
    }
    
    @Override
    public int getFillColor() {
        return this.mFillColor;
    }
    
    public void setFillColor(final int color) {
        this.mFillColor = color;
        this.mFillDrawable = null;
    }
    
    @Override
    public Drawable getFillDrawable() {
        return this.mFillDrawable;
    }
    
    @TargetApi(18)
    public void setFillDrawable(final Drawable drawable) {
        this.mFillDrawable = drawable;
    }
    
    @Override
    public int getFillAlpha() {
        return this.mFillAlpha;
    }
    
    public void setFillAlpha(final int alpha) {
        this.mFillAlpha = alpha;
    }
    
    public void setLineWidth(float width) {
        if (width < 0.2f) {
            width = 0.2f;
        }
        if (width > 10.0f) {
            width = 10.0f;
        }
        this.mLineWidth = Utils.convertDpToPixel(width);
    }
    
    @Override
    public float getLineWidth() {
        return this.mLineWidth;
    }
    
    @Override
    public void setDrawFilled(final boolean filled) {
        this.mDrawFilled = filled;
    }
    
    @Override
    public boolean isDrawFilledEnabled() {
        return this.mDrawFilled;
    }
}
