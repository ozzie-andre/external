// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import android.graphics.Path;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
//import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Canvas;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import android.graphics.Color;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Paint;
import com.github.mikephil.charting.buffer.BarBuffer;
import android.graphics.RectF;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;

public class BarChartRenderer extends DataRenderer
{
    protected BarDataProvider mChart;
    protected RectF mBarRect;
    protected BarBuffer[] mBarBuffers;
    protected Paint mShadowPaint;
    
    public BarChartRenderer(final BarDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mBarRect = new RectF();
        this.mChart = chart;
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint.Style.FILL);
        this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        this.mHighlightPaint.setAlpha(120);
        (this.mShadowPaint = new Paint(1)).setStyle(Paint.Style.FILL);
    }
    
    @Override
    public void initBuffers() {
        final BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; ++i) {
            final IBarDataSet set = barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1), barData.getGroupSpace(), barData.getDataSetCount(), set.isStacked());
        }
    }
    
    @Override
    public void drawData(final Canvas c) {
        final BarData barData = this.mChart.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); ++i) {
            final IBarDataSet set = barData.getDataSetByIndex(i);
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set, i);
            }
        }
    }
    
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
        if (this.mChart.isDrawBarShadowEnabled()) {
            for (int j = 0; j < buffer.size(); j += 4) {
                if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    c.drawRect(buffer.buffer[j], this.mViewPortHandler.contentTop(), buffer.buffer[j + 2], this.mViewPortHandler.contentBottom(), this.mShadowPaint);
                }
            }
        }
        if (dataSet.getColors().size() > 1) {
            for (int j = 0; j < buffer.size(); j += 4) {
                if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    this.mRenderPaint.setColor(dataSet.getColor(j / 4));
                    c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
                }
            }
        }
        else {
            this.mRenderPaint.setColor(dataSet.getColor());
            for (int j = 0; j < buffer.size(); j += 4) {
                if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
                }
            }
        }
    }
    
    protected void prepareBarHighlight(final float x, final float y1, final float y2, final float barspaceHalf, final Transformer trans) {
        final float barWidth = 0.5f;
        final float left = x - barWidth + barspaceHalf;
        final float right = x + barWidth - barspaceHalf;
        this.mBarRect.set(left, y1, right, y2);
        trans.rectValueToPixel(this.mBarRect, this.mAnimator.getPhaseY());
    }
    
    @Override
    public void drawValues(final Canvas c) {
        if (this.passesCheck()) {
            final List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset = 0.0f;
            float negOffset = 0.0f;
            final boolean drawValueAboveBar = this.mChart.isDrawValueAboveBarEnabled();
            for (int i = 0; i < this.mChart.getBarData().getDataSetCount(); ++i) {
                final IBarDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        this.applyValueTextStyle(dataSet);
                        final boolean isInverted = this.mChart.isInverted(dataSet.getAxisDependency());
                        final float valueTextHeight = Utils.calcTextHeight(this.mValuePaint, "8");
                        posOffset = (drawValueAboveBar ? (-valueOffsetPlus) : (valueTextHeight + valueOffsetPlus));
                        negOffset = (drawValueAboveBar ? (valueTextHeight + valueOffsetPlus) : (-valueOffsetPlus));
                        if (isInverted) {
                            posOffset = -posOffset - valueTextHeight;
                            negOffset = -negOffset - valueTextHeight;
                        }
                        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        final float[] valuePoints = this.getTransformedValues(trans, dataSet, i);
                        if (!dataSet.isStacked()) {
                            for (int j = 0; j < valuePoints.length * this.mAnimator.getPhaseX(); j += 2) {
                                if (!this.mViewPortHandler.isInBoundsRight(valuePoints[j])) {
                                    break;
                                }
                                if (this.mViewPortHandler.isInBoundsY(valuePoints[j + 1])) {
                                    if (this.mViewPortHandler.isInBoundsLeft(valuePoints[j])) {
                                        final BarEntry entry = dataSet.getEntryForIndex(j / 2);
                                        final float val = entry.getVal();
                                        this.drawValue(c, dataSet.getValueFormatter(), val, entry, i, valuePoints[j], valuePoints[j + 1] + ((val >= 0.0f) ? posOffset : negOffset), dataSet.getValueTextColor(j / 2));
                                    }
                                }
                            }
                        }
                        else {
                            for (int j = 0; j < (valuePoints.length - 1) * this.mAnimator.getPhaseX(); j += 2) {
                                final BarEntry entry = dataSet.getEntryForIndex(j / 2);
                                final float[] vals = entry.getVals();
                                if (vals == null) {
                                    if (!this.mViewPortHandler.isInBoundsRight(valuePoints[j])) {
                                        break;
                                    }
                                    if (this.mViewPortHandler.isInBoundsY(valuePoints[j + 1])) {
                                        if (this.mViewPortHandler.isInBoundsLeft(valuePoints[j])) {
                                            this.drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, valuePoints[j], valuePoints[j + 1] + ((entry.getVal() >= 0.0f) ? posOffset : negOffset), dataSet.getValueTextColor(j / 2));
                                        }
                                    }
                                }
                                else {
                                    final int color = dataSet.getValueTextColor(j / 2);
                                    final float[] transformed = new float[vals.length * 2];
                                    float posY = 0.0f;
                                    float negY = -entry.getNegativeSum();
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
                                        transformed[k + 1] = y * this.mAnimator.getPhaseY();
                                    }
                                    trans.pointValuesToPixel(transformed);
                                    for (int k = 0; k < transformed.length; k += 2) {
                                        final float x = valuePoints[j];
                                        final float y2 = transformed[k + 1] + ((vals[k / 2] >= 0.0f) ? posOffset : negOffset);
                                        if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                            break;
                                        }
                                        if (this.mViewPortHandler.isInBoundsY(y2)) {
                                            if (this.mViewPortHandler.isInBoundsLeft(x)) {
                                                this.drawValue(c, dataSet.getValueFormatter(), vals[k / 2], entry, i, x, y2, color);
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
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        final int setCount = this.mChart.getBarData().getDataSetCount();
        for (int i = 0; i < indices.length; ++i) {
            final Highlight h = indices[i];
            final int index = h.getXIndex();
            final int dataSetIndex = h.getDataSetIndex();
            final IBarDataSet set = this.mChart.getBarData().getDataSetByIndex(dataSetIndex);
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final float barspaceHalf = set.getBarSpace() / 2.0f;
                    final Transformer trans = this.mChart.getTransformer(set.getAxisDependency());
                    this.mHighlightPaint.setColor(set.getHighLightColor());
                    this.mHighlightPaint.setAlpha(set.getHighLightAlpha());
                    if (index >= 0 && index < this.mChart.getXChartMax() * this.mAnimator.getPhaseX() / setCount) {
                        final BarEntry e = set.getEntryForXIndex(index);
                        if (e != null) {
                            if (e.getXIndex() == index) {
                                final float groupspace = this.mChart.getBarData().getGroupSpace();
                                final boolean isStack = h.getStackIndex() >= 0;
                                final float x = index * setCount + dataSetIndex + groupspace / 2.0f + groupspace * index;
                                float y1;
                                float y2;
                                if (isStack) {
                                    y1 = h.getRange().from;
                                    y2 = h.getRange().to;
                                }
                                else {
                                    y1 = e.getVal();
                                    y2 = 0.0f;
                                }
                                this.prepareBarHighlight(x, y1, y2, barspaceHalf, trans);
                                c.drawRect(this.mBarRect, this.mHighlightPaint);
                                if (this.mChart.isDrawHighlightArrowEnabled()) {
                                    this.mHighlightPaint.setAlpha(255);
                                    final float offsetY = this.mAnimator.getPhaseY() * 0.07f;
                                    final float[] values = new float[9];
                                    trans.getPixelToValueMatrix().getValues(values);
                                    final float xToYRel = Math.abs(values[4] / values[0]);
                                    final float arrowWidth = set.getBarSpace() / 2.0f;
                                    final float arrowHeight = arrowWidth * xToYRel;
                                    final float yArrow = ((y1 > -y2) ? y1 : y1) * this.mAnimator.getPhaseY();
                                    final Path arrow = new Path();
                                    arrow.moveTo(x + 0.4f, yArrow + offsetY);
                                    arrow.lineTo(x + 0.4f + arrowWidth, yArrow + offsetY - arrowHeight);
                                    arrow.lineTo(x + 0.4f + arrowWidth, yArrow + offsetY + arrowHeight);
                                    trans.pathValueToPixel(arrow);
                                    c.drawPath(arrow, this.mHighlightPaint);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public float[] getTransformedValues(final Transformer trans, final IBarDataSet data, final int dataSetIndex) {
        return trans.generateTransformedValuesBarChart(data, dataSetIndex, this.mChart.getBarData(), this.mAnimator.getPhaseY());
    }
    
    protected boolean passesCheck() {
        return this.mChart.getBarData().getYValCount() < this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX();
    }
    
    @Override
    public void drawExtras(final Canvas c) {
    }
}
