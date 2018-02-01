// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

public class Highlight
{
    private int mXIndex;
    private int mDataSetIndex;
    private int mStackIndex;
    private Range mRange;
    
    public Highlight(final int x, final int dataSet) {
        this.mStackIndex = -1;
        this.mXIndex = x;
        this.mDataSetIndex = dataSet;
    }
    
    public Highlight(final int x, final int dataSet, final int stackIndex) {
        this(x, dataSet);
        this.mStackIndex = stackIndex;
    }
    
    public Highlight(final int x, final int dataSet, final int stackIndex, final Range range) {
        this(x, dataSet, stackIndex);
        this.mRange = range;
    }
    
    public int getDataSetIndex() {
        return this.mDataSetIndex;
    }
    
    public int getXIndex() {
        return this.mXIndex;
    }
    
    public int getStackIndex() {
        return this.mStackIndex;
    }
    
    public Range getRange() {
        return this.mRange;
    }
    
    public boolean equalTo(final Highlight h) {
        return h != null && (this.mDataSetIndex == h.mDataSetIndex && this.mXIndex == h.mXIndex && this.mStackIndex == h.mStackIndex);
    }
    
    @Override
    public String toString() {
        return "Highlight, xIndex: " + this.mXIndex + ", dataSetIndex: " + this.mDataSetIndex + ", stackIndex (only stacked barentry): " + this.mStackIndex;
    }
}
