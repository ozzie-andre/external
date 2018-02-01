// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.Entry;
import android.graphics.PointF;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Path;
//import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.data.RadarData;
import android.graphics.Canvas;
import android.graphics.Color;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Paint;
import com.github.mikephil.charting.charts.RadarChart;

public class RadarChartRenderer extends LineRadarRenderer
{
    protected RadarChart mChart;
    protected Paint mWebPaint;
    protected Paint mHighlightCirclePaint;
    
    public RadarChartRenderer(final RadarChart chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
        (this.mWebPaint = new Paint(1)).setStyle(Paint.Style.STROKE);
        this.mHighlightCirclePaint = new Paint(1);
    }
    
    public Paint getWebPaint() {
        return this.mWebPaint;
    }
    
    @Override
    public void initBuffers() {
    }
    
    @Override
    public void drawData(final Canvas c) {
        final RadarData radarData = this.mChart.getData();
        int mostEntries = 0;
        for (final IRadarDataSet set : radarData.getDataSets()) {
            if (set.getEntryCount() > mostEntries) {
                mostEntries = set.getEntryCount();
            }
        }
        for (final IRadarDataSet set : radarData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set, mostEntries);
            }
        }
    }
    
    protected void drawDataSet(final Canvas c, final IRadarDataSet dataSet, final int mostEntries) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final PointF center = this.mChart.getCenterOffsets();
        final Path surface = new Path();
        boolean hasMovedToPoint = false;
        for (int j = 0; j < dataSet.getEntryCount(); ++j) {
            this.mRenderPaint.setColor(dataSet.getColor(j));
            final Entry e = dataSet.getEntryForIndex(j);
            final PointF p = Utils.getPosition(center, (e.getVal() - this.mChart.getYChartMin()) * factor * phaseY, sliceangle * j * phaseX + this.mChart.getRotationAngle());
            if (!Float.isNaN(p.x)) {
                if (!hasMovedToPoint) {
                    surface.moveTo(p.x, p.y);
                    hasMovedToPoint = true;
                }
                else {
                    surface.lineTo(p.x, p.y);
                }
            }
        }
        if (dataSet.getEntryCount() > mostEntries) {
            surface.lineTo(center.x, center.y);
        }
        surface.close();
        if (dataSet.isDrawFilledEnabled()) {
            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                this.drawFilledPath(c, surface, drawable);
            }
            else {
                this.drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }
        this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255) {
            c.drawPath(surface, this.mRenderPaint);
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final PointF center = this.mChart.getCenterOffsets();
        final float yoffset = Utils.convertDpToPixel(5.0f);
        for (int i = 0; i < this.mChart.getData().getDataSetCount(); ++i) {
            final IRadarDataSet dataSet = this.mChart.getData().getDataSetByIndex(i);
            if (dataSet.isDrawValuesEnabled()) {
                if (dataSet.getEntryCount() != 0) {
                    this.applyValueTextStyle(dataSet);
                    for (int j = 0; j < dataSet.getEntryCount(); ++j) {
                        final Entry entry = dataSet.getEntryForIndex(j);
                        final PointF p = Utils.getPosition(center, (entry.getVal() - this.mChart.getYChartMin()) * factor * phaseY, sliceangle * j * phaseX + this.mChart.getRotationAngle());
                        this.drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, p.x, p.y - yoffset, dataSet.getValueTextColor(j));
                    }
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas c) {
        this.drawWeb(c);
    }
    
    protected void drawWeb(final Canvas c) {
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final float rotationangle = this.mChart.getRotationAngle();
        final PointF center = this.mChart.getCenterOffsets();
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
        this.mWebPaint.setColor(this.mChart.getWebColor());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        for (int xIncrements = 1 + this.mChart.getSkipWebLineCount(), i = 0; i < this.mChart.getData().getXValCount(); i += xIncrements) {
            final PointF p = Utils.getPosition(center, this.mChart.getYRange() * factor, sliceangle * i + rotationangle);
            c.drawLine(center.x, center.y, p.x, p.y, this.mWebPaint);
        }
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
        this.mWebPaint.setColor(this.mChart.getWebColorInner());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        for (int labelCount = this.mChart.getYAxis().mEntryCount, j = 0; j < labelCount; ++j) {
            for (int k = 0; k < this.mChart.getData().getXValCount(); ++k) {
                final float r = (this.mChart.getYAxis().mEntries[j] - this.mChart.getYChartMin()) * factor;
                final PointF p2 = Utils.getPosition(center, r, sliceangle * k + rotationangle);
                final PointF p3 = Utils.getPosition(center, r, sliceangle * (k + 1) + rotationangle);
                c.drawLine(p2.x, p2.y, p3.x, p3.y, this.mWebPaint);
            }
        }
    }
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final PointF center = this.mChart.getCenterOffsets();
        for (int i = 0; i < indices.length; ++i) {
            final IRadarDataSet set = this.mChart.getData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final int xIndex = indices[i].getXIndex();
                    final Entry e = set.getEntryForXIndex(xIndex);
                    if (e != null) {
                        if (e.getXIndex() == xIndex) {
                            final int j = set.getEntryIndex(e);
                            final float y = e.getVal() - this.mChart.getYChartMin();
                            if (!Float.isNaN(y)) {
                                final PointF p = Utils.getPosition(center, y * factor * phaseY, sliceangle * j * phaseX + this.mChart.getRotationAngle());
                                final float[] pts = { p.x, p.y };
                                this.drawHighlightLines(c, pts, set);
                                if (set.isDrawHighlightCircleEnabled() && !Float.isNaN(pts[0]) && !Float.isNaN(pts[1])) {
                                    int strokeColor = set.getHighlightCircleStrokeColor();
                                    if (strokeColor == 1122867) {
                                        strokeColor = set.getColor(0);
                                    }
                                    if (set.getHighlightCircleStrokeAlpha() < 255) {
                                        strokeColor = ColorTemplate.getColorWithAlphaComponent(strokeColor, set.getHighlightCircleStrokeAlpha());
                                    }
                                    this.drawHighlightCircle(c, p, set.getHighlightCircleInnerRadius(), set.getHighlightCircleOuterRadius(), set.getHighlightCircleFillColor(), strokeColor, set.getHighlightCircleStrokeWidth());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void drawHighlightCircle(final Canvas c, final PointF point, float innerRadius, float outerRadius, final int fillColor, final int strokeColor, final float strokeWidth) {
        c.save();
        outerRadius = Utils.convertDpToPixel(outerRadius);
        innerRadius = Utils.convertDpToPixel(innerRadius);
        if (fillColor != 1122867) {
            final Path p = new Path();
            p.addCircle(point.x, point.y, outerRadius, Path.Direction.CW);
            if (innerRadius > 0.0f) {
                p.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW);
            }
            this.mHighlightCirclePaint.setColor(fillColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.FILL);
            c.drawPath(p, this.mHighlightCirclePaint);
        }
        if (strokeColor != 1122867) {
            this.mHighlightCirclePaint.setColor(strokeColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
            this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
            c.drawCircle(point.x, point.y, outerRadius, this.mHighlightCirclePaint);
        }
        c.restore();
    }
}
