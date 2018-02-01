// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import android.util.Log;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import android.annotation.SuppressLint;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.view.View;
import com.github.mikephil.charting.utils.Utils;
import android.view.VelocityTracker;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.PointF;
import android.graphics.Matrix;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.charts.BarLineChartBase;
@SuppressWarnings("rawtypes")

public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>>
{
    private Matrix mMatrix;
    private Matrix mSavedMatrix;
    private PointF mTouchStartPoint;
    private PointF mTouchPointCenter;
    private float mSavedXDist;
    private float mSavedYDist;
    private float mSavedDist;
    private IDataSet mClosestDataSetToTouch;
    private VelocityTracker mVelocityTracker;
    private long mDecelerationLastTime;
    private PointF mDecelerationCurrentPoint;
    private PointF mDecelerationVelocity;
    private float mDragTriggerDist;
    private float mMinScalePointerDistance;
    
    public BarLineChartTouchListener(final BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart, final Matrix touchMatrix) {
        super(chart);
        this.mMatrix = new Matrix();
        this.mSavedMatrix = new Matrix();
        this.mTouchStartPoint = new PointF();
        this.mTouchPointCenter = new PointF();
        this.mSavedXDist = 1.0f;
        this.mSavedYDist = 1.0f;
        this.mSavedDist = 1.0f;
        this.mDecelerationLastTime = 0L;
        this.mDecelerationCurrentPoint = new PointF();
        this.mDecelerationVelocity = new PointF();
        this.mMatrix = touchMatrix;
        this.mDragTriggerDist = Utils.convertDpToPixel(3.0f);
        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);
    }
    
	@SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouch(final View v, final MotionEvent event) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        if (event.getActionMasked() == 3 && this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (this.mTouchMode == 0) {
            this.mGestureDetector.onTouchEvent(event);
        }
        if (!((BarLineChartBase)this.mChart).isDragEnabled() && !((BarLineChartBase)this.mChart).isScaleXEnabled() && !((BarLineChartBase)this.mChart).isScaleYEnabled()) {
            return true;
        }
        switch (event.getAction() & 0xFF) {
            case 0: {
                this.startAction(event);
                this.stopDeceleration();
                this.saveTouchStart(event);
                break;
            }
            case 5: {
                if (event.getPointerCount() >= 2) {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    this.saveTouchStart(event);
                    this.mSavedXDist = getXDist(event);
                    this.mSavedYDist = getYDist(event);
                    this.mSavedDist = spacing(event);
                    if (this.mSavedDist > 10.0f) {
                        if (((BarLineChartBase)this.mChart).isPinchZoomEnabled()) {
                            this.mTouchMode = 4;
                        }
                        else if (this.mSavedXDist > this.mSavedYDist) {
                            this.mTouchMode = 2;
                        }
                        else {
                            this.mTouchMode = 3;
                        }
                    }
                    midPoint(this.mTouchPointCenter, event);
                    break;
                }
                break;
            }
            case 2: {
                if (this.mTouchMode == 1) {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    this.performDrag(event);
                    break;
                }
                if (this.mTouchMode == 2 || this.mTouchMode == 3 || this.mTouchMode == 4) {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    if (((BarLineChartBase)this.mChart).isScaleXEnabled() || ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                        this.performZoom(event);
                        break;
                    }
                    break;
                }
                else {
                    if (this.mTouchMode != 0 || Math.abs(ChartTouchListener.distance(event.getX(), this.mTouchStartPoint.x, event.getY(), this.mTouchStartPoint.y)) <= this.mDragTriggerDist) {
                        break;
                    }
                    if (((BarLineChartBase)this.mChart).hasNoDragOffset()) {
                        if (!((BarLineChartBase)this.mChart).isFullyZoomedOut() && ((BarLineChartBase)this.mChart).isDragEnabled()) {
                            this.mTouchMode = 1;
                            break;
                        }
                        this.mLastGesture = ChartGesture.DRAG;
                        if (((BarLineChartBase)this.mChart).isHighlightPerDragEnabled()) {
                            this.performHighlightDrag(event);
                            break;
                        }
                        break;
                    }
                    else {
                        if (((BarLineChartBase)this.mChart).isDragEnabled()) {
                            this.mLastGesture = ChartGesture.DRAG;
                            this.mTouchMode = 1;
                            break;
                        }
                        break;
                    }
                }
                //break;
            }
            case 1: {
                final VelocityTracker velocityTracker = this.mVelocityTracker;
                final int pointerId = event.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, (float)Utils.getMaximumFlingVelocity());
                final float velocityY = velocityTracker.getYVelocity(pointerId);
                final float velocityX = velocityTracker.getXVelocity(pointerId);
                if ((Math.abs(velocityX) > Utils.getMinimumFlingVelocity() || Math.abs(velocityY) > Utils.getMinimumFlingVelocity()) && this.mTouchMode == 1 && ((BarLineChartBase)this.mChart).isDragDecelerationEnabled()) {
                    this.stopDeceleration();
                    this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDecelerationCurrentPoint = new PointF(event.getX(), event.getY());
                    this.mDecelerationVelocity = new PointF(velocityX, velocityY);
                    Utils.postInvalidateOnAnimation((View)this.mChart);
                }
                if (this.mTouchMode == 2 || this.mTouchMode == 3 || this.mTouchMode == 4 || this.mTouchMode == 5) {
                    ((BarLineChartBase)this.mChart).calculateOffsets();
                    ((BarLineChartBase)this.mChart).postInvalidate();
                }
                this.mTouchMode = 0;
                ((BarLineChartBase)this.mChart).enableScroll();
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.endAction(event);
                break;
            }
            case 6: {
                Utils.velocityTrackerPointerUpCleanUpIfNecessary(event, this.mVelocityTracker);
                this.mTouchMode = 5;
                break;
            }
            case 3: {
                this.mTouchMode = 0;
                this.endAction(event);
                break;
            }
        }
        this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, (View)this.mChart, true);
        return true;
    }
    
    private void saveTouchStart(final MotionEvent event) {
        this.mSavedMatrix.set(this.mMatrix);
        this.mTouchStartPoint.set(event.getX(), event.getY());
        this.mClosestDataSetToTouch = ((BarLineChartBase)this.mChart).getDataSetByTouchPoint(event.getX(), event.getY());
    }
    
    private void performDrag(final MotionEvent event) {
        this.mLastGesture = ChartGesture.DRAG;
        this.mMatrix.set(this.mSavedMatrix);
        final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        float dX;
        float dY;
        if (((BarLineChartBase)this.mChart).isAnyAxisInverted() && this.mClosestDataSetToTouch != null && ((BarLineChartBase)this.mChart).getAxis(this.mClosestDataSetToTouch.getAxisDependency()).isInverted()) {
            if (this.mChart instanceof HorizontalBarChart) {
                dX = -(event.getX() - this.mTouchStartPoint.x);
                dY = event.getY() - this.mTouchStartPoint.y;
            }
            else {
                dX = event.getX() - this.mTouchStartPoint.x;
                dY = -(event.getY() - this.mTouchStartPoint.y);
            }
        }
        else {
            dX = event.getX() - this.mTouchStartPoint.x;
            dY = event.getY() - this.mTouchStartPoint.y;
        }
        this.mMatrix.postTranslate(dX, dY);
        if (l != null) {
            l.onChartTranslate(event, dX, dY);
        }
    }
    
    private void performZoom(final MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
            final float totalDist = spacing(event);
            if (totalDist > this.mMinScalePointerDistance) {
                final PointF t = this.getTrans(this.mTouchPointCenter.x, this.mTouchPointCenter.y);
                final ViewPortHandler h = ((BarLineChartBase)this.mChart).getViewPortHandler();
                if (this.mTouchMode == 4) {
                    this.mLastGesture = ChartGesture.PINCH_ZOOM;
                    final float scale = totalDist / this.mSavedDist;
                    final boolean isZoomingOut = scale < 1.0f;
                    final boolean canZoomMoreX = isZoomingOut ? h.canZoomOutMoreX() : h.canZoomInMoreX();
                    final boolean canZoomMoreY = isZoomingOut ? h.canZoomOutMoreY() : h.canZoomInMoreY();
                    final float scaleX = ((BarLineChartBase)this.mChart).isScaleXEnabled() ? scale : 1.0f;
                    final float scaleY = ((BarLineChartBase)this.mChart).isScaleYEnabled() ? scale : 1.0f;
                    if (canZoomMoreY || canZoomMoreX) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(scaleX, scaleY, t.x, t.y);
                        if (l != null) {
                            l.onChartScale(event, scaleX, scaleY);
                        }
                    }
                }
                else if (this.mTouchMode == 2 && ((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                    this.mLastGesture = ChartGesture.X_ZOOM;
                    final float xDist = getXDist(event);
                    final float scaleX2 = xDist / this.mSavedXDist;
                    final boolean isZoomingOut2 = scaleX2 < 1.0f;
                    final boolean canZoomMoreX2 = isZoomingOut2 ? h.canZoomOutMoreX() : h.canZoomInMoreX();
                    if (canZoomMoreX2) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(scaleX2, 1.0f, t.x, t.y);
                        if (l != null) {
                            l.onChartScale(event, scaleX2, 1.0f);
                        }
                    }
                }
                else if (this.mTouchMode == 3 && ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                    this.mLastGesture = ChartGesture.Y_ZOOM;
                    final float yDist = getYDist(event);
                    final float scaleY2 = yDist / this.mSavedYDist;
                    final boolean isZoomingOut2 = scaleY2 < 1.0f;
                    final boolean canZoomMoreY = isZoomingOut2 ? h.canZoomOutMoreY() : h.canZoomInMoreY();
                    if (canZoomMoreY) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(1.0f, scaleY2, t.x, t.y);
                        if (l != null) {
                            l.onChartScale(event, 1.0f, scaleY2);
                        }
                    }
                }
            }
        }
    }
    
    private void performHighlightDrag(final MotionEvent e) {
        final Highlight h = ((BarLineChartBase)this.mChart).getHighlightByTouchPoint(e.getX(), e.getY());
        if (h != null && !h.equalTo(this.mLastHighlighted)) {
            this.mLastHighlighted = h;
            ((BarLineChartBase)this.mChart).highlightValue(h, true);
        }
    }
    
    private static void midPoint(final PointF point, final MotionEvent event) {
        final float x = event.getX(0) + event.getX(1);
        final float y = event.getY(0) + event.getY(1);
        point.set(x / 2.0f, y / 2.0f);
    }
    
    private static float spacing(final MotionEvent event) {
        final float x = event.getX(0) - event.getX(1);
        final float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    
    private static float getXDist(final MotionEvent e) {
        final float x = Math.abs(e.getX(0) - e.getX(1));
        return x;
    }
    
    private static float getYDist(final MotionEvent e) {
        final float y = Math.abs(e.getY(0) - e.getY(1));
        return y;
    }
    
    public PointF getTrans(final float x, final float y) {
        final ViewPortHandler vph = ((BarLineChartBase)this.mChart).getViewPortHandler();
        final float xTrans = x - vph.offsetLeft();
        float yTrans = 0.0f;
        if (((BarLineChartBase)this.mChart).isAnyAxisInverted() && this.mClosestDataSetToTouch != null && ((BarLineChartBase)this.mChart).isInverted(this.mClosestDataSetToTouch.getAxisDependency())) {
            yTrans = -(y - vph.offsetTop());
        }
        else {
            yTrans = -(((BarLineChartBase)this.mChart).getMeasuredHeight() - y - vph.offsetBottom());
        }
        return new PointF(xTrans, yTrans);
    }
    
    public Matrix getMatrix() {
        return this.mMatrix;
    }
    
    public boolean onDoubleTap(final MotionEvent e) {
        this.mLastGesture = ChartGesture.DOUBLE_TAP;
        final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartDoubleTapped(e);
        }
        if (((BarLineChartBase)this.mChart).isDoubleTapToZoomEnabled()) {
            final PointF trans = this.getTrans(e.getX(), e.getY());
            ((BarLineChartBase)this.mChart).zoom(((BarLineChartBase)this.mChart).isScaleXEnabled() ? 1.4f : 1.0f, ((BarLineChartBase)this.mChart).isScaleYEnabled() ? 1.4f : 1.0f, trans.x, trans.y);
            if (((BarLineChartBase)this.mChart).isLogEnabled()) {
                Log.i("BarlineChartTouch", "Double-Tap, Zooming In, x: " + trans.x + ", y: " + trans.y);
            }
        }
        return super.onDoubleTap(e);
    }
    
    public void onLongPress(final MotionEvent e) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartLongPressed(e);
        }
    }
    
    public boolean onSingleTapUp(final MotionEvent e) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartSingleTapped(e);
        }
        if (!((BarLineChartBase)this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        final Highlight h = ((BarLineChartBase)this.mChart).getHighlightByTouchPoint(e.getX(), e.getY());
        this.performHighlight(h, e);
        return super.onSingleTapUp(e);
    }
    
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        this.mLastGesture = ChartGesture.FLING;
        final OnChartGestureListener l = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartFling(e1, e2, velocityX, velocityY);
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
    
    public void stopDeceleration() {
        this.mDecelerationVelocity = new PointF(0.0f, 0.0f);
    }
    
    public void computeScroll() {
        if (this.mDecelerationVelocity.x == 0.0f && this.mDecelerationVelocity.y == 0.0f) {
            return;
        }
        final long currentTime = AnimationUtils.currentAnimationTimeMillis();
        final PointF mDecelerationVelocity = this.mDecelerationVelocity;
        mDecelerationVelocity.x *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
        final PointF mDecelerationVelocity2 = this.mDecelerationVelocity;
        mDecelerationVelocity2.y *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
        final float timeInterval = (currentTime - this.mDecelerationLastTime) / 1000.0f;
        final float distanceX = this.mDecelerationVelocity.x * timeInterval;
        final float distanceY = this.mDecelerationVelocity.y * timeInterval;
        final PointF mDecelerationCurrentPoint = this.mDecelerationCurrentPoint;
        mDecelerationCurrentPoint.x += distanceX;
        final PointF mDecelerationCurrentPoint2 = this.mDecelerationCurrentPoint;
        mDecelerationCurrentPoint2.y += distanceY;
        final MotionEvent event = MotionEvent.obtain(currentTime, currentTime, 2, this.mDecelerationCurrentPoint.x, this.mDecelerationCurrentPoint.y, 0);
        this.performDrag(event);
        event.recycle();
        this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, (View)this.mChart, false);
        this.mDecelerationLastTime = currentTime;
        if (Math.abs(this.mDecelerationVelocity.x) >= 0.01 || Math.abs(this.mDecelerationVelocity.y) >= 0.01) {
            Utils.postInvalidateOnAnimation((View)this.mChart);
        }
        else {
            ((BarLineChartBase)this.mChart).calculateOffsets();
            ((BarLineChartBase)this.mChart).postInvalidate();
            this.stopDeceleration();
        }
    }
}
