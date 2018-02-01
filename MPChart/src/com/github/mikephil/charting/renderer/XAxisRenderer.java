// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.components.LimitLine;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.FSize;
import java.util.List;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Path;
import com.github.mikephil.charting.components.XAxis;

public class XAxisRenderer extends AxisRenderer
{
    protected XAxis mXAxis;
    float[] mLimitLineSegmentsBuffer;
    private Path mLimitLinePath;
    
    public XAxisRenderer(final ViewPortHandler viewPortHandler, final XAxis xAxis, final Transformer trans) {
        super(viewPortHandler, trans);
        this.mLimitLineSegmentsBuffer = new float[4];
        this.mLimitLinePath = new Path();
        this.mXAxis = xAxis;
        this.mAxisLabelPaint.setColor(-16777216);
        this.mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
        this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0f));
    }
    
    public void computeAxis(final float xValMaximumLength, final List<String> xValues) {
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        final StringBuilder widthText = new StringBuilder();
        for (int xValChars = Math.round(xValMaximumLength), i = 0; i < xValChars; ++i) {
            widthText.append('h');
        }
        final FSize labelSize = Utils.calcTextSize(this.mAxisLabelPaint, widthText.toString());
        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelWidth, labelHeight, this.mXAxis.getLabelRotationAngle());
        final StringBuilder space = new StringBuilder();
        for (int xValSpaceChars = this.mXAxis.getSpaceBetweenLabels(), j = 0; j < xValSpaceChars; ++j) {
            space.append('h');
        }
        final FSize spaceSize = Utils.calcTextSize(this.mAxisLabelPaint, space.toString());
        this.mXAxis.mLabelWidth = Math.round(labelWidth + spaceSize.width);
        this.mXAxis.mLabelHeight = Math.round(labelHeight);
        this.mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width + spaceSize.width);
        this.mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);
        this.mXAxis.setValues(xValues);
    }
    
    @Override
    public void renderAxisLabels(final Canvas c) {
        if (!this.mXAxis.isEnabled() || !this.mXAxis.isDrawLabelsEnabled()) {
            return;
        }
        final float yoffset = this.mXAxis.getYOffset();
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
            this.drawLabels(c, this.mViewPortHandler.contentTop() - yoffset, new PointF(0.5f, 1.0f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
            this.drawLabels(c, this.mViewPortHandler.contentTop() + yoffset + this.mXAxis.mLabelRotatedHeight, new PointF(0.5f, 1.0f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            this.drawLabels(c, this.mViewPortHandler.contentBottom() + yoffset, new PointF(0.5f, 0.0f));
        }
        else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
            this.drawLabels(c, this.mViewPortHandler.contentBottom() - yoffset - this.mXAxis.mLabelRotatedHeight, new PointF(0.5f, 0.0f));
        }
        else {
            this.drawLabels(c, this.mViewPortHandler.contentTop() - yoffset, new PointF(0.5f, 1.0f));
            this.drawLabels(c, this.mViewPortHandler.contentBottom() + yoffset, new PointF(0.5f, 0.0f));
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
            c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
        }
        if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
    }
    
    protected void drawLabels(final Canvas c, final float pos, final PointF anchor) {
        final float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        final float[] position = { 0.0f, 0.0f };
        for (int i = this.mMinX; i <= this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[0] = i;
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsX(position[0])) {
                final String label = this.mXAxis.getValues().get(i);
                if (this.mXAxis.isAvoidFirstLastClippingEnabled()) {
                    if (i == this.mXAxis.getValues().size() - 1 && this.mXAxis.getValues().size() > 1) {
                        final float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        if (width > this.mViewPortHandler.offsetRight() * 2.0f && position[0] + width > this.mViewPortHandler.getChartWidth()) {
                            final float[] array = position;
                            final int n = 0;
                            array[n] -= width / 2.0f;
                        }
                    }
                    else if (i == 0) {
                        final float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        final float[] array2 = position;
                        final int n2 = 0;
                        array2[n2] += width / 2.0f;
                    }
                }
                this.drawLabel(c, label, i, position[0], pos, anchor, labelRotationAngleDegrees);
            }
        }
    }
    
    protected void drawLabel(final Canvas c, final String label, final int xIndex, final float x, final float y, final PointF anchor, final float angleDegrees) {
        final String formattedLabel = this.mXAxis.getValueFormatter().getXValue(label, xIndex, this.mViewPortHandler);
        Utils.drawText(c, formattedLabel, x, y, this.mAxisLabelPaint, anchor, angleDegrees);
    }
    
    @Override
    public void renderGridLines(final Canvas c) {
        if (!this.mXAxis.isDrawGridLinesEnabled() || !this.mXAxis.isEnabled()) {
            return;
        }
        final float[] position = { 0.0f, 0.0f };
        this.mGridPaint.setColor(this.mXAxis.getGridColor());
        this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
        this.mGridPaint.setPathEffect((PathEffect)this.mXAxis.getGridDashPathEffect());
        final Path gridLinePath = new Path();
        for (int i = this.mMinX; i <= this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[0] = i;
            this.mTrans.pointValuesToPixel(position);
            if (position[0] >= this.mViewPortHandler.offsetLeft() && position[0] <= this.mViewPortHandler.getChartWidth()) {
                gridLinePath.moveTo(position[0], this.mViewPortHandler.contentBottom());
                gridLinePath.lineTo(position[0], this.mViewPortHandler.contentTop());
                c.drawPath(gridLinePath, this.mGridPaint);
            }
            gridLinePath.reset();
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas c) {
        final List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines == null || limitLines.size() <= 0) {
            return;
        }
        final float[] position = new float[2];
        for (int i = 0; i < limitLines.size(); ++i) {
            final LimitLine l = limitLines.get(i);
            if (l.isEnabled()) {
                position[0] = l.getLimit();
                position[1] = 0.0f;
                this.mTrans.pointValuesToPixel(position);
                this.renderLimitLineLine(c, l, position);
                this.renderLimitLineLabel(c, l, position, 2.0f + l.getYOffset());
            }
        }
    }
    
    public void renderLimitLineLine(final Canvas c, final LimitLine limitLine, final float[] position) {
        this.mLimitLineSegmentsBuffer[0] = position[0];
        this.mLimitLineSegmentsBuffer[1] = this.mViewPortHandler.contentTop();
        this.mLimitLineSegmentsBuffer[2] = position[0];
        this.mLimitLineSegmentsBuffer[3] = this.mViewPortHandler.contentBottom();
        this.mLimitLinePath.reset();
        this.mLimitLinePath.moveTo(this.mLimitLineSegmentsBuffer[0], this.mLimitLineSegmentsBuffer[1]);
        this.mLimitLinePath.lineTo(this.mLimitLineSegmentsBuffer[2], this.mLimitLineSegmentsBuffer[3]);
        this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
        this.mLimitLinePaint.setColor(limitLine.getLineColor());
        this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        this.mLimitLinePaint.setPathEffect((PathEffect)limitLine.getDashPathEffect());
        c.drawPath(this.mLimitLinePath, this.mLimitLinePaint);
    }
    
    public void renderLimitLineLabel(final Canvas c, final LimitLine limitLine, final float[] position, final float yOffset) {
        final String label = limitLine.getLabel();
        if (label != null && !label.equals("")) {
            this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
            this.mLimitLinePaint.setPathEffect((PathEffect)null);
            this.mLimitLinePaint.setColor(limitLine.getTextColor());
            this.mLimitLinePaint.setStrokeWidth(0.5f);
            this.mLimitLinePaint.setTextSize(limitLine.getTextSize());
            final float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();
            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                final float labelLineHeight = Utils.calcTextHeight(this.mLimitLinePaint, label);
                this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                c.drawText(label, position[0] + xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
            }
            else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                c.drawText(label, position[0] + xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
            }
            else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                final float labelLineHeight = Utils.calcTextHeight(this.mLimitLinePaint, label);
                c.drawText(label, position[0] - xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
            }
            else {
                this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                c.drawText(label, position[0] - xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
            }
        }
    }
}
