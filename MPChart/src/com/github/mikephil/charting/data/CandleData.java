// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

public class CandleData extends BarLineScatterCandleBubbleData<ICandleDataSet>
{
    public CandleData() {
    }
    
    public CandleData(final List<String> xVals) {
        super(xVals);
    }
    
    public CandleData(final String[] xVals) {
        super(xVals);
    }
    
    public CandleData(final List<String> xVals, final List<ICandleDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public CandleData(final String[] xVals, final List<ICandleDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public CandleData(final List<String> xVals, final ICandleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public CandleData(final String[] xVals, final ICandleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<ICandleDataSet> toList(final ICandleDataSet dataSet) {
        final List<ICandleDataSet> sets = new ArrayList<ICandleDataSet>();
        sets.add(dataSet);
        return sets;
    }
}
