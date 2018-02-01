// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import android.graphics.Typeface;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
//import java.util.Iterator;
import com.github.mikephil.charting.components.YAxis;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

@SuppressWarnings({ "rawtypes", "unchecked"} )

public abstract class ChartData<T extends IDataSet<? extends Entry>>
{
    protected float mYMax;
    protected float mYMin;
    protected float mLeftAxisMax;
    protected float mLeftAxisMin;
    protected float mRightAxisMax;
    protected float mRightAxisMin;
    private int mYValCount;
    private float mXValMaximumLength;
    protected List<String> mXVals;
    protected List<T> mDataSets;
    
    public ChartData() {
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mLeftAxisMax = 0.0f;
        this.mLeftAxisMin = 0.0f;
        this.mRightAxisMax = 0.0f;
        this.mRightAxisMin = 0.0f;
        this.mYValCount = 0;
        this.mXValMaximumLength = 0.0f;
        this.mXVals = new ArrayList<String>();
        this.mDataSets = new ArrayList<T>();
    }
    
    public ChartData(final List<String> xVals) {
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mLeftAxisMax = 0.0f;
        this.mLeftAxisMin = 0.0f;
        this.mRightAxisMax = 0.0f;
        this.mRightAxisMin = 0.0f;
        this.mYValCount = 0;
        this.mXValMaximumLength = 0.0f;
        this.mXVals = xVals;
        this.mDataSets = new ArrayList<T>();
        this.init();
    }
    
    public ChartData(final String[] xVals) {
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mLeftAxisMax = 0.0f;
        this.mLeftAxisMin = 0.0f;
        this.mRightAxisMax = 0.0f;
        this.mRightAxisMin = 0.0f;
        this.mYValCount = 0;
        this.mXValMaximumLength = 0.0f;
        this.mXVals = this.arrayToList(xVals);
        this.mDataSets = new ArrayList<T>();
        this.init();
    }
    
    public ChartData(final List<String> xVals, final List<T> sets) {
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mLeftAxisMax = 0.0f;
        this.mLeftAxisMin = 0.0f;
        this.mRightAxisMax = 0.0f;
        this.mRightAxisMin = 0.0f;
        this.mYValCount = 0;
        this.mXValMaximumLength = 0.0f;
        this.mXVals = xVals;
        this.mDataSets = sets;
        this.init();
    }
    
    public ChartData(final String[] xVals, final List<T> sets) {
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mLeftAxisMax = 0.0f;
        this.mLeftAxisMin = 0.0f;
        this.mRightAxisMax = 0.0f;
        this.mRightAxisMin = 0.0f;
        this.mYValCount = 0;
        this.mXValMaximumLength = 0.0f;
        this.mXVals = this.arrayToList(xVals);
        this.mDataSets = sets;
        this.init();
    }
    
    private List<String> arrayToList(final String[] array) {
        return Arrays.asList(array);
    }
    
    protected void init() {
        this.checkLegal();
        this.calcYValueCount();
        this.calcMinMax(0, this.mYValCount);
        this.calcXValMaximumLength();
    }
    
    private void calcXValMaximumLength() {
        if (this.mXVals.size() <= 0) {
            this.mXValMaximumLength = 1.0f;
            return;
        }
        int max = 1;
        for (int i = 0; i < this.mXVals.size(); ++i) {
            final int length = this.mXVals.get(i).length();
            if (length > max) {
                max = length;
            }
        }
        this.mXValMaximumLength = max;
    }
    
