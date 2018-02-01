// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.highlight.Highlight;
import android.os.Build;
import android.text.Layout;
import android.graphics.Color;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.Entry;
import android.graphics.PointF;
//import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.graphics.Paint;
import com.github.mikephil.charting.charts.PieChart;

public class PieChartRenderer extends DataRenderer
{
    protected PieChart mChart;
    protected Paint mHolePaint;
    protected Paint mTransparentCirclePaint;
    private TextPaint mCenterTextPaint;
    private StaticLayout mCenterTextLayout;
    private CharSequence mCenterTextLastValue;
    private RectF mCenterTextLastBounds;
    private RectF[] mRectBuffer;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Canvas mBitmapCanvas;
    private Path mPathBuffer;
    private RectF mInnerRectBuffer;
    private Path mHoleCirclePath;
    
    public PieChartRenderer(final PieChart chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mCenterTextLastBounds = new RectF();
        this.mRectBuffer = new RectF[] { new RectF(), new RectF(), new RectF() };
        this.mPathBuffer = new Path();
        this.mInnerRectBuffer = new RectF();
        this.mHoleCirclePath = new Path();
        this.mChart = chart;
        (this.mHolePaint = new Paint(1)).setColor(-1);
        this.mHolePaint.setStyle(Paint.Style.FILL);
        (this.mTransparentCirclePaint = new Paint(1)).setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        (this.mCenterTextPaint = new TextPaint(1)).setColor(-16777216);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
    }
    
