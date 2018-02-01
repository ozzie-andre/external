// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import java.util.ArrayList;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import java.util.List;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.FSize;

public class Legend extends ComponentBase
{
    private int[] mColors;
    private String[] mLabels;
    private int[] mExtraColors;
    private String[] mExtraLabels;
    private boolean mIsLegendCustom;
    private LegendPosition mPosition;
    private LegendDirection mDirection;
    private LegendForm mShape;
    private float mFormSize;
    private float mXEntrySpace;
    private float mYEntrySpace;
    private float mFormToTextSpace;
    private float mStackSpace;
    private float mMaxSizePercent;
    public float mNeededWidth;
    public float mNeededHeight;
    public float mTextHeightMax;
    public float mTextWidthMax;
    private boolean mWordWrapEnabled;
    private FSize[] mCalculatedLabelSizes;
    private Boolean[] mCalculatedLabelBreakPoints;
    private FSize[] mCalculatedLineSizes;
    
    public Legend() {
        this.mIsLegendCustom = false;
        this.mPosition = LegendPosition.BELOW_CHART_LEFT;
        this.mDirection = LegendDirection.LEFT_TO_RIGHT;
        this.mShape = LegendForm.SQUARE;
        this.mFormSize = 8.0f;
        this.mXEntrySpace = 6.0f;
        this.mYEntrySpace = 0.0f;
        this.mFormToTextSpace = 5.0f;
        this.mStackSpace = 3.0f;
        this.mMaxSizePercent = 0.95f;
        this.mNeededWidth = 0.0f;
        this.mNeededHeight = 0.0f;
        this.mTextHeightMax = 0.0f;
        this.mTextWidthMax = 0.0f;
        this.mWordWrapEnabled = false;
        this.mCalculatedLabelSizes = new FSize[0];
        this.mCalculatedLabelBreakPoints = new Boolean[0];
        this.mCalculatedLineSizes = new FSize[0];
        this.mFormSize = Utils.convertDpToPixel(8.0f);
        this.mXEntrySpace = Utils.convertDpToPixel(6.0f);
        this.mYEntrySpace = Utils.convertDpToPixel(0.0f);
        this.mFormToTextSpace = Utils.convertDpToPixel(5.0f);
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mStackSpace = Utils.convertDpToPixel(3.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(4.0f);
    }
    
    public Legend(final int[] colors, final String[] labels) {
        this();
        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        }
        if (colors.length != labels.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mColors = colors;
        this.mLabels = labels;
    }
    
    public Legend(final List<Integer> colors, final List<String> labels) {
        this();
        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        }
        if (colors.size() != labels.size()) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mColors = Utils.convertIntegers(colors);
        this.mLabels = Utils.convertStrings(labels);
    }
    
    public void setComputedColors(final List<Integer> colors) {
        this.mColors = Utils.convertIntegers(colors);
    }
    
    public void setComputedLabels(final List<String> labels) {
        this.mLabels = Utils.convertStrings(labels);
    }
    
    public float getMaximumEntryWidth(final Paint p) {
        float max = 0.0f;
        for (int i = 0; i < this.mLabels.length; ++i) {
            if (this.mLabels[i] != null) {
                final float length = Utils.calcTextWidth(p, this.mLabels[i]);
                if (length > max) {
                    max = length;
                }
            }
        }
        return max + this.mFormSize + this.mFormToTextSpace;
    }
    
    public float getMaximumEntryHeight(final Paint p) {
        float max = 0.0f;
        for (int i = 0; i < this.mLabels.length; ++i) {
            if (this.mLabels[i] != null) {
                final float length = Utils.calcTextHeight(p, this.mLabels[i]);
                if (length > max) {
                    max = length;
                }
            }
        }
        return max;
    }
    
    public int[] getColors() {
        return this.mColors;
    }
    
    public String[] getLabels() {
        return this.mLabels;
    }
    
    public String getLabel(final int index) {
        return this.mLabels[index];
    }
    
    public int[] getExtraColors() {
        return this.mExtraColors;
    }
    
    public String[] getExtraLabels() {
        return this.mExtraLabels;
    }
    
    public void setExtra(final List<Integer> colors, final List<String> labels) {
        this.mExtraColors = Utils.convertIntegers(colors);
        this.mExtraLabels = Utils.convertStrings(labels);
    }
    
    public void setExtra(final int[] colors, final String[] labels) {
        this.mExtraColors = colors;
        this.mExtraLabels = labels;
    }
    
    public void setCustom(final int[] colors, final String[] labels) {
        if (colors.length != labels.length) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mLabels = labels;
        this.mColors = colors;
        this.mIsLegendCustom = true;
    }
    
    public void setCustom(final List<Integer> colors, final List<String> labels) {
        if (colors.size() != labels.size()) {
            throw new IllegalArgumentException("colors array and labels array need to be of same size");
        }
        this.mColors = Utils.convertIntegers(colors);
        this.mLabels = Utils.convertStrings(labels);
        this.mIsLegendCustom = true;
    }
    
    public void resetCustom() {
        this.mIsLegendCustom = false;
    }
    
