// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data.filter;

import java.util.ArrayList;
import com.github.mikephil.charting.data.Entry;
import java.util.List;

public class Approximator
{
    private ApproximatorType mType;
    private double mTolerance;
    private float mScaleRatio;
    private float mDeltaRatio;
    private boolean[] keep;
    
    public Approximator() {
        this.mType = ApproximatorType.DOUGLAS_PEUCKER;
        this.mTolerance = 0.0;
        this.mScaleRatio = 1.0f;
        this.mDeltaRatio = 1.0f;
        this.mType = ApproximatorType.NONE;
    }
    
    public Approximator(final ApproximatorType type, final double tolerance) {
        this.mType = ApproximatorType.DOUGLAS_PEUCKER;
        this.mTolerance = 0.0;
        this.mScaleRatio = 1.0f;
        this.mDeltaRatio = 1.0f;
        this.setup(type, tolerance);
    }
    
    public void setup(final ApproximatorType type, final double tolerance) {
        this.mType = type;
        this.mTolerance = tolerance;
    }
    
    public void setTolerance(final double tolerance) {
        this.mTolerance = tolerance;
    }
    
    public void setType(final ApproximatorType type) {
        this.mType = type;
    }
    
    public void setRatios(final float deltaRatio, final float scaleRatio) {
        this.mDeltaRatio = deltaRatio;
        this.mScaleRatio = scaleRatio;
    }
    
    public List<Entry> filter(final List<Entry> points) {
        return this.filter(points, this.mTolerance);
    }
    
    public List<Entry> filter(final List<Entry> points, final double tolerance) {
        if (tolerance <= 0.0) {
            return points;
        }
        this.keep = new boolean[points.size()];
        switch (this.mType) {
            case DOUGLAS_PEUCKER: {
                return this.reduceWithDouglasPeuker(points, tolerance);
            }
            case NONE: {
                return points;
            }
            default: {
                return points;
            }
        }
    }
    
    private List<Entry> reduceWithDouglasPeuker(final List<Entry> entries, final double epsilon) {
        if (epsilon <= 0.0 || entries.size() < 3) {
            return entries;
        }
        this.keep[0] = true;
        this.keep[entries.size() - 1] = true;
        this.algorithmDouglasPeucker(entries, epsilon, 0, entries.size() - 1);
        final List<Entry> reducedEntries = new ArrayList<Entry>();
        for (int i = 0; i < entries.size(); ++i) {
            if (this.keep[i]) {
                final Entry curEntry = entries.get(i);
                reducedEntries.add(new Entry(curEntry.getVal(), curEntry.getXIndex()));
            }
        }
        return reducedEntries;
    }
    
    private void algorithmDouglasPeucker(final List<Entry> entries, final double epsilon, final int start, final int end) {
        if (end <= start + 1) {
            return;
        }
        int maxDistIndex = 0;
        double distMax = 0.0;
        final Entry firstEntry = entries.get(start);
        final Entry lastEntry = entries.get(end);
        for (int i = start + 1; i < end; ++i) {
            final double dist = this.calcAngleBetweenLines(firstEntry, lastEntry, firstEntry, entries.get(i));
            if (dist > distMax) {
                distMax = dist;
                maxDistIndex = i;
            }
        }
        if (distMax > epsilon) {
            this.keep[maxDistIndex] = true;
            this.algorithmDouglasPeucker(entries, epsilon, start, maxDistIndex);
            this.algorithmDouglasPeucker(entries, epsilon, maxDistIndex, end);
        }
    }
    
    public double calcPointToLineDistance(final Entry startEntry, final Entry endEntry, final Entry entryPoint) {
        final float xDiffEndStart = endEntry.getXIndex() - startEntry.getXIndex();
        final float xDiffEntryStart = entryPoint.getXIndex() - startEntry.getXIndex();
        final double normalLength = Math.sqrt(xDiffEndStart * xDiffEndStart + (endEntry.getVal() - startEntry.getVal()) * (endEntry.getVal() - startEntry.getVal()));
        return Math.abs(xDiffEntryStart * (endEntry.getVal() - startEntry.getVal()) - (entryPoint.getVal() - startEntry.getVal()) * xDiffEndStart) / normalLength;
    }
    
    public double calcAngleBetweenLines(final Entry start1, final Entry end1, final Entry start2, final Entry end2) {
        final double angle1 = this.calcAngleWithRatios(start1, end1);
        final double angle2 = this.calcAngleWithRatios(start2, end2);
        return Math.abs(angle1 - angle2);
    }
    
    public double calcAngleWithRatios(final Entry p1, final Entry p2) {
        final float dx = p2.getXIndex() * this.mDeltaRatio - p1.getXIndex() * this.mDeltaRatio;
        final float dy = p2.getVal() * this.mScaleRatio - p1.getVal() * this.mScaleRatio;
        final double angle = Math.atan2(dy, dx) * 180.0 / 3.141592653589793;
        return angle;
    }
    
    public double calcAngle(final Entry p1, final Entry p2) {
        final float dx = p2.getXIndex() - p1.getXIndex();
        final float dy = p2.getVal() - p1.getVal();
        final double angle = Math.atan2(dy, dx) * 180.0 / 3.141592653589793;
        return angle;
    }
    
    public enum ApproximatorType
    {
        NONE("NONE", 0), 
        DOUGLAS_PEUCKER("DOUGLAS_PEUCKER", 1);
        
        private ApproximatorType(final String s, final int n) {
        }
    }
}