    private void checkLegal() {
        if (this.mDataSets == null) {
            return;
        }
        if (this instanceof ScatterData || this instanceof CombinedData) {
            return;
        }
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            if (this.mDataSets.get(i).getEntryCount() > this.mXVals.size()) {
                throw new IllegalArgumentException("One or more of the DataSet Entry arrays are longer than the x-values array of this ChartData object.");
            }
        }
    }
    
    public void notifyDataChanged() {
        this.init();
    }
    
	public void calcMinMax(final int start, final int end) {
        if (this.mDataSets == null || this.mDataSets.size() < 1) {
            this.mYMax = 0.0f;
            this.mYMin = 0.0f;
        }
        else {
            this.mYMin = Float.MAX_VALUE;
            this.mYMax = -3.4028235E38f;
            for (int i = 0; i < this.mDataSets.size(); ++i) {
                final IDataSet set = this.mDataSets.get(i);
                set.calcMinMax(start, end);
                if (set.getYMin() < this.mYMin) {
                    this.mYMin = set.getYMin();
                }
                if (set.getYMax() > this.mYMax) {
                    this.mYMax = set.getYMax();
                }
            }
            if (this.mYMin == Float.MAX_VALUE) {
                this.mYMin = 0.0f;
                this.mYMax = 0.0f;
            }
            final T firstLeft = this.getFirstLeft();
            if (firstLeft != null) {
                this.mLeftAxisMax = firstLeft.getYMax();
                this.mLeftAxisMin = firstLeft.getYMin();
                for (final IDataSet dataSet : this.mDataSets) {
                    if (dataSet.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                        if (dataSet.getYMin() < this.mLeftAxisMin) {
                            this.mLeftAxisMin = dataSet.getYMin();
                        }
                        if (dataSet.getYMax() <= this.mLeftAxisMax) {
                            continue;
                        }
                        this.mLeftAxisMax = dataSet.getYMax();
                    }
                }
            }
            final T firstRight = this.getFirstRight();
            if (firstRight != null) {
                this.mRightAxisMax = firstRight.getYMax();
                this.mRightAxisMin = firstRight.getYMin();
                for (final IDataSet dataSet2 : this.mDataSets) {
                    if (dataSet2.getAxisDependency() == YAxis.AxisDependency.RIGHT) {
                        if (dataSet2.getYMin() < this.mRightAxisMin) {
                            this.mRightAxisMin = dataSet2.getYMin();
                        }
                        if (dataSet2.getYMax() <= this.mRightAxisMax) {
                            continue;
                        }
                        this.mRightAxisMax = dataSet2.getYMax();
                    }
                }
            }
            this.handleEmptyAxis(firstLeft, firstRight);
        }
    }
    
    protected void calcYValueCount() {
        this.mYValCount = 0;
        if (this.mDataSets == null) {
            return;
        }
        int count = 0;
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            count += this.mDataSets.get(i).getEntryCount();
        }
        this.mYValCount = count;
    }
    
    public int getDataSetCount() {
        if (this.mDataSets == null) {
            return 0;
        }
        return this.mDataSets.size();
    }
    
    public float getYMin() {
        return this.mYMin;
    }
    
    public float getYMin(final YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisMin;
        }
        return this.mRightAxisMin;
    }
    
    public float getYMax() {
        return this.mYMax;
    }
    
    public float getYMax(final YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisMax;
        }
        return this.mRightAxisMax;
    }
    
    public float getXValMaximumLength() {
        return this.mXValMaximumLength;
    }
    
    public int getYValCount() {
        return this.mYValCount;
    }
    
    public List<String> getXVals() {
        return this.mXVals;
    }
    
    public void addXValue(final String xVal) {
        if (xVal != null && xVal.length() > this.mXValMaximumLength) {
            this.mXValMaximumLength = xVal.length();
        }
        this.mXVals.add(xVal);
    }
    
    public void removeXValue(final int index) {
        this.mXVals.remove(index);
    }
    
    public List<T> getDataSets() {
        return this.mDataSets;
    }
    
    protected int getDataSetIndexByLabel(final List<T> dataSets, final String label, final boolean ignorecase) {
        if (ignorecase) {
            for (int i = 0; i < dataSets.size(); ++i) {
                if (label.equalsIgnoreCase(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < dataSets.size(); ++i) {
                if (label.equals(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int getXValCount() {
        return this.mXVals.size();
    }
    
    protected String[] getDataSetLabels() {
        final String[] types = new String[this.mDataSets.size()];
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            types[i] = this.mDataSets.get(i).getLabel();
        }
        return types;
    }
    
    public Entry getEntryForHighlight(final Highlight highlight) {
        if (highlight.getDataSetIndex() >= this.mDataSets.size()) {
            return null;
        }
        return ((IDataSet<Entry>)this.mDataSets.get(highlight.getDataSetIndex())).getEntryForXIndex(highlight.getXIndex());
    }
    
    public T getDataSetByLabel(final String label, final boolean ignorecase) {
        final int index = this.getDataSetIndexByLabel(this.mDataSets, label, ignorecase);
        if (index < 0 || index >= this.mDataSets.size()) {
            return null;
        }
        return this.mDataSets.get(index);
    }
    
    public T getDataSetByIndex(final int index) {
        if (this.mDataSets == null || index < 0 || index >= this.mDataSets.size()) {
            return null;
        }
        return this.mDataSets.get(index);
    }
    
    public void addDataSet(final T d) {
        if (d == null) {
            return;
        }
        this.mYValCount += d.getEntryCount();
        if (this.mDataSets.size() <= 0) {
            this.mYMax = d.getYMax();
            this.mYMin = d.getYMin();
            if (d.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                this.mLeftAxisMax = d.getYMax();
                this.mLeftAxisMin = d.getYMin();
            }
            else {
                this.mRightAxisMax = d.getYMax();
                this.mRightAxisMin = d.getYMin();
            }
        }
        else {
            if (this.mYMax < d.getYMax()) {
                this.mYMax = d.getYMax();
            }
            if (this.mYMin > d.getYMin()) {
                this.mYMin = d.getYMin();
            }
            if (d.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                if (this.mLeftAxisMax < d.getYMax()) {
                    this.mLeftAxisMax = d.getYMax();
                }
                if (this.mLeftAxisMin > d.getYMin()) {
                    this.mLeftAxisMin = d.getYMin();
                }
            }
            else {
                if (this.mRightAxisMax < d.getYMax()) {
                    this.mRightAxisMax = d.getYMax();
                }
                if (this.mRightAxisMin > d.getYMin()) {
                    this.mRightAxisMin = d.getYMin();
                }
            }
        }
        this.mDataSets.add(d);
        this.handleEmptyAxis(this.getFirstLeft(), this.getFirstRight());
    }
    
    private void handleEmptyAxis(final T firstLeft, final T firstRight) {
        if (firstLeft == null) {
            this.mLeftAxisMax = this.mRightAxisMax;
            this.mLeftAxisMin = this.mRightAxisMin;
        }
        else if (firstRight == null) {
            this.mRightAxisMax = this.mLeftAxisMax;
            this.mRightAxisMin = this.mLeftAxisMin;
        }
    }
    
    public boolean removeDataSet(final T d) {
        if (d == null) {
            return false;
        }
        final boolean removed = this.mDataSets.remove(d);
        if (removed) {
            this.calcMinMax(0, this.mYValCount -= d.getEntryCount());
        }
        return removed;
    }
    
    public boolean removeDataSet(final int index) {
        if (index >= this.mDataSets.size() || index < 0) {
            return false;
        }
        final T set = this.mDataSets.get(index);
        return this.removeDataSet(set);
    }
    
 	public void addEntry(final Entry e, final int dataSetIndex) {
        if (this.mDataSets.size() > dataSetIndex && dataSetIndex >= 0) {
            final IDataSet set = this.mDataSets.get(dataSetIndex);
            if (!set.addEntry(e)) {
                return;
            }
            final float val = e.getVal();
            if (this.mYValCount == 0) {
                this.mYMin = val;
                this.mYMax = val;
                if (set.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                    this.mLeftAxisMax = e.getVal();
                    this.mLeftAxisMin = e.getVal();
                }
                else {
                    this.mRightAxisMax = e.getVal();
                    this.mRightAxisMin = e.getVal();
                }
            }
            else {
                if (this.mYMax < val) {
                    this.mYMax = val;
                }
                if (this.mYMin > val) {
                    this.mYMin = val;
                }
                if (set.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                    if (this.mLeftAxisMax < e.getVal()) {
                        this.mLeftAxisMax = e.getVal();
                    }
                    if (this.mLeftAxisMin > e.getVal()) {
                        this.mLeftAxisMin = e.getVal();
                    }
                }
                else {
                    if (this.mRightAxisMax < e.getVal()) {
                        this.mRightAxisMax = e.getVal();
                    }
                    if (this.mRightAxisMin > e.getVal()) {
                        this.mRightAxisMin = e.getVal();
                    }
                }
            }
            ++this.mYValCount;
            this.handleEmptyAxis(this.getFirstLeft(), this.getFirstRight());
        }
        else {
            Log.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.");
        }
    }
    
    public boolean removeEntry(final Entry e, final int dataSetIndex) {
        if (e == null || dataSetIndex >= this.mDataSets.size()) {
            return false;
        }
        final IDataSet set = this.mDataSets.get(dataSetIndex);
        if (set != null) {
            final boolean removed = set.removeEntry(e);
            if (removed) {
                this.calcMinMax(0, --this.mYValCount);
            }
            return removed;
        }
        return false;
    }
    
    public boolean removeEntry(final int xIndex, final int dataSetIndex) {
        if (dataSetIndex >= this.mDataSets.size()) {
            return false;
        }
        final IDataSet dataSet = this.mDataSets.get(dataSetIndex);
        final Entry e = dataSet.getEntryForXIndex(xIndex);
        return e != null && e.getXIndex() == xIndex && this.removeEntry(e, dataSetIndex);
    }
    
    public T getDataSetForEntry(final Entry e) {
        if (e == null) {
            return null;
        }
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            final T set = this.mDataSets.get(i);
            for (int j = 0; j < set.getEntryCount(); ++j) {
                if (e.equalTo(((IDataSet<Entry>)set).getEntryForXIndex(e.getXIndex()))) {
                    return set;
                }
            }
        }
        return null;
    }
    
    public int[] getColors() {
        if (this.mDataSets == null) {
            return null;
        }
        int clrcnt = 0;
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            clrcnt += this.mDataSets.get(i).getColors().size();
        }
        final int[] colors = new int[clrcnt];
        int cnt = 0;
        for (int j = 0; j < this.mDataSets.size(); ++j) {
            final List<Integer> clrs = this.mDataSets.get(j).getColors();
            for (final Integer clr : clrs) {
                colors[cnt] = clr;
                ++cnt;
            }
        }
        return colors;
    }
    
    public int getIndexOfDataSet(final T dataSet) {
        for (int i = 0; i < this.mDataSets.size(); ++i) {
            if (this.mDataSets.get(i) == dataSet) {
                return i;
            }
        }
        return -1;
    }
    
    public T getFirstLeft() {
        for (final T dataSet : this.mDataSets) {
            if (dataSet.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                return dataSet;
            }
        }
        return null;
    }
    
    public T getFirstRight() {
        for (final T dataSet : this.mDataSets) {
            if (dataSet.getAxisDependency() == YAxis.AxisDependency.RIGHT) {
                return dataSet;
            }
        }
        return null;
    }
    
    public static List<String> generateXVals(final int from, final int to) {
        final List<String> xvals = new ArrayList<String>();
        for (int i = from; i < to; ++i) {
            xvals.add(new StringBuilder().append(i).toString());
        }
        return xvals;
    }
    
    public void setValueFormatter(final ValueFormatter f) {
        if (f == null) {
            return;
        }
        for (final IDataSet set : this.mDataSets) {
            set.setValueFormatter(f);
        }
    }
    
    public void setValueTextColor(final int color) {
        for (final IDataSet set : this.mDataSets) {
            set.setValueTextColor(color);
        }
    }
    
    public void setValueTextColors(final List<Integer> colors) {
        for (final IDataSet set : this.mDataSets) {
            set.setValueTextColors(colors);
        }
    }
    
    public void setValueTypeface(final Typeface tf) {
        for (final IDataSet set : this.mDataSets) {
            set.setValueTypeface(tf);
        }
    }
    
    public void setValueTextSize(final float size) {
        for (final IDataSet set : this.mDataSets) {
            set.setValueTextSize(size);
        }
    }
    
    public void setDrawValues(final boolean enabled) {
        for (final IDataSet set : this.mDataSets) {
            set.setDrawValues(enabled);
        }
    }
    
    public void setHighlightEnabled(final boolean enabled) {
        for (final IDataSet set : this.mDataSets) {
            set.setHighlightEnabled(enabled);
        }
    }
    
    public boolean isHighlightEnabled() {
        for (final IDataSet set : this.mDataSets) {
            if (!set.isHighlightEnabled()) {
                return false;
            }
        }
        return true;
    }
    
    public void clearValues() {
        this.mDataSets.clear();
        this.notifyDataChanged();
    }
    
    public boolean contains(final T dataSet) {
        for (final T set : this.mDataSets) {
            if (set.equals(dataSet)) {
                return true;
            }
        }
        return false;
    }
}
