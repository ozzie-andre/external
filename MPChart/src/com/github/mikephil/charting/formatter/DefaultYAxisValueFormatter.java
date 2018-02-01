// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;
import java.text.DecimalFormat;

public class DefaultYAxisValueFormatter implements YAxisValueFormatter
{
    private DecimalFormat mFormat;
    
    public DefaultYAxisValueFormatter(final int digits) {
        final StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; ++i) {
            if (i == 0) {
                b.append(".");
            }
            b.append("0");
        }
        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }
    
    @Override
    public String getFormattedValue(final float value, final YAxis yAxis) {
        return this.mFormat.format(value);
    }
}
