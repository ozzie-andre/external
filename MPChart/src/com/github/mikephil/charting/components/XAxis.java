// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import java.util.ArrayList;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import java.util.List;

public class XAxis extends AxisBase
{
    protected List<String> mValues;
    public int mLabelWidth;
    public int mLabelHeight;
    public int mLabelRotatedWidth;
    public int mLabelRotatedHeight;
    protected float mLabelRotationAngle;
    private int mSpaceBetweenLabels;
    public int mAxisLabelModulus;
    private boolean mIsAxisModulusCustom;
    private boolean mAvoidFirstLastClipping;
    protected XAxisValueFormatter mXAxisValueFormatter;
    private XAxisPosition mPosition;
    
    public XAxis() {
        this.mValues = new ArrayList<String>();
        this.mLabelWidth = 1;
        this.mLabelHeight = 1;
        this.mLabelRotatedWidth = 1;
        this.mLabelRotatedHeight = 1;
        this.mLabelRotationAngle = 0.0f;
        this.mSpaceBetweenLabels = 4;
        this.mAxisLabelModulus = 1;
        this.mIsAxisModulusCustom = false;
        this.mAvoidFirstLastClipping = false;
        this.mXAxisValueFormatter = new DefaultXAxisValueFormatter();
        this.mPosition = XAxisPosition.TOP;
        this.mYOffset = Utils.convertDpToPixel(4.0f);
    }
    
    public XAxisPosition getPosition() {
        return this.mPosition;
    }
    
    public void setPosition(final XAxisPosition pos) {
        this.mPosition = pos;
    }
    
    public float getLabelRotationAngle() {
        return this.mLabelRotationAngle;
    }
    
    public void setLabelRotationAngle(final float angle) {
        this.mLabelRotationAngle = angle;
    }
    
    public void setSpaceBetweenLabels(final int spaceCharacters) {
        this.mSpaceBetweenLabels = spaceCharacters;
    }
    
    public void setLabelsToSkip(int count) {
        if (count < 0) {
            count = 0;
        }
        this.mIsAxisModulusCustom = true;
        this.mAxisLabelModulus = count + 1;
    }
    
    public void resetLabelsToSkip() {
        this.mIsAxisModulusCustom = false;
    }
    
    public boolean isAxisModulusCustom() {
        return this.mIsAxisModulusCustom;
    }
    
    public int getSpaceBetweenLabels() {
        return this.mSpaceBetweenLabels;
    }
    
    public void setAvoidFirstLastClipping(final boolean enabled) {
        this.mAvoidFirstLastClipping = enabled;
    }
    
    public boolean isAvoidFirstLastClippingEnabled() {
        return this.mAvoidFirstLastClipping;
    }
    
    public void setValues(final List<String> values) {
        this.mValues = values;
    }
    
    public List<String> getValues() {
        return this.mValues;
    }
    
    public void setValueFormatter(final XAxisValueFormatter formatter) {
        if (formatter == null) {
            this.mXAxisValueFormatter = new DefaultXAxisValueFormatter();
        }
        else {
            this.mXAxisValueFormatter = formatter;
        }
    }
    
    public XAxisValueFormatter getValueFormatter() {
        return this.mXAxisValueFormatter;
    }
    
    @Override
    public String getLongestLabel() {
        String longest = "";
        for (int i = 0; i < this.mValues.size(); ++i) {
            final String text = this.mValues.get(i);
            if (longest.length() < text.length()) {
                longest = text;
            }
        }
        return longest;
    }
    
    public enum XAxisPosition
    {
        TOP("TOP", 0), 
        BOTTOM("BOTTOM", 1), 
        BOTH_SIDED("BOTH_SIDED", 2), 
        TOP_INSIDE("TOP_INSIDE", 3), 
        BOTTOM_INSIDE("BOTTOM_INSIDE", 4);
        
        private XAxisPosition(final String s, final int n) {
        }
    }
}
