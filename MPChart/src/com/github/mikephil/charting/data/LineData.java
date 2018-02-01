// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet>
{
    public LineData() {
    }
    
    public LineData(final List<String> xVals) {
        super(xVals);
    }
    
    public LineData(final String[] xVals) {
        super(xVals);
    }
    
    public LineData(final List<String> xVals, final List<ILineDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public LineData(final String[] xVals, final List<ILineDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public LineData(final List<String> xVals, final ILineDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public LineData(final String[] xVals, final ILineDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<ILineDataSet> toList(final ILineDataSet dataSet) {
        final List<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(dataSet);
        return sets;
    }
}
