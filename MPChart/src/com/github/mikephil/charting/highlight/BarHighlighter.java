// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class BarHighlighter extends ChartHighlighter<BarDataProvider>
{
    public BarHighlighter(final BarDataProvider chart) {
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
            final float[] pts = { 0.0f, y };
            ((BarDataProvider)this.mChart).getTransformer(set.getAxisDependency()).pixelsToValue(pts);
            return this.getStackedHighlight(h, set, h.getXIndex(), h.getDataSetIndex(), pts[1]);
        }
        return h;
    }
    
    @Override
    protected int getXIndex(final float x) {
        if (!((BarDataProvider)this.mChart).getBarData().isGrouped()) {
            return super.getXIndex(x);
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
    protected int getDataSetIndex(final int xIndex, final float x, final float y) {
        if (!((BarDataProvider)this.mChart).getBarData().isGrouped()) {
            return 0;
        }
        final float baseNoSpace = this.getBase(x);
        final int setCount = ((BarDataProvider)this.mChart).getBarData().getDataSetCount();
        int dataSetIndex = (int)baseNoSpace % setCount;
        if (dataSetIndex < 0) {
            dataSetIndex = 0;
        }
        else if (dataSetIndex >= setCount) {
            dataSetIndex = setCount - 1;
        }
        return dataSetIndex;
    }
    
    protected Highlight getStackedHighlight(final Highlight old, final IBarDataSet set, final int xIndex, final int dataSetIndex, final double yValue) {
        final BarEntry entry = set.getEntryForXIndex(xIndex);
        if (entry == null || entry.getVals() == null) {
            return old;
        }
        final Range[] ranges = this.getRanges(entry);
        final int stackIndex = this.getClosestStackIndex(ranges, (float)yValue);
        if (ranges.length > 0) {
            return new Highlight(xIndex, dataSetIndex, stackIndex, ranges[stackIndex]);
        }
        return null;
    }
    
    protected int getClosestStackIndex(final Range[] ranges, final float value) {
        if (ranges == null || ranges.length == 0) {
            return 0;
        }
        int stackIndex = 0;
        for (final Range range : ranges) {
            if (range.contains(value)) {
                return stackIndex;
            }
            ++stackIndex;
        }
        final int length = Math.max(ranges.length - 1, 0);
        return (value > ranges[length].to) ? length : 0;
    }
    
    protected float getBase(final float x) {
        final float[] pts = { x, 0.0f };
        ((BarDataProvider)this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        final float xVal = pts[0];
        final int setCount = ((BarDataProvider)this.mChart).getBarData().getDataSetCount();
        final int steps = (int)(xVal / (setCount + ((BarDataProvider)this.mChart).getBarData().getGroupSpace()));
        final float groupSpaceSum = ((BarDataProvider)this.mChart).getBarData().getGroupSpace() * steps;
        final float baseNoSpace = xVal - groupSpaceSum;
        return baseNoSpace;
    }
    
    protected Range[] getRanges(final BarEntry entry) {
        final float[] values = entry.getVals();
        if (values == null || values.length == 0) {
            return new Range[0];
        }
        final Range[] ranges = new Range[values.length];
        float negRemain = -entry.getNegativeSum();
        float posRemain = 0.0f;
        for (int i = 0; i < ranges.length; ++i) {
            final float value = values[i];
            if (value < 0.0f) {
                ranges[i] = new Range(negRemain, negRemain + Math.abs(value));
                negRemain += Math.abs(value);
            }
            else {
                ranges[i] = new Range(posRemain, posRemain + value);
                posRemain += value;
            }
        }
        return ranges;
    }
}
