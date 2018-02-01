// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.Entry;

public interface IRadarDataSet extends ILineRadarDataSet<Entry>
{
    boolean isDrawHighlightCircleEnabled();
    
    void setDrawHighlightCircleEnabled(final boolean p0);
    
    int getHighlightCircleFillColor();
    
    int getHighlightCircleStrokeColor();
    
    int getHighlightCircleStrokeAlpha();
    
    float getHighlightCircleInnerRadius();
    
    float getHighlightCircleOuterRadius();
    
    float getHighlightCircleStrokeWidth();
}
