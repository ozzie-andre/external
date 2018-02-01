// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import android.graphics.Color;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarDataSet extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet
{
    private float mBarSpace;
    private int mStackSize;
    private int mBarShadowColor;
    private int mHighLightAlpha;
    private int mEntryCountStacks;
    private String[] mStackLabels;
    
    public BarDataSet(final List<BarEntry> yVals, final String label) {
        super(yVals, label);
        this.mBarSpace = 0.15f;
        this.mStackSize = 1;
        this.mBarShadowColor = Color.rgb(215, 215, 215);
        this.mHighLightAlpha = 120;
        this.mEntryCountStacks = 0;
        this.mStackLabels = new String[] { "Stack" };
        this.mHighLightColor = Color.rgb(0, 0, 0);
        this.calcStackSize(yVals);
        this.calcEntryCountIncludingStacks(yVals);
    }
    
    @Override
    public DataSet<BarEntry> copy() {
        final List<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(((BarEntry)this.mYVals.get(i)).copy());
        }
        final BarDataSet copied = new BarDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mStackSize = this.mStackSize;
        copied.mBarSpace = this.mBarSpace;
        copied.mBarShadowColor = this.mBarShadowColor;
        copied.mStackLabels = this.mStackLabels;
        copied.mHighLightColor = this.mHighLightColor;
        copied.mHighLightAlpha = this.mHighLightAlpha;
        return copied;
    }
    
    private void calcEntryCountIncludingStacks(final List<BarEntry> yVals) {
        this.mEntryCountStacks = 0;
        for (int i = 0; i < yVals.size(); ++i) {
            final float[] vals = yVals.get(i).getVals();
            if (vals == null) {
                ++this.mEntryCountStacks;
            }
            else {
                this.mEntryCountStacks += vals.length;
            }
        }
    }
    
    private void calcStackSize(final List<BarEntry> yVals) {
        for (int i = 0; i < yVals.size(); ++i) {
            final float[] vals = yVals.get(i).getVals();
            if (vals != null && vals.length > this.mStackSize) {
                this.mStackSize = vals.length;
            }
        }
    }
    
    @Override
    public void calcMinMax(final int start, final int end) {
        if (this.mYVals == null) {
            return;
        }
        final int yValCount = this.mYVals.size();
        if (yValCount == 0) {
            return;
        }
        int endValue;
        if (end == 0 || end >= yValCount) {
            endValue = yValCount - 1;
        }
        else {
            endValue = end;
        }
        this.mYMin = Float.MAX_VALUE;
        this.mYMax = -3.4028235E38f;
        for (int i = start; i <= endValue; ++i) {
            final BarEntry e = (BarEntry)this.mYVals.get(i);
            if (e != null && !Float.isNaN(e.getVal())) {
                if (e.getVals() == null) {
                    if (e.getVal() < this.mYMin) {
                        this.mYMin = e.getVal();
                    }
                    if (e.getVal() > this.mYMax) {
                        this.mYMax = e.getVal();
                    }
                }
                else {
                    if (-e.getNegativeSum() < this.mYMin) {
                        this.mYMin = -e.getNegativeSum();
                    }
                    if (e.getPositiveSum() > this.mYMax) {
                        this.mYMax = e.getPositiveSum();
                    }
                }
            }
        }
        if (this.mYMin == Float.MAX_VALUE) {
            this.mYMin = 0.0f;
            this.mYMax = 0.0f;
        }
    }
    
    @Override
    public int getStackSize() {
        return this.mStackSize;
    }
    
    @Override
    public boolean isStacked() {
        return this.mStackSize > 1;
    }
    
    public int getEntryCountStacks() {
        return this.mEntryCountStacks;
    }
    
    public float getBarSpacePercent() {
        return this.mBarSpace * 100.0f;
    }
    
    @Override
    public float getBarSpace() {
        return this.mBarSpace;
    }
    
    public void setBarSpacePercent(final float percent) {
        this.mBarSpace = percent / 100.0f;
    }
    
    public void setBarShadowColor(final int color) {
        this.mBarShadowColor = color;
    }
    
    @Override
    public int getBarShadowColor() {
        return this.mBarShadowColor;
    }
    
    public void setHighLightAlpha(final int alpha) {
        this.mHighLightAlpha = alpha;
    }
    
    @Override
    public int getHighLightAlpha() {
        return this.mHighLightAlpha;
    }
    
    public void setStackLabels(final String[] labels) {
        this.mStackLabels = labels;
    }
    
    @Override
    public String[] getStackLabels() {
        return this.mStackLabels;
    }
}
