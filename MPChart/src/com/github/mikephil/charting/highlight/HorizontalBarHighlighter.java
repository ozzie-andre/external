// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class HorizontalBarHighlighter extends BarHighlighter
{
    public HorizontalBarHighlighter(final BarDataProvider chart) {
        super(chart);
    }
    
    @Override
    public Highlight getHighlight(final float x, final float y) {
        final Highlight h = super.getHighlight(x, y);
        if (h == null) {
            return h;
        }
        final IBarDataSet set = ((BarDataProvider)this.mChart).getBarData().getDataSetByIndex(h.getDataSetIndex());
        if (set.isStacked()) {
            final float[] pts = { y, 0.0f };
            ((BarDataProvider)this.mChart).getTransformer(set.getAxisDependency()).pixelsToValue(pts);
            return this.getStackedHighlight(h, set, h.getXIndex(), h.getDataSetIndex(), pts[0]);
        }
        return h;
    }
    
    @Override
    protected int getXIndex(final float x) {
        if (!((BarDataProvider)this.mChart).getBarData().isGrouped()) {
            final float[] pts = { 0.0f, x };
            ((BarDataProvider)this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
            return Math.round(pts[1]);
        }
        final float baseNoSpace = this.getBase(x);
        final int setCount = ((BarDataProvider)this.mChart).getBarData().getDataSetCount();
        int xIndex = (int)baseNoSpace / setCount;
        final int valCount = ((BarDataProvider)this.mChart).getData().getXValCount();
        if (xIndex < 0) {
            xIndex = 0;
        }
        else if (xIndex >= valCount) {
            xIndex = valCount - 1;
        }
        return xIndex;
    }
    
    @Override
    protected float getBase(final float y) {
        final float[] pts = { 0.0f, y };
        ((BarDataProvider)this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        final float yVal = pts[1];
        final int setCount = ((BarDataProvider)this.mChart).getBarData().getDataSetCount();
        final int steps = (int)(yVal / (setCount + ((BarDataProvider)this.mChart).getBarData().getGroupSpace()));
        final float groupSpaceSum = ((BarDataProvider)this.mChart).getBarData().getGroupSpace() * steps;
        final float baseNoSpace = yVal - groupSpaceSum;
        return baseNoSpace;
    }
}
