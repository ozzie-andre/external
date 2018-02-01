// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.YAxis;
@SuppressWarnings("rawtypes")
public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface
{
    Transformer getTransformer(final YAxis.AxisDependency p0);
    
    int getMaxVisibleCount();
    
    boolean isInverted(final YAxis.AxisDependency p0);
    
    int getLowestVisibleXIndex();
    
    int getHighestVisibleXIndex();
    
    BarLineScatterCandleBubbleData getData();
}