    public boolean isLegendCustom() {
        return this.mIsLegendCustom;
    }
    
    public LegendPosition getPosition() {
        return this.mPosition;
    }
    
    public void setPosition(final LegendPosition pos) {
        this.mPosition = pos;
    }
    
    public LegendDirection getDirection() {
        return this.mDirection;
    }
    
    public void setDirection(final LegendDirection pos) {
        this.mDirection = pos;
    }
    
    public LegendForm getForm() {
        return this.mShape;
    }
    
    public void setForm(final LegendForm shape) {
        this.mShape = shape;
    }
    
    public void setFormSize(final float size) {
        this.mFormSize = Utils.convertDpToPixel(size);
    }
    
    public float getFormSize() {
        return this.mFormSize;
    }
    
    public float getXEntrySpace() {
        return this.mXEntrySpace;
    }
    
    public void setXEntrySpace(final float space) {
        this.mXEntrySpace = Utils.convertDpToPixel(space);
    }
    
    public float getYEntrySpace() {
        return this.mYEntrySpace;
    }
    
    public void setYEntrySpace(final float space) {
        this.mYEntrySpace = Utils.convertDpToPixel(space);
    }
    
    public float getFormToTextSpace() {
        return this.mFormToTextSpace;
    }
    
    public void setFormToTextSpace(final float space) {
        this.mFormToTextSpace = Utils.convertDpToPixel(space);
    }
    
    public float getStackSpace() {
        return this.mStackSpace;
    }
    
    public void setStackSpace(final float space) {
        this.mStackSpace = space;
    }
    
    public float getFullWidth(final Paint labelpaint) {
        float width = 0.0f;
        for (int i = 0; i < this.mLabels.length; ++i) {
            if (this.mLabels[i] != null) {
                if (this.mColors[i] != 1122868) {
                    width += this.mFormSize + this.mFormToTextSpace;
                }
                width += Utils.calcTextWidth(labelpaint, this.mLabels[i]);
                if (i < this.mLabels.length - 1) {
                    width += this.mXEntrySpace;
                }
            }
            else {
                width += this.mFormSize;
                if (i < this.mLabels.length - 1) {
                    width += this.mStackSpace;
                }
            }
        }
        return width;
    }
    
    public float getFullHeight(final Paint labelpaint) {
        float height = 0.0f;
        for (int i = 0; i < this.mLabels.length; ++i) {
            if (this.mLabels[i] != null) {
                height += Utils.calcTextHeight(labelpaint, this.mLabels[i]);
                if (i < this.mLabels.length - 1) {
                    height += this.mYEntrySpace;
                }
            }
        }
        return height;
    }
    
    public void setWordWrapEnabled(final boolean enabled) {
        this.mWordWrapEnabled = enabled;
    }
    
    public boolean isWordWrapEnabled() {
        return this.mWordWrapEnabled;
    }
    
    public float getMaxSizePercent() {
        return this.mMaxSizePercent;
    }
    
    public void setMaxSizePercent(final float maxSize) {
        this.mMaxSizePercent = maxSize;
    }
    
    public FSize[] getCalculatedLabelSizes() {
        return this.mCalculatedLabelSizes;
    }
    
    public Boolean[] getCalculatedLabelBreakPoints() {
        return this.mCalculatedLabelBreakPoints;
    }
    
    public FSize[] getCalculatedLineSizes() {
        return this.mCalculatedLineSizes;
    }
    
