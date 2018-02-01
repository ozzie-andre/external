// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;

public abstract class DataSet<T extends Entry> extends BaseDataSet<T>
{
    protected List<T> mYVals;
    protected float mYMax;
    protected float mYMin;
    
    public DataSet(final List<T> yVals, final String label) {
        super(label);
        this.mYVals = null;
        this.mYMax = 0.0f;
        this.mYMin = 0.0f;
        this.mYVals = yVals;
        if (this.mYVals == null) {
            this.mYVals = new ArrayList<T>();
        }
        this.calcMinMax(0, this.mYVals.size());
    }
    
    @Override
    public void calcMinMax(final int start, final int end) {
        if (this.mYVals == null) {
            return;
        }
        final int yValCount = this.mYVals.size();
        if (yValCount == 0) {
            return;
        }
        int endValue;
        if (end == 0 || end >= yValCount) {
            endValue = yValCount - 1;
        }
        else {
            endValue = end;
        }
        this.mYMin = Float.MAX_VALUE;
        this.mYMax = -3.4028235E38f;
        for (int i = start; i <= endValue; ++i) {
            final T e = this.mYVals.get(i);
            if (e != null && !Float.isNaN(e.getVal())) {
                if (e.getVal() < this.mYMin) {
                    this.mYMin = e.getVal();
                }
                if (e.getVal() > this.mYMax) {
                    this.mYMax = e.getVal();
                }
            }
        }
        if (this.mYMin == Float.MAX_VALUE) {
            this.mYMin = 0.0f;
            this.mYMax = 0.0f;
        }
    }
    
    @Override
    public int getEntryCount() {
        return this.mYVals.size();
    }
    
    public List<T> getYVals() {
        return this.mYVals;
    }
    
    public abstract DataSet<T> copy();
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.toSimpleString());
        for (int i = 0; i < this.mYVals.size(); ++i) {
            buffer.append(String.valueOf(this.mYVals.get(i).toString()) + " ");
        }
        return buffer.toString();
    }
    
    public String toSimpleString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("DataSet, label: " + ((this.getLabel() == null) ? "" : this.getLabel()) + ", entries: " + this.mYVals.size() + "\n");
        return buffer.toString();
    }
    
    @Override
    public float getYMin() {
        return this.mYMin;
    }
    
    @Override
    public float getYMax() {
        return this.mYMax;
    }
    
    @Override
    public void addEntryOrdered(final T e) {
        if (e == null) {
            return;
        }
        final float val = e.getVal();
        if (this.mYVals == null) {
            this.mYVals = new ArrayList<T>();
        }
        if (this.mYVals.size() == 0) {
            this.mYMax = val;
            this.mYMin = val;
        }
        else {
            if (this.mYMax < val) {
                this.mYMax = val;
            }
            if (this.mYMin > val) {
                this.mYMin = val;
            }
        }
        if (this.mYVals.size() > 0 && this.mYVals.get(this.mYVals.size() - 1).getXIndex() > e.getXIndex()) {
            final int closestIndex = this.getEntryIndex(e.getXIndex(), Rounding.UP);
            this.mYVals.add(closestIndex, e);
            return;
        }
        this.mYVals.add(e);
    }
    
    @Override
    public void clear() {
        this.mYVals.clear();
        this.notifyDataSetChanged();
    }
    
    @Override
    public boolean addEntry(final T e) {
        if (e == null) {
            return false;
        }
        final float val = e.getVal();
        List<T> yVals = this.getYVals();
        if (yVals == null) {
            yVals = new ArrayList<T>();
        }
        if (yVals.size() == 0) {
            this.mYMax = val;
            this.mYMin = val;
        }
        else {
            if (this.mYMax < val) {
                this.mYMax = val;
            }
            if (this.mYMin > val) {
                this.mYMin = val;
            }
        }
        yVals.add(e);
        return true;
    }
    
    @Override
    public boolean removeEntry(final T e) {
        if (e == null) {
            return false;
        }
        if (this.mYVals == null) {
            return false;
        }
        final boolean removed = this.mYVals.remove(e);
        if (removed) {
            this.calcMinMax(0, this.mYVals.size());
        }
        return removed;
    }
    
    @Override
    public int getEntryIndex(final Entry e) {
        return this.mYVals.indexOf(e);
    }
    
    @Override
    public T getEntryForXIndex(final int xIndex, final Rounding rounding) {
        final int index = this.getEntryIndex(xIndex, rounding);
        if (index > -1) {
            return this.mYVals.get(index);
        }
        return null;
    }
    
    @Override
    public T getEntryForXIndex(final int xIndex) {
        return this.getEntryForXIndex(xIndex, Rounding.CLOSEST);
    }
    
    @Override
    public T getEntryForIndex(final int index) {
        return this.mYVals.get(index);
    }
    
    @Override
    public int getEntryIndex(final int xIndex, final Rounding rounding) {
        int low = 0;
        int high = this.mYVals.size() - 1;
        int closest = -1;
        while (low <= high) {
            int m = (high + low) / 2;
            if (xIndex == this.mYVals.get(m).getXIndex()) {
                while (m > 0 && this.mYVals.get(m - 1).getXIndex() == xIndex) {
                    --m;
                }
                return m;
            }
            if (xIndex > this.mYVals.get(m).getXIndex()) {
                low = m + 1;
            }
            else {
                high = m - 1;
            }
            closest = m;
        }
        if (closest != -1) {
            final int closestXIndex = this.mYVals.get(closest).getXIndex();
            if (rounding == Rounding.UP) {
                if (closestXIndex < xIndex && closest < this.mYVals.size() - 1) {
                    ++closest;
                }
            }
            else if (rounding == Rounding.DOWN && closestXIndex > xIndex && closest > 0) {
                --closest;
            }
        }
        return closest;
    }
    
    @Override
    public float getYValForXIndex(final int xIndex) {
        final Entry e = this.getEntryForXIndex(xIndex);
        if (e != null && e.getXIndex() == xIndex) {
            return e.getVal();
        }
        return Float.NaN;
    }
    
    public List<T> getEntriesForXIndex(final int xIndex) {
        final List<T> entries = new ArrayList<T>();
        int low = 0;
        int high = this.mYVals.size() - 1;
        while (low <= high) {
            int m = (high + low) / 2;
            T entry = this.mYVals.get(m);
            if (xIndex == entry.getXIndex()) {
                while (m > 0 && this.mYVals.get(m - 1).getXIndex() == xIndex) {
                    --m;
                }
                for (high = this.mYVals.size(); m < high; ++m) {
                    entry = this.mYVals.get(m);
                    if (entry.getXIndex() != xIndex) {
                        break;
                    }
                    entries.add(entry);
                }
            }
            if (xIndex > entry.getXIndex()) {
                low = m + 1;
            }
            else {
                high = m - 1;
            }
        }
        return entries;
    }
    
    public enum Rounding
    {
        UP("UP", 0), 
        DOWN("DOWN", 1), 
        CLOSEST("CLOSEST", 2);
        
        private Rounding(final String s, final int n) {
        }
    }
}
