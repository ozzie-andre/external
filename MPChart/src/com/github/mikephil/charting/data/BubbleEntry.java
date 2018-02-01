// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

public class BubbleEntry extends Entry
{
    private float mSize;
    
    public BubbleEntry(final int xIndex, final float val, final float size) {
        super(val, xIndex);
        this.mSize = 0.0f;
        this.mSize = size;
    }
    
    public BubbleEntry(final int xIndex, final float val, final float size, final Object data) {
        super(val, xIndex, data);
        this.mSize = 0.0f;
        this.mSize = size;
    }
    
    @Override
    public BubbleEntry copy() {
        final BubbleEntry c = new BubbleEntry(this.getXIndex(), this.getVal(), this.mSize, this.getData());
        return c;
    }
    
    public float getSize() {
        return this.mSize;
    }
    
    public void setSize(final float size) {
        this.mSize = size;
    }
}
