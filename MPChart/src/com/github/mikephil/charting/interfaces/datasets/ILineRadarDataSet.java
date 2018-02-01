// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.Entry;

public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T>
{
    int getFillColor();
    
    Drawable getFillDrawable();
    
    int getFillAlpha();
    
    float getLineWidth();
    
    boolean isDrawFilledEnabled();
    
    void setDrawFilled(final boolean p0);
}
