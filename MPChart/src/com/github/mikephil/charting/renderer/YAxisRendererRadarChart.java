// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
//import com.github.mikephil.charting.data.RadarData;
import android.graphics.Path;
import android.graphics.PathEffect;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.PointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
//import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.charts.RadarChart;

public class YAxisRendererRadarChart extends YAxisRenderer
{
    private RadarChart mChart;
    
    public YAxisRendererRadarChart(final ViewPortHandler viewPortHandler, final YAxis yAxis, final RadarChart chart) {
        super(viewPortHandler, yAxis, null);
        this.mChart = chart;
    }
    
    @Override
    public void computeAxis(final float yMin, final float yMax) {
        this.computeAxisValues(yMin, yMax);
    }
    
    @Override
    protected void computeAxisValues(final float min, final float max) {
        final int labelCount = this.mYAxis.getLabelCount();
        final double range = Math.abs(max - min);
        if (labelCount == 0 || range <= 0.0) {
            this.mYAxis.mEntries = new float[0];
            this.mYAxis.mEntryCount = 0;
            return;
        }
        final double rawInterval = range / labelCount;
        double interval = Utils.roundToNextSignificant(rawInterval);
        final double intervalMagnitude = Math.pow(10.0, (int)Math.log10(interval));
        final int intervalSigDigit = (int)(interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            interval = Math.floor(10.0 * intervalMagnitude);
        }
        if (this.mYAxis.isForceLabelsEnabled()) {
            final float step = (float)range / (labelCount - 1);
            if (this.mYAxis.mEntries.length < (this.mYAxis.mEntryCount = labelCount)) {
                this.mYAxis.mEntries = new float[labelCount];
            }
            float v = min;
            for (int i = 0; i < labelCount; ++i) {
                this.mYAxis.mEntries[i] = v;
                v += step;
            }
        }
        else if (this.mYAxis.isShowOnlyMinMaxEnabled()) {
            this.mYAxis.mEntryCount = 2;
            (this.mYAxis.mEntries = new float[2])[0] = min;
            this.mYAxis.mEntries[1] = max;
        }
        else {
            final double rawCount = min / interval;
            double first = (rawCount < 0.0) ? (Math.floor(rawCount) * interval) : (Math.ceil(rawCount) * interval);
            if (first == 0.0) {
                first = 0.0;
            }
            final double last = Utils.nextUp(Math.floor(max / interval) * interval);
            int n = 0;
            for (double f = first; f <= last; f += interval) {
                ++n;
            }
            if (!this.mYAxis.isAxisMaxCustom()) {
                ++n;
            }
            if (this.mYAxis.mEntries.length < (this.mYAxis.mEntryCount = n)) {
                this.mYAxis.mEntries = new float[n];
            }
            double f = first;
            for (int j = 0; j < n; ++j) {
                this.mYAxis.mEntries[j] = (float)f;
                f += interval;
            }
        }
        if (interval < 1.0) {
            this.mYAxis.mDecimals = (int)Math.ceil(-Math.log10(interval));
        }
        else {
            this.mYAxis.mDecimals = 0;
        }
        if (this.mYAxis.mEntries[0] < min) {
            this.mYAxis.mAxisMinimum = this.mYAxis.mEntries[0];
        }
        this.mYAxis.mAxisMaximum = this.mYAxis.mEntries[this.mYAxis.mEntryCount - 1];
        this.mYAxis.mAxisRange = Math.abs(this.mYAxis.mAxisMaximum - this.mYAxis.mAxisMinimum);
    }
    
    @Override
    public void renderAxisLabels(final Canvas c) {
        if (!this.mYAxis.isEnabled() || !this.mYAxis.isDrawLabelsEnabled()) {
            return;
        }
        this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
        final PointF center = this.mChart.getCenterOffsets();
        final float factor = this.mChart.getFactor();
        for (int labelCount = this.mYAxis.mEntryCount, j = 0; j < labelCount && (j != labelCount - 1 || this.mYAxis.isDrawTopYLabelEntryEnabled()); ++j) {
            final float r = (this.mYAxis.mEntries[j] - this.mYAxis.mAxisMinimum) * factor;
            final PointF p = Utils.getPosition(center, r, this.mChart.getRotationAngle());
            final String label = this.mYAxis.getFormattedLabel(j);
            c.drawText(label, p.x + 10.0f, p.y, this.mAxisLabelPaint);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas c) {
        final List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines == null) {
            return;
        }
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final PointF center = this.mChart.getCenterOffsets();
        for (int i = 0; i < limitLines.size(); ++i) {
            final LimitLine l = limitLines.get(i);
            if (l.isEnabled()) {
                this.mLimitLinePaint.setColor(l.getLineColor());
                this.mLimitLinePaint.setPathEffect((PathEffect)l.getDashPathEffect());
                this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                final float r = (l.getLimit() - this.mChart.getYChartMin()) * factor;
                final Path limitPath = new Path();
                for (int j = 0; j < this.mChart.getData().getXValCount(); ++j) {
                    final PointF p = Utils.getPosition(center, r, sliceangle * j + this.mChart.getRotationAngle());
                    if (j == 0) {
                        limitPath.moveTo(p.x, p.y);
                    }
                    else {
                        limitPath.lineTo(p.x, p.y);
                    }
                }
                limitPath.close();
                c.drawPath(limitPath, this.mLimitLinePaint);
            }
        }
    }
}
