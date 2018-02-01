// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

public class CandleEntry extends Entry
{
    private float mShadowHigh;
    private float mShadowLow;
    private float mClose;
    private float mOpen;
    
    public CandleEntry(final int xIndex, final float shadowH, final float shadowL, final float open, final float close) {
        super((shadowH + shadowL) / 2.0f, xIndex);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }
    
    public CandleEntry(final int xIndex, final float shadowH, final float shadowL, final float open, final float close, final Object data) {
        super((shadowH + shadowL) / 2.0f, xIndex, data);
        this.mShadowHigh = 0.0f;
        this.mShadowLow = 0.0f;
        this.mClose = 0.0f;
        this.mOpen = 0.0f;
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }
    
    public float getShadowRange() {
        return Math.abs(this.mShadowHigh - this.mShadowLow);
    }
    
    public float getBodyRange() {
        return Math.abs(this.mOpen - this.mClose);
    }
    
    @Override
    public float getVal() {
        return super.getVal();
    }
    
    @Override
    public CandleEntry copy() {
        final CandleEntry c = new CandleEntry(this.getXIndex(), this.mShadowHigh, this.mShadowLow, this.mOpen, this.mClose, this.getData());
        return c;
    }
    
    public float getHigh() {
        return this.mShadowHigh;
    }
    
    public void setHigh(final float mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }
    
    public float getLow() {
        return this.mShadowLow;
    }
    
    public void setLow(final float mShadowLow) {
        this.mShadowLow = mShadowLow;
    }
    
    public float getClose() {
        return this.mClose;
    }
    
    public void setClose(final float mClose) {
        this.mClose = mClose;
    }
    
    public float getOpen() {
        return this.mOpen;
    }
    
    public void setOpen(final float mOpen) {
        this.mOpen = mOpen;
    }
}
