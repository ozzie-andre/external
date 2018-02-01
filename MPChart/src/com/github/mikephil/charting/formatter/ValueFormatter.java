// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;

public interface ValueFormatter
{
    String getFormattedValue(final float p0, final Entry p1, final int p2, final ViewPortHandler p3);
}
