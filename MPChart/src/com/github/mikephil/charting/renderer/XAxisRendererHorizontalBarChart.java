// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import android.graphics.PathEffect;
import android.graphics.Paint;
import com.github.mikephil.charting.components.LimitLine;
import android.graphics.Path;
import com.github.mikephil.charting.data.BarData;
import android.graphics.PointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class XAxisRendererHorizontalBarChart extends XAxisRendererBarChart
{
    public XAxisRendererHorizontalBarChart(final ViewPortHandler viewPortHandler, final XAxis xAxis, final Transformer trans, final BarChart chart) {
        super(viewPortHandler, xAxis, trans, chart);
    }
    
    @Override
    public void computeAxis(final float xValAverageLength, final List<String> xValues) {
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        this.mXAxis.setValues(xValues);
        final String longest = this.mXAxis.getLongestLabel();
        final FSize labelSize = Utils.calcTextSize(this.mAxisLabelPaint, longest);
        final float labelWidth = (int)(labelSize.width + this.mXAxis.getXOffset() * 3.5f);
        final float labelHeight = labelSize.height;
        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelSize.width, labelHeight, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(labelWidth);
        this.mXAxis.mLabelHeight = Math.round(labelHeight);
        this.mXAxis.mLabelRotatedWidth = (int)(labelRotatedSize.width + this.mXAxis.getXOffset() * 3.5f);
        this.mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);
    }
    
    @Override
    public void renderAxisLabels(final Canvas c) {
        if (!this.mXAxis.isEnabled() || !this.mXAxis.isDrawLabelsEnabled()) {
            return;
        }
        final float xoffset = this.mXAxis.getXOffset();
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
            this.drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, new PointF(0.0f, 0.5f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
            this.drawLabels(c, this.mViewPortHandler.contentRight() - xoffset, new PointF(1.0f, 0.5f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            this.drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, new PointF(1.0f, 0.5f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
            this.drawLabels(c, this.mViewPortHandler.contentLeft() + xoffset, new PointF(0.0f, 0.5f));
        }
        else {
            this.drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, new PointF(0.0f, 0.5f));
            this.drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, new PointF(1.0f, 0.5f));
        }
    }
    
    @Override
    protected void drawLabels(final Canvas c, final float pos, final PointF anchor) {
        final float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        final float[] position = { 0.0f, 0.0f };
        final BarData bd = this.mChart.getData();
        final int step = bd.getDataSetCount();
        for (int i = this.mMinX; i <= this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[1] = i * step + i * bd.getGroupSpace() + bd.getGroupSpace() / 2.0f;
            if (step > 1) {
                final float[] array = position;
                final int n = 1;
                array[n] += (step - 1.0f) / 2.0f;
            }
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsY(position[1])) {
                final String label = this.mXAxis.getValues().get(i);
                this.drawLabel(c, label, i, pos, position[1], anchor, labelRotationAngleDegrees);
            }
        }
    }
    
    @Override
    public void renderGridLines(final Canvas c) {
        if (!this.mXAxis.isDrawGridLinesEnabled() || !this.mXAxis.isEnabled()) {
            return;
        }
        final float[] position = { 0.0f, 0.0f };
        this.mGridPaint.setColor(this.mXAxis.getGridColor());
        this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
        final BarData bd = this.mChart.getData();
        final int step = bd.getDataSetCount();
        for (int i = this.mMinX; i <= this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[1] = i * step + i * bd.getGroupSpace() - 0.5f;
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsY(position[1])) {
                c.drawLine(this.mViewPortHandler.contentLeft(), position[1], this.mViewPortHandler.contentRight(), position[1], this.mGridPaint);
            }
        }
    }
    
    @Override
    public void renderAxisLine(final Canvas c) {
        if (!this.mXAxis.isDrawAxisLineEnabled() || !this.mXAxis.isEnabled()) {
            return;
        }
        this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
        this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas c) {
        final List<LimitLine> limitLines = this.mXAxis.getLimitLines();
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
