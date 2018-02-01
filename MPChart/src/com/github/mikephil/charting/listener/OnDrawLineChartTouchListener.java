// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;

public class OnDrawLineChartTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener
{
    public boolean onTouch(final View v, final MotionEvent event) {
        return false;
    }
}
