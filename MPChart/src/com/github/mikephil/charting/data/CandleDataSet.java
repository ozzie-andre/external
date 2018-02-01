// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Paint;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

public class CandleDataSet extends LineScatterCandleRadarDataSet<CandleEntry> implements ICandleDataSet
{
    private float mShadowWidth;
    private boolean mShowCandleBar;
    private float mBarSpace;
    private boolean mShadowColorSameAsCandle;
    protected Paint.Style mIncreasingPaintStyle;
    protected Paint.Style mDecreasingPaintStyle;
    protected int mNeutralColor;
    protected int mIncreasingColor;
    protected int mDecreasingColor;
    protected int mShadowColor;
    
    public CandleDataSet(final List<CandleEntry> yVals, final String label) {
        super(yVals, label);
        this.mShadowWidth = 3.0f;
        this.mShowCandleBar = true;
        this.mBarSpace = 0.1f;
        this.mShadowColorSameAsCandle = false;
        this.mIncreasingPaintStyle = Paint.Style.STROKE;
        this.mDecreasingPaintStyle = Paint.Style.FILL;
        this.mNeutralColor = 1122867;
        this.mIncreasingColor = 1122867;
        this.mDecreasingColor = 1122867;
        this.mShadowColor = 1122867;
    }
    
    @Override
    public DataSet<CandleEntry> copy() {
        final List<CandleEntry> yVals = new ArrayList<CandleEntry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(((CandleEntry)this.mYVals.get(i)).copy());
        }
        final CandleDataSet copied = new CandleDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mShadowWidth = this.mShadowWidth;
        copied.mShowCandleBar = this.mShowCandleBar;
        copied.mBarSpace = this.mBarSpace;
        copied.mHighLightColor = this.mHighLightColor;
        copied.mIncreasingPaintStyle = this.mIncreasingPaintStyle;
        copied.mDecreasingPaintStyle = this.mDecreasingPaintStyle;
        copied.mShadowColor = this.mShadowColor;
        return copied;
    }
    
    @Override
    public void calcMinMax(final int start, final int end) {
        if (this.mYVals == null) {
            return;
        }
        if (this.mYVals.size() == 0) {
            return;
        }
        int endValue;
        if (end == 0 || end >= this.mYVals.size()) {
            endValue = this.mYVals.size() - 1;
        }
        else {
            endValue = end;
        }
        this.mYMin = Float.MAX_VALUE;
        this.mYMax = -3.4028235E38f;
        for (int i = start; i <= endValue; ++i) {
            final CandleEntry e = (CandleEntry)this.mYVals.get(i);
            if (e.getLow() < this.mYMin) {
                this.mYMin = e.getLow();
            }
            if (e.getHigh() > this.mYMax) {
                this.mYMax = e.getHigh();
            }
        }
    }
    
    public void setBarSpace(float space) {
        if (space < 0.0f) {
            space = 0.0f;
        }
        if (space > 0.45f) {
            space = 0.45f;
        }
        this.mBarSpace = space;
    }
    
    @Override
    public float getBarSpace() {
        return this.mBarSpace;
    }
    
    public void setShadowWidth(final float width) {
        this.mShadowWidth = Utils.convertDpToPixel(width);
    }
    
    @Override
    public float getShadowWidth() {
        return this.mShadowWidth;
    }
    
    public void setShowCandleBar(final boolean showCandleBar) {
        this.mShowCandleBar = showCandleBar;
    }
    
    @Override
    public boolean getShowCandleBar() {
        return this.mShowCandleBar;
    }
    
    public void setNeutralColor(final int color) {
        this.mNeutralColor = color;
    }
    
    @Override
    public int getNeutralColor() {
        return this.mNeutralColor;
    }
    
    public void setIncreasingColor(final int color) {
        this.mIncreasingColor = color;
    }
    
    @Override
    public int getIncreasingColor() {
        return this.mIncreasingColor;
    }
    
    public void setDecreasingColor(final int color) {
        this.mDecreasingColor = color;
    }
    
    @Override
    public int getDecreasingColor() {
        return this.mDecreasingColor;
    }
    
    @Override
    public Paint.Style getIncreasingPaintStyle() {
        return this.mIncreasingPaintStyle;
    }
    
    public void setIncreasingPaintStyle(final Paint.Style paintStyle) {
        this.mIncreasingPaintStyle = paintStyle;
    }
    
    @Override
    public Paint.Style getDecreasingPaintStyle() {
        return this.mDecreasingPaintStyle;
    }
    
    public void setDecreasingPaintStyle(final Paint.Style decreasingPaintStyle) {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }
    
    @Override
    public int getShadowColor() {
        return this.mShadowColor;
    }
    
    public void setShadowColor(final int shadowColor) {
        this.mShadowColor = shadowColor;
    }
    
    @Override
    public boolean getShadowColorSameAsCandle() {
        return this.mShadowColorSameAsCandle;
    }
    
    public void setShadowColorSameAsCandle(final boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
