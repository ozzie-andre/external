// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;

public interface OnDrawListener
{
    void onEntryAdded(final Entry p0);
    
    void onEntryMoved(final Entry p0);
    
    void onDrawFinished(final DataSet<?> p0);
}
