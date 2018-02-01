// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet>
{
    private float mGroupSpace;
    
    public BarData() {
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final List<String> xVals) {
        super(xVals);
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final String[] xVals) {
        super(xVals);
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final List<String> xVals, final List<IBarDataSet> dataSets) {
        super(xVals, dataSets);
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final String[] xVals, final List<IBarDataSet> dataSets) {
        super(xVals, dataSets);
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final List<String> xVals, final IBarDataSet dataSet) {
        super(xVals, toList(dataSet));
        this.mGroupSpace = 0.8f;
    }
    
    public BarData(final String[] xVals, final IBarDataSet dataSet) {
        super(xVals, toList(dataSet));
        this.mGroupSpace = 0.8f;
    }
    
    private static List<IBarDataSet> toList(final IBarDataSet dataSet) {
        final List<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(dataSet);
        return sets;
    }
    
    public float getGroupSpace() {
        if (this.mDataSets.size() <= 1) {
            return 0.0f;
        }
        return this.mGroupSpace;
    }
    
    public void setGroupSpace(final float percent) {
        this.mGroupSpace = percent / 100.0f;
    }
    
    public boolean isGrouped() {
        return this.mDataSets.size() > 1;
    }
}
