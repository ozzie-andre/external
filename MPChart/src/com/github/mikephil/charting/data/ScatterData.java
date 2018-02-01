// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

//import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet>
{
    public ScatterData() {
    }
    
    public ScatterData(final List<String> xVals) {
        super(xVals);
    }
    
    public ScatterData(final String[] xVals) {
        super(xVals);
    }
    
    public ScatterData(final List<String> xVals, final List<IScatterDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public ScatterData(final String[] xVals, final List<IScatterDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public ScatterData(final List<String> xVals, final IScatterDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public ScatterData(final String[] xVals, final IScatterDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<IScatterDataSet> toList(final IScatterDataSet dataSet) {
        final List<IScatterDataSet> sets = new ArrayList<IScatterDataSet>();
        sets.add(dataSet);
        return sets;
    }
    
    public float getGreatestShapeSize() {
        float max = 0.0f;
        for (final IScatterDataSet set : this.mDataSets) {
            final float size = set.getScatterShapeSize();
            if (size > max) {
                max = size;
            }
        }
        return max;
    }
}
