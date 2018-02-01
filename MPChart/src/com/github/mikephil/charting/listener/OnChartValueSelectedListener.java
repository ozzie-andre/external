// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;

public interface OnChartValueSelectedListener
{
    void onValueSelected(final Entry p0, final int p1, final Highlight p2);
    
    void onNothingSelected();
}
