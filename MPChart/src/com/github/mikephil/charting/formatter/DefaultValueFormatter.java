// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class DefaultValueFormatter implements ValueFormatter
{
    private DecimalFormat mFormat;
    
    public DefaultValueFormatter(final int digits) {
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
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        return this.mFormat.format(value);
    }
}
