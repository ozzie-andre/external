// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry>
{
    float getScatterShapeSize();
    
    ScatterChart.ScatterShape getScatterShape();
    
    float getScatterShapeHoleRadius();
    
    int getScatterShapeHoleColor();
}
