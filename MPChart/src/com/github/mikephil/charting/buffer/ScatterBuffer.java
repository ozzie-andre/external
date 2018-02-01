// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterBuffer extends AbstractBuffer<IScatterDataSet>
{
    public ScatterBuffer(final int size) {
        super(size);
    }
    
    protected void addForm(final float x, final float y) {
        this.buffer[this.index++] = x;
        this.buffer[this.index++] = y;
    }
    
    @Override
    public void feed(final IScatterDataSet data) {
        final float size = data.getEntryCount() * this.phaseX;
        for (int i = 0; i < size; ++i) {
            final Entry e = data.getEntryForIndex(i);
            this.addForm(e.getXIndex(), e.getVal() * this.phaseY);
        }
        this.reset();
    }
}
