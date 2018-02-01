// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;
//import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Canvas;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class HorizontalBarChartRenderer extends BarChartRenderer
{
    public HorizontalBarChartRenderer(final BarDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.mValuePaint.setTextAlign(Paint.Align.LEFT);
    }
    
    @Override
    public void initBuffers() {
        final BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; ++i) {
            final IBarDataSet set = barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new HorizontalBarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1), barData.getGroupSpace(), barData.getDataSetCount(), set.isStacked());
        }
    }
    
    @Override
    protected void drawDataSet(final Canvas c, final IBarDataSet dataSet, final int index) {
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        this.mShadowPaint.setColor(dataSet.getBarShadowColor());
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final BarBuffer buffer = this.mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setBarSpace(dataSet.getBarSpace());
        buffer.setDataSet(index);
        buffer.setInverted(this.mChart.isInverted(dataSet.getAxisDependency()));
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        for (int j = 0; j < buffer.size() && this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3]); j += 4) {
            if (this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                if (this.mChart.isDrawBarShadowEnabled()) {
                    c.drawRect(this.mViewPortHandler.contentLeft(), buffer.buffer[j + 1], this.mViewPortHandler.contentRight(), buffer.buffer[j + 3], this.mShadowPaint);
                }
                this.mRenderPaint.setColor(dataSet.getColor(j / 4));
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        if (this.passesCheck()) {
            final List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            final float valueOffsetPlus = Utils.convertDpToPixel(5.0f);
            float posOffset = 0.0f;
            float negOffset = 0.0f;
            final boolean drawValueAboveBar = this.mChart.isDrawValueAboveBarEnabled();
            for (int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i) {
                final IBarDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        final boolean isInverted = this.mChart.isInverted(dataSet.getAxisDependency());
                        this.applyValueTextStyle(dataSet);
                        final float halfTextHeight = Utils.calcTextHeight(this.mValuePaint, "10") / 2.0f;
                        final ValueFormatter formatter = dataSet.getValueFormatter();
                        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        final float[] valuePoints = this.getTransformedValues(trans, dataSet, i);
                        if (!dataSet.isStacked()) {
                            for (int j = 0; j < valuePoints.length * this.mAnimator.getPhaseX(); j += 2) {
                                if (!this.mViewPortHandler.isInBoundsTop(valuePoints[j + 1])) {
                                    break;
                                }
                                if (this.mViewPortHandler.isInBoundsX(valuePoints[j])) {
                                    if (this.mViewPortHandler.isInBoundsBottom(valuePoints[j + 1])) {
                                        final BarEntry e = dataSet.getEntryForIndex(j / 2);
                                        final float val = e.getVal();
                                        final String formattedValue = formatter.getFormattedValue(val, e, i, this.mViewPortHandler);
                                        final float valueTextWidth = Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                        posOffset = (drawValueAboveBar ? valueOffsetPlus : (-(valueTextWidth + valueOffsetPlus)));
                                        negOffset = (drawValueAboveBar ? (-(valueTextWidth + valueOffsetPlus)) : valueOffsetPlus);
                                        if (isInverted) {
                                            posOffset = -posOffset - valueTextWidth;
                                            negOffset = -negOffset - valueTextWidth;
                                        }
                                        this.drawValue(c, formattedValue, valuePoints[j] + ((val >= 0.0f) ? posOffset : negOffset), valuePoints[j + 1] + halfTextHeight, dataSet.getValueTextColor(j / 2));
                                    }
                                }
                            }
                        }
                        else {
                            for (int j = 0; j < (valuePoints.length - 1) * this.mAnimator.getPhaseX(); j += 2) {
                                final BarEntry e = dataSet.getEntryForIndex(j / 2);
                                final float[] vals = e.getVals();
                                if (vals == null) {
                                    if (!this.mViewPortHandler.isInBoundsTop(valuePoints[j + 1])) {
                                        break;
                                    }
                                    if (this.mViewPortHandler.isInBoundsX(valuePoints[j])) {
                                        if (this.mViewPortHandler.isInBoundsBottom(valuePoints[j + 1])) {
                                            final float val2 = e.getVal();
                                            final String formattedValue2 = formatter.getFormattedValue(val2, e, i, this.mViewPortHandler);
                                            final float valueTextWidth2 = Utils.calcTextWidth(this.mValuePaint, formattedValue2);
                                            posOffset = (drawValueAboveBar ? valueOffsetPlus : (-(valueTextWidth2 + valueOffsetPlus)));
                                            negOffset = (drawValueAboveBar ? (-(valueTextWidth2 + valueOffsetPlus)) : valueOffsetPlus);
                                            if (isInverted) {
                                                posOffset = -posOffset - valueTextWidth2;
                                                negOffset = -negOffset - valueTextWidth2;
                                            }
                                            this.drawValue(c, formattedValue2, valuePoints[j] + ((e.getVal() >= 0.0f) ? posOffset : negOffset), valuePoints[j + 1] + halfTextHeight, dataSet.getValueTextColor(j / 2));
                                        }
                                    }
                                }
                                else {
                                    final float[] transformed = new float[vals.length * 2];
                                    float posY = 0.0f;
                                    float negY = -e.getNegativeSum();
                                    for (int k = 0, idx = 0; k < transformed.length; k += 2, ++idx) {
                                        final float value = vals[idx];
                                        float y;
                                        if (value >= 0.0f) {
                                            posY = (y = posY + value);
                                        }
                                        else {
                                            y = negY;
                                            negY -= value;
                                        }
                                        transformed[k] = y * this.mAnimator.getPhaseY();
                                    }
                                    trans.pointValuesToPixel(transformed);
                                    for (int k = 0; k < transformed.length; k += 2) {
                                        final float val3 = vals[k / 2];
                                        final String formattedValue3 = formatter.getFormattedValue(val3, e, i, this.mViewPortHandler);
                                        final float valueTextWidth3 = Utils.calcTextWidth(this.mValuePaint, formattedValue3);
                                        posOffset = (drawValueAboveBar ? valueOffsetPlus : (-(valueTextWidth3 + valueOffsetPlus)));
                                        negOffset = (drawValueAboveBar ? (-(valueTextWidth3 + valueOffsetPlus)) : valueOffsetPlus);
                                        if (isInverted) {
                                            posOffset = -posOffset - valueTextWidth3;
                                            negOffset = -negOffset - valueTextWidth3;
                                        }
                                        final float x = transformed[k] + ((val3 >= 0.0f) ? posOffset : negOffset);
                                        final float y2 = valuePoints[j + 1];
                                        if (!this.mViewPortHandler.isInBoundsTop(y2)) {
                                            break;
                                        }
                                        if (this.mViewPortHandler.isInBoundsX(x)) {
                                            if (this.mViewPortHandler.isInBoundsBottom(y2)) {
                                                this.drawValue(c, formattedValue3, x, y2 + halfTextHeight, dataSet.getValueTextColor(j / 2));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void drawValue(final Canvas c, final String valueText, final float x, final float y, final int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }
    
    @Override
    protected void prepareBarHighlight(final float x, final float y1, final float y2, final float barspaceHalf, final Transformer trans) {
        final float top = x - 0.5f + barspaceHalf;
        final float bottom = x + 0.5f - barspaceHalf;
        this.mBarRect.set(y1, top, y2, bottom);
        trans.rectValueToPixelHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }
    
    @Override
    public float[] getTransformedValues(final Transformer trans, final IBarDataSet data, final int dataSetIndex) {
        return trans.generateTransformedValuesHorizontalBarChart(data, dataSetIndex, this.mChart.getBarData(), this.mAnimator.getPhaseY());
    }
    
    @Override
    protected boolean passesCheck() {
        return this.mChart.getBarData().getYValCount() < this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleY();
    }
}
