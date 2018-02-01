// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

//import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;

public class BubbleData extends BarLineScatterCandleBubbleData<IBubbleDataSet>
{
    public BubbleData() {
    }
    
    public BubbleData(final List<String> xVals) {
        super(xVals);
    }
    
    public BubbleData(final String[] xVals) {
        super(xVals);
    }
    
    public BubbleData(final List<String> xVals, final List<IBubbleDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public BubbleData(final String[] xVals, final List<IBubbleDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public BubbleData(final List<String> xVals, final IBubbleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public BubbleData(final String[] xVals, final IBubbleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<IBubbleDataSet> toList(final IBubbleDataSet dataSet) {
        final List<IBubbleDataSet> sets = new ArrayList<IBubbleDataSet>();
        sets.add(dataSet);
        return sets;
    }
    
    public void setHighlightCircleWidth(final float width) {
        for (final IBubbleDataSet set : this.mDataSets) {
            set.setHighlightCircleWidth(width);
        }
    }
}
