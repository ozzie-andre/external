// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BarEntry;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry>
{
    float getBarSpace();
    
    boolean isStacked();
    
    int getStackSize();
    
    int getBarShadowColor();
    
    int getHighLightAlpha();
    
    String[] getStackLabels();
}
