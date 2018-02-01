// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import com.github.mikephil.charting.jobs.AnimatedMoveViewJob;
import com.github.mikephil.charting.jobs.MoveViewJob;
import android.annotation.TargetApi;
import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.jobs.AnimatedZoomJob;
import android.os.Build;
import com.github.mikephil.charting.jobs.ZoomJob;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.View;
import android.view.MotionEvent;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.data.ChartData;
import android.util.Log;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.listener.OnDrawListener;
import android.graphics.Paint;
import android.annotation.SuppressLint;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

@SuppressLint({ "RtlHardcoded" })
@SuppressWarnings({ "unchecked", "rawtypes"} )

public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleBubbleDataProvider
{
    protected int mMaxVisibleCount;
    private boolean mAutoScaleMinMaxEnabled;
    private Integer mAutoScaleLastLowestVisibleXIndex;
    private Integer mAutoScaleLastHighestVisibleXIndex;
    protected boolean mPinchZoomEnabled;
    protected boolean mDoubleTapToZoomEnabled;
    protected boolean mHighlightPerDragEnabled;
    private boolean mDragEnabled;
    private boolean mScaleXEnabled;
    private boolean mScaleYEnabled;
    protected Paint mGridBackgroundPaint;
    protected Paint mBorderPaint;
    protected boolean mDrawGridBackground;
    protected boolean mDrawBorders;
    protected float mMinOffset;
    protected boolean mKeepPositionOnRotation;
    protected OnDrawListener mDrawListener;
    protected YAxis mAxisLeft;
    protected YAxis mAxisRight;
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;
    protected Transformer mLeftAxisTransformer;
    protected Transformer mRightAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;
    private long totalTime;
    private long drawCycles;
    private boolean mCustomViewPortEnabled;
    
    public BarLineChartBase(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mAutoScaleLastLowestVisibleXIndex = null;
        this.mAutoScaleLastHighestVisibleXIndex = null;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mCustomViewPortEnabled = false;
    }
    
    public BarLineChartBase(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mAutoScaleLastLowestVisibleXIndex = null;
        this.mAutoScaleLastHighestVisibleXIndex = null;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mCustomViewPortEnabled = false;
    }
    
    public BarLineChartBase(final Context context) {
        super(context);
        this.mMaxVisibleCount = 100;
        this.mAutoScaleMinMaxEnabled = false;
        this.mAutoScaleLastLowestVisibleXIndex = null;
        this.mAutoScaleLastHighestVisibleXIndex = null;
        this.mPinchZoomEnabled = false;
        this.mDoubleTapToZoomEnabled = true;
        this.mHighlightPerDragEnabled = true;
        this.mDragEnabled = true;
        this.mScaleXEnabled = true;
        this.mScaleYEnabled = true;
        this.mDrawGridBackground = false;
        this.mDrawBorders = false;
        this.mMinOffset = 15.0f;
        this.mKeepPositionOnRotation = false;
        this.totalTime = 0L;
        this.drawCycles = 0L;
        this.mCustomViewPortEnabled = false;
    }
    
	@Override
    protected void init() {
        super.init();
        this.mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        this.mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);
        this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mRightAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRenderer(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
        //this.setHighlighter(new ChartHighlighter((T)this));
        this.setHighlighter(new ChartHighlighter(this));
        this.mChartTouchListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch());
        (this.mGridBackgroundPaint = new Paint()).setStyle(Paint.Style.FILL);
        this.mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240));
        (this.mBorderPaint = new Paint()).setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setColor(-16777216);
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData == null) {
            return;
        }
        final long starttime = System.currentTimeMillis();
        this.calcModulus();
        this.mXAxisRenderer.calcXBounds(this, this.mXAxis.mAxisLabelModulus);
        this.mRenderer.calcXBounds(this, this.mXAxis.mAxisLabelModulus);
        this.drawGridBackground(canvas);
        if (this.mAxisLeft.isEnabled()) {
            this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
        }
        if (this.mAxisRight.isEnabled()) {
            this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum);
        }
        this.mXAxisRenderer.renderAxisLine(canvas);
        this.mAxisRendererLeft.renderAxisLine(canvas);
        this.mAxisRendererRight.renderAxisLine(canvas);
        if (this.mAutoScaleMinMaxEnabled) {
            final int lowestVisibleXIndex = this.getLowestVisibleXIndex();
            final int highestVisibleXIndex = this.getHighestVisibleXIndex();
            if (this.mAutoScaleLastLowestVisibleXIndex == null || this.mAutoScaleLastLowestVisibleXIndex != lowestVisibleXIndex || this.mAutoScaleLastHighestVisibleXIndex == null || this.mAutoScaleLastHighestVisibleXIndex != highestVisibleXIndex) {
                this.calcMinMax();
                this.calculateOffsets();
                this.mAutoScaleLastLowestVisibleXIndex = lowestVisibleXIndex;
                this.mAutoScaleLastHighestVisibleXIndex = highestVisibleXIndex;
            }
        }
        final int clipRestoreCount = canvas.save();
        canvas.clipRect(this.mViewPortHandler.getContentRect());
        this.mXAxisRenderer.renderGridLines(canvas);
        this.mAxisRendererLeft.renderGridLines(canvas);
        this.mAxisRendererRight.renderGridLines(canvas);
        if (this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(canvas);
        }
        if (this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(canvas);
        }
        if (this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(canvas);
        }
        this.mRenderer.drawData(canvas);
        if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        canvas.restoreToCount(clipRestoreCount);
        this.mRenderer.drawExtras(canvas);
        if (!this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            this.mXAxisRenderer.renderLimitLines(canvas);
        }
        if (!this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererLeft.renderLimitLines(canvas);
        }
        if (!this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            this.mAxisRendererRight.renderLimitLines(canvas);
        }
        this.mXAxisRenderer.renderAxisLabels(canvas);
        this.mAxisRendererLeft.renderAxisLabels(canvas);
        this.mAxisRendererRight.renderAxisLabels(canvas);
        this.mRenderer.drawValues(canvas);
        this.mLegendRenderer.renderLegend(canvas);
        this.drawMarkers(canvas);
        this.drawDescription(canvas);
        if (this.mLogEnabled) {
            final long drawtime = System.currentTimeMillis() - starttime;
            this.totalTime += drawtime;
            ++this.drawCycles;
            final long average = this.totalTime / this.drawCycles;
            Log.i("MPAndroidChart", "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: " + this.drawCycles);
        }
    }
    
    public void resetTracking() {
        this.totalTime = 0L;
        this.drawCycles = 0L;
    }
    
    protected void prepareValuePxMatrix() {
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Preparing Value-Px Matrix, xmin: " + this.mXAxis.mAxisMinimum + ", xmax: " + this.mXAxis.mAxisMaximum + ", xdelta: " + this.mXAxis.mAxisRange);
        }
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisRight.mAxisRange, this.mAxisRight.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisLeft.mAxisRange, this.mAxisLeft.mAxisMinimum);
    }
    
    protected void prepareOffsetMatrix() {
        this.mRightAxisTransformer.prepareMatrixOffset(this.mAxisRight.isInverted());
        this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (this.mData == null) {
            if (this.mLogEnabled) {
                Log.i("MPAndroidChart", "Preparing... DATA NOT SET.");
            }
            return;
        }
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Preparing...");
        }
        if (this.mRenderer != null) {
            this.mRenderer.initBuffers();
        }
        this.calcMinMax();
        this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
        this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum);
        this.mXAxisRenderer.computeAxis(this.mData.getXValMaximumLength(), this.mData.getXVals());
        if (this.mLegend != null) {
            this.mLegendRenderer.computeLegend(this.mData);
        }
        this.calculateOffsets();
    }
    
    @Override
    protected void calcMinMax() {
        if (this.mAutoScaleMinMaxEnabled) {
            this.mData.calcMinMax(this.getLowestVisibleXIndex(), this.getHighestVisibleXIndex());
        }
        this.mXAxis.mAxisMaximum = this.mData.getXVals().size() - 1;
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
        this.mAxisLeft.calcMinMax(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calcMinMax(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
    }
    
    public void calculateOffsets() {
        if (!this.mCustomViewPortEnabled) {
            float offsetLeft = 0.0f;
            float offsetRight = 0.0f;
            float offsetTop = 0.0f;
            float offsetBottom = 0.0f;
            if (this.mLegend != null && this.mLegend.isEnabled()) {
                if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                    offsetRight += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset() * 2.0f;
                }
                else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                    offsetLeft += Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getXOffset() * 2.0f;
                }
                else if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                    final float yOffset = this.mLegend.mTextHeightMax;
                    offsetBottom += Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                }
                else if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                    final float yOffset = this.mLegend.mTextHeightMax;
                    offsetTop += Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                }
            }
            if (this.mAxisLeft.needsOffset()) {
                offsetLeft += this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
            }
            if (this.mAxisRight.needsOffset()) {
                offsetRight += this.mAxisRight.getRequiredWidthSpace(this.mAxisRendererRight.getPaintAxisLabels());
            }
            if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
                final float xlabelheight = this.mXAxis.mLabelRotatedHeight + this.mXAxis.getYOffset();
                if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                    offsetBottom += xlabelheight;
                }
                else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                    offsetTop += xlabelheight;
                }
                else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight;
                }
            }
            offsetTop += this.getExtraTopOffset();
            offsetRight += this.getExtraRightOffset();
            offsetBottom += this.getExtraBottomOffset();
            offsetLeft += this.getExtraLeftOffset();
            final float minOffset = Utils.convertDpToPixel(this.mMinOffset);
            this.mViewPortHandler.restrainViewPort(Math.max(minOffset, offsetLeft), Math.max(minOffset, offsetTop), Math.max(minOffset, offsetRight), Math.max(minOffset, offsetBottom));
            if (this.mLogEnabled) {
                Log.i("MPAndroidChart", "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                Log.i("MPAndroidChart", "Content: " + this.mViewPortHandler.getContentRect().toString());
            }
        }
        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }
    
    protected void calcModulus() {
        if (this.mXAxis == null || !this.mXAxis.isEnabled()) {
            return;
        }
        if (!this.mXAxis.isAxisModulusCustom()) {
            final float[] values = new float[9];
            this.mViewPortHandler.getMatrixTouch().getValues(values);
            this.mXAxis.mAxisLabelModulus = (int)Math.ceil(this.mData.getXValCount() * this.mXAxis.mLabelRotatedWidth / (this.mViewPortHandler.contentWidth() * values[0]));
        }
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "X-Axis modulus: " + this.mXAxis.mAxisLabelModulus + ", x-axis label width: " + this.mXAxis.mLabelWidth + ", x-axis label rotated width: " + this.mXAxis.mLabelRotatedWidth + ", content width: " + this.mViewPortHandler.contentWidth());
        }
        if (this.mXAxis.mAxisLabelModulus < 1) {
            this.mXAxis.mAxisLabelModulus = 1;
        }
    }
    
	@Override
    protected float[] getMarkerPosition(final Entry e, final Highlight highlight) {
        final int dataSetIndex = highlight.getDataSetIndex();
        float xPos = e.getXIndex();
        float yPos = e.getVal();
        if (this instanceof BarChart) {
            final BarData bd = (BarData)this.mData;
            final float space = bd.getGroupSpace();
            final int setCount = this.mData.getDataSetCount();
            final int i = e.getXIndex();
            if (this instanceof HorizontalBarChart) {
                final float y = yPos = i + i * (setCount - 1) + dataSetIndex + space * i + space / 2.0f;
                final BarEntry entry = (BarEntry)e;
                if (entry.getVals() != null) {
                    xPos = highlight.getRange().to;
                }
                else {
                    xPos = e.getVal();
                }
                xPos *= this.mAnimator.getPhaseY();
            }
            else {
                final float x = xPos = i + i * (setCount - 1) + dataSetIndex + space * i + space / 2.0f;
                final BarEntry entry = (BarEntry)e;
                if (entry.getVals() != null) {
                    yPos = highlight.getRange().to;
                }
                else {
                    yPos = e.getVal();
                }
                yPos *= this.mAnimator.getPhaseY();
            }
        }
        else {
            yPos *= this.mAnimator.getPhaseY();
        }
        final float[] pts = { xPos, yPos };
       // this.getTransformer(((ChartData<IBarLineScatterCandleBubbleDataSet>)this.mData).getDataSetByIndex(dataSetIndex).getAxisDependency()).pointValuesToPixel(pts);
        this.getTransformer(this.mData.getDataSetByIndex(dataSetIndex).getAxisDependency()).pointValuesToPixel(pts);
        return pts;
    }
    
    protected void drawGridBackground(final Canvas c) {
        if (this.mDrawGridBackground) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
        }
        if (this.mDrawBorders) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
        }
    }
    
    @Override
    public Transformer getTransformer(final YAxis.AxisDependency which) {
        if (which == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisTransformer;
        }
        return this.mRightAxisTransformer;
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);
        return this.mChartTouchListener != null && this.mData != null && this.mTouchEnabled && this.mChartTouchListener.onTouch((View)this, event);
    }
    
    public void computeScroll() {
        if (this.mChartTouchListener instanceof BarLineChartTouchListener) {
            ((BarLineChartTouchListener)this.mChartTouchListener).computeScroll();
        }
    }
    
    public void zoomIn() {
        final PointF center = this.mViewPortHandler.getContentCenter();
        final Matrix save = this.mViewPortHandler.zoomIn(center.x, -center.y);
        this.mViewPortHandler.refresh(save, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoomOut() {
        final PointF center = this.mViewPortHandler.getContentCenter();
        final Matrix save = this.mViewPortHandler.zoomOut(center.x, -center.y);
        this.mViewPortHandler.refresh(save, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoom(final float scaleX, final float scaleY, final float x, final float y) {
        final Matrix save = this.mViewPortHandler.zoom(scaleX, scaleY, x, y);
        this.mViewPortHandler.refresh(save, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void zoom(final float scaleX, final float scaleY, final float xValue, final float yValue, final YAxis.AxisDependency axis) {
        final Runnable job = new ZoomJob(this.mViewPortHandler, scaleX, scaleY, xValue, yValue, this.getTransformer(axis), axis, (View)this);
        this.addViewportJob(job);
    }
    
    @TargetApi(11)
    public void zoomAndCenterAnimated(final float scaleX, final float scaleY, final float xValue, final float yValue, final YAxis.AxisDependency axis, final long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            final PointD origin = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            final Runnable job = new AnimatedZoomJob(this.mViewPortHandler, (View)this, this.getTransformer(axis), this.getAxis(axis), this.mXAxis.getValues().size(), scaleX, scaleY, this.mViewPortHandler.getScaleX(), this.mViewPortHandler.getScaleY(), xValue, yValue, (float)origin.x, (float)origin.y, duration);
            this.addViewportJob(job);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute zoomAndCenterAnimated(...) on API level < 11");
        }
    }
    
    public void fitScreen() {
        final Matrix save = this.mViewPortHandler.fitScreen();
        this.mViewPortHandler.refresh(save, (View)this, false);
        this.calculateOffsets();
        this.postInvalidate();
    }
    
    public void setScaleMinima(final float scaleX, final float scaleY) {
        this.mViewPortHandler.setMinimumScaleX(scaleX);
        this.mViewPortHandler.setMinimumScaleY(scaleY);
    }
    
    public void setVisibleXRangeMaximum(final float maxXRange) {
        final float xScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinimumScaleX(xScale);
    }
    
    public void setVisibleXRangeMinimum(final float minXRange) {
        final float xScale = this.mXAxis.mAxisRange / minXRange;
        this.mViewPortHandler.setMaximumScaleX(xScale);
    }
    
    public void setVisibleXRange(final float minXRange, final float maxXRange) {
        final float maxScale = this.mXAxis.mAxisRange / minXRange;
        final float minScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }
    
    public void setVisibleYRangeMaximum(final float maxYRange, final YAxis.AxisDependency axis) {
        final float yScale = this.getDeltaY(axis) / maxYRange;
        this.mViewPortHandler.setMinimumScaleY(yScale);
    }
    
    public void moveViewToX(final float xIndex) {
        final Runnable job = new MoveViewJob(this.mViewPortHandler, xIndex, 0.0f, this.getTransformer(YAxis.AxisDependency.LEFT), (View)this);
        this.addViewportJob(job);
    }
    
    public void moveViewToY(final float yValue, final YAxis.AxisDependency axis) {
        final float valsInView = this.getDeltaY(axis) / this.mViewPortHandler.getScaleY();
        final Runnable job = new MoveViewJob(this.mViewPortHandler, 0.0f, yValue + valsInView / 2.0f, this.getTransformer(axis), (View)this);
        this.addViewportJob(job);
    }
    
    public void moveViewTo(final float xIndex, final float yValue, final YAxis.AxisDependency axis) {
        final float valsInView = this.getDeltaY(axis) / this.mViewPortHandler.getScaleY();
        final Runnable job = new MoveViewJob(this.mViewPortHandler, xIndex, yValue + valsInView / 2.0f, this.getTransformer(axis), (View)this);
        this.addViewportJob(job);
    }
    
    @TargetApi(11)
    public void moveViewToAnimated(final float xIndex, final float yValue, final YAxis.AxisDependency axis, final long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            final PointD bounds = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            final float valsInView = this.getDeltaY(axis) / this.mViewPortHandler.getScaleY();
            final Runnable job = new AnimatedMoveViewJob(this.mViewPortHandler, xIndex, yValue + valsInView / 2.0f, this.getTransformer(axis), (View)this, (float)bounds.x, (float)bounds.y, duration);
            this.addViewportJob(job);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute moveViewToAnimated(...) on API level < 11");
        }
    }
    
    public void centerViewTo(final float xIndex, final float yValue, final YAxis.AxisDependency axis) {
        final float valsInView = this.getDeltaY(axis) / this.mViewPortHandler.getScaleY();
        final float xsInView = this.getXAxis().getValues().size() / this.mViewPortHandler.getScaleX();
        final Runnable job = new MoveViewJob(this.mViewPortHandler, xIndex - xsInView / 2.0f, yValue + valsInView / 2.0f, this.getTransformer(axis), (View)this);
        this.addViewportJob(job);
    }
    
    @TargetApi(11)
    public void centerViewToAnimated(final float xIndex, final float yValue, final YAxis.AxisDependency axis, final long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            final PointD bounds = this.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            final float valsInView = this.getDeltaY(axis) / this.mViewPortHandler.getScaleY();
            final float xsInView = this.getXAxis().getValues().size() / this.mViewPortHandler.getScaleX();
            final Runnable job = new AnimatedMoveViewJob(this.mViewPortHandler, xIndex - xsInView / 2.0f, yValue + valsInView / 2.0f, this.getTransformer(axis), (View)this, (float)bounds.x, (float)bounds.y, duration);
            this.addViewportJob(job);
        }
        else {
            Log.e("MPAndroidChart", "Unable to execute centerViewToAnimated(...) on API level < 11");
        }
    }
    
    public void setViewPortOffsets(final float left, final float top, final float right, final float bottom) {
        this.mCustomViewPortEnabled = true;
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                BarLineChartBase.this.mViewPortHandler.restrainViewPort(left, top, right, bottom);
                BarLineChartBase.this.prepareOffsetMatrix();
                BarLineChartBase.this.prepareValuePxMatrix();
            }
        });
    }
    
    public void resetViewPortOffsets() {
        this.mCustomViewPortEnabled = false;
        this.calculateOffsets();
    }
    
    public float getDeltaY(final YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft.mAxisRange;
        }
        return this.mAxisRight.mAxisRange;
    }
    
    public void setOnDrawListener(final OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }
    
    public OnDrawListener getDrawListener() {
        return this.mDrawListener;
    }
    
    public PointF getPosition(final Entry e, final YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        }
        final float[] vals = { e.getXIndex(), e.getVal() };
        this.getTransformer(axis).pointValuesToPixel(vals);
        return new PointF(vals[0], vals[1]);
    }
    
    public void setMaxVisibleValueCount(final int count) {
        this.mMaxVisibleCount = count;
    }
    
    @Override
    public int getMaxVisibleCount() {
        return this.mMaxVisibleCount;
    }
    
    public void setHighlightPerDragEnabled(final boolean enabled) {
        this.mHighlightPerDragEnabled = enabled;
    }
    
    public boolean isHighlightPerDragEnabled() {
        return this.mHighlightPerDragEnabled;
    }
    
    public void setGridBackgroundColor(final int color) {
        this.mGridBackgroundPaint.setColor(color);
    }
    
    public void setDragEnabled(final boolean enabled) {
        this.mDragEnabled = enabled;
    }
    
    public boolean isDragEnabled() {
        return this.mDragEnabled;
    }
    
    public void setScaleEnabled(final boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }
    
    public void setScaleXEnabled(final boolean enabled) {
        this.mScaleXEnabled = enabled;
    }
    
    public void setScaleYEnabled(final boolean enabled) {
        this.mScaleYEnabled = enabled;
    }
    
    public boolean isScaleXEnabled() {
        return this.mScaleXEnabled;
    }
    
    public boolean isScaleYEnabled() {
        return this.mScaleYEnabled;
    }
    
    public void setDoubleTapToZoomEnabled(final boolean enabled) {
        this.mDoubleTapToZoomEnabled = enabled;
    }
    
    public boolean isDoubleTapToZoomEnabled() {
        return this.mDoubleTapToZoomEnabled;
    }
    
    public void setDrawGridBackground(final boolean enabled) {
        this.mDrawGridBackground = enabled;
    }
    
    public void setDrawBorders(final boolean enabled) {
        this.mDrawBorders = enabled;
    }
    
    public void setBorderWidth(final float width) {
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }
    
    public void setBorderColor(final int color) {
        this.mBorderPaint.setColor(color);
    }
    
    public float getMinOffset() {
        return this.mMinOffset;
    }
    
    public void setMinOffset(final float minOffset) {
        this.mMinOffset = minOffset;
    }
    
    public boolean isKeepPositionOnRotation() {
        return this.mKeepPositionOnRotation;
    }
    
    public void setKeepPositionOnRotation(final boolean keepPositionOnRotation) {
        this.mKeepPositionOnRotation = keepPositionOnRotation;
    }
    
    public Highlight getHighlightByTouchPoint(final float x, final float y) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        }
        return this.getHighlighter().getHighlight(x, y);
    }
    
    public PointD getValuesByTouchPoint(final float x, final float y, final YAxis.AxisDependency axis) {
        final float[] pts = { x, y };
        this.getTransformer(axis).pixelsToValue(pts);
        final double xTouchVal = pts[0];
        final double yTouchVal = pts[1];
        return new PointD(xTouchVal, yTouchVal);
    }
    
    public PointD getPixelsForValues(final float x, final float y, final YAxis.AxisDependency axis) {
        final float[] pts = { x, y };
        this.getTransformer(axis).pointValuesToPixel(pts);
        return new PointD(pts[0], pts[1]);
    }
    
    public float getYValueByTouchPoint(final float x, final float y, final YAxis.AxisDependency axis) {
        return (float)this.getValuesByTouchPoint(x, y, axis).y;
    }
    
    public Entry getEntryByTouchPoint(final float x, final float y) {
        final Highlight h = this.getHighlightByTouchPoint(x, y);
        if (h != null) {
            return this.mData.getEntryForHighlight(h);
        }
        return null;
    }
    
    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(final float x, final float y) {
        final Highlight h = this.getHighlightByTouchPoint(x, y);
        if (h != null) {
           // return ((ChartData<IBarLineScatterCandleBubbleDataSet>)this.mData).getDataSetByIndex(h.getDataSetIndex());
        	 return this.mData.getDataSetByIndex(h.getDataSetIndex()); 
        }
        return null;
    }
    
    @Override
    public int getLowestVisibleXIndex() {
        final float[] pts = { this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (pts[0] <= 0.0f) ? 0 : ((int)(pts[0] + 1.0f));
    }
    
    @Override
    public int getHighestVisibleXIndex() {
        final float[] pts = { this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom() };
        this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return (pts[0] >= this.mData.getXValCount()) ? (this.mData.getXValCount() - 1) : ((int)pts[0]);
    }
    
    public float getScaleX() {
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleX();
    }
    
    public float getScaleY() {
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleY();
    }
    
    public boolean isFullyZoomedOut() {
        return this.mViewPortHandler.isFullyZoomedOut();
    }
    
    public YAxis getAxisLeft() {
        return this.mAxisLeft;
    }
    
    public YAxis getAxisRight() {
        return this.mAxisRight;
    }
    
    public YAxis getAxis(final YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft;
        }
        return this.mAxisRight;
    }
    
    @Override
    public boolean isInverted(final YAxis.AxisDependency axis) {
        return this.getAxis(axis).isInverted();
    }
    
    public void setPinchZoom(final boolean enabled) {
        this.mPinchZoomEnabled = enabled;
    }
    
    public boolean isPinchZoomEnabled() {
        return this.mPinchZoomEnabled;
    }
    
    public void setDragOffsetX(final float offset) {
        this.mViewPortHandler.setDragOffsetX(offset);
    }
    
    public void setDragOffsetY(final float offset) {
        this.mViewPortHandler.setDragOffsetY(offset);
    }
    
    public boolean hasNoDragOffset() {
        return this.mViewPortHandler.hasNoDragOffset();
    }
    
    public XAxisRenderer getRendererXAxis() {
        return this.mXAxisRenderer;
    }
    
    public void setXAxisRenderer(final XAxisRenderer xAxisRenderer) {
        this.mXAxisRenderer = xAxisRenderer;
    }
    
    public YAxisRenderer getRendererLeftYAxis() {
        return this.mAxisRendererLeft;
    }
    
    public void setRendererLeftYAxis(final YAxisRenderer rendererLeftYAxis) {
        this.mAxisRendererLeft = rendererLeftYAxis;
    }
    
    public YAxisRenderer getRendererRightYAxis() {
        return this.mAxisRendererRight;
    }
    
    public void setRendererRightYAxis(final YAxisRenderer rendererRightYAxis) {
        this.mAxisRendererRight = rendererRightYAxis;
    }
    
    public float getYChartMax() {
        return Math.max(this.mAxisLeft.mAxisMaximum, this.mAxisRight.mAxisMaximum);
    }
    
    public float getYChartMin() {
        return Math.min(this.mAxisLeft.mAxisMinimum, this.mAxisRight.mAxisMinimum);
    }
    
    public boolean isAnyAxisInverted() {
        return this.mAxisLeft.isInverted() || this.mAxisRight.isInverted();
    }
    
    public void setAutoScaleMinMaxEnabled(final boolean enabled) {
        this.mAutoScaleMinMaxEnabled = enabled;
    }
    
    public boolean isAutoScaleMinMaxEnabled() {
        return this.mAutoScaleMinMaxEnabled;
    }
    
    @Override
    public void setPaint(final Paint p, final int which) {
        super.setPaint(p, which);
        switch (which) {
            case 4: {
                this.mGridBackgroundPaint = p;
                break;
            }
        }
    }
    
    @Override
    public Paint getPaint(final int which) {
        final Paint p = super.getPaint(which);
        if (p != null) {
            return p;
        }
        switch (which) {
            case 4: {
                return this.mGridBackgroundPaint;
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        final float[] pts = new float[2];
        if (this.mKeepPositionOnRotation) {
            pts[0] = this.mViewPortHandler.contentLeft();
            pts[1] = this.mViewPortHandler.contentTop();
            this.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        }
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mKeepPositionOnRotation) {
            this.getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(pts);
            this.mViewPortHandler.centerViewPort(pts, (View)this);
        }
        else {
            this.mViewPortHandler.refresh(this.mViewPortHandler.getMatrixTouch(), (View)this, true);
        }
    }
}
