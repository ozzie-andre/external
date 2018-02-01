// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public abstract class BarLineScatterCandleBubbleData<T extends IBarLineScatterCandleBubbleDataSet<? extends Entry>> extends ChartData<T>
{
    public BarLineScatterCandleBubbleData() {
    }
    
    public BarLineScatterCandleBubbleData(final List<String> xVals) {
        super(xVals);
    }
    
    public BarLineScatterCandleBubbleData(final String[] xVals) {
        super(xVals);
    }
    
    public BarLineScatterCandleBubbleData(final List<String> xVals, final List<T> sets) {
        super(xVals, sets);
    }
    
    public BarLineScatterCandleBubbleData(final String[] xVals, final List<T> sets) {
        super(xVals, sets);
    }
}
