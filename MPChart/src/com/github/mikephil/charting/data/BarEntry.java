// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.data;

public class BarEntry extends Entry
{
    private float[] mVals;
    private float mNegativeSum;
    private float mPositiveSum;
    
    public BarEntry(final float[] vals, final int xIndex) {
        super(calcSum(vals), xIndex);
        this.mVals = vals;
        this.calcPosNegSum();
    }
    
    public BarEntry(final float val, final int xIndex) {
        super(val, xIndex);
    }
    
    public BarEntry(final float[] vals, final int xIndex, final String label) {
        super(calcSum(vals), xIndex, label);
        this.mVals = vals;
        this.calcPosNegSum();
    }
    
    public BarEntry(final float val, final int xIndex, final Object data) {
        super(val, xIndex, data);
    }
    
    @Override
    public BarEntry copy() {
        final BarEntry copied = new BarEntry(this.getVal(), this.getXIndex(), this.getData());
        copied.setVals(this.mVals);
        return copied;
    }
    
    public float[] getVals() {
        return this.mVals;
    }
    
    public void setVals(final float[] vals) {
        this.setVal(calcSum(vals));
        this.mVals = vals;
        this.calcPosNegSum();
    }
    
    @Override
    public float getVal() {
        return super.getVal();
    }
    
    public boolean isStacked() {
        return this.mVals != null;
    }
    
    public float getBelowSum(final int stackIndex) {
        if (this.mVals == null) {
            return 0.0f;
        }
        float remainder = 0.0f;
        for (int index = this.mVals.length - 1; index > stackIndex && index >= 0; --index) {
            remainder += this.mVals[index];
        }
        return remainder;
    }
    
    public float getPositiveSum() {
        return this.mPositiveSum;
    }
    
    public float getNegativeSum() {
        return this.mNegativeSum;
    }
    
    private void calcPosNegSum() {
        if (this.mVals == null) {
            this.mNegativeSum = 0.0f;
            this.mPositiveSum = 0.0f;
            return;
        }
        float sumNeg = 0.0f;
        float sumPos = 0.0f;
        float[] mVals;
        for (int length = (mVals = this.mVals).length, i = 0; i < length; ++i) {
            final float f = mVals[i];
            if (f <= 0.0f) {
                sumNeg += Math.abs(f);
            }
            else {
                sumPos += f;
            }
        }
        this.mNegativeSum = sumNeg;
        this.mPositiveSum = sumPos;
    }
    
    private static float calcSum(final float[] vals) {
        if (vals == null) {
            return 0.0f;
        }
        float sum = 0.0f;
        for (final float f : vals) {
            sum += f;
        }
        return sum;
    }
}
