// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer
{
    public HorizontalBarBuffer(final int size, final float groupspace, final int dataSetCount, final boolean containsStacks) {
        super(size, groupspace, dataSetCount, containsStacks);
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
                final float bottom = x - barWidth + barSpaceHalf;
                final float top = x + barWidth - barSpaceHalf;
                float left;
                float right;
                if (this.mInverted) {
                    left = ((y >= 0.0f) ? y : 0.0f);
                    right = ((y <= 0.0f) ? y : 0.0f);
                }
                else {
                    right = ((y >= 0.0f) ? y : 0.0f);
                    left = ((y <= 0.0f) ? y : 0.0f);
                }
                if (right > 0.0f) {
                    right *= this.phaseY;
                }
                else {
                    left *= this.phaseY;
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
                    final float bottom2 = x - barWidth + barSpaceHalf;
                    final float top2 = x + barWidth - barSpaceHalf;
                    float left2;
                    float right2;
                    if (this.mInverted) {
                        left2 = ((y >= yStart) ? y : yStart);
                        right2 = ((y <= yStart) ? y : yStart);
                    }
                    else {
                        right2 = ((y >= yStart) ? y : yStart);
                        left2 = ((y <= yStart) ? y : yStart);
                    }
                    right2 *= this.phaseY;
                    left2 *= this.phaseY;
                    this.addBar(left2, top2, right2, bottom2);
                }
            }
        }
        this.reset();
    }
}
