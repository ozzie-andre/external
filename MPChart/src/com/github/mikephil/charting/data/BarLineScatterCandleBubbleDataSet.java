// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import android.graphics.Color;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry> extends DataSet<T> implements IBarLineScatterCandleBubbleDataSet<T>
{
    protected int mHighLightColor;
    
    public BarLineScatterCandleBubbleDataSet(final List<T> yVals, final String label) {
        super(yVals, label);
        this.mHighLightColor = Color.rgb(255, 187, 115);
    }
    
    public void setHighLightColor(final int color) {
        this.mHighLightColor = color;
    }
    
    @Override
    public int getHighLightColor() {
        return this.mHighLightColor;
    }
}
