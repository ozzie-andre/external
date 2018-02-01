// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

public class RadarData extends ChartData<IRadarDataSet>
{
    public RadarData() {
    }
    
    public RadarData(final List<String> xVals) {
        super(xVals);
    }
    
    public RadarData(final String[] xVals) {
        super(xVals);
    }
    
    public RadarData(final List<String> xVals, final List<IRadarDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public RadarData(final String[] xVals, final List<IRadarDataSet> dataSets) {
        super(xVals, dataSets);
    }
    
    public RadarData(final List<String> xVals, final IRadarDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    public RadarData(final String[] xVals, final IRadarDataSet dataSet) {
        super(xVals, toList(dataSet));
    }
    
    private static List<IRadarDataSet> toList(final IRadarDataSet dataSet) {
        final List<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(dataSet);
        return sets;
    }
}
