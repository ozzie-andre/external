// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.data.Entry;
import java.text.DecimalFormat;

public class LargeValueFormatter implements ValueFormatter, YAxisValueFormatter
{
    private static String[] SUFFIX;
    private static final int MAX_LENGTH = 4;
    private DecimalFormat mFormat;
    private String mText;
    
    static {
        LargeValueFormatter.SUFFIX = new String[] { "", "k", "m", "b", "t" };
    }
    
    public LargeValueFormatter() {
        this.mText = "";
        this.mFormat = new DecimalFormat("###E0");
    }
    
    public LargeValueFormatter(final String appendix) {
        this();
        this.mText = appendix;
    }
    
    @Override
    public String getFormattedValue(final float value, final Entry entry, final int dataSetIndex, final ViewPortHandler viewPortHandler) {
        return String.valueOf(this.makePretty(value)) + this.mText;
    }
    
    @Override
    public String getFormattedValue(final float value, final YAxis yAxis) {
        return String.valueOf(this.makePretty(value)) + this.mText;
    }
    
    public void setAppendix(final String appendix) {
        this.mText = appendix;
    }
    
    public void setSuffix(final String[] suff) {
        if (suff.length == 5) {
            LargeValueFormatter.SUFFIX = suff;
        }
    }
    
    private String makePretty(final double number) {
        String r;
        for (r = this.mFormat.format(number), r = r.replaceAll("E[0-9]", LargeValueFormatter.SUFFIX[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]); r.length() > 4 || r.matches("[0-9]+\\.[a-z]"); r = String.valueOf(r.substring(0, r.length() - 2)) + r.substring(r.length() - 1)) {}
        return r;
    }
}
