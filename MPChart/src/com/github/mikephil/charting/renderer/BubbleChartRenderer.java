// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
//import com.github.mikephil.charting.data.Entry;
import android.graphics.Color;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.data.BubbleEntry;
//import java.util.Iterator;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;

public class BubbleChartRenderer extends DataRenderer
{
    protected BubbleDataProvider mChart;
    private float[] sizeBuffer;
    private float[] pointBuffer;
    private float[] _hsvBuffer;
    
    public BubbleChartRenderer(final BubbleDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.sizeBuffer = new float[4];
        this.pointBuffer = new float[2];
        this._hsvBuffer = new float[3];
        this.mChart = chart;
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
    }
    
    @Override
    public void initBuffers() {
    }
    
    @Override
    public void drawData(final Canvas c) {
        final BubbleData bubbleData = this.mChart.getBubbleData();
        for (final IBubbleDataSet set : bubbleData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set);
            }
        }
    }
    
    protected float getShapeSize(final float entrySize, final float maxSize, final float reference) {
        final float factor = (maxSize == 0.0f) ? 1.0f : ((float)Math.sqrt(entrySize / maxSize));
        final float shapeSize = reference * factor;
        return shapeSize;
    }
    
    protected void drawDataSet(final Canvas c, final IBubbleDataSet dataSet) {
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final BubbleEntry entryFrom = dataSet.getEntryForXIndex(this.mMinX);
        final BubbleEntry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
        final int minx = Math.max(dataSet.getEntryIndex(entryFrom), 0);
        final int maxx = Math.min(dataSet.getEntryIndex(entryTo) + 1, dataSet.getEntryCount());
        this.sizeBuffer[0] = 0.0f;
        this.sizeBuffer[2] = 1.0f;
        trans.pointValuesToPixel(this.sizeBuffer);
        final float maxBubbleWidth = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
        final float maxBubbleHeight = Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop());
        final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);
        for (int j = minx; j < maxx; ++j) {
            final BubbleEntry entry = dataSet.getEntryForIndex(j);
            this.pointBuffer[0] = (entry.getXIndex() - minx) * phaseX + minx;
            this.pointBuffer[1] = entry.getVal() * phaseY;
            trans.pointValuesToPixel(this.pointBuffer);
            final float shapeHalf = this.getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize) / 2.0f;
            if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + shapeHalf)) {
                if (this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - shapeHalf)) {
                    if (this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + shapeHalf)) {
                        if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - shapeHalf)) {
                            break;
                        }
                        final int color = dataSet.getColor(entry.getXIndex());
                        this.mRenderPaint.setColor(color);
                        c.drawCircle(this.pointBuffer[0], this.pointBuffer[1], shapeHalf, this.mRenderPaint);
                    }
                }
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        final BubbleData bubbleData = this.mChart.getBubbleData();
        if (bubbleData == null) {
            return;
        }
        if (bubbleData.getYValCount() < (int)Math.ceil(this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX())) {
            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();
            final float lineHeight = Utils.calcTextHeight(this.mValuePaint, "1");
            for (int i = 0; i < dataSets.size(); ++i) {
                final IBubbleDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        this.applyValueTextStyle(dataSet);
                        final float phaseX = this.mAnimator.getPhaseX();
                        final float phaseY = this.mAnimator.getPhaseY();
                        final BubbleEntry entryFrom = dataSet.getEntryForXIndex(this.mMinX);
                        final BubbleEntry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
                        final int minx = dataSet.getEntryIndex(entryFrom);
                        final int maxx = Math.min(dataSet.getEntryIndex(entryTo) + 1, dataSet.getEntryCount());
                        final float[] positions = this.mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesBubble(dataSet, phaseX, phaseY, minx, maxx);
                        final float alpha = (phaseX == 1.0f) ? phaseY : phaseX;
                        for (int j = 0; j < positions.length; j += 2) {
                            int valueTextColor = dataSet.getValueTextColor(j / 2 + minx);
                            valueTextColor = Color.argb(Math.round(255.0f * alpha), Color.red(valueTextColor), Color.green(valueTextColor), Color.blue(valueTextColor));
                            final float x = positions[j];
                            final float y = positions[j + 1];
                            if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsLeft(x)) {
                                if (this.mViewPortHandler.isInBoundsY(y)) {
                                    final BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + minx);
                                    this.drawValue(c, dataSet.getValueFormatter(), entry.getSize(), entry, i, x, y + 0.5f * lineHeight, valueTextColor);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas c) {
    }
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        final BubbleData bubbleData = this.mChart.getBubbleData();
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        for (final Highlight indice : indices) {
            final IBubbleDataSet dataSet = bubbleData.getDataSetByIndex(indice.getDataSetIndex());
            if (dataSet != null) {
                if (dataSet.isHighlightEnabled()) {
                    final BubbleEntry entryFrom = dataSet.getEntryForXIndex(this.mMinX);
                    final BubbleEntry entryTo = dataSet.getEntryForXIndex(this.mMaxX);
                    final int minx = dataSet.getEntryIndex(entryFrom);
                    final int maxx = Math.min(dataSet.getEntryIndex(entryTo) + 1, dataSet.getEntryCount());
                    final BubbleEntry entry = (BubbleEntry)bubbleData.getEntryForHighlight(indice);
                    if (entry != null) {
                        if (entry.getXIndex() == indice.getXIndex()) {
                            final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                            this.sizeBuffer[0] = 0.0f;
                            this.sizeBuffer[2] = 1.0f;
                            trans.pointValuesToPixel(this.sizeBuffer);
                            final float maxBubbleWidth = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
                            final float maxBubbleHeight = Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop());
                            final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);
                            this.pointBuffer[0] = (entry.getXIndex() - minx) * phaseX + minx;
                            this.pointBuffer[1] = entry.getVal() * phaseY;
                            trans.pointValuesToPixel(this.pointBuffer);
                            final float shapeHalf = this.getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize) / 2.0f;
                            if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + shapeHalf)) {
                                if (this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - shapeHalf)) {
                                    if (this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + shapeHalf)) {
                                        if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - shapeHalf)) {
                                            break;
                                        }
                                        if (indice.getXIndex() >= minx) {
                                            if (indice.getXIndex() < maxx) {
                                                final int originalColor = dataSet.getColor(entry.getXIndex());
                                                Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor), Color.blue(originalColor), this._hsvBuffer);
                                                final float[] hsvBuffer = this._hsvBuffer;
                                                final int n = 2;
                                                hsvBuffer[n] *= 0.5f;
                                                final int color = Color.HSVToColor(Color.alpha(originalColor), this._hsvBuffer);
                                                this.mHighlightPaint.setColor(color);
                                                this.mHighlightPaint.setStrokeWidth(dataSet.getHighlightCircleWidth());
                                                c.drawCircle(this.pointBuffer[0], this.pointBuffer[1], shapeHalf, this.mHighlightPaint);
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
}
