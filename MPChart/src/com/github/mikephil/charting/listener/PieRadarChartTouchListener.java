// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.utils.SelectionDetail;
import java.util.List;
//import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.highlight.Highlight;
import android.annotation.SuppressLint;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.utils.Utils;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import android.graphics.PointF;
import com.github.mikephil.charting.charts.PieRadarChartBase;

@SuppressWarnings("rawtypes")

public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>>
{
    private PointF mTouchStartPoint;
    private float mStartAngle;
    private ArrayList<AngularVelocitySample> _velocitySamples;
    private long mDecelerationLastTime;
    private float mDecelerationAngularVelocity;
    
    public PieRadarChartTouchListener(final PieRadarChartBase<?> chart) {
        super(chart);
        this.mTouchStartPoint = new PointF();
        this.mStartAngle = 0.0f;
        this._velocitySamples = new ArrayList<AngularVelocitySample>();
        this.mDecelerationLastTime = 0L;
        this.mDecelerationAngularVelocity = 0.0f;
    }
    
	@SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouch(final View v, final MotionEvent event) {
        if (this.mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        if (((PieRadarChartBase)this.mChart).isRotationEnabled()) {
            final float x = event.getX();
            final float y = event.getY();
            switch (event.getAction()) {
                case 0: {
                    this.startAction(event);
                    this.stopDeceleration();
                    this.resetVelocity();
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.sampleVelocity(x, y);
                    }
                    this.setGestureStartAngle(x, y);
                    this.mTouchStartPoint.x = x;
                    this.mTouchStartPoint.y = y;
                    break;
                }
                case 2: {
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.sampleVelocity(x, y);
                    }
                    if (this.mTouchMode == 0 && ChartTouchListener.distance(x, this.mTouchStartPoint.x, y, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0f)) {
                        this.mLastGesture = ChartGesture.ROTATE;
                        this.mTouchMode = 6;
                        ((PieRadarChartBase)this.mChart).disableScroll();
                    }
                    else if (this.mTouchMode == 6) {
                        this.updateGestureRotation(x, y);
                        ((PieRadarChartBase)this.mChart).invalidate();
                    }
                    this.endAction(event);
                    break;
                }
                case 1: {
                    if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                        this.stopDeceleration();
                        this.sampleVelocity(x, y);
                        this.mDecelerationAngularVelocity = this.calculateVelocity();
                        if (this.mDecelerationAngularVelocity != 0.0f) {
                            this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                            Utils.postInvalidateOnAnimation((View)this.mChart);
                        }
                    }
                    ((PieRadarChartBase)this.mChart).enableScroll();
                    this.mTouchMode = 0;
                    this.endAction(event);
                    break;
                }
            }
        }
        return true;
    }
    
    public void onLongPress(final MotionEvent me) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        final OnChartGestureListener l = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartLongPressed(me);
        }
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent e) {
        return true;
    }
    
    public boolean onSingleTapUp(final MotionEvent e) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        final OnChartGestureListener l = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartSingleTapped(e);
        }
        if (!((PieRadarChartBase)this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        final float distance = ((PieRadarChartBase)this.mChart).distanceToCenter(e.getX(), e.getY());
        if (distance > ((PieRadarChartBase)this.mChart).getRadius()) {
            if (this.mLastHighlighted == null) {
                ((PieRadarChartBase)this.mChart).highlightValues(null);
            }
            else {
                ((PieRadarChartBase)this.mChart).highlightTouch(null);
            }
            this.mLastHighlighted = null;
        }
        else {
            float angle = ((PieRadarChartBase)this.mChart).getAngleForPoint(e.getX(), e.getY());
            if (this.mChart instanceof PieChart) {
                angle /= ((PieRadarChartBase)this.mChart).getAnimator().getPhaseY();
            }
            final int index = ((PieRadarChartBase)this.mChart).getIndexForAngle(angle);
            if (index < 0) {
                ((PieRadarChartBase)this.mChart).highlightValues(null);
                this.mLastHighlighted = null;
            }
            else {
                final List<SelectionDetail> valsAtIndex = (List<SelectionDetail>)((PieRadarChartBase)this.mChart).getSelectionDetailsAtIndex(index);
                int dataSetIndex = 0;
                if (this.mChart instanceof RadarChart) {
                    dataSetIndex = Utils.getClosestDataSetIndex(valsAtIndex, distance / ((RadarChart)this.mChart).getFactor(), null);
                }
                if (dataSetIndex < 0) {
                    ((PieRadarChartBase)this.mChart).highlightValues(null);
                    this.mLastHighlighted = null;
                }
                else {
                    final Highlight h = new Highlight(index, dataSetIndex);
                    this.performHighlight(h, e);
                }
            }
        }
        return true;
    }
    
    private void resetVelocity() {
        this._velocitySamples.clear();
    }
    
    private void sampleVelocity(final float touchLocationX, final float touchLocationY) {
        final long currentTime = AnimationUtils.currentAnimationTimeMillis();
        this._velocitySamples.add(new AngularVelocitySample(currentTime, ((PieRadarChartBase)this.mChart).getAngleForPoint(touchLocationX, touchLocationY)));
        for (int i = 0, count = this._velocitySamples.size(); i < count - 2 && currentTime - this._velocitySamples.get(i).time > 1000L; --i, --count, ++i) {
            this._velocitySamples.remove(0);
        }
    }
    
    private float calculateVelocity() {
        if (this._velocitySamples.isEmpty()) {
            return 0.0f;
        }
        final AngularVelocitySample firstSample = this._velocitySamples.get(0);
        final AngularVelocitySample lastSample = this._velocitySamples.get(this._velocitySamples.size() - 1);
        AngularVelocitySample beforeLastSample = firstSample;
        for (int i = this._velocitySamples.size() - 1; i >= 0; --i) {
            beforeLastSample = this._velocitySamples.get(i);
            if (beforeLastSample.angle != lastSample.angle) {
                break;
            }
        }
        float timeDelta = (lastSample.time - firstSample.time) / 1000.0f;
        if (timeDelta == 0.0f) {
            timeDelta = 0.1f;
        }
        boolean clockwise = lastSample.angle >= beforeLastSample.angle;
        if (Math.abs(lastSample.angle - beforeLastSample.angle) > 270.0) {
            clockwise = !clockwise;
        }
        if (lastSample.angle - firstSample.angle > 180.0) {
            final AngularVelocitySample angularVelocitySample = firstSample;
            angularVelocitySample.angle += 360.0;
        }
        else if (firstSample.angle - lastSample.angle > 180.0) {
            final AngularVelocitySample angularVelocitySample2 = lastSample;
            angularVelocitySample2.angle += 360.0;
        }
        float velocity = Math.abs((lastSample.angle - firstSample.angle) / timeDelta);
        if (!clockwise) {
            velocity = -velocity;
        }
        return velocity;
    }
    
    public void setGestureStartAngle(final float x, final float y) {
        this.mStartAngle = ((PieRadarChartBase)this.mChart).getAngleForPoint(x, y) - ((PieRadarChartBase)this.mChart).getRawRotationAngle();
    }
    
    public void updateGestureRotation(final float x, final float y) {
        ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getAngleForPoint(x, y) - this.mStartAngle);
    }
    
    public void stopDeceleration() {
        this.mDecelerationAngularVelocity = 0.0f;
    }
    
    public void computeScroll() {
        if (this.mDecelerationAngularVelocity == 0.0f) {
            return;
        }
        final long currentTime = AnimationUtils.currentAnimationTimeMillis();
        this.mDecelerationAngularVelocity *= ((PieRadarChartBase)this.mChart).getDragDecelerationFrictionCoef();
        final float timeInterval = (currentTime - this.mDecelerationLastTime) / 1000.0f;
        ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getRotationAngle() + this.mDecelerationAngularVelocity * timeInterval);
        this.mDecelerationLastTime = currentTime;
        if (Math.abs(this.mDecelerationAngularVelocity) >= 0.001) {
            Utils.postInvalidateOnAnimation((View)this.mChart);
        }
        else {
            this.stopDeceleration();
        }
    }
    
    private class AngularVelocitySample
    {
        public long time;
        public float angle;
        
        public AngularVelocitySample(final long time, final float angle) {
            this.time = time;
            this.angle = angle;
        }
    }
}
