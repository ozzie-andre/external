// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class StackedValueFormatter implements ValueFormatter
{
    private boolean mDrawWholeStack;
    private String mAppendix;
    private DecimalFormat mFormat;
    
    public StackedValueFormatter(final boolean drawWholeStack, final String appendix, final int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mAppendix = appendix;
        final StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; ++i) {
            if (i == 0) {
                b.append(".");
            }
            b.append("0");
        }
        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }
    
    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        if (!this.mDrawWholeStack && entry instanceof BarEntry) {
            final BarEntry barEntry = (BarEntry)entry;
            final float[] vals = barEntry.getVals();
            if (vals != null) {
                if (vals[vals.length - 1] == value) {
                    return String.valueOf(this.mFormat.format(barEntry.getVal())) + this.mAppendix;
                }
                return "";
            }
        }
        return String.valueOf(this.mFormat.format(value)) + this.mAppendix;
    }
}
