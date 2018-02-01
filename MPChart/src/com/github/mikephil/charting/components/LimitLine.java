// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class LimitLine extends ComponentBase
{
    private float mLimit;
    private float mLineWidth;
    private int mLineColor;
    private Paint.Style mTextStyle;
    private String mLabel;
    private DashPathEffect mDashPathEffect;
    private LimitLabelPosition mLabelPosition;
    
    public LimitLine(final float limit) {
        this.mLimit = 0.0f;
        this.mLineWidth = 2.0f;
        this.mLineColor = Color.rgb(237, 91, 91);
        this.mTextStyle = Paint.Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = LimitLabelPosition.RIGHT_TOP;
        this.mLimit = limit;
    }
    
    public LimitLine(final float limit, final String label) {
        this.mLimit = 0.0f;
        this.mLineWidth = 2.0f;
        this.mLineColor = Color.rgb(237, 91, 91);
        this.mTextStyle = Paint.Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = LimitLabelPosition.RIGHT_TOP;
        this.mLimit = limit;
        this.mLabel = label;
    }
    
    public float getLimit() {
        return this.mLimit;
    }
    
    public void setLineWidth(float width) {
        if (width < 0.2f) {
            width = 0.2f;
        }
        if (width > 12.0f) {
            width = 12.0f;
        }
        this.mLineWidth = Utils.convertDpToPixel(width);
    }
    
    public float getLineWidth() {
        return this.mLineWidth;
    }
    
    public void setLineColor(final int color) {
        this.mLineColor = color;
    }
    
    public int getLineColor() {
        return this.mLineColor;
    }
    
    public void enableDashedLine(final float lineLength, final float spaceLength, final float phase) {
        this.mDashPathEffect = new DashPathEffect(new float[] { lineLength, spaceLength }, phase);
    }
    
    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }
    
    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }
    
    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }
    
    public void setTextStyle(final Paint.Style style) {
        this.mTextStyle = style;
    }
    
    public Paint.Style getTextStyle() {
        return this.mTextStyle;
    }
    
    public void setLabelPosition(final LimitLabelPosition pos) {
        this.mLabelPosition = pos;
    }
    
    public LimitLabelPosition getLabelPosition() {
        return this.mLabelPosition;
    }
    
    public void setLabel(final String label) {
        this.mLabel = label;
    }
    
    public String getLabel() {
        return this.mLabel;
    }
    
    public enum LimitLabelPosition
    {
        LEFT_TOP("LEFT_TOP", 0), 
        LEFT_BOTTOM("LEFT_BOTTOM", 1), 
        RIGHT_TOP("RIGHT_TOP", 2), 
        RIGHT_BOTTOM("RIGHT_BOTTOM", 3);
        
        private LimitLabelPosition(final String s, final int n) {
        }
    }
}
