// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry>
{
    void setHighlightCircleWidth(final float p0);
    
    float getXMax();
    
    float getXMin();
    
    float getMaxSize();
    
    float getHighlightCircleWidth();
}
