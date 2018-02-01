// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieDataSet extends DataSet<Entry> implements IPieDataSet
{
    private float mSliceSpace;
    private float mShift;
    
    public PieDataSet(final List<Entry> yVals, final String label) {
        super(yVals, label);
        this.mSliceSpace = 0.0f;
        this.mShift = 18.0f;
    }
    
    @Override
    public DataSet<Entry> copy() {
        final List<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < this.mYVals.size(); ++i) {
            yVals.add(this.mYVals.get(i).copy());
        }
        final PieDataSet copied = new PieDataSet(yVals, this.getLabel());
        copied.mColors = this.mColors;
        copied.mSliceSpace = this.mSliceSpace;
        copied.mShift = this.mShift;
        return copied;
    }
    
    public void setSliceSpace(float spaceDp) {
        if (spaceDp > 20.0f) {
            spaceDp = 20.0f;
        }
        if (spaceDp < 0.0f) {
            spaceDp = 0.0f;
        }
        this.mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }
    
    @Override
    public float getSliceSpace() {
        return this.mSliceSpace;
    }
    
    public void setSelectionShift(final float shift) {
        this.mShift = Utils.convertDpToPixel(shift);
    }
    
    @Override
    public float getSelectionShift() {
        return this.mShift;
    }
}
