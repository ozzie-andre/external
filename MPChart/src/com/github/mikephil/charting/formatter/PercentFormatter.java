// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class PercentFormatter implements ValueFormatter, YAxisValueFormatter
{
    protected DecimalFormat mFormat;
    
    public PercentFormatter() {
        this.mFormat = new DecimalFormat("###,###,##0.0");
    }
    
    public PercentFormatter(final DecimalFormat format) {
        this.mFormat = format;
    }
    
    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        return String.valueOf(this.mFormat.format(value)) + " %";
    }
    
    @Override
    public String getFormattedValue(final float value, final YAxis yAxis) {
        return String.valueOf(this.mFormat.format(value)) + " %";
    }
}
