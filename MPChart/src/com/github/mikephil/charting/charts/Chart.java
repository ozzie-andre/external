// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import android.provider.MediaStore;
import android.content.ContentValues;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.os.Environment;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import java.util.List;
import android.view.ViewParent;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.EasingFunction;
import android.view.View;
import android.text.TextUtils;
import android.graphics.Canvas;
//import java.util.Iterator;
import android.util.Log;
import android.graphics.Color;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import android.animation.ValueAnimator;
import android.os.Build;
import android.util.AttributeSet;
import android.content.Context;
import java.util.ArrayList;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.PointF;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import android.graphics.Paint;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.annotation.SuppressLint;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import android.view.ViewGroup;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.ChartData;

@SuppressLint({ "NewApi" })
@SuppressWarnings("rawtypes")
public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface
{
    public static final String LOG_TAG = "MPAndroidChart";
    protected boolean mLogEnabled;
    protected T mData;
    protected boolean mHighLightPerTapEnabled;
    private boolean mDragDecelerationEnabled;
    private float mDragDecelerationFrictionCoef;
    protected ValueFormatter mDefaultFormatter;
    protected Paint mDescPaint;
    protected Paint mInfoPaint;
    protected String mDescription;
    protected XAxis mXAxis;
    protected boolean mTouchEnabled;
    protected Legend mLegend;
    protected OnChartValueSelectedListener mSelectionListener;
    protected ChartTouchListener mChartTouchListener;
    private String mNoDataText;
    private OnChartGestureListener mGestureListener;
    private String mNoDataTextDescription;
    protected LegendRenderer mLegendRenderer;
    protected DataRenderer mRenderer;
    protected ChartHighlighter mHighlighter;
    protected ViewPortHandler mViewPortHandler;
    protected ChartAnimator mAnimator;
    private float mExtraTopOffset;
    private float mExtraRightOffset;
    private float mExtraBottomOffset;
    private float mExtraLeftOffset;
    private boolean mOffsetsCalculated;
    protected Paint mDrawPaint;
    private PointF mDescriptionPosition;
    protected Highlight[] mIndicesToHighlight;
    protected boolean mDrawMarkerViews;
    protected MarkerView mMarkerView;
    public static final int PAINT_GRID_BACKGROUND = 4;
    public static final int PAINT_INFO = 7;
    public static final int PAINT_DESCRIPTION = 11;
    public static final int PAINT_HOLE = 13;
    public static final int PAINT_CENTER_TEXT = 14;
    public static final int PAINT_LEGEND_LABEL = 18;
    protected ArrayList<Runnable> mJobs;
    private boolean mUnbind;
    
    public Chart(final Context context) {
        super(context);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDescription = "Description";
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mDrawMarkerViews = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    public Chart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDescription = "Description";
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mDrawMarkerViews = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    public Chart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mLogEnabled = false;
        this.mData = null;
        this.mHighLightPerTapEnabled = true;
        this.mDragDecelerationEnabled = true;
        this.mDragDecelerationFrictionCoef = 0.9f;
        this.mDescription = "Description";
        this.mTouchEnabled = true;
        this.mNoDataText = "No chart data available.";
        this.mExtraTopOffset = 0.0f;
        this.mExtraRightOffset = 0.0f;
        this.mExtraBottomOffset = 0.0f;
        this.mExtraLeftOffset = 0.0f;
        this.mOffsetsCalculated = false;
        this.mDrawMarkerViews = true;
        this.mJobs = new ArrayList<Runnable>();
        this.mUnbind = false;
        this.init();
    }
    
    protected void init() {
        this.setWillNotDraw(false);
        if (Build.VERSION.SDK_INT < 11) {
            this.mAnimator = new ChartAnimator();
        }
        else {
            this.mAnimator = new ChartAnimator((ValueAnimator.AnimatorUpdateListener)new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator animation) {
                    Chart.this.postInvalidate();
                }
            });
        }
        Utils.init(this.getContext());
        this.mDefaultFormatter = new DefaultValueFormatter(1);
        this.mViewPortHandler = new ViewPortHandler();
        this.mLegend = new Legend();
        this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler, this.mLegend);
        this.mXAxis = new XAxis();
        (this.mDescPaint = new Paint(1)).setColor(-16777216);
        this.mDescPaint.setTextAlign(Paint.Align.RIGHT);
        this.mDescPaint.setTextSize(Utils.convertDpToPixel(9.0f));
        (this.mInfoPaint = new Paint(1)).setColor(Color.rgb(247, 189, 51));
        this.mInfoPaint.setTextAlign(Paint.Align.CENTER);
        this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mDrawPaint = new Paint(4);
        if (this.mLogEnabled) {
            Log.i("", "Chart.init()");
        }
    }
    
    public void setData(final T data) {
        if (data == null) {
            Log.e("MPAndroidChart", "Cannot set data for chart. Provided data object is null.");
            return;
        }
        this.mOffsetsCalculated = false;
        this.mData = data;
        this.calculateFormatter(data.getYMin(), data.getYMax());
        for (final IDataSet set : this.mData.getDataSets()) {
            if (Utils.needsDefaultFormatter(set.getValueFormatter())) {
                set.setValueFormatter(this.mDefaultFormatter);
            }
        }
        this.notifyDataSetChanged();
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "Data is set.");
        }
    }
    
    public void clear() {
        this.mData = null;
        this.mIndicesToHighlight = null;
        this.invalidate();
    }
    
    public void clearValues() {
        this.mData.clearValues();
        this.invalidate();
    }
    
    public boolean isEmpty() {
        return this.mData == null || this.mData.getYValCount() <= 0;
    }
    
    public abstract void notifyDataSetChanged();
    
    protected abstract void calculateOffsets();
    
    protected abstract void calcMinMax();
    
    protected void calculateFormatter(final float min, final float max) {
        float reference = 0.0f;
        if (this.mData == null || this.mData.getXValCount() < 2) {
            reference = Math.max(Math.abs(min), Math.abs(max));
        }
        else {
            reference = Math.abs(max - min);
        }
        final int digits = Utils.getDecimals(reference);
        this.mDefaultFormatter = new DefaultValueFormatter(digits);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mData == null) {
            final boolean hasText = !TextUtils.isEmpty((CharSequence)this.mNoDataText);
            final boolean hasDescription = !TextUtils.isEmpty((CharSequence)this.mNoDataTextDescription);
            final float line1height = hasText ? Utils.calcTextHeight(this.mInfoPaint, this.mNoDataText) : 0.0f;
            final float line2height = hasDescription ? Utils.calcTextHeight(this.mInfoPaint, this.mNoDataTextDescription) : 0.0f;
            final float lineSpacing = (hasText && hasDescription) ? (this.mInfoPaint.getFontSpacing() - line1height) : 0.0f;
            float y = (this.getHeight() - (line1height + lineSpacing + line2height)) / 2.0f + line1height;
            if (hasText) {
                canvas.drawText(this.mNoDataText, (float)(this.getWidth() / 2), y, this.mInfoPaint);
                if (hasDescription) {
                    y = y + line1height + lineSpacing;
                }
            }
            if (hasDescription) {
                canvas.drawText(this.mNoDataTextDescription, (float)(this.getWidth() / 2), y, this.mInfoPaint);
            }
            return;
        }
        if (!this.mOffsetsCalculated) {
            this.calculateOffsets();
            this.mOffsetsCalculated = true;
        }
    }
    
    protected void drawDescription(final Canvas c) {
        if (!this.mDescription.equals("")) {
            if (this.mDescriptionPosition == null) {
                c.drawText(this.mDescription, this.getWidth() - this.mViewPortHandler.offsetRight() - 10.0f, this.getHeight() - this.mViewPortHandler.offsetBottom() - 10.0f, this.mDescPaint);
            }
            else {
                c.drawText(this.mDescription, this.mDescriptionPosition.x, this.mDescriptionPosition.y, this.mDescPaint);
            }
        }
    }
    
    public Highlight[] getHighlighted() {
        return this.mIndicesToHighlight;
    }
    
    public boolean isHighlightPerTapEnabled() {
        return this.mHighLightPerTapEnabled;
    }
    
    public void setHighlightPerTapEnabled(final boolean enabled) {
        this.mHighLightPerTapEnabled = enabled;
    }
    
    public boolean valuesToHighlight() {
        return this.mIndicesToHighlight != null && this.mIndicesToHighlight.length > 0 && this.mIndicesToHighlight[0] != null;
    }
    
    public void highlightValues(final Highlight[] highs) {
        this.mIndicesToHighlight = highs;
        if (highs == null || highs.length <= 0 || highs[0] == null) {
            this.mChartTouchListener.setLastHighlighted(null);
        }
        else {
            this.mChartTouchListener.setLastHighlighted(highs[0]);
        }
        this.invalidate();
    }
    
    public void highlightValue(final int xIndex, final int dataSetIndex) {
        if (xIndex < 0 || dataSetIndex < 0 || xIndex >= this.mData.getXValCount() || dataSetIndex >= this.mData.getDataSetCount()) {
            this.highlightValues(null);
        }
        else {
            this.highlightValues(new Highlight[] { new Highlight(xIndex, dataSetIndex) });
        }
    }
    
    public void highlightValue(final Highlight highlight) {
        this.highlightValue(highlight, false);
    }
    
    public void highlightValue(Highlight high, final boolean callListener) {
        Entry e = null;
        if (high == null) {
            this.mIndicesToHighlight = null;
        }
        else {
            if (this.mLogEnabled) {
                Log.i("MPAndroidChart", "Highlighted: " + high.toString());
            }
            e = this.mData.getEntryForHighlight(high);
            if (e == null || e.getXIndex() != high.getXIndex()) {
                this.mIndicesToHighlight = null;
                high = null;
            }
            else {
                this.mIndicesToHighlight = new Highlight[] { high };
            }
        }
        if (callListener && this.mSelectionListener != null) {
            if (!this.valuesToHighlight()) {
                this.mSelectionListener.onNothingSelected();
            }
            else {
                this.mSelectionListener.onValueSelected(e, high.getDataSetIndex(), high);
            }
        }
        this.invalidate();
    }
    
    @Deprecated
    public void highlightTouch(final Highlight high) {
        this.highlightValue(high, true);
    }
    
    public void setOnTouchListener(final ChartTouchListener l) {
        this.mChartTouchListener = l;
    }
    
    protected void drawMarkers(final Canvas canvas) {
        if (this.mMarkerView == null || !this.mDrawMarkerViews || !this.valuesToHighlight()) {
            return;
        }
        for (int i = 0; i < this.mIndicesToHighlight.length; ++i) {
            final Highlight highlight = this.mIndicesToHighlight[i];
            final int xIndex = highlight.getXIndex();
            final int dataSetIndex = highlight.getDataSetIndex();
            final float deltaX = this.mXAxis.mAxisRange;
            if (xIndex <= deltaX && xIndex <= deltaX * this.mAnimator.getPhaseX()) {
                final Entry e = this.mData.getEntryForHighlight(this.mIndicesToHighlight[i]);
                if (e != null) {
                    if (e.getXIndex() == this.mIndicesToHighlight[i].getXIndex()) {
                        final float[] pos = this.getMarkerPosition(e, highlight);
                        if (this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
                            this.mMarkerView.refreshContent(e, highlight);
                            this.mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                            this.mMarkerView.layout(0, 0, this.mMarkerView.getMeasuredWidth(), this.mMarkerView.getMeasuredHeight());
                            if (pos[1] - this.mMarkerView.getHeight() <= 0.0f) {
                                final float y = this.mMarkerView.getHeight() - pos[1];
                                this.mMarkerView.draw(canvas, pos[0], pos[1] + y);
                            }
                            else {
                                this.mMarkerView.draw(canvas, pos[0], pos[1]);
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected abstract float[] getMarkerPosition(final Entry p0, final Highlight p1);
    
    public ChartAnimator getAnimator() {
        return this.mAnimator;
    }
    
    public boolean isDragDecelerationEnabled() {
        return this.mDragDecelerationEnabled;
    }
    
    public void setDragDecelerationEnabled(final boolean enabled) {
        this.mDragDecelerationEnabled = enabled;
    }
    
    public float getDragDecelerationFrictionCoef() {
        return this.mDragDecelerationFrictionCoef;
    }
    
    public void setDragDecelerationFrictionCoef(float newValue) {
        if (newValue < 0.0f) {
            newValue = 0.0f;
        }
        if (newValue >= 1.0f) {
            newValue = 0.999f;
        }
        this.mDragDecelerationFrictionCoef = newValue;
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY, final EasingFunction easingX, final EasingFunction easingY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }
    
    public void animateX(final int durationMillis, final EasingFunction easing) {
        this.mAnimator.animateX(durationMillis, easing);
    }
    
    public void animateY(final int durationMillis, final EasingFunction easing) {
        this.mAnimator.animateY(durationMillis, easing);
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY, final Easing.EasingOption easingX, final Easing.EasingOption easingY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }
    
    public void animateX(final int durationMillis, final Easing.EasingOption easing) {
        this.mAnimator.animateX(durationMillis, easing);
    }
    
    public void animateY(final int durationMillis, final Easing.EasingOption easing) {
        this.mAnimator.animateY(durationMillis, easing);
    }
    
    public void animateX(final int durationMillis) {
        this.mAnimator.animateX(durationMillis);
    }
    
    public void animateY(final int durationMillis) {
        this.mAnimator.animateY(durationMillis);
    }
    
    public void animateXY(final int durationMillisX, final int durationMillisY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY);
    }
    
    public XAxis getXAxis() {
        return this.mXAxis;
    }
    
    public ValueFormatter getDefaultValueFormatter() {
        return this.mDefaultFormatter;
    }
    
    public void setOnChartValueSelectedListener(final OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }
    
    public void setOnChartGestureListener(final OnChartGestureListener l) {
        this.mGestureListener = l;
    }
    
    public OnChartGestureListener getOnChartGestureListener() {
        return this.mGestureListener;
    }
    
    public float getYMax() {
        return this.mData.getYMax();
    }
    
    public float getYMin() {
        return this.mData.getYMin();
    }
    
    public float getXChartMax() {
        return this.mXAxis.mAxisMaximum;
    }
    
    public float getXChartMin() {
        return this.mXAxis.mAxisMinimum;
    }
    
    public int getXValCount() {
        return this.mData.getXValCount();
    }
    
    public int getValueCount() {
        return this.mData.getYValCount();
    }
    
    public PointF getCenter() {
        return new PointF(this.getWidth() / 2.0f, this.getHeight() / 2.0f);
    }
    
    public PointF getCenterOffsets() {
        return this.mViewPortHandler.getContentCenter();
    }
    
    public void setDescription(String desc) {
        if (desc == null) {
            desc = "";
        }
        this.mDescription = desc;
    }
    
    public void setDescriptionPosition(final float x, final float y) {
        this.mDescriptionPosition = new PointF(x, y);
    }
    
    public void setDescriptionTypeface(final Typeface t) {
        this.mDescPaint.setTypeface(t);
    }
    
    public void setDescriptionTextSize(float size) {
        if (size > 16.0f) {
            size = 16.0f;
        }
        if (size < 6.0f) {
            size = 6.0f;
        }
        this.mDescPaint.setTextSize(Utils.convertDpToPixel(size));
    }
    
    public void setDescriptionColor(final int color) {
        this.mDescPaint.setColor(color);
    }
    
    public void setExtraOffsets(final float left, final float top, final float right, final float bottom) {
        this.setExtraLeftOffset(left);
        this.setExtraTopOffset(top);
        this.setExtraRightOffset(right);
        this.setExtraBottomOffset(bottom);
    }
    
    public void setExtraTopOffset(final float offset) {
        this.mExtraTopOffset = Utils.convertDpToPixel(offset);
    }
    
    public float getExtraTopOffset() {
        return this.mExtraTopOffset;
    }
    
    public void setExtraRightOffset(final float offset) {
        this.mExtraRightOffset = Utils.convertDpToPixel(offset);
    }
    
    public float getExtraRightOffset() {
        return this.mExtraRightOffset;
    }
    
    public void setExtraBottomOffset(final float offset) {
        this.mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }
    
    public float getExtraBottomOffset() {
        return this.mExtraBottomOffset;
    }
    
    public void setExtraLeftOffset(final float offset) {
        this.mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }
    
    public float getExtraLeftOffset() {
        return this.mExtraLeftOffset;
    }
    
    public void setLogEnabled(final boolean enabled) {
        this.mLogEnabled = enabled;
    }
    
    public boolean isLogEnabled() {
        return this.mLogEnabled;
    }
    
    public void setNoDataText(final String text) {
        this.mNoDataText = text;
    }
    
    public void setNoDataTextDescription(final String text) {
        this.mNoDataTextDescription = text;
    }
    
    public void setTouchEnabled(final boolean enabled) {
        this.mTouchEnabled = enabled;
    }
    
    public void setMarkerView(final MarkerView v) {
        this.mMarkerView = v;
    }
    
    public MarkerView getMarkerView() {
        return this.mMarkerView;
    }
    
    public Legend getLegend() {
        return this.mLegend;
    }
    
    public LegendRenderer getLegendRenderer() {
        return this.mLegendRenderer;
    }
    
    public RectF getContentRect() {
        return this.mViewPortHandler.getContentRect();
    }
    
    public void disableScroll() {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }
    
    public void enableScroll() {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }
    
    public void setPaint(final Paint p, final int which) {
        switch (which) {
            case 7: {
                this.mInfoPaint = p;
                break;
            }
            case 11: {
                this.mDescPaint = p;
                break;
            }
        }
    }
    
    public Paint getPaint(final int which) {
        switch (which) {
            case 7: {
                return this.mInfoPaint;
            }
            case 11: {
                return this.mDescPaint;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean isDrawMarkerViewEnabled() {
        return this.mDrawMarkerViews;
    }
    
    public void setDrawMarkerViews(final boolean enabled) {
        this.mDrawMarkerViews = enabled;
    }
    
    public String getXValue(final int index) {
        if (this.mData == null || this.mData.getXValCount() <= index) {
            return null;
        }
        return this.mData.getXVals().get(index);
    }
    
    public List<Entry> getEntriesAtIndex(final int xIndex) {
        final List<Entry> vals = new ArrayList<Entry>();
        for (int i = 0; i < this.mData.getDataSetCount(); ++i) {
            final IDataSet set = (IDataSet)this.mData.getDataSetByIndex(i);
            final Entry e = set.getEntryForXIndex(xIndex);
            if (e != null) {
                vals.add(e);
            }
        }
        return vals;
    }
    
    public T getData() {
        return this.mData;
    }
    
    public ViewPortHandler getViewPortHandler() {
        return this.mViewPortHandler;
    }
    
    public DataRenderer getRenderer() {
        return this.mRenderer;
    }
    
    public void setRenderer(final DataRenderer renderer) {
        if (renderer != null) {
            this.mRenderer = renderer;
        }
    }
    

	public ChartHighlighter getHighlighter() {
        return this.mHighlighter;
    }
    
    public void setHighlighter(final ChartHighlighter highlighter) {
        this.mHighlighter = highlighter;
    }
    
    public PointF getCenterOfView() {
        return this.getCenter();
    }
    
    public Bitmap getChartBitmap() {
        final Bitmap returnedBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(returnedBitmap);
        final Drawable bgDrawable = this.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }
        else {
            canvas.drawColor(-1);
        }
        this.draw(canvas);
        return returnedBitmap;
    }
    
    public boolean saveToPath(final String title, final String pathOnSD) {
        final Bitmap b = this.getChartBitmap();
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(String.valueOf(Environment.getExternalStorageDirectory().getPath()) + pathOnSD + "/" + title + ".png");
            b.compress(Bitmap.CompressFormat.PNG, 40, stream);
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean saveToGallery(String fileName, final String subFolderPath, final String fileDescription, final Bitmap.CompressFormat format, int quality) {
        if (quality < 0 || quality > 100) {
            quality = 50;
        }
        final long currentTime = System.currentTimeMillis();
        final File extBaseDir = Environment.getExternalStorageDirectory();
        final File file = new File(String.valueOf(extBaseDir.getAbsolutePath()) + "/DCIM/" + subFolderPath);
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }
        String mimeType = "";
        switch (format) {
            case PNG: {
                mimeType = "image/png";
                if (!fileName.endsWith(".png")) {
                    fileName = String.valueOf(fileName) + ".png";
                    break;
                }
                break;
            }
            case WEBP: {
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp")) {
                    fileName = String.valueOf(fileName) + ".webp";
                    break;
                }
                break;
            }
            default: {
                mimeType = "image/jpeg";
                if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                    fileName = String.valueOf(fileName) + ".jpg";
                    break;
                }
                break;
            }
        }
        final String filePath = String.valueOf(file.getAbsolutePath()) + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            final Bitmap b = this.getChartBitmap();
            b.compress(format, quality, (OutputStream)out);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        final long size = new File(filePath).length();
        final ContentValues values = new ContentValues(8);
        values.put("title", fileName);
        values.put("_display_name", fileName);
        values.put("date_added", currentTime);
        values.put("mime_type", mimeType);
        values.put("description", fileDescription);
        values.put("orientation", 0);
        values.put("_data", filePath);
        values.put("_size", size);
        return this.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) != null;
    }
    
    public boolean saveToGallery(final String fileName, final int quality) {
        return this.saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.JPEG, quality);
    }
    
    public void removeViewportJob(final Runnable job) {
        this.mJobs.remove(job);
    }
    
    public void clearAllViewportJobs() {
        this.mJobs.clear();
    }
    
    public void addViewportJob(final Runnable job) {
        if (this.mViewPortHandler.hasChartDimens()) {
            this.post(job);
        }
        else {
            this.mJobs.add(job);
        }
    }
    
    public ArrayList<Runnable> getJobs() {
        return this.mJobs;
    }
    
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            this.getChildAt(i).layout(left, top, right, bottom);
        }
    }
    
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int size = (int)Utils.convertDpToPixel(50.0f);
        this.setMeasuredDimension(Math.max(this.getSuggestedMinimumWidth(), resolveSize(size, widthMeasureSpec)), Math.max(this.getSuggestedMinimumHeight(), resolveSize(size, heightMeasureSpec)));
    }
    
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "OnSizeChanged()");
        }
        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            this.mViewPortHandler.setChartDimens(w, h);
            if (this.mLogEnabled) {
                Log.i("MPAndroidChart", "Setting chart dimens, width: " + w + ", height: " + h);
            }
            for (final Runnable r : this.mJobs) {
                this.post(r);
            }
            this.mJobs.clear();
        }
        this.notifyDataSetChanged();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    public void setHardwareAccelerationEnabled(final boolean enabled) {
        if (Build.VERSION.SDK_INT >= 11) {
            if (enabled) {
                this.setLayerType(2, (Paint)null);
            }
            else {
                this.setLayerType(1, (Paint)null);
            }
        }
        else {
            Log.e("MPAndroidChart", "Cannot enable/disable hardware acceleration for devices below API level 11.");
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mUnbind) {
            this.unbindDrawables((View)this);
        }
    }
    
    private void unbindDrawables(final View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback((Drawable.Callback)null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                this.unbindDrawables(((ViewGroup)view).getChildAt(i));
            }
            ((ViewGroup)view).removeAllViews();
        }
    }
    
    public void setUnbindEnabled(final boolean enabled) {
        this.mUnbind = enabled;
    }
}
