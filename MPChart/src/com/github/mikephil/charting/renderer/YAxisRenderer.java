// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import java.util.List;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.components.YAxis;

public class YAxisRenderer extends AxisRenderer
{
    protected YAxis mYAxis;
    protected Paint mZeroLinePaint;
    
    public YAxisRenderer(final ViewPortHandler viewPortHandler, final YAxis yAxis, final Transformer trans) {
        super(viewPortHandler, trans);
        this.mYAxis = yAxis;
        this.mAxisLabelPaint.setColor(-16777216);
        this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0f));
        (this.mZeroLinePaint = new Paint(1)).setColor(-7829368);
        this.mZeroLinePaint.setStrokeWidth(1.0f);
        this.mZeroLinePaint.setStyle(Paint.Style.STROKE);
    }
    
    public void computeAxis(float yMin, float yMax) {
        if (this.mViewPortHandler.contentWidth() > 10.0f && !this.mViewPortHandler.isFullyZoomedOutY()) {
            final PointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            final PointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
            if (!this.mYAxis.isInverted()) {
                yMin = (float)p2.y;
                yMax = (float)p1.y;
            }
            else {
                yMin = (float)p1.y;
                yMax = (float)p2.y;
            }
        }
        this.computeAxisValues(yMin, yMax);
    }
    
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
        if (this.mYAxis.isGranularityEnabled()) {
            interval = ((interval < this.mYAxis.getGranularity()) ? this.mYAxis.getGranularity() : interval);
        }
        final double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10.0, (int)Math.log10(interval)));
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
            final double first = Math.ceil(min / interval) * interval;
            final double last = Utils.nextUp(Math.floor(max / interval) * interval);
            int n = 0;
            for (double f = first; f <= last; f += interval) {
                ++n;
            }
            if (this.mYAxis.mEntries.length < (this.mYAxis.mEntryCount = n)) {
                this.mYAxis.mEntries = new float[n];
            }
            double f = first;
            for (int j = 0; j < n; ++j) {
                if (f == 0.0) {
                    f = 0.0;
                }
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
    }
    
    @Override
    public void renderAxisLabels(final Canvas c) {
        if (!this.mYAxis.isEnabled() || !this.mYAxis.isDrawLabelsEnabled()) {
            return;
        }
        final float[] positions = new float[this.mYAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            positions[i + 1] = this.mYAxis.mEntries[i / 2];
        }
        this.mTrans.pointValuesToPixel(positions);
        this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
        final float xoffset = this.mYAxis.getXOffset();
        final float yoffset = Utils.calcTextHeight(this.mAxisLabelPaint, "A") / 2.5f + this.mYAxis.getYOffset();
        final YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
        final YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
        float xPos = 0.0f;
        if (dependency == YAxis.AxisDependency.LEFT) {
            if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                xPos = this.mViewPortHandler.offsetLeft() - xoffset;
            }
            else {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                xPos = this.mViewPortHandler.offsetLeft() + xoffset;
            }
        }
        else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
            this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
            xPos = this.mViewPortHandler.contentRight() + xoffset;
        }
        else {
            this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
            xPos = this.mViewPortHandler.contentRight() - xoffset;
        }
        this.drawYLabels(c, xPos, positions, yoffset);
    }
    
    @Override
    public void renderAxisLine(final Canvas c) {
        if (!this.mYAxis.isEnabled() || !this.mYAxis.isDrawAxisLineEnabled()) {
            return;
        }
        this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
        this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
        if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
        else {
            c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
    }
    
    protected void drawYLabels(final Canvas c, final float fixedPosition, final float[] positions, final float offset) {
        for (int i = 0; i < this.mYAxis.mEntryCount; ++i) {
            final String text = this.mYAxis.getFormattedLabel(i);
            if (!this.mYAxis.isDrawTopYLabelEntryEnabled() && i >= this.mYAxis.mEntryCount - 1) {
                return;
            }
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, this.mAxisLabelPaint);
        }
    }
    
    @Override
    public void renderGridLines(final Canvas c) {
        if (!this.mYAxis.isEnabled()) {
            return;
        }
        final float[] position = new float[2];
        if (this.mYAxis.isDrawGridLinesEnabled()) {
            this.mGridPaint.setColor(this.mYAxis.getGridColor());
            this.mGridPaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
            this.mGridPaint.setPathEffect((PathEffect)this.mYAxis.getGridDashPathEffect());
            final Path gridLinePath = new Path();
            for (int i = 0; i < this.mYAxis.mEntryCount; ++i) {
                position[1] = this.mYAxis.mEntries[i];
                this.mTrans.pointValuesToPixel(position);
                gridLinePath.moveTo(this.mViewPortHandler.offsetLeft(), position[1]);
                gridLinePath.lineTo(this.mViewPortHandler.contentRight(), position[1]);
                c.drawPath(gridLinePath, this.mGridPaint);
                gridLinePath.reset();
            }
        }
        if (this.mYAxis.isDrawZeroLineEnabled()) {
            position[1] = 0.0f;
            this.mTrans.pointValuesToPixel(position);
            this.drawZeroLine(c, this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.contentRight(), position[1] - 1.0f, position[1] - 1.0f);
        }
    }
    
    protected void drawZeroLine(final Canvas c, final float x1, final float x2, final float y1, final float y2) {
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        final Path zeroLinePath = new Path();
        zeroLinePath.moveTo(x1, y1);
        zeroLinePath.lineTo(x2, y2);
        c.drawPath(zeroLinePath, this.mZeroLinePaint);
    }
    
    @Override
    public void renderLimitLines(final Canvas c) {
        final List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines == null || limitLines.size() <= 0) {
            return;
        }
        final float[] pts = new float[2];
        final Path limitLinePath = new Path();
        for (int i = 0; i < limitLines.size(); ++i) {
            final LimitLine l = limitLines.get(i);
            if (l.isEnabled()) {
                this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
                this.mLimitLinePaint.setColor(l.getLineColor());
                this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                this.mLimitLinePaint.setPathEffect((PathEffect)l.getDashPathEffect());
                pts[1] = l.getLimit();
                this.mTrans.pointValuesToPixel(pts);
                limitLinePath.moveTo(this.mViewPortHandler.contentLeft(), pts[1]);
                limitLinePath.lineTo(this.mViewPortHandler.contentRight(), pts[1]);
                c.drawPath(limitLinePath, this.mLimitLinePaint);
                limitLinePath.reset();
                final String label = l.getLabel();
                if (label != null && !label.equals("")) {
                    this.mLimitLinePaint.setStyle(l.getTextStyle());
                    this.mLimitLinePaint.setPathEffect((PathEffect)null);
                    this.mLimitLinePaint.setColor(l.getTextColor());
                    this.mLimitLinePaint.setTypeface(l.getTypeface());
                    this.mLimitLinePaint.setStrokeWidth(0.5f);
                    this.mLimitLinePaint.setTextSize(l.getTextSize());
                    final float labelLineHeight = Utils.calcTextHeight(this.mLimitLinePaint, label);
                    final float xOffset = Utils.convertDpToPixel(4.0f) + l.getXOffset();
                    final float yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset();
                    final LimitLine.LimitLabelPosition position = l.getLabelPosition();
                    if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                        this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                        c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, pts[1] - yOffset + labelLineHeight, this.mLimitLinePaint);
                    }
                    else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                        this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                        c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                    }
                    else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                        this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                        c.drawText(label, this.mViewPortHandler.contentLeft() + xOffset, pts[1] - yOffset + labelLineHeight, this.mLimitLinePaint);
                    }
                    else {
                        this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                        c.drawText(label, this.mViewPortHandler.offsetLeft() + xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                    }
                }
            }
        }
    }
}
