// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

public final class FSize
{
    public final float width;
    public final float height;
    
    public FSize(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof FSize) {
            final FSize other = (FSize)obj;
            return this.width == other.width && this.height == other.height;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.width) + "x" + this.height;
    }
    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(this.width) ^ Float.floatToIntBits(this.height);
    }
}
