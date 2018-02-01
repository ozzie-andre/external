// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import android.annotation.SuppressLint;
import android.animation.ValueAnimator;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.os.Build;
import com.github.mikephil.charting.animation.Easing;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.SelectionDetail;
import java.util.List;
import android.graphics.RectF;
import com.github.mikephil.charting.components.XAxis;
import android.util.Log;
import android.graphics.PointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.Legend;
import android.view.View;
import android.view.MotionEvent;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.ChartData;

public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>> extends Chart<T>
{
    private float mRotationAngle;
    private float mRawRotationAngle;
    protected boolean mRotateEnabled;
    protected float mMinOffset;
    
    public PieRadarChartBase(final Context context) {
        super(context);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    public PieRadarChartBase(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    public PieRadarChartBase(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mRotationAngle = 270.0f;
        this.mRawRotationAngle = 270.0f;
        this.mRotateEnabled = true;
        this.mMinOffset = 0.0f;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mChartTouchListener = new PieRadarChartTouchListener(this);
    }
    
    @Override
    protected void calcMinMax() {
        this.mXAxis.mAxisRange = this.mData.getXVals().size() - 1;
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        if (this.mTouchEnabled && this.mChartTouchListener != null) {
            return this.mChartTouchListener.onTouch((View)this, event);
        }
        return super.onTouchEvent(event);
    }
    
    public void computeScroll() {
        if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener)this.mChartTouchListener).computeScroll();
        }
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (this.mData == null) {
            return;
        }
        this.calcMinMax();
        if (this.mLegend != null) {
            this.mLegendRenderer.computeLegend(this.mData);
        }
        this.calculateOffsets();
    }
    
    public void calculateOffsets() {
        float legendLeft = 0.0f;
        float legendRight = 0.0f;
        float legendBottom = 0.0f;
        float legendTop = 0.0f;
        if (this.mLegend != null && this.mLegend.isEnabled()) {
            final float fullLegendWidth = Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getFormSize() + this.mLegend.getFormToTextSpace();
            if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                final float spacing = Utils.convertDpToPixel(13.0f);
                legendRight = fullLegendWidth + spacing;
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART) {
                final float spacing = Utils.convertDpToPixel(8.0f);
                final float legendWidth = fullLegendWidth + spacing;
                final float legendHeight = this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax;
                final PointF c = this.getCenter();
                final PointF bottomRight = new PointF(this.getWidth() - legendWidth + 15.0f, legendHeight + 15.0f);
                final float distLegend = this.distanceToCenter(bottomRight.x, bottomRight.y);
                final PointF reference = this.getPosition(c, this.getRadius(), this.getAngleForPoint(bottomRight.x, bottomRight.y));
                final float distReference = this.distanceToCenter(reference.x, reference.y);
                final float min = Utils.convertDpToPixel(5.0f);
                if (distLegend < distReference) {
                    final float diff = distReference - distLegend;
                    legendRight = min + diff;
                }
                if (bottomRight.y >= c.y && this.getHeight() - legendWidth > this.getWidth()) {
                    legendRight = legendWidth;
                }
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                final float spacing = Utils.convertDpToPixel(13.0f);
                legendLeft = fullLegendWidth + spacing;
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART) {
                final float spacing = Utils.convertDpToPixel(8.0f);
                final float legendWidth = fullLegendWidth + spacing;
                final float legendHeight = this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax;
                final PointF c = this.getCenter();
                final PointF bottomLeft = new PointF(legendWidth - 15.0f, legendHeight + 15.0f);
                final float distLegend = this.distanceToCenter(bottomLeft.x, bottomLeft.y);
                final PointF reference = this.getPosition(c, this.getRadius(), this.getAngleForPoint(bottomLeft.x, bottomLeft.y));
                final float distReference = this.distanceToCenter(reference.x, reference.y);
                final float min = Utils.convertDpToPixel(5.0f);
                if (distLegend < distReference) {
                    final float diff = distReference - distLegend;
                    legendLeft = min + diff;
                }
                if (bottomLeft.y >= c.y && this.getHeight() - legendWidth > this.getWidth()) {
                    legendLeft = legendWidth;
                }
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                final float yOffset = this.getRequiredLegendOffset();
                legendBottom = Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            }
            else if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                final float yOffset = this.getRequiredLegendOffset();
                legendTop = Math.min(this.mLegend.mNeededHeight + yOffset, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            }
            legendLeft += this.getRequiredBaseOffset();
            legendRight += this.getRequiredBaseOffset();
            legendTop += this.getRequiredBaseOffset();
        }
        float minOffset = Utils.convertDpToPixel(this.mMinOffset);
        if (this instanceof RadarChart) {
            final XAxis x = ((RadarChart)this).getXAxis();
            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, x.mLabelRotatedWidth);
            }
        }
        legendTop += this.getExtraTopOffset();
        legendRight += this.getExtraRightOffset();
        legendBottom += this.getExtraBottomOffset();
        legendLeft += this.getExtraLeftOffset();
        final float offsetLeft = Math.max(minOffset, legendLeft);
        final float offsetTop = Math.max(minOffset, legendTop);
        final float offsetRight = Math.max(minOffset, legendRight);
        final float offsetBottom = Math.max(minOffset, Math.max(this.getRequiredBaseOffset(), legendBottom));
        this.mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
        if (this.mLogEnabled) {
            Log.i("MPAndroidChart", "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
        }
    }
    
    public float getAngleForPoint(final float x, final float y) {
        final PointF c = this.getCenterOffsets();
        final double tx = x - c.x;
        final double ty = y - c.y;
        final double length = Math.sqrt(tx * tx + ty * ty);
        final double r = Math.acos(ty / length);
        float angle = (float)Math.toDegrees(r);
        if (x > c.x) {
            angle = 360.0f - angle;
        }
        angle += 90.0f;
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        return angle;
    }
    
    protected PointF getPosition(final PointF center, final float dist, final float angle) {
        final PointF p = new PointF((float)(center.x + dist * Math.cos(Math.toRadians(angle))), (float)(center.y + dist * Math.sin(Math.toRadians(angle))));
        return p;
    }
    
    public float distanceToCenter(final float x, final float y) {
        final PointF c = this.getCenterOffsets();
        float dist = 0.0f;
        float xDist = 0.0f;
        float yDist = 0.0f;
        if (x > c.x) {
            xDist = x - c.x;
        }
        else {
            xDist = c.x - x;
        }
        if (y > c.y) {
            yDist = y - c.y;
        }
        else {
            yDist = c.y - y;
        }
        dist = (float)Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0));
        return dist;
    }
    
    public abstract int getIndexForAngle(final float p0);
    
    public void setRotationAngle(final float angle) {
        this.mRawRotationAngle = angle;
        this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
    }
    
    public float getRawRotationAngle() {
        return this.mRawRotationAngle;
    }
    
    public float getRotationAngle() {
        return this.mRotationAngle;
    }
    
    public void setRotationEnabled(final boolean enabled) {
        this.mRotateEnabled = enabled;
    }
    
    public boolean isRotationEnabled() {
        return this.mRotateEnabled;
    }
    
    public float getMinOffset() {
        return this.mMinOffset;
    }
    
    public void setMinOffset(final float minOffset) {
        this.mMinOffset = minOffset;
    }
    
    public float getDiameter() {
        final RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width(), content.height());
    }
    
    public abstract float getRadius();
    
    protected abstract float getRequiredLegendOffset();
    
    protected abstract float getRequiredBaseOffset();
    
    public float getYChartMax() {
        return 0.0f;
    }
    
    public float getYChartMin() {
        return 0.0f;
    }
    
    public List<SelectionDetail> getSelectionDetailsAtIndex(final int xIndex) {
        final List<SelectionDetail> vals = new ArrayList<SelectionDetail>();
        for (int i = 0; i < this.mData.getDataSetCount(); ++i) {
            final IDataSet<?> dataSet = ((ChartData<IDataSet<?>>)this.mData).getDataSetByIndex(i);
            final float yVal = dataSet.getYValForXIndex(xIndex);
            if (yVal != Float.NaN) {
                vals.add(new SelectionDetail(yVal, i, dataSet));
            }
        }
        return vals;
    }
    
    @SuppressLint({ "NewApi" })
    public void spin(final int durationmillis, final float fromangle, final float toangle, final Easing.EasingOption easing) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        this.setRotationAngle(fromangle);
        final ObjectAnimator spinAnimator = ObjectAnimator.ofFloat((Object)this, "rotationAngle", new float[] { fromangle, toangle });
        spinAnimator.setDuration((long)durationmillis);
        spinAnimator.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easing));
        spinAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator animation) {
                PieRadarChartBase.this.postInvalidate();
            }
        });
        spinAnimator.start();
    }
}
