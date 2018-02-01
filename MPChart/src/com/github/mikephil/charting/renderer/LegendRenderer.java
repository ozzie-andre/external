// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.FSize;
import android.graphics.Canvas;
import android.graphics.Typeface;
import java.util.List;
//import java.util.Collection;
import java.util.Collections;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.components.Legend;
import android.graphics.Paint;

public class LegendRenderer extends Renderer
{
    protected Paint mLegendLabelPaint;
    protected Paint mLegendFormPaint;
    protected Legend mLegend;
    
    public LegendRenderer(final ViewPortHandler viewPortHandler, final Legend legend) {
        super(viewPortHandler);
        this.mLegend = legend;
        (this.mLegendLabelPaint = new Paint(1)).setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Paint.Align.LEFT);
        (this.mLegendFormPaint = new Paint(1)).setStyle(Paint.Style.FILL);
        this.mLegendFormPaint.setStrokeWidth(3.0f);
    }
    
    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }
    
    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }
    
    public void computeLegend(final ChartData<?> data) {
        if (!this.mLegend.isLegendCustom()) {
            final List<String> labels = new ArrayList<String>();
            final List<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < data.getDataSetCount(); ++i) {
                final IDataSet dataSet = (IDataSet)data.getDataSetByIndex(i);
                final List<Integer> clrs = (List<Integer>)dataSet.getColors();
                final int entryCount = dataSet.getEntryCount();
                if (dataSet instanceof IBarDataSet && ((IBarDataSet)dataSet).isStacked()) {
                    final IBarDataSet bds = (IBarDataSet)dataSet;
                    final String[] sLabels = bds.getStackLabels();
                    for (int j = 0; j < clrs.size() && j < bds.getStackSize(); ++j) {
                        labels.add(sLabels[j % sLabels.length]);
                        colors.add(clrs.get(j));
                    }
                    if (bds.getLabel() != null) {
                        colors.add(1122868);
                        labels.add(bds.getLabel());
                    }
                }
                else if (dataSet instanceof IPieDataSet) {
                    final List<String> xVals = data.getXVals();
                    final IPieDataSet pds = (IPieDataSet)dataSet;
                    for (int j = 0; j < clrs.size() && j < entryCount && j < xVals.size(); ++j) {
                        labels.add(xVals.get(j));
                        colors.add(clrs.get(j));
                    }
                    if (pds.getLabel() != null) {
                        colors.add(1122868);
                        labels.add(pds.getLabel());
                    }
                }
                else if (dataSet instanceof ICandleDataSet && ((ICandleDataSet)dataSet).getDecreasingColor() != 1122867) {
                    colors.add(((ICandleDataSet)dataSet).getDecreasingColor());
                    colors.add(((ICandleDataSet)dataSet).getIncreasingColor());
                    labels.add(null);
                    labels.add(dataSet.getLabel());
                }
                else {
                    for (int k = 0; k < clrs.size() && k < entryCount; ++k) {
                        if (k < clrs.size() - 1 && k < entryCount - 1) {
                            labels.add(null);
                        }
                        else {
                            final String label = ((IDataSet)data.getDataSetByIndex(i)).getLabel();
                            labels.add(label);
                        }
                        colors.add(clrs.get(k));
                    }
                }
            }
            if (this.mLegend.getExtraColors() != null && this.mLegend.getExtraLabels() != null) {
                int[] extraColors;
                for (int length = (extraColors = this.mLegend.getExtraColors()).length, l = 0; l < length; ++l) {
                    final int color = extraColors[l];
                    colors.add(color);
                }
                Collections.addAll(labels, this.mLegend.getExtraLabels());
            }
            this.mLegend.setComputedColors(colors);
            this.mLegend.setComputedLabels(labels);
        }
        final Typeface tf = this.mLegend.getTypeface();
        if (tf != null) {
            this.mLegendLabelPaint.setTypeface(tf);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }
    
    public void renderLegend(final Canvas c) {
        if (!this.mLegend.isEnabled()) {
            return;
        }
        final Typeface tf = this.mLegend.getTypeface();
        if (tf != null) {
            this.mLegendLabelPaint.setTypeface(tf);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        final float labelLineHeight = Utils.getLineHeight(this.mLegendLabelPaint);
        final float labelLineSpacing = Utils.getLineSpacing(this.mLegendLabelPaint) + this.mLegend.getYEntrySpace();
        final float formYOffset = labelLineHeight - Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0f;
        final String[] labels = this.mLegend.getLabels();
        final int[] colors = this.mLegend.getColors();
        final float formToTextSpace = this.mLegend.getFormToTextSpace();
        final float xEntrySpace = this.mLegend.getXEntrySpace();
        final Legend.LegendDirection direction = this.mLegend.getDirection();
        final float formSize = this.mLegend.getFormSize();
        final float stackSpace = this.mLegend.getStackSpace();
        final float yoffset = this.mLegend.getYOffset();
        final float xoffset = this.mLegend.getXOffset();
        final Legend.LegendPosition legendPosition = this.mLegend.getPosition();
        switch (legendPosition) {
            case BELOW_CHART_LEFT:
            case BELOW_CHART_RIGHT:
            case BELOW_CHART_CENTER:
            case ABOVE_CHART_LEFT:
            case ABOVE_CHART_RIGHT:
            case ABOVE_CHART_CENTER: {
                final float contentWidth = this.mViewPortHandler.contentWidth();
                float originPosX;
                if (legendPosition == Legend.LegendPosition.BELOW_CHART_LEFT || legendPosition == Legend.LegendPosition.ABOVE_CHART_LEFT) {
                    originPosX = this.mViewPortHandler.contentLeft() + xoffset;
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        originPosX += this.mLegend.mNeededWidth;
                    }
                }
                else if (legendPosition == Legend.LegendPosition.BELOW_CHART_RIGHT || legendPosition == Legend.LegendPosition.ABOVE_CHART_RIGHT) {
                    originPosX = this.mViewPortHandler.contentRight() - xoffset;
                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        originPosX -= this.mLegend.mNeededWidth;
                    }
                }
                else {
                    originPosX = this.mViewPortHandler.contentLeft() + contentWidth / 2.0f - this.mLegend.mNeededWidth / 2.0f;
                }
                final FSize[] calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
                final FSize[] calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
                final Boolean[] calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
                float posX = originPosX;
                float posY;
                if (legendPosition == Legend.LegendPosition.ABOVE_CHART_LEFT || legendPosition == Legend.LegendPosition.ABOVE_CHART_RIGHT || legendPosition == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                    posY = 0.0f;
                }
                else {
                    posY = this.mViewPortHandler.getChartHeight() - yoffset - this.mLegend.mNeededHeight;
                }
                int lineIndex = 0;
                for (int i = 0, count = labels.length; i < count; ++i) {
                    if (i < calculatedLabelBreakPoints.length && calculatedLabelBreakPoints[i]) {
                        posX = originPosX;
                        posY += labelLineHeight + labelLineSpacing;
                    }
                    if (posX == originPosX && legendPosition == Legend.LegendPosition.BELOW_CHART_CENTER && lineIndex < calculatedLineSizes.length) {
                        posX += ((direction == Legend.LegendDirection.RIGHT_TO_LEFT) ? calculatedLineSizes[lineIndex].width : (-calculatedLineSizes[lineIndex].width)) / 2.0f;
                        ++lineIndex;
                    }
                    final boolean drawingForm = colors[i] != 1122868;
                    final boolean isStacked = labels[i] == null;
                    if (drawingForm) {
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX -= formSize;
                        }
                        this.drawForm(c, posX, posY + formYOffset, i, this.mLegend);
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += formSize;
                        }
                    }
                    if (!isStacked) {
                        if (drawingForm) {
                            posX += ((direction == Legend.LegendDirection.RIGHT_TO_LEFT) ? (-formToTextSpace) : formToTextSpace);
                        }
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX -= calculatedLabelSizes[i].width;
                        }
                        this.drawLabel(c, posX, posY + labelLineHeight, labels[i]);
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += calculatedLabelSizes[i].width;
                        }
                        posX += ((direction == Legend.LegendDirection.RIGHT_TO_LEFT) ? (-xEntrySpace) : xEntrySpace);
                    }
                    else {
                        posX += ((direction == Legend.LegendDirection.RIGHT_TO_LEFT) ? (-stackSpace) : stackSpace);
                    }
                }
                break;
            }
            case RIGHT_OF_CHART:
            case RIGHT_OF_CHART_CENTER:
            case RIGHT_OF_CHART_INSIDE:
            case LEFT_OF_CHART:
            case LEFT_OF_CHART_CENTER:
            case LEFT_OF_CHART_INSIDE:
            case PIECHART_CENTER: {
                float stack = 0.0f;
                boolean wasStacked = false;
                float posX;
                float posY;
                if (legendPosition == Legend.LegendPosition.PIECHART_CENTER) {
                    posX = this.mViewPortHandler.getChartWidth() / 2.0f + ((direction == Legend.LegendDirection.LEFT_TO_RIGHT) ? (-this.mLegend.mTextWidthMax / 2.0f) : (this.mLegend.mTextWidthMax / 2.0f));
                    posY = this.mViewPortHandler.getChartHeight() / 2.0f - this.mLegend.mNeededHeight / 2.0f + this.mLegend.getYOffset();
                }
                else {
                    final boolean isRightAligned = legendPosition == Legend.LegendPosition.RIGHT_OF_CHART || legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_CENTER || legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_INSIDE;
                    if (isRightAligned) {
                        posX = this.mViewPortHandler.getChartWidth() - xoffset;
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX -= this.mLegend.mTextWidthMax;
                        }
                    }
                    else {
                        posX = xoffset;
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX += this.mLegend.mTextWidthMax;
                        }
                    }
                    if (legendPosition == Legend.LegendPosition.RIGHT_OF_CHART || legendPosition == Legend.LegendPosition.LEFT_OF_CHART) {
                        posY = this.mViewPortHandler.contentTop() + yoffset;
                    }
                    else if (legendPosition == Legend.LegendPosition.RIGHT_OF_CHART_CENTER || legendPosition == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                        posY = this.mViewPortHandler.getChartHeight() / 2.0f - this.mLegend.mNeededHeight / 2.0f;
                    }
                    else {
                        posY = this.mViewPortHandler.contentTop() + yoffset;
                    }
                }
                for (int j = 0; j < labels.length; ++j) {
                    final Boolean drawingForm2 = colors[j] != 1122868;
                    float x = posX;
                    if (drawingForm2) {
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            x += stack;
                        }
                        else {
                            x -= formSize - stack;
                        }
                        this.drawForm(c, x, posY + formYOffset, j, this.mLegend);
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            x += formSize;
                        }
                    }
                    if (labels[j] != null) {
                        if (drawingForm2 && !wasStacked) {
                            x += ((direction == Legend.LegendDirection.LEFT_TO_RIGHT) ? formToTextSpace : (-formToTextSpace));
                        }
                        else if (wasStacked) {
                            x = posX;
                        }
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            x -= Utils.calcTextWidth(this.mLegendLabelPaint, labels[j]);
                        }
                        if (!wasStacked) {
                            this.drawLabel(c, x, posY + labelLineHeight, labels[j]);
                        }
                        else {
                            posY += labelLineHeight + labelLineSpacing;
                            this.drawLabel(c, x, posY + labelLineHeight, labels[j]);
                        }
                        posY += labelLineHeight + labelLineSpacing;
                        stack = 0.0f;
                    }
                    else {
                        stack += formSize + stackSpace;
                        wasStacked = true;
                    }
                }
                break;
            }
        }
    }
    
    protected void drawForm(final Canvas c, final float x, final float y, final int index, final Legend legend) {
        if (legend.getColors()[index] == 1122868) {
            return;
        }
        this.mLegendFormPaint.setColor(legend.getColors()[index]);
        final float formsize = legend.getFormSize();
        final float half = formsize / 2.0f;
        switch (legend.getForm()) {
            case CIRCLE: {
                c.drawCircle(x + half, y, half, this.mLegendFormPaint);
                break;
            }
            case SQUARE: {
                c.drawRect(x, y - half, x + formsize, y + half, this.mLegendFormPaint);
                break;
            }
            case LINE: {
                c.drawLine(x, y, x + formsize, y, this.mLegendFormPaint);
                break;
            }
        }
    }
    
    protected void drawLabel(final Canvas c, final float x, final float y, final String label) {
        c.drawText(label, x, y, this.mLegendLabelPaint);
    }
}
