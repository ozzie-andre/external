// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer<IBarDataSet>
{
    protected float mBarSpace;
    protected float mGroupSpace;
    protected int mDataSetIndex;
    protected int mDataSetCount;
    protected boolean mContainsStacks;
    protected boolean mInverted;
    
    public BarBuffer(final int size, final float groupspace, final int dataSetCount, final boolean containsStacks) {
        super(size);
        this.mBarSpace = 0.0f;
        this.mGroupSpace = 0.0f;
        this.mDataSetIndex = 0;
        this.mDataSetCount = 1;
        this.mContainsStacks = false;
        this.mInverted = false;
        this.mGroupSpace = groupspace;
        this.mDataSetCount = dataSetCount;
        this.mContainsStacks = containsStacks;
    }
    
    public void setBarSpace(final float barspace) {
        this.mBarSpace = barspace;
    }
    
    public void setDataSet(final int index) {
        this.mDataSetIndex = index;
    }
    
    public void setInverted(final boolean inverted) {
        this.mInverted = inverted;
    }
    
    protected void addBar(final float left, final float top, final float right, final float bottom) {
        this.buffer[this.index++] = left;
        this.buffer[this.index++] = top;
        this.buffer[this.index++] = right;
        this.buffer[this.index++] = bottom;
    }
    
    @Override
    public void feed(final IBarDataSet data) {
        final float size = data.getEntryCount() * this.phaseX;
        final int dataSetOffset = this.mDataSetCount - 1;
        final float barSpaceHalf = this.mBarSpace / 2.0f;
        final float groupSpaceHalf = this.mGroupSpace / 2.0f;
        final float barWidth = 0.5f;
        for (int i = 0; i < size; ++i) {
            final BarEntry e = data.getEntryForIndex(i);
            final float x = e.getXIndex() + e.getXIndex() * dataSetOffset + this.mDataSetIndex + this.mGroupSpace * e.getXIndex() + groupSpaceHalf;
            float y = e.getVal();
            final float[] vals = e.getVals();
            if (!this.mContainsStacks || vals == null) {
                final float left = x - barWidth + barSpaceHalf;
                final float right = x + barWidth - barSpaceHalf;
                float bottom;
                float top;
                if (this.mInverted) {
                    bottom = ((y >= 0.0f) ? y : 0.0f);
                    top = ((y <= 0.0f) ? y : 0.0f);
                }
                else {
                    top = ((y >= 0.0f) ? y : 0.0f);
                    bottom = ((y <= 0.0f) ? y : 0.0f);
                }
                if (top > 0.0f) {
                    top *= this.phaseY;
                }
                else {
                    bottom *= this.phaseY;
                }
                this.addBar(left, top, right, bottom);
            }
            else {
                float posY = 0.0f;
                float negY = -e.getNegativeSum();
                float yStart = 0.0f;
                for (int k = 0; k < vals.length; ++k) {
                    final float value = vals[k];
                    if (value >= 0.0f) {
                        y = posY;
                        yStart = (posY += value);
                    }
                    else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }
                    final float left2 = x - barWidth + barSpaceHalf;
                    final float right2 = x + barWidth - barSpaceHalf;
                    float bottom2;
                    float top2;
                    if (this.mInverted) {
                        bottom2 = ((y >= yStart) ? y : yStart);
                        top2 = ((y <= yStart) ? y : yStart);
                    }
                    else {
                        top2 = ((y >= yStart) ? y : yStart);
                        bottom2 = ((y <= yStart) ? y : yStart);
                    }
                    top2 *= this.phaseY;
                    bottom2 *= this.phaseY;
                    this.addBar(left2, top2, right2, bottom2);
                }
            }
        }
        this.reset();
    }
}
