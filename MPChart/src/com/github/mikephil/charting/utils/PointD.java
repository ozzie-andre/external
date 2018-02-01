// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

public class PointD
{
    public double x;
    public double y;
    
    public PointD(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "PointD, x: " + this.x + ", y: " + this.y;
    }
}
