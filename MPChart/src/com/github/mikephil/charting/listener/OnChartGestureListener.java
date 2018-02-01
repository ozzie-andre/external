// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import android.view.MotionEvent;

public interface OnChartGestureListener
{
    void onChartGestureStart(final MotionEvent p0, final ChartTouchListener.ChartGesture p1);
    
    void onChartGestureEnd(final MotionEvent p0, final ChartTouchListener.ChartGesture p1);
    
    void onChartLongPressed(final MotionEvent p0);
    
    void onChartDoubleTapped(final MotionEvent p0);
    
    void onChartSingleTapped(final MotionEvent p0);
    
    void onChartFling(final MotionEvent p0, final MotionEvent p1, final float p2, final float p3);
    
    void onChartScale(final MotionEvent p0, final float p1, final float p2);
    
    void onChartTranslate(final MotionEvent p0, final float p1, final float p2);
}
