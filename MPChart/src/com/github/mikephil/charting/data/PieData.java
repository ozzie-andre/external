// 
//  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieData extends ChartData<IPieDataSet>
{
    public PieData() {
    }
    
    public PieData(final List<String> xVals) {
        super(xVals);
    }
    
    public PieData(final String[] xVals) {
        super(xVals);
    }
    
    public PieData(final List<String> xVals, final IPieDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public PieData(final String[] xVals, final IPieDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<IPieDataSet> toList(final IPieDataSet dataSet) {
        final List<IPieDataSet> sets = new ArrayList<IPieDataSet>();
        sets.add(dataSet);
        return sets;
    }
    
    public void setDataSet(final IPieDataSet dataSet) {
        this.mDataSets.clear();
        //this.mDataSets.add((T)dataSet);
        this.mDataSets.add(dataSet);
        this.init();
    }
    
    public IPieDataSet getDataSet() {
        return (IPieDataSet)this.mDataSets.get(0);
    }
    
    @Override
    public IPieDataSet getDataSetByIndex(final int index) {
        return (index == 0) ? this.getDataSet() : null;
    }
    
    @Override
    public IPieDataSet getDataSetByLabel(final String label, final boolean ignorecase) {
        return ignorecase ? (label.equalsIgnoreCase(((IPieDataSet)this.mDataSets.get(0)).getLabel()) ? ((IPieDataSet)this.mDataSets.get(0)) : null) : (label.equals(((IPieDataSet)this.mDataSets.get(0)).getLabel()) ? ((IPieDataSet)this.mDataSets.get(0)) : null);
    }
    
    public float getYValueSum() {
        float sum = 0.0f;
        for (int i = 0; i < this.getDataSet().getEntryCount(); ++i) {
            sum += this.getDataSet().getEntryForIndex(i).getVal();
        }
        return sum;
    }
}