    public void calculateDimensions(final Paint labelpaint, final ViewPortHandler viewPortHandler) {
        if (this.mPosition == LegendPosition.RIGHT_OF_CHART || this.mPosition == LegendPosition.RIGHT_OF_CHART_CENTER || this.mPosition == LegendPosition.LEFT_OF_CHART || this.mPosition == LegendPosition.LEFT_OF_CHART_CENTER || this.mPosition == LegendPosition.PIECHART_CENTER) {
            this.mNeededWidth = this.getMaximumEntryWidth(labelpaint);
            this.mNeededHeight = this.getFullHeight(labelpaint);
            this.mTextWidthMax = this.mNeededWidth;
            this.mTextHeightMax = this.getMaximumEntryHeight(labelpaint);
        }
        else if (this.mPosition == LegendPosition.BELOW_CHART_LEFT || this.mPosition == LegendPosition.BELOW_CHART_RIGHT || this.mPosition == LegendPosition.BELOW_CHART_CENTER || this.mPosition == LegendPosition.ABOVE_CHART_LEFT || this.mPosition == LegendPosition.ABOVE_CHART_RIGHT || this.mPosition == LegendPosition.ABOVE_CHART_CENTER) {
            final int labelCount = this.mLabels.length;
            final float labelLineHeight = Utils.getLineHeight(labelpaint);
            final float labelLineSpacing = Utils.getLineSpacing(labelpaint) + this.mYEntrySpace;
            final float contentWidth = viewPortHandler.contentWidth();
            final ArrayList<FSize> calculatedLabelSizes = new ArrayList<FSize>(labelCount);
            final ArrayList<Boolean> calculatedLabelBreakPoints = new ArrayList<Boolean>(labelCount);
            final ArrayList<FSize> calculatedLineSizes = new ArrayList<FSize>();
            float maxLineWidth = 0.0f;
            float currentLineWidth = 0.0f;
            float requiredWidth = 0.0f;
            int stackedStartIndex = -1;
            for (int i = 0; i < labelCount; ++i) {
                final boolean drawingForm = this.mColors[i] != 1122868;
                calculatedLabelBreakPoints.add(false);
                if (stackedStartIndex == -1) {
                    requiredWidth = 0.0f;
                }
                else {
                    requiredWidth += this.mStackSpace;
                }
                if (this.mLabels[i] != null) {
                    calculatedLabelSizes.add(Utils.calcTextSize(labelpaint, this.mLabels[i]));
                    requiredWidth += (drawingForm ? (this.mFormToTextSpace + this.mFormSize) : 0.0f);
                    requiredWidth += calculatedLabelSizes.get(i).width;
                }
                else {
                    calculatedLabelSizes.add(new FSize(0.0f, 0.0f));
                    requiredWidth += (drawingForm ? this.mFormSize : 0.0f);
                    if (stackedStartIndex == -1) {
                        stackedStartIndex = i;
                    }
                }
                if (this.mLabels[i] != null || i == labelCount - 1) {
                    final float requiredSpacing = (currentLineWidth == 0.0f) ? 0.0f : this.mXEntrySpace;
                    if (!this.mWordWrapEnabled || currentLineWidth == 0.0f || contentWidth - currentLineWidth >= requiredSpacing + requiredWidth) {
                        currentLineWidth += requiredSpacing + requiredWidth;
                    }
                    else {
                        calculatedLineSizes.add(new FSize(currentLineWidth, labelLineHeight));
                        maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                        calculatedLabelBreakPoints.set((stackedStartIndex > -1) ? stackedStartIndex : i, true);
                        currentLineWidth = requiredWidth;
                    }
                    if (i == labelCount - 1) {
                        calculatedLineSizes.add(new FSize(currentLineWidth, labelLineHeight));
                        maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                    }
                }
                stackedStartIndex = ((this.mLabels[i] != null) ? -1 : stackedStartIndex);
            }
            this.mCalculatedLabelSizes = calculatedLabelSizes.toArray(new FSize[calculatedLabelSizes.size()]);
            this.mCalculatedLabelBreakPoints = calculatedLabelBreakPoints.toArray(new Boolean[calculatedLabelBreakPoints.size()]);
            this.mCalculatedLineSizes = calculatedLineSizes.toArray(new FSize[calculatedLineSizes.size()]);
            this.mTextWidthMax = this.getMaximumEntryWidth(labelpaint);
            this.mTextHeightMax = this.getMaximumEntryHeight(labelpaint);
            this.mNeededWidth = maxLineWidth;
            this.mNeededHeight = labelLineHeight * this.mCalculatedLineSizes.length + labelLineSpacing * ((this.mCalculatedLineSizes.length == 0) ? 0 : (this.mCalculatedLineSizes.length - 1));
        }
        else {
            this.mNeededWidth = this.getFullWidth(labelpaint);
            this.mNeededHeight = this.getMaximumEntryHeight(labelpaint);
            this.mTextWidthMax = this.getMaximumEntryWidth(labelpaint);
            this.mTextHeightMax = this.mNeededHeight;
        }
    }
    
    public enum LegendDirection
    {
        LEFT_TO_RIGHT("LEFT_TO_RIGHT", 0), 
        RIGHT_TO_LEFT("RIGHT_TO_LEFT", 1);
        
        private LegendDirection(final String s, final int n) {
        }
    }
    
    public enum LegendForm
    {
        SQUARE("SQUARE", 0), 
        CIRCLE("CIRCLE", 1), 
        LINE("LINE", 2);
        
        private LegendForm(final String s, final int n) {
        }
    }
    
    public enum LegendPosition
    {
        RIGHT_OF_CHART("RIGHT_OF_CHART", 0), 
        RIGHT_OF_CHART_CENTER("RIGHT_OF_CHART_CENTER", 1), 
        RIGHT_OF_CHART_INSIDE("RIGHT_OF_CHART_INSIDE", 2), 
        LEFT_OF_CHART("LEFT_OF_CHART", 3), 
        LEFT_OF_CHART_CENTER("LEFT_OF_CHART_CENTER", 4), 
        LEFT_OF_CHART_INSIDE("LEFT_OF_CHART_INSIDE", 5), 
        BELOW_CHART_LEFT("BELOW_CHART_LEFT", 6), 
        BELOW_CHART_RIGHT("BELOW_CHART_RIGHT", 7), 
        BELOW_CHART_CENTER("BELOW_CHART_CENTER", 8), 
        ABOVE_CHART_LEFT("ABOVE_CHART_LEFT", 9), 
        ABOVE_CHART_RIGHT("ABOVE_CHART_RIGHT", 10), 
        ABOVE_CHART_CENTER("ABOVE_CHART_CENTER", 11), 
        PIECHART_CENTER("PIECHART_CENTER", 12);
        
        private LegendPosition(final String s, final int n) {
        }
    }
}
