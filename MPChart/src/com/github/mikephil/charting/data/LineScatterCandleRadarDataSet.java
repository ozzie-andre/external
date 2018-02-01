// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;

public abstract class LineScatterCandleRadarDataSet<T extends Entry> extends BarLineScatterCandleBubbleDataSet<T> implements ILineScatterCandleRadarDataSet<T>
{
    protected boolean mDrawVerticalHighlightIndicator;
    protected boolean mDrawHorizontalHighlightIndicator;
    protected float mHighlightLineWidth;
    protected DashPathEffect mHighlightDashPathEffect;
    
    public LineScatterCandleRadarDataSet(final List<T> yVals, final String label) {
        super(yVals, label);
        this.mDrawVerticalHighlightIndicator = true;
        this.mDrawHorizontalHighlightIndicator = true;
        this.mHighlightLineWidth = 0.5f;
        this.mHighlightDashPathEffect = null;
        this.mHighlightLineWidth = Utils.convertDpToPixel(0.5f);
    }
    
    public void setDrawHorizontalHighlightIndicator(final boolean enabled) {
        this.mDrawHorizontalHighlightIndicator = enabled;
    }
    
    public void setDrawVerticalHighlightIndicator(final boolean enabled) {
        this.mDrawVerticalHighlightIndicator = enabled;
    }
    
    public void setDrawHighlightIndicators(final boolean enabled) {
        this.setDrawVerticalHighlightIndicator(enabled);
        this.setDrawHorizontalHighlightIndicator(enabled);
    }
    
    @Override
    public boolean isVerticalHighlightIndicatorEnabled() {
        return this.mDrawVerticalHighlightIndicator;
    }
    
    @Override
    public boolean isHorizontalHighlightIndicatorEnabled() {
        return this.mDrawHorizontalHighlightIndicator;
    }
    
    public void setHighlightLineWidth(final float width) {
        this.mHighlightLineWidth = Utils.convertDpToPixel(width);
    }
    
    @Override
    public float getHighlightLineWidth() {
        return this.mHighlightLineWidth;
    }
    
    public void enableDashedHighlightLine(final float lineLength, final float spaceLength, final float phase) {
        this.mHighlightDashPathEffect = new DashPathEffect(new float[] { lineLength, spaceLength }, phase);
    }
    
    public void disableDashedHighlightLine() {
        this.mHighlightDashPathEffect = null;
    }
    
    public boolean isDashedHighlightLineEnabled() {
        return this.mHighlightDashPathEffect != null;
    }
    
    @Override
    public DashPathEffect getDashPathEffectHighlight() {
        return this.mHighlightDashPathEffect;
    }
}
