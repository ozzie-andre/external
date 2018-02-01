// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import android.content.Context;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.graphics.Color;
import java.util.ArrayList;
import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.components.YAxis;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T>
{
    protected List<Integer> mColors;
    protected List<Integer> mValueColors;
    private String mLabel;
    protected YAxis.AxisDependency mAxisDependency;
    protected boolean mHighlightEnabled;
    protected transient ValueFormatter mValueFormatter;
    protected Typeface mValueTypeface;
    protected boolean mDrawValues;
    protected float mValueTextSize;
    protected boolean mVisible;
    
    public BaseDataSet() {
        this.mColors = null;
        this.mValueColors = null;
        this.mLabel = "DataSet";
        this.mAxisDependency = YAxis.AxisDependency.LEFT;
        this.mHighlightEnabled = true;
        this.mDrawValues = true;
        this.mValueTextSize = 17.0f;
        this.mVisible = true;
        this.mColors = new ArrayList<Integer>();
        this.mValueColors = new ArrayList<Integer>();
        this.mColors.add(Color.rgb(140, 234, 255));
        this.mValueColors.add(-16777216);
    }
    
    public BaseDataSet(final String label) {
        this();
        this.mLabel = label;
    }
    
    public void notifyDataSetChanged() {
        this.calcMinMax(0, this.getEntryCount() - 1);
    }
    
    @Override
    public List<Integer> getColors() {
        return this.mColors;
    }
    
    public List<Integer> getValueColors() {
        return this.mValueColors;
    }
    
    @Override
    public int getColor() {
        return this.mColors.get(0);
    }
    
    @Override
    public int getColor(final int index) {
        return this.mColors.get(index % this.mColors.size());
    }
    
    public void setColors(final List<Integer> colors) {
        this.mColors = colors;
    }
    
    public void setColors(final int[] colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }
    
    public void setColors(final int[] colors, final Context c) {
        final List<Integer> clrs = new ArrayList<Integer>();
        for (final int color : colors) {
            clrs.add(c.getResources().getColor(color));
        }
        this.mColors = clrs;
    }
    
    public void addColor(final int color) {
        if (this.mColors == null) {
            this.mColors = new ArrayList<Integer>();
        }
        this.mColors.add(color);
    }
    
    public void setColor(final int color) {
        this.resetColors();
        this.mColors.add(color);
    }
    
    public void setColor(final int color, final int alpha) {
        this.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }
    
    public void setColors(final int[] colors, final int alpha) {
        this.resetColors();
        for (final int color : colors) {
            this.addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }
    
    public void resetColors() {
        this.mColors = new ArrayList<Integer>();
    }
    
    @Override
    public void setLabel(final String label) {
        this.mLabel = label;
    }
    
    @Override
    public String getLabel() {
        return this.mLabel;
    }
    
    @Override
    public void setHighlightEnabled(final boolean enabled) {
        this.mHighlightEnabled = enabled;
    }
    
    @Override
    public boolean isHighlightEnabled() {
        return this.mHighlightEnabled;
    }
    
    @Override
    public void setValueFormatter(final ValueFormatter f) {
        if (f == null) {
            return;
        }
        this.mValueFormatter = f;
    }
    
    @Override
    public ValueFormatter getValueFormatter() {
        if (this.mValueFormatter == null) {
            return new DefaultValueFormatter(1);
        }
        return this.mValueFormatter;
    }
    
    @Override
    public void setValueTextColor(final int color) {
        this.mValueColors.clear();
        this.mValueColors.add(color);
    }
    
    @Override
    public void setValueTextColors(final List<Integer> colors) {
        this.mValueColors = colors;
    }
    
    @Override
    public void setValueTypeface(final Typeface tf) {
        this.mValueTypeface = tf;
    }
    
    @Override
    public void setValueTextSize(final float size) {
        this.mValueTextSize = Utils.convertDpToPixel(size);
    }
    
    @Override
    public int getValueTextColor() {
        return this.mValueColors.get(0);
    }
    
    @Override
    public int getValueTextColor(final int index) {
        return this.mValueColors.get(index % this.mValueColors.size());
    }
    
    @Override
    public Typeface getValueTypeface() {
        return this.mValueTypeface;
    }
    
    @Override
    public float getValueTextSize() {
        return this.mValueTextSize;
    }
    
    @Override
    public void setDrawValues(final boolean enabled) {
        this.mDrawValues = enabled;
    }
    
    @Override
    public boolean isDrawValuesEnabled() {
        return this.mDrawValues;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        this.mVisible = visible;
    }
    
    @Override
    public boolean isVisible() {
        return this.mVisible;
    }
    
    @Override
    public YAxis.AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }
    
    @Override
    public void setAxisDependency(final YAxis.AxisDependency dependency) {
        this.mAxisDependency = dependency;
    }
    
    @Override
    public int getIndexInEntries(final int xIndex) {
        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (xIndex == this.getEntryForIndex(i).getXIndex()) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean removeFirst() {
        final T entry = this.getEntryForIndex(0);
        return this.removeEntry(entry);
    }
    
    @Override
    public boolean removeLast() {
        final T entry = this.getEntryForIndex(this.getEntryCount() - 1);
        return this.removeEntry(entry);
    }
    
    @Override
    public boolean removeEntry(final int xIndex) {
        final T e = this.getEntryForXIndex(xIndex);
        return this.removeEntry(e);
    }
    
    @Override
    public boolean contains(final T e) {
        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (this.getEntryForIndex(i).equals(e)) {
                return true;
            }
        }
        return false;
    }
}
