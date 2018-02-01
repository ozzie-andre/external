// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class Renderer
{
    protected ViewPortHandler mViewPortHandler;
    protected int mMinX;
    protected int mMaxX;
    
    public Renderer(final ViewPortHandler viewPortHandler) {
        this.mMinX = 0;
        this.mMaxX = 0;
        this.mViewPortHandler = viewPortHandler;
    }
    
    protected boolean fitsBounds(final float val, final float min, final float max) {
        return val >= min && val <= max;
    }
    
    public void calcXBounds(final BarLineScatterCandleBubbleDataProvider dataProvider, final int xAxisModulus) {
        final int low = dataProvider.getLowestVisibleXIndex();
        final int high = dataProvider.getHighestVisibleXIndex();
        final int subLow = (low % xAxisModulus == 0) ? xAxisModulus : 0;
        this.mMinX = Math.max(low / xAxisModulus * xAxisModulus - subLow, 0);
        this.mMaxX = Math.min(high / xAxisModulus * xAxisModulus + xAxisModulus, (int)dataProvider.getXChartMax());
    }
}
