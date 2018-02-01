// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.animation;

import android.annotation.SuppressLint;
import android.animation.TimeInterpolator;

@SuppressLint({ "NewApi" })
public interface EasingFunction extends TimeInterpolator
{
    float getInterpolation(final float p0);
}
