// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.graphics.RectF;
import android.graphics.PointF;

public interface ChartInterface
{
    float getXChartMin();
    
    float getXChartMax();
    
    float getYChartMin();
    
    float getYChartMax();
    
    int getXValCount();
    
    int getWidth();
    
    int getHeight();
    
    PointF getCenterOfView();
    
    PointF getCenterOffsets();
    
    RectF getContentRect();
    
    ValueFormatter getDefaultValueFormatter();
    
    ChartData getData();
}
