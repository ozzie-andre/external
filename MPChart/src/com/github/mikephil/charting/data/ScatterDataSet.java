// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet
{
    private float mShapeSize;
    private ScatterChart.ScatterShape mScatterShape;
    private float mScatterShapeHoleRadius;
    private int mScatterShapeHoleColor;
    
    public ScatterDataSet(final List<Entry> yVals, final String label) {
        super(yVals, label);
        this.mShapeSize = 15.0f;
        this.mScatterShape = ScatterChart.ScatterShape.SQUARE;
        this.mScatterShapeHoleRadius = 0.0f;
        this.mScatterShapeHoleColor = 1122867;
    }
    
    @Override
    public DataSet<Entry> copy() {
        final List<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(this.mYVals.get(i).copy());
        }
        final ScatterDataSet copied = new ScatterDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mShapeSize = this.mShapeSize;
        copied.mScatterShape = this.mScatterShape;
        copied.mScatterShapeHoleRadius = this.mScatterShapeHoleRadius;
        copied.mScatterShapeHoleColor = this.mScatterShapeHoleColor;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
    }
    
    public void setScatterShapeSize(final float size) {
        this.mShapeSize = size;
    }
    
    @Override
    public float getScatterShapeSize() {
        return this.mShapeSize;
    }
    
    public void setScatterShape(final ScatterChart.ScatterShape shape) {
        this.mScatterShape = shape;
    }
    
    @Override
    public ScatterChart.ScatterShape getScatterShape() {
        return this.mScatterShape;
    }
    
    public void setScatterShapeHoleRadius(final float holeRadius) {
        this.mScatterShapeHoleRadius = holeRadius;
    }
    
    @Override
    public float getScatterShapeHoleRadius() {
        return this.mScatterShapeHoleRadius;
    }
    
    public void setScatterShapeHoleColor(final int holeColor) {
        this.mScatterShapeHoleColor = holeColor;
    }
    
    @Override
    public int getScatterShapeHoleColor() {
        return this.mScatterShapeHoleColor;
    }
}
