// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

//import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.List;
//import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Paint;
import com.github.mikephil.charting.data.CandleEntry;
//import java.util.Iterator;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer
{
    protected CandleDataProvider mChart;
    private float[] mShadowBuffers;
    private float[] mBodyBuffers;
    private float[] mRangeBuffers;
    private float[] mOpenBuffers;
    private float[] mCloseBuffers;
    
    public CandleStickChartRenderer(final CandleDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mShadowBuffers = new float[8];
        this.mBodyBuffers = new float[4];
        this.mRangeBuffers = new float[4];
        this.mOpenBuffers = new float[4];
        this.mCloseBuffers = new float[4];
        this.mChart = chart;
    }
    
    @Override
    public void initBuffers() {
    }
    
    @Override
    public void drawData(final Canvas c) {
        final CandleData candleData = this.mChart.getCandleData();
        for (final ICandleDataSet set : candleData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas c, final ICandleDataSet dataSet) {
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float barSpace = dataSet.getBarSpace();
        final boolean showCandleBar = dataSet.getShowCandleBar();
        final int minx = Math.max(this.mMinX, 0);
        final int maxx = Math.min(this.mMaxX + 1, dataSet.getEntryCount());
        this.mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());
        for (int j = minx, count = (int)Math.ceil((maxx - minx) * phaseX + minx); j < count; ++j) {
            final CandleEntry e = dataSet.getEntryForIndex(j);
            final int xIndex = e.getXIndex();
            if (xIndex >= minx) {
                if (xIndex < maxx) {
                    final float open = e.getOpen();
                    final float close = e.getClose();
                    final float high = e.getHigh();
                    final float low = e.getLow();
                    if (showCandleBar) {
                        this.mShadowBuffers[0] = xIndex;
                        this.mShadowBuffers[2] = xIndex;
                        this.mShadowBuffers[4] = xIndex;
                        this.mShadowBuffers[6] = xIndex;
                        if (open > close) {
                            this.mShadowBuffers[1] = high * phaseY;
                            this.mShadowBuffers[3] = open * phaseY;
                            this.mShadowBuffers[5] = low * phaseY;
                            this.mShadowBuffers[7] = close * phaseY;
                        }
                        else if (open < close) {
                            this.mShadowBuffers[1] = high * phaseY;
                            this.mShadowBuffers[3] = close * phaseY;
                            this.mShadowBuffers[5] = low * phaseY;
                            this.mShadowBuffers[7] = open * phaseY;
                        }
                        else {
                            this.mShadowBuffers[1] = high * phaseY;
                            this.mShadowBuffers[3] = open * phaseY;
                            this.mShadowBuffers[5] = low * phaseY;
                            this.mShadowBuffers[7] = this.mShadowBuffers[3];
                        }
                        trans.pointValuesToPixel(this.mShadowBuffers);
                        if (dataSet.getShadowColorSameAsCandle()) {
                            if (open > close) {
                                this.mRenderPaint.setColor((dataSet.getDecreasingColor() == 1122867) ? dataSet.getColor(j) : dataSet.getDecreasingColor());
                            }
                            else if (open < close) {
                                this.mRenderPaint.setColor((dataSet.getIncreasingColor() == 1122867) ? dataSet.getColor(j) : dataSet.getIncreasingColor());
                            }
                            else {
                                this.mRenderPaint.setColor((dataSet.getNeutralColor() == 1122867) ? dataSet.getColor(j) : dataSet.getNeutralColor());
                            }
                        }
                        else {
                            this.mRenderPaint.setColor((dataSet.getShadowColor() == 1122867) ? dataSet.getColor(j) : dataSet.getShadowColor());
                        }
                        this.mRenderPaint.setStyle(Paint.Style.STROKE);
                        c.drawLines(this.mShadowBuffers, this.mRenderPaint);
                        this.mBodyBuffers[0] = xIndex - 0.5f + barSpace;
                        this.mBodyBuffers[1] = close * phaseY;
                        this.mBodyBuffers[2] = xIndex + 0.5f - barSpace;
                        this.mBodyBuffers[3] = open * phaseY;
                        trans.pointValuesToPixel(this.mBodyBuffers);
                        if (open > close) {
                            if (dataSet.getDecreasingColor() == 1122867) {
                                this.mRenderPaint.setColor(dataSet.getColor(j));
                            }
                            else {
                                this.mRenderPaint.setColor(dataSet.getDecreasingColor());
                            }
                            this.mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());
                            c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
                        }
                        else if (open < close) {
                            if (dataSet.getIncreasingColor() == 1122867) {
                                this.mRenderPaint.setColor(dataSet.getColor(j));
                            }
                            else {
                                this.mRenderPaint.setColor(dataSet.getIncreasingColor());
                            }
                            this.mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());
                            c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                        }
                        else {
                            if (dataSet.getNeutralColor() == 1122867) {
                                this.mRenderPaint.setColor(dataSet.getColor(j));
                            }
                            else {
                                this.mRenderPaint.setColor(dataSet.getNeutralColor());
                            }
                            c.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                        }
                    }
                    else {
                        this.mRangeBuffers[0] = xIndex;
                        this.mRangeBuffers[1] = high * phaseY;
                        this.mRangeBuffers[2] = xIndex;
                        this.mRangeBuffers[3] = low * phaseY;
                        this.mOpenBuffers[0] = xIndex - 0.5f + barSpace;
                        this.mOpenBuffers[1] = open * phaseY;
                        this.mOpenBuffers[2] = xIndex;
                        this.mOpenBuffers[3] = open * phaseY;
                        this.mCloseBuffers[0] = xIndex + 0.5f - barSpace;
                        this.mCloseBuffers[1] = close * phaseY;
                        this.mCloseBuffers[2] = xIndex;
                        this.mCloseBuffers[3] = close * phaseY;
                        trans.pointValuesToPixel(this.mRangeBuffers);
                        trans.pointValuesToPixel(this.mOpenBuffers);
                        trans.pointValuesToPixel(this.mCloseBuffers);
                        int barColor;
                        if (open > close) {
                            barColor = ((dataSet.getDecreasingColor() == 1122867) ? dataSet.getColor(j) : dataSet.getDecreasingColor());
                        }
                        else if (open < close) {
                            barColor = ((dataSet.getIncreasingColor() == 1122867) ? dataSet.getColor(j) : dataSet.getIncreasingColor());
                        }
                        else {
                            barColor = ((dataSet.getNeutralColor() == 1122867) ? dataSet.getColor(j) : dataSet.getNeutralColor());
                        }
                        this.mRenderPaint.setColor(barColor);
                        c.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
                        c.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
                        c.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
                    }
                }
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        if (this.mChart.getCandleData().getYValCount() < this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX()) {
            final List<ICandleDataSet> dataSets = this.mChart.getCandleData().getDataSets();
            for (int i = 0; i < dataSets.size(); ++i) {
                final ICandleDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        this.applyValueTextStyle(dataSet);
                        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        final int minx = Math.max(this.mMinX, 0);
                        final int maxx = Math.min(this.mMaxX + 1, dataSet.getEntryCount());
                        final float[] positions = trans.generateTransformedValuesCandle(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), minx, maxx);
                        final float yOffset = Utils.convertDpToPixel(5.0f);
                        for (int j = 0; j < positions.length; j += 2) {
                            final float x = positions[j];
                            final float y = positions[j + 1];
                            if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsLeft(x)) {
                                if (this.mViewPortHandler.isInBoundsY(y)) {
                                    final CandleEntry entry = dataSet.getEntryForIndex(j / 2 + minx);
                                    this.drawValue(c, dataSet.getValueFormatter(), entry.getHigh(), entry, i, x, y - yOffset, dataSet.getValueTextColor(j / 2));
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
        for (int i = 0; i < indices.length; ++i) {
            final int xIndex = indices[i].getXIndex();
            final ICandleDataSet set = this.mChart.getCandleData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final CandleEntry e = set.getEntryForXIndex(xIndex);
                    if (e != null) {
                        if (e.getXIndex() == xIndex) {
                            final float low = e.getLow() * this.mAnimator.getPhaseY();
                            final float high = e.getHigh() * this.mAnimator.getPhaseY();
                            final float y = (low + high) / 2.0f;
                            final float min = this.mChart.getYChartMin();
                            final float max = this.mChart.getYChartMax();
                            final float[] pts = { xIndex, y };
                            this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                            this.drawHighlightLines(c, pts, set);
                        }
                    }
                }
            }
        }
    }
}
