// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.Entry;
import java.util.Comparator;

public class EntryXIndexComparator implements Comparator<Entry>
{
    @Override
    public int compare(final Entry entry1, final Entry entry2) {
        return entry1.getXIndex() - entry2.getXIndex();
    }
}
