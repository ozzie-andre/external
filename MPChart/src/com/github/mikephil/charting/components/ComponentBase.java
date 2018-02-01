// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.Typeface;

public abstract class ComponentBase
{
    protected boolean mEnabled;
    protected float mXOffset;
    protected float mYOffset;
    protected Typeface mTypeface;
    protected float mTextSize;
    protected int mTextColor;
    
    public ComponentBase() {
        this.mEnabled = true;
        this.mXOffset = 5.0f;
        this.mYOffset = 5.0f;
        this.mTypeface = null;
        this.mTextSize = 10.0f;
        this.mTextColor = -16777216;
    }
    
    public float getXOffset() {
        return this.mXOffset;
    }
    
    public void setXOffset(final float xOffset) {
        this.mXOffset = Utils.convertDpToPixel(xOffset);
    }
    
    public float getYOffset() {
        return this.mYOffset;
    }
    
    public void setYOffset(final float yOffset) {
        this.mYOffset = Utils.convertDpToPixel(yOffset);
    }
    
    public Typeface getTypeface() {
        return this.mTypeface;
    }
    
    public void setTypeface(final Typeface tf) {
        this.mTypeface = tf;
    }
    
    public void setTextSize(float size) {
        if (size > 24.0f) {
            size = 24.0f;
        }
        if (size < 6.0f) {
            size = 6.0f;
        }
        this.mTextSize = Utils.convertDpToPixel(size);
    }
    
    public float getTextSize() {
        return this.mTextSize;
    }
    
    public void setTextColor(final int color) {
        this.mTextColor = color;
    }
    
    public int getTextColor() {
        return this.mTextColor;
    }
    
    public void setEnabled(final boolean enabled) {
        this.mEnabled = enabled;
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
}
