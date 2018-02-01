// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet
{
    protected float mXMax;
    protected float mXMin;
    protected float mMaxSize;
    private float mHighlightCircleWidth;
    
    public BubbleDataSet(final List<BubbleEntry> yVals, final String label) {
        super(yVals, label);
        this.mHighlightCircleWidth = 2.5f;
    }
    
    @Override
    public void setHighlightCircleWidth(final float width) {
        this.mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }
    
    @Override
    public float getHighlightCircleWidth() {
        return this.mHighlightCircleWidth;
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
        this.mYMin = this.yMin((BubbleEntry)this.mYVals.get(start));
        this.mYMax = this.yMax((BubbleEntry)this.mYVals.get(start));
        for (int i = start; i <= endValue; ++i) {
            final BubbleEntry entry = (BubbleEntry)this.mYVals.get(i);
            final float ymin = this.yMin(entry);
            final float ymax = this.yMax(entry);
            if (ymin < this.mYMin) {
                this.mYMin = ymin;
            }
            if (ymax > this.mYMax) {
                this.mYMax = ymax;
            }
            final float xmin = this.xMin(entry);
            final float xmax = this.xMax(entry);
            if (xmin < this.mXMin) {
                this.mXMin = xmin;
            }
            if (xmax > this.mXMax) {
                this.mXMax = xmax;
            }
            final float size = this.largestSize(entry);
            if (size > this.mMaxSize) {
                this.mMaxSize = size;
            }
        }
    }
    
    @Override
    public DataSet<BubbleEntry> copy() {
        final List<BubbleEntry> yVals = new ArrayList<BubbleEntry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(((BubbleEntry)this.mYVals.get(i)).copy());
        }
        final BubbleDataSet copied = new BubbleDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
    }
    
    @Override
    public float getXMax() {
        return this.mXMax;
    }
    
    @Override
    public float getXMin() {
        return this.mXMin;
    }
    
    @Override
    public float getMaxSize() {
        return this.mMaxSize;
    }
    
    private float yMin(final BubbleEntry entry) {
        return entry.getVal();
    }
    
    private float yMax(final BubbleEntry entry) {
        return entry.getVal();
    }
    
    private float xMin(final BubbleEntry entry) {
        return entry.getXIndex();
    }
    
    private float xMax(final BubbleEntry entry) {
        return entry.getXIndex();
    }
    
    private float largestSize(final BubbleEntry entry) {
        return entry.getSize();
    }
}
