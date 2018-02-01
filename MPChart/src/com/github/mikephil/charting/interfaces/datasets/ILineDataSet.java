// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.formatter.FillFormatter;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.data.Entry;

public interface ILineDataSet extends ILineRadarDataSet<Entry>
{
    float getCubicIntensity();
    
    boolean isDrawCubicEnabled();
    
    boolean isDrawSteppedEnabled();
    
    float getCircleRadius();
    
    int getCircleColor(final int p0);
    
    boolean isDrawCirclesEnabled();
    
    int getCircleHoleColor();
    
    boolean isDrawCircleHoleEnabled();
    
    DashPathEffect getDashPathEffect();
    
    boolean isDashedLineEnabled();
    
    FillFormatter getFillFormatter();
    
    int getIndicatorMode();
}
