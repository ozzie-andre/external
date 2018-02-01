// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;

public class DefaultXAxisValueFormatter implements XAxisValueFormatter
{
    @Override
    public String getXValue(final String original, final int index, final ViewPortHandler viewPortHandler) {
        return original;
    }
}
