// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

public final class Range
{
    public float from;
    public float to;
    
    public Range(final float from, final float to) {
        this.from = from;
        this.to = to;
    }
    
    public boolean contains(final float value) {
        return value > this.from && value <= this.to;
    }
    
    public boolean isLarger(final float value) {
        return value > this.to;
    }
    
    public boolean isSmaller(final float value) {
        return value < this.from;
    }
}