    public Paint getPaintHole() {
        return this.mHolePaint;
    }
    
    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }
    
    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }
    
    @Override
    public void initBuffers() {
    }
    
    @Override
    public void drawData(final Canvas c) {
        final int width = (int)this.mViewPortHandler.getChartWidth();
        final int height = (int)this.mViewPortHandler.getChartHeight();
        if (this.mDrawBitmap == null || this.mDrawBitmap.get().getWidth() != width || this.mDrawBitmap.get().getHeight() != height) {
            if (width <= 0 || height <= 0) {
                return;
            }
            this.mDrawBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444));
            this.mBitmapCanvas = new Canvas((Bitmap)this.mDrawBitmap.get());
        }
        this.mDrawBitmap.get().eraseColor(0);
        final PieData pieData = this.mChart.getData();
        for (final IPieDataSet set : pieData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set);
            }
        }
    }
    
    protected float calculateMinimumRadiusForSpacedSlice(final PointF center, final float radius, final float angle, final float arcStartPointX, final float arcStartPointY, final float startAngle, final float sweepAngle) {
        final float angleMiddle = startAngle + sweepAngle / 2.0f;
        final float arcEndPointX = center.x + radius * (float)Math.cos((startAngle + sweepAngle) * 0.017453292f);
        final float arcEndPointY = center.y + radius * (float)Math.sin((startAngle + sweepAngle) * 0.017453292f);
        final float arcMidPointX = center.x + radius * (float)Math.cos(angleMiddle * 0.017453292f);
        final float arcMidPointY = center.y + radius * (float)Math.sin(angleMiddle * 0.017453292f);
        final double basePointsDistance = Math.sqrt(Math.pow(arcEndPointX - arcStartPointX, 2.0) + Math.pow(arcEndPointY - arcStartPointY, 2.0));
        final float containedTriangleHeight = (float)(basePointsDistance / 2.0 * Math.tan((180.0 - angle) / 2.0 * 0.017453292519943295));
        float spacedRadius = radius - containedTriangleHeight;
        spacedRadius -= (float)Math.sqrt(Math.pow(arcMidPointX - (arcEndPointX + arcStartPointX) / 2.0f, 2.0) + Math.pow(arcMidPointY - (arcEndPointY + arcStartPointY) / 2.0f, 2.0));
        return spacedRadius;
    }
    
    protected void drawDataSet(final Canvas c, final IPieDataSet dataSet) {
        float angle = 0.0f;
        final float rotationAngle = this.mChart.getRotationAngle();
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final RectF circleBox = this.mChart.getCircleBox();
        final int entryCount = dataSet.getEntryCount();
        final float[] drawAngles = this.mChart.getDrawAngles();
        final PointF center = this.mChart.getCenterCircleBox();
        final float radius = this.mChart.getRadius();
        final boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawInnerArc ? (radius * (this.mChart.getHoleRadius() / 100.0f)) : 0.0f;
        int visibleAngleCount = 0;
        for (int j = 0; j < entryCount; ++j) {
            if (Math.abs(dataSet.getEntryForIndex(j).getVal()) > 1.0E-6) {
                ++visibleAngleCount;
            }
        }
        final float sliceSpace = (visibleAngleCount <= 1) ? 0.0f : dataSet.getSliceSpace();
        for (int i = 0; i < entryCount; ++i) {
            final float sliceAngle = drawAngles[i];
            float innerRadius = userInnerRadius;
            final Entry e = dataSet.getEntryForIndex(i);
            if (Math.abs(e.getVal()) > 1.0E-6 && !this.mChart.needsHighlight(e.getXIndex(), this.mChart.getData().getIndexOfDataSet(dataSet))) {
                final boolean accountForSliceSpacing = sliceSpace > 0.0f && sliceAngle <= 180.0f;
                this.mRenderPaint.setColor(dataSet.getColor(i));
                final float sliceSpaceAngleOuter = (visibleAngleCount == 1) ? 0.0f : (sliceSpace / (0.017453292f * radius));
                final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.0f) * phaseY;
                float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                if (sweepAngleOuter < 0.0f) {
                    sweepAngleOuter = 0.0f;
                }
                this.mPathBuffer.reset();
                float arcStartPointX = 0.0f;
                float arcStartPointY = 0.0f;
                if (sweepAngleOuter % 360.0f == 0.0f) {
                    this.mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
                }
                else {
                    arcStartPointX = center.x + radius * (float)Math.cos(startAngleOuter * 0.017453292f);
                    arcStartPointY = center.y + radius * (float)Math.sin(startAngleOuter * 0.017453292f);
                    this.mPathBuffer.moveTo(arcStartPointX, arcStartPointY);
                    this.mPathBuffer.arcTo(circleBox, startAngleOuter, sweepAngleOuter);
                }
                this.mInnerRectBuffer.set(center.x - innerRadius, center.y - innerRadius, center.x + innerRadius, center.y + innerRadius);
                if (drawInnerArc && (innerRadius > 0.0f || accountForSliceSpacing)) {
                    if (accountForSliceSpacing) {
                        float minSpacedRadius = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                        if (minSpacedRadius < 0.0f) {
                            minSpacedRadius = -minSpacedRadius;
                        }
                        innerRadius = Math.max(innerRadius, minSpacedRadius);
                    }
                    final float sliceSpaceAngleInner = (visibleAngleCount == 1 || innerRadius == 0.0f) ? 0.0f : (sliceSpace / (0.017453292f * innerRadius));
                    final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.0f) * phaseY;
                    float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                    if (sweepAngleInner < 0.0f) {
                        sweepAngleInner = 0.0f;
                    }
                    final float endAngleInner = startAngleInner + sweepAngleInner;
                    if (sweepAngleOuter % 360.0f == 0.0f) {
                        this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                    }
                    else {
                        this.mPathBuffer.lineTo(center.x + innerRadius * (float)Math.cos(endAngleInner * 0.017453292f), center.y + innerRadius * (float)Math.sin(endAngleInner * 0.017453292f));
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                    }
                }
                else if (sweepAngleOuter % 360.0f != 0.0f) {
                    if (accountForSliceSpacing) {
                        final float angleMiddle = startAngleOuter + sweepAngleOuter / 2.0f;
                        final float sliceSpaceOffset = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                        final float arcEndPointX = center.x + sliceSpaceOffset * (float)Math.cos(angleMiddle * 0.017453292f);
                        final float arcEndPointY = center.y + sliceSpaceOffset * (float)Math.sin(angleMiddle * 0.017453292f);
                        this.mPathBuffer.lineTo(arcEndPointX, arcEndPointY);
                    }
                    else {
                        this.mPathBuffer.lineTo(center.x, center.y);
                    }
                }
                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
            }
            angle += sliceAngle * phaseX;
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        final PointF center = this.mChart.getCenterCircleBox();
        float r = this.mChart.getRadius();
        final float rotationAngle = this.mChart.getRotationAngle();
        final float[] drawAngles = this.mChart.getDrawAngles();
        final float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        float off = r / 10.0f * 3.6f;
        if (this.mChart.isDrawHoleEnabled()) {
            off = (r - r / 100.0f * this.mChart.getHoleRadius()) / 2.0f;
        }
        r -= off;
        final PieData data = this.mChart.getData();
        final List<IPieDataSet> dataSets = data.getDataSets();
        final float yValueSum = data.getYValueSum();
        final boolean drawXVals = this.mChart.isDrawSliceTextEnabled();
        int xIndex = 0;
        for (int i = 0; i < dataSets.size(); ++i) {
            final IPieDataSet dataSet = dataSets.get(i);
            if (dataSet.isDrawValuesEnabled() || drawXVals) {
                this.applyValueTextStyle(dataSet);
                final float lineHeight = Utils.calcTextHeight(this.mValuePaint, "Q") + Utils.convertDpToPixel(4.0f);
                final int entryCount = dataSet.getEntryCount();
                for (int j = 0, maxEntry = Math.min((int)Math.ceil(entryCount * phaseX), entryCount); j < maxEntry; ++j) {
                    final Entry entry = dataSet.getEntryForIndex(j);
                    float angle;
                    if (xIndex == 0) {
                        angle = 0.0f;
                    }
                    else {
                        angle = absoluteAngles[xIndex - 1] * phaseX;
                    }
                    final float sliceAngle = drawAngles[xIndex];
                    final float sliceSpace = dataSet.getSliceSpace();
                    final float sliceSpaceMiddleAngle = sliceSpace / (0.017453292f * r);
                    final float offset = (sliceAngle - sliceSpaceMiddleAngle / 2.0f) / 2.0f;
                    angle += offset;
                    final float x = (float)(r * Math.cos(Math.toRadians(rotationAngle + angle)) + center.x);
                    final float y = (float)(r * Math.sin(Math.toRadians(rotationAngle + angle)) + center.y);
                    final float value = this.mChart.isUsePercentValuesEnabled() ? (entry.getVal() / yValueSum * 100.0f) : entry.getVal();
                    final ValueFormatter formatter = dataSet.getValueFormatter();
                    final boolean drawYVals = dataSet.isDrawValuesEnabled();
                    if (drawXVals && drawYVals) {
                        this.drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));
                        if (j < data.getXValCount()) {
                            c.drawText((String)data.getXVals().get(j), x, y + lineHeight, this.mValuePaint);
                        }
                    }
                    else if (drawXVals) {
                        if (j < data.getXValCount()) {
                            this.mValuePaint.setColor(dataSet.getValueTextColor(j));
                            c.drawText((String)data.getXVals().get(j), x, y + lineHeight / 2.0f, this.mValuePaint);
                        }
                    }
                    else if (drawYVals) {
                        this.drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2.0f, dataSet.getValueTextColor(j));
                    }
                    ++xIndex;
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas c) {
        this.drawHole(c);
        c.drawBitmap((Bitmap)this.mDrawBitmap.get(), 0.0f, 0.0f, (Paint)null);
        this.drawCenterText(c);
    }
    
    protected void drawHole(final Canvas c) {
        if (this.mChart.isDrawHoleEnabled()) {
            final float radius = this.mChart.getRadius();
            final float holeRadius = radius * (this.mChart.getHoleRadius() / 100.0f);
            final PointF center = this.mChart.getCenterCircleBox();
            if (Color.alpha(this.mHolePaint.getColor()) > 0) {
                this.mBitmapCanvas.drawCircle(center.x, center.y, holeRadius, this.mHolePaint);
            }
            if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
                final int alpha = this.mTransparentCirclePaint.getAlpha();
                final float secondHoleRadius = radius * (this.mChart.getTransparentCircleRadius() / 100.0f);
                this.mTransparentCirclePaint.setAlpha((int)(alpha * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
                this.mHoleCirclePath.reset();
                this.mHoleCirclePath.addCircle(center.x, center.y, secondHoleRadius, Path.Direction.CW);
                this.mHoleCirclePath.addCircle(center.x, center.y, holeRadius, Path.Direction.CCW);
                this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
                this.mTransparentCirclePaint.setAlpha(alpha);
            }
        }
    }
    
    protected void drawCenterText(final Canvas c) {
        final CharSequence centerText = this.mChart.getCenterText();
        if (this.mChart.isDrawCenterTextEnabled() && centerText != null) {
            final PointF center = this.mChart.getCenterCircleBox();
            final float innerRadius = (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) ? (this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f)) : this.mChart.getRadius();
            final RectF holeRect = this.mRectBuffer[0];
            holeRect.left = center.x - innerRadius;
            holeRect.top = center.y - innerRadius;
            holeRect.right = center.x + innerRadius;
            holeRect.bottom = center.y + innerRadius;
            final RectF boundingRect = this.mRectBuffer[1];
            boundingRect.set(holeRect);
            final float radiusPercent = this.mChart.getCenterTextRadiusPercent() / 100.0f;
            if (radiusPercent > 0.0) {
                boundingRect.inset((boundingRect.width() - boundingRect.width() * radiusPercent) / 2.0f, (boundingRect.height() - boundingRect.height() * radiusPercent) / 2.0f);
            }
            if (!centerText.equals(this.mCenterTextLastValue) || !boundingRect.equals((Object)this.mCenterTextLastBounds)) {
                this.mCenterTextLastBounds.set(boundingRect);
                this.mCenterTextLastValue = centerText;
                final float width = this.mCenterTextLastBounds.width();
                this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int)Math.max(Math.ceil(width), 1.0), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
            final float layoutHeight = this.mCenterTextLayout.getHeight();
            c.save();
            if (Build.VERSION.SDK_INT >= 18) {
                final Path path = new Path();
                path.addOval(holeRect, Path.Direction.CW);
                c.clipPath(path);
            }
            c.translate(boundingRect.left, boundingRect.top + (boundingRect.height() - layoutHeight) / 2.0f);
            this.mCenterTextLayout.draw(c);
            c.restore();
        }
    }
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float rotationAngle = this.mChart.getRotationAngle();
        final float[] drawAngles = this.mChart.getDrawAngles();
        final float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        final PointF center = this.mChart.getCenterCircleBox();
        final float radius = this.mChart.getRadius();
        final boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawInnerArc ? (radius * (this.mChart.getHoleRadius() / 100.0f)) : 0.0f;
        final RectF highlightedCircleBox = new RectF();
        for (int i = 0; i < indices.length; ++i) {
            final int xIndex = indices[i].getXIndex();
            if (xIndex < drawAngles.length) {
                final IPieDataSet set = this.mChart.getData().getDataSetByIndex(indices[i].getDataSetIndex());
                if (set != null) {
                    if (set.isHighlightEnabled()) {
                        final int entryCount = set.getEntryCount();
                        int visibleAngleCount = 0;
                        for (int j = 0; j < entryCount; ++j) {
                            if (Math.abs(set.getEntryForIndex(j).getVal()) > 1.0E-6) {
                                ++visibleAngleCount;
                            }
                        }
                        float angle;
                        if (xIndex == 0) {
                            angle = 0.0f;
                        }
                        else {
                            angle = absoluteAngles[xIndex - 1] * phaseX;
                        }
                        final float sliceSpace = (visibleAngleCount <= 1) ? 0.0f : set.getSliceSpace();
                        final float sliceAngle = drawAngles[xIndex];
                        float innerRadius = userInnerRadius;
                        final float shift = set.getSelectionShift();
                        final float highlightedRadius = radius + shift;
                        highlightedCircleBox.set(this.mChart.getCircleBox());
                        highlightedCircleBox.inset(-shift, -shift);
                        final boolean accountForSliceSpacing = sliceSpace > 0.0f && sliceAngle <= 180.0f;
                        this.mRenderPaint.setColor(set.getColor(xIndex));
                        final float sliceSpaceAngleOuter = (visibleAngleCount == 1) ? 0.0f : (sliceSpace / (0.017453292f * radius));
                        final float sliceSpaceAngleShifted = (visibleAngleCount == 1) ? 0.0f : (sliceSpace / (0.017453292f * highlightedRadius));
                        final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.0f) * phaseY;
                        float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                        if (sweepAngleOuter < 0.0f) {
                            sweepAngleOuter = 0.0f;
                        }
                        final float startAngleShifted = rotationAngle + (angle + sliceSpaceAngleShifted / 2.0f) * phaseY;
                        float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
                        if (sweepAngleShifted < 0.0f) {
                            sweepAngleShifted = 0.0f;
                        }
                        this.mPathBuffer.reset();
                        if (sweepAngleOuter % 360.0f == 0.0f) {
                            this.mPathBuffer.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
                        }
                        else {
                            this.mPathBuffer.moveTo(center.x + highlightedRadius * (float)Math.cos(startAngleShifted * 0.017453292f), center.y + highlightedRadius * (float)Math.sin(startAngleShifted * 0.017453292f));
                            this.mPathBuffer.arcTo(highlightedCircleBox, startAngleShifted, sweepAngleShifted);
                        }
                        float sliceSpaceRadius = 0.0f;
                        if (accountForSliceSpacing) {
                            sliceSpaceRadius = this.calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, center.x + radius * (float)Math.cos(startAngleOuter * 0.017453292f), center.y + radius * (float)Math.sin(startAngleOuter * 0.017453292f), startAngleOuter, sweepAngleOuter);
                        }
                        this.mInnerRectBuffer.set(center.x - innerRadius, center.y - innerRadius, center.x + innerRadius, center.y + innerRadius);
                        if (drawInnerArc && (innerRadius > 0.0f || accountForSliceSpacing)) {
                            if (accountForSliceSpacing) {
                                float minSpacedRadius = sliceSpaceRadius;
                                if (minSpacedRadius < 0.0f) {
                                    minSpacedRadius = -minSpacedRadius;
                                }
                                innerRadius = Math.max(innerRadius, minSpacedRadius);
                            }
                            final float sliceSpaceAngleInner = (visibleAngleCount == 1 || innerRadius == 0.0f) ? 0.0f : (sliceSpace / (0.017453292f * innerRadius));
                            final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.0f) * phaseY;
                            float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                            if (sweepAngleInner < 0.0f) {
                                sweepAngleInner = 0.0f;
                            }
                            final float endAngleInner = startAngleInner + sweepAngleInner;
                            if (sweepAngleOuter % 360.0f == 0.0f) {
                                this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                            }
                            else {
                                this.mPathBuffer.lineTo(center.x + innerRadius * (float)Math.cos(endAngleInner * 0.017453292f), center.y + innerRadius * (float)Math.sin(endAngleInner * 0.017453292f));
                                this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                            }
                        }
                        else if (sweepAngleOuter % 360.0f != 0.0f) {
                            if (accountForSliceSpacing) {
                                final float angleMiddle = startAngleOuter + sweepAngleOuter / 2.0f;
                                final float arcEndPointX = center.x + sliceSpaceRadius * (float)Math.cos(angleMiddle * 0.017453292f);
                                final float arcEndPointY = center.y + sliceSpaceRadius * (float)Math.sin(angleMiddle * 0.017453292f);
                                this.mPathBuffer.lineTo(arcEndPointX, arcEndPointY);
                            }
                            else {
                                this.mPathBuffer.lineTo(center.x, center.y);
                            }
                        }
                        this.mPathBuffer.close();
                        this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                    }
                }
            }
        }
    }
    
    protected void drawRoundedSlices(final Canvas c) {
        if (!this.mChart.isDrawRoundedSlicesEnabled()) {
            return;
        }
        final IPieDataSet dataSet = this.mChart.getData().getDataSet();
        if (!dataSet.isVisible()) {
            return;
        }
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final PointF center = this.mChart.getCenterCircleBox();
        final float r = this.mChart.getRadius();
        final float circleRadius = (r - r * this.mChart.getHoleRadius() / 100.0f) / 2.0f;
        final float[] drawAngles = this.mChart.getDrawAngles();
        float angle = this.mChart.getRotationAngle();
        for (int j = 0; j < dataSet.getEntryCount(); ++j) {
            final float sliceAngle = drawAngles[j];
            final Entry e = dataSet.getEntryForIndex(j);
            if (Math.abs(e.getVal()) > 1.0E-6) {
                final float x = (float)((r - circleRadius) * Math.cos(Math.toRadians((angle + sliceAngle) * phaseY)) + center.x);
                final float y = (float)((r - circleRadius) * Math.sin(Math.toRadians((angle + sliceAngle) * phaseY)) + center.y);
                this.mRenderPaint.setColor(dataSet.getColor(j));
                this.mBitmapCanvas.drawCircle(x, y, circleRadius, this.mRenderPaint);
            }
            angle += sliceAngle * phaseX;
        }
    }
    
    public void releaseBitmap() {
        if (this.mDrawBitmap != null) {
            this.mDrawBitmap.get().recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
