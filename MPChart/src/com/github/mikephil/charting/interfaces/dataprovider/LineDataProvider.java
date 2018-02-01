// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider
{
    LineData getLineData();
    
    YAxis getAxis(final YAxis.AxisDependency p0);
}
