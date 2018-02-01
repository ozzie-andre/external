// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public class SelectionDetail
{
    public float val;
    public int dataSetIndex;
    public IDataSet dataSet;
    
    public SelectionDetail(final float val, final int dataSetIndex, final IDataSet set) {
        this.val = val;
        this.dataSetIndex = dataSetIndex;
        this.dataSet = set;
    }
}
