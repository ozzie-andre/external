// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;

public interface IDataSet<T extends Entry>
{
    float getYMin();
    
    float getYMax();
    
    int getEntryCount();
    
    void calcMinMax(final int p0, final int p1);
    
    T getEntryForXIndex(final int p0);
    
    T getEntryForXIndex(final int p0, final DataSet.Rounding p1);
    
    T getEntryForIndex(final int p0);
    
    int getEntryIndex(final int p0, final DataSet.Rounding p1);
    
    int getEntryIndex(final T p0);
    
    float getYValForXIndex(final int p0);
    
    int getIndexInEntries(final int p0);
    
    boolean addEntry(final T p0);
    
    boolean removeEntry(final T p0);
    
    void addEntryOrdered(final T p0);
    
    boolean removeFirst();
    
    boolean removeLast();
    
    boolean removeEntry(final int p0);
    
    boolean contains(final T p0);
    
    void clear();
    
    String getLabel();
    
    void setLabel(final String p0);
    
    YAxis.AxisDependency getAxisDependency();
    
    void setAxisDependency(final YAxis.AxisDependency p0);
    
    List<Integer> getColors();
    
    int getColor();
    
    int getColor(final int p0);
    
    boolean isHighlightEnabled();
    
    void setHighlightEnabled(final boolean p0);
    
    void setValueFormatter(final ValueFormatter p0);
    
    ValueFormatter getValueFormatter();
    
    void setValueTextColor(final int p0);
    
    void setValueTextColors(final List<Integer> p0);
    
    void setValueTypeface(final Typeface p0);
    
    void setValueTextSize(final float p0);
    
    int getValueTextColor();
    
    int getValueTextColor(final int p0);
    
    Typeface getValueTypeface();
    
    float getValueTextSize();
    
    void setDrawValues(final boolean p0);
    
    boolean isDrawValuesEnabled();
    
    void setVisible(final boolean p0);
    
    boolean isVisible();
}
