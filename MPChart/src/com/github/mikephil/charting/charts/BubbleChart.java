// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

//import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.renderer.BubbleChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.data.BubbleData;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider
{
    public BubbleChart(final Context context) {
        super(context);
    }
    
    public BubbleChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    public BubbleChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new BubbleChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        if (this.mXAxis.mAxisRange == 0.0f && ((BubbleData)this.mData).getYValCount() > 0) {
            this.mXAxis.mAxisRange = 1.0f;
        }
        this.mXAxis.mAxisMinimum = -0.5f;
        this.mXAxis.mAxisMaximum = ((BubbleData)this.mData).getXValCount() - 0.5f;
        if (this.mRenderer != null) {
            for (final IBubbleDataSet set : ((BubbleData)this.mData).getDataSets()) {
                final float xmin = set.getXMin();
                final float xmax = set.getXMax();
                if (xmin < this.mXAxis.mAxisMinimum) {
                    this.mXAxis.mAxisMinimum = xmin;
                }
                if (xmax > this.mXAxis.mAxisMaximum) {
                    this.mXAxis.mAxisMaximum = xmax;
                }
            }
        }
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
    }
    
    @Override
    public BubbleData getBubbleData() {
        return (BubbleData)this.mData;
    }
}
