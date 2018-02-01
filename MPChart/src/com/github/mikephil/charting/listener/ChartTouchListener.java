// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import android.view.MotionEvent;
import com.github.mikephil.charting.highlight.Highlight;
import android.view.View;
import android.view.GestureDetector;
import com.github.mikephil.charting.charts.Chart;

public abstract class ChartTouchListener<T extends Chart<?>> extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener
{
    protected ChartGesture mLastGesture;
    protected static final int NONE = 0;
    protected static final int DRAG = 1;
    protected static final int X_ZOOM = 2;
    protected static final int Y_ZOOM = 3;
    protected static final int PINCH_ZOOM = 4;
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;
    protected int mTouchMode;
    protected Highlight mLastHighlighted;
    protected GestureDetector mGestureDetector;
    protected T mChart;
    
    public ChartTouchListener(final T chart) {
        this.mLastGesture = ChartGesture.NONE;
        this.mTouchMode = 0;
        this.mChart = chart;
        this.mGestureDetector = new GestureDetector(chart.getContext(), (GestureDetector.OnGestureListener)this);
    }
    
    public void startAction(final MotionEvent me) {
        final OnChartGestureListener l = this.mChart.getOnChartGestureListener();
        if (l != null) {
            l.onChartGestureStart(me, this.mLastGesture);
        }
    }
    
    public void endAction(final MotionEvent me) {
        final OnChartGestureListener l = this.mChart.getOnChartGestureListener();
        if (l != null) {
            l.onChartGestureEnd(me, this.mLastGesture);
        }
    }
    
    public void setLastHighlighted(final Highlight high) {
        this.mLastHighlighted = high;
    }
    
    public int getTouchMode() {
        return this.mTouchMode;
    }
    
    public ChartGesture getLastGesture() {
        return this.mLastGesture;
    }
    
    protected void performHighlight(final Highlight h, final MotionEvent e) {
        if (h == null || h.equalTo(this.mLastHighlighted)) {
            this.mChart.highlightTouch(null);
            this.mLastHighlighted = null;
        }
        else {
            this.mLastHighlighted = h;
            this.mChart.highlightTouch(h);
        }
    }
    
    protected static float distance(final float eventX, final float startX, final float eventY, final float startY) {
        final float dx = eventX - startX;
        final float dy = eventY - startY;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    
    public enum ChartGesture
    {
        NONE("NONE", 0), 
        DRAG("DRAG", 1), 
        X_ZOOM("X_ZOOM", 2), 
        Y_ZOOM("Y_ZOOM", 3), 
        PINCH_ZOOM("PINCH_ZOOM", 4), 
        ROTATE("ROTATE", 5), 
        SINGLE_TAP("SINGLE_TAP", 6), 
        DOUBLE_TAP("DOUBLE_TAP", 7), 
        LONG_PRESS("LONG_PRESS", 8), 
        FLING("FLING", 9);
        
        private ChartGesture(final String s, final int n) {
        }
    }
}
