// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

//import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.RectF;
import android.graphics.PointF;
import java.util.List;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.data.DataSet;
import android.graphics.PathEffect;
//import java.util.Iterator;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import android.graphics.Paint;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;

public class LineChartRenderer extends LineRadarRenderer
{
    protected LineDataProvider mChart;
    protected Paint mCirclePaintInner;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Canvas mBitmapCanvas;
    protected Bitmap.Config mBitmapConfig;
    protected Path cubicPath;
    protected Path cubicFillPath;
    private float[] mLineBuffer;
    
    public LineChartRenderer(final LineDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mBitmapConfig = Bitmap.Config.ARGB_8888;
        this.cubicPath = new Path();
        this.cubicFillPath = new Path();
        this.mLineBuffer = new float[4];
        this.mChart = chart;
        (this.mCirclePaintInner = new Paint(1)).setStyle(Paint.Style.FILL);
        this.mCirclePaintInner.setColor(-1);
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
            this.mDrawBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(width, height, this.mBitmapConfig));
            this.mBitmapCanvas = new Canvas((Bitmap)this.mDrawBitmap.get());
        }
        this.mDrawBitmap.get().eraseColor(0);
        final LineData lineData = this.mChart.getLineData();
        for (final ILineDataSet set : lineData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                this.drawDataSet(c, set);
            }
        }
        c.drawBitmap((Bitmap)this.mDrawBitmap.get(), 0.0f, 0.0f, this.mRenderPaint);
    }
    
    protected void drawDataSet(final Canvas c, final ILineDataSet dataSet) {
        if (dataSet.getEntryCount() < 1) {
            return;
        }
        this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        this.mRenderPaint.setPathEffect((PathEffect)dataSet.getDashPathEffect());
        if (dataSet.isDrawCubicEnabled()) {
            this.drawCubic(c, dataSet);
        }
        else {
            this.drawLinear(c, dataSet);
        }
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    protected void drawCubic(final Canvas c, final ILineDataSet dataSet) {
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        final int entryCount = dataSet.getEntryCount();
        final Entry entryFrom = dataSet.getEntryForXIndex((this.mMinX < 0) ? 0 : this.mMinX, DataSet.Rounding.DOWN);
        final Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
        final int diff = (entryFrom == entryTo) ? 1 : 0;
        final int minx = Math.max(dataSet.getEntryIndex(entryFrom) - diff, 0);
        final int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float intensity = dataSet.getCubicIntensity();
        this.cubicPath.reset();
        final int size = (int)Math.ceil((maxx - minx) * phaseX + minx);
        if (size - minx >= 2) {
            float prevDx = 0.0f;
            float prevDy = 0.0f;
            float curDx = 0.0f;
            float curDy = 0.0f;
            Entry prevPrev = dataSet.getEntryForIndex(minx);
            Entry cur;
            Entry prev = cur = prevPrev;
            Entry next = dataSet.getEntryForIndex(minx + 1);
            this.cubicPath.moveTo((float)cur.getXIndex(), cur.getVal() * phaseY);
            for (int j = minx + 1, count = Math.min(size, entryCount - 1); j < count; ++j) {
                prevPrev = dataSet.getEntryForIndex((j == 1) ? 0 : (j - 2));
                prev = dataSet.getEntryForIndex(j - 1);
                cur = dataSet.getEntryForIndex(j);
                next = dataSet.getEntryForIndex(j + 1);
                prevDx = (cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (next.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (next.getVal() - prev.getVal()) * intensity;
                this.cubicPath.cubicTo(prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY, cur.getXIndex() - curDx, (cur.getVal() - curDy) * phaseY, (float)cur.getXIndex(), cur.getVal() * phaseY);
            }
            if (size > entryCount - 1) {
                prevPrev = dataSet.getEntryForIndex((entryCount >= 3) ? (entryCount - 3) : (entryCount - 2));
                prev = dataSet.getEntryForIndex(entryCount - 2);
                cur = (next = dataSet.getEntryForIndex(entryCount - 1));
                prevDx = (cur.getXIndex() - prevPrev.getXIndex()) * intensity;
                prevDy = (cur.getVal() - prevPrev.getVal()) * intensity;
                curDx = (next.getXIndex() - prev.getXIndex()) * intensity;
                curDy = (next.getVal() - prev.getVal()) * intensity;
                this.cubicPath.cubicTo(prev.getXIndex() + prevDx, (prev.getVal() + prevDy) * phaseY, cur.getXIndex() - curDx, (cur.getVal() - curDy) * phaseY, (float)cur.getXIndex(), cur.getVal() * phaseY);
            }
        }
        if (dataSet.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            this.drawCubicFill(this.mBitmapCanvas, dataSet, this.cubicFillPath, trans, minx, size);
        }
        this.mRenderPaint.setColor(dataSet.getColor());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        trans.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect((PathEffect)null);
    }
    
    protected void drawCubicFill(final Canvas c, final ILineDataSet dataSet, final Path spline, final Transformer trans, final int from, final int to) {
        if (to - from <= 1) {
            return;
        }
        final float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
        final Entry toEntry = dataSet.getEntryForIndex(to - 1);
        final Entry fromEntry = dataSet.getEntryForIndex(from);
        final float xTo = (toEntry == null) ? 0 : toEntry.getXIndex();
        final float xFrom = (fromEntry == null) ? 0 : fromEntry.getXIndex();
        spline.lineTo(xTo, fillMin);
        spline.lineTo(xFrom, fillMin);
        spline.close();
        trans.pathValueToPixel(spline);
        final Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            this.drawFilledPath(c, spline, drawable);
        }
        else {
            this.drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
        }
    }
    
    protected void drawLinear(final Canvas c, final ILineDataSet dataSet) {
        final int entryCount = dataSet.getEntryCount();
        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        Canvas canvas = null;
        if (dataSet.isDashedLineEnabled()) {
            canvas = this.mBitmapCanvas;
        }
        else {
            canvas = c;
        }
        final Entry entryFrom = dataSet.getEntryForXIndex((this.mMinX < 0) ? 0 : this.mMinX, DataSet.Rounding.DOWN);
        final Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
        final int diff = (entryFrom == entryTo) ? 1 : 0;
        final int minx = Math.max(dataSet.getEntryIndex(entryFrom) - diff, 0);
        final int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
        final int count = (int)Math.ceil((maxx - minx) * phaseX + minx);
        if (dataSet.getColors().size() > 1) {
            if (this.mLineBuffer.length != pointsPerEntryPair * 2) {
                this.mLineBuffer = new float[pointsPerEntryPair * 2];
            }
            for (int j = minx; j < count; ++j) {
                if (count > 1 && j == count - 1) {
                    break;
                }
                Entry e = dataSet.getEntryForIndex(j);
                if (e != null) {
                    this.mLineBuffer[0] = e.getXIndex();
                    this.mLineBuffer[1] = e.getVal() * phaseY;
                    if (j + 1 < count) {
                        e = dataSet.getEntryForIndex(j + 1);
                        if (e == null) {
                            break;
                        }
                        if (isDrawSteppedEnabled) {
                            this.mLineBuffer[2] = e.getXIndex();
                            this.mLineBuffer[3] = this.mLineBuffer[1];
                            this.mLineBuffer[4] = this.mLineBuffer[2];
                            this.mLineBuffer[5] = this.mLineBuffer[3];
                            this.mLineBuffer[6] = e.getXIndex();
                            this.mLineBuffer[7] = e.getVal() * phaseY;
                        }
                        else {
                            this.mLineBuffer[2] = e.getXIndex();
                            this.mLineBuffer[3] = e.getVal() * phaseY;
                        }
                    }
                    else {
                        this.mLineBuffer[2] = this.mLineBuffer[0];
                        this.mLineBuffer[3] = this.mLineBuffer[1];
                    }
                    trans.pointValuesToPixel(this.mLineBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(this.mLineBuffer[0])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(this.mLineBuffer[2]) && (this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3]))) {
                        if (this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3])) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                            canvas.drawLines(this.mLineBuffer, 0, pointsPerEntryPair * 2, this.mRenderPaint);
                        }
                    }
                }
            }
        }
        else {
            if (this.mLineBuffer.length != Math.max((entryCount - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2) {
                this.mLineBuffer = new float[Math.max((entryCount - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2];
            }
            Entry e2 = dataSet.getEntryForIndex(minx);
            if (e2 != null) {
                int x = (count > 1) ? (minx + 1) : minx;
                int i = 0;
                while (x < count) {
                    e2 = dataSet.getEntryForIndex((x == 0) ? 0 : (x - 1));
                    final Entry e3 = dataSet.getEntryForIndex(x);
                    if (e2 != null) {
                        if (e3 != null) {
                            this.mLineBuffer[i++] = e2.getXIndex();
                            this.mLineBuffer[i++] = e2.getVal() * phaseY;
                            if (isDrawSteppedEnabled) {
                                this.mLineBuffer[i++] = e3.getXIndex();
                                this.mLineBuffer[i++] = e2.getVal() * phaseY;
                                this.mLineBuffer[i++] = e3.getXIndex();
                                this.mLineBuffer[i++] = e2.getVal() * phaseY;
                            }
                            this.mLineBuffer[i++] = e3.getXIndex();
                            this.mLineBuffer[i++] = e3.getVal() * phaseY;
                        }
                    }
                    ++x;
                }
                trans.pointValuesToPixel(this.mLineBuffer);
                final int size = Math.max((count - minx - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                this.mRenderPaint.setColor(dataSet.getColor());
                canvas.drawLines(this.mLineBuffer, 0, size, this.mRenderPaint);
            }
        }
        this.mRenderPaint.setPathEffect((PathEffect)null);
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            this.drawLinearFill(c, dataSet, minx, maxx, trans);
        }
    }
    
    protected void drawLinearFill(final Canvas c, final ILineDataSet dataSet, final int minx, final int maxx, final Transformer trans) {
        final Path filled = this.generateFilledPath(dataSet, minx, maxx);
        trans.pathValueToPixel(filled);
        final Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            this.drawFilledPath(c, filled, drawable);
        }
        else {
            this.drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
        }
    }
    
    private Path generateFilledPath(final ILineDataSet dataSet, final int from, final int to) {
        final float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final Path filled = new Path();
        final Entry entry = dataSet.getEntryForIndex(from);
        filled.moveTo((float)entry.getXIndex(), fillMin);
        filled.lineTo((float)entry.getXIndex(), entry.getVal() * phaseY);
        for (int x = from + 1, count = (int)Math.ceil((to - from) * phaseX + from); x < count; ++x) {
            final Entry e = dataSet.getEntryForIndex(x);
            if (isDrawSteppedEnabled) {
                final Entry ePrev = dataSet.getEntryForIndex(x - 1);
                if (ePrev == null) {
                    continue;
                }
                filled.lineTo((float)e.getXIndex(), ePrev.getVal() * phaseY);
            }
            filled.lineTo((float)e.getXIndex(), e.getVal() * phaseY);
        }
        filled.lineTo((float)dataSet.getEntryForIndex(Math.max(Math.min((int)Math.ceil((to - from) * phaseX + from) - 1, dataSet.getEntryCount() - 1), 0)).getXIndex(), fillMin);
        filled.close();
        return filled;
    }
    
    @Override
    public void drawValues(final Canvas c) {
        if (this.mChart.getLineData().getYValCount() < this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX()) {
            final List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
            for (int i = 0; i < dataSets.size(); ++i) {
                final ILineDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        this.applyValueTextStyle(dataSet);
                        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                        int valOffset = (int)(dataSet.getCircleRadius() * 1.75f);
                        if (!dataSet.isDrawCirclesEnabled()) {
                            valOffset /= 2;
                        }
                        final int entryCount = dataSet.getEntryCount();
                        final Entry entryFrom = dataSet.getEntryForXIndex((this.mMinX < 0) ? 0 : this.mMinX, DataSet.Rounding.DOWN);
                        final Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
                        final int diff = (entryFrom == entryTo) ? 1 : 0;
                        final int minx = Math.max(dataSet.getEntryIndex(entryFrom) - diff, 0);
                        final int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
                        final float[] positions = trans.generateTransformedValuesLine(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), minx, maxx);
                        for (int j = 0; j < positions.length; j += 2) {
                            final float x = positions[j];
                            final float y = positions[j + 1];
                            if (!this.mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsLeft(x)) {
                                if (this.mViewPortHandler.isInBoundsY(y)) {
                                    final Entry entry = dataSet.getEntryForIndex(j / 2 + minx);
                                    this.drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, x, y - valOffset, dataSet.getValueTextColor(j / 2));
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
        this.drawCircles(c);
    }
    
    protected void drawCircles(final Canvas c) {
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float[] circlesBuffer = new float[2];
        final List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
        for (int i = 0; i < dataSets.size(); ++i) {
            final ILineDataSet dataSet = dataSets.get(i);
            if (dataSet.isVisible() && dataSet.isDrawCirclesEnabled()) {
                if (dataSet.getEntryCount() != 0) {
                    this.mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
                    final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    final int entryCount = dataSet.getEntryCount();
                    final Entry entryFrom = dataSet.getEntryForXIndex((this.mMinX < 0) ? 0 : this.mMinX, DataSet.Rounding.DOWN);
                    final Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
                    final int diff = (entryFrom == entryTo) ? 1 : 0;
                    final int minx = Math.max(dataSet.getEntryIndex(entryFrom) - diff, 0);
                    final int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
                    final float halfsize = dataSet.getCircleRadius() / 2.0f;
                    for (int j = minx, count = (int)Math.ceil((maxx - minx) * phaseX + minx); j < count; ++j) {
                        final Entry e = dataSet.getEntryForIndex(j);
                        if (e == null) {
                            break;
                        }
                        circlesBuffer[0] = e.getXIndex();
                        circlesBuffer[1] = e.getVal() * phaseY;
                        trans.pointValuesToPixel(circlesBuffer);
                        if (!this.mViewPortHandler.isInBoundsRight(circlesBuffer[0])) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(circlesBuffer[0])) {
                            if (this.mViewPortHandler.isInBoundsY(circlesBuffer[1])) {
                                final int circleColor = dataSet.getCircleColor(j);
                                this.mRenderPaint.setColor(circleColor);
                                final int MODE = dataSet.getIndicatorMode();
                                if (MODE == 0) {
                                    c.drawCircle(circlesBuffer[0], circlesBuffer[1], dataSet.getCircleRadius(), this.mRenderPaint);
                                }
                                else if (MODE == 1) {
                                    final PointF a = new PointF(circlesBuffer[0], circlesBuffer[1] - dataSet.getCircleRadius());
                                    final PointF b = new PointF(circlesBuffer[0] - dataSet.getCircleRadius(), circlesBuffer[1] + dataSet.getCircleRadius());
                                    final PointF cc = new PointF(circlesBuffer[0] + dataSet.getCircleRadius(), circlesBuffer[1] + dataSet.getCircleRadius());
                                    final Path path = new Path();
                                    path.setFillType(Path.FillType.EVEN_ODD);
                                    path.moveTo(b.x, b.y);
                                    path.lineTo(a.x, a.y);
                                    path.lineTo(cc.x, cc.y);
                                    path.lineTo(b.x, b.y);
                                    path.close();
                                    c.drawPath(path, this.mRenderPaint);
                                }
                                else if (MODE == 2) {
                                    final RectF rectF = new RectF(circlesBuffer[0] - dataSet.getCircleRadius(), circlesBuffer[1] - dataSet.getCircleRadius(), circlesBuffer[0] + dataSet.getCircleRadius(), circlesBuffer[1] + dataSet.getCircleRadius());
                                    c.drawRect(rectF, this.mRenderPaint);
                                }
                                if (dataSet.isDrawCircleHoleEnabled() && circleColor != this.mCirclePaintInner.getColor()) {
                                    c.drawCircle(circlesBuffer[0], circlesBuffer[1], halfsize, this.mCirclePaintInner);
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
        for (int i = 0; i < indices.length; ++i) {
            final ILineDataSet set = this.mChart.getLineData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final int xIndex = indices[i].getXIndex();
                    if (xIndex <= this.mChart.getXChartMax() * this.mAnimator.getPhaseX()) {
                        final float yVal = set.getYValForXIndex(xIndex);
                        if (yVal != Float.NaN) {
                            final float y = yVal * this.mAnimator.getPhaseY();
                            final float[] pts = { xIndex, y };
                            this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                            this.drawHighlightLines(c, pts, set);
                        }
                    }
                }
            }
        }
    }
    
    public void setBitmapConfig(final Bitmap.Config config) {
        this.mBitmapConfig = config;
        this.releaseBitmap();
    }
    
    public Bitmap.Config getBitmapConfig() {
        return this.mBitmapConfig;
    }
    
    public void releaseBitmap() {
        if (this.mDrawBitmap != null) {
            this.mDrawBitmap.get().recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
