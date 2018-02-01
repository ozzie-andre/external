// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

//import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
//import java.util.Iterator;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
//import com.github.mikephil.charting.highlight.ChartHighlighter;
//import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.highlight.CombinedHighlighter;
import android.util.AttributeSet;
import android.content.Context;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.data.CombinedData;

public class CombinedChart extends BarLineChartBase<CombinedData> implements LineDataProvider, BarDataProvider, ScatterDataProvider, CandleDataProvider, BubbleDataProvider
{
    private boolean mDrawHighlightArrow;
    private boolean mDrawValueAboveBar;
    private boolean mDrawBarShadow;
    protected DrawOrder[] mDrawOrder;
    
    public CombinedChart(final Context context) {
        super(context);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mDrawOrder = new DrawOrder[] { DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER };
    }
    
    public CombinedChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mDrawOrder = new DrawOrder[] { DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER };
    }
    
    public CombinedChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mDrawHighlightArrow = false;
        this.mDrawValueAboveBar = true;
        this.mDrawBarShadow = false;
        this.mDrawOrder = new DrawOrder[] { DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER };
    }
    
    @Override
    protected void init() {
        super.init();
        this.setHighlighter(new CombinedHighlighter(this));
    }
    
    @Override
    protected void calcMinMax() {
        super.calcMinMax();
        if (this.getBarData() != null || this.getCandleData() != null || this.getBubbleData() != null) {
            this.mXAxis.mAxisMinimum = -0.5f;
            this.mXAxis.mAxisMaximum = ((CombinedData)this.mData).getXVals().size() - 0.5f;
            if (this.getBubbleData() != null) {
                for (final IBubbleDataSet set : this.getBubbleData().getDataSets()) {
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
        }
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
        if (this.mXAxis.mAxisRange == 0.0f && this.getLineData() != null && this.getLineData().getYValCount() > 0) {
            this.mXAxis.mAxisRange = 1.0f;
        }
    }
    
    @Override
    public void setData(final CombinedData data) {
        this.mData = null;
        this.mRenderer = null;
        super.setData(data);
        (this.mRenderer = new CombinedChartRenderer(this, this.mAnimator, this.mViewPortHandler)).initBuffers();
    }
    
    @Override
    public LineData getLineData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getLineData();
    }
    
    @Override
    public BarData getBarData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getBarData();
    }
    
    @Override
    public ScatterData getScatterData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getScatterData();
    }
    
    @Override
    public CandleData getCandleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getCandleData();
    }
    
    @Override
    public BubbleData getBubbleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData)this.mData).getBubbleData();
    }
    
    @Override
    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }
    
    @Override
    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }
    
    @Override
    public boolean isDrawHighlightArrowEnabled() {
        return this.mDrawHighlightArrow;
    }
    
    public void setDrawHighlightArrow(final boolean enabled) {
        this.mDrawHighlightArrow = enabled;
    }
    
    public void setDrawValueAboveBar(final boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }
    
    public void setDrawBarShadow(final boolean enabled) {
        this.mDrawBarShadow = enabled;
    }
    
    public DrawOrder[] getDrawOrder() {
        return this.mDrawOrder;
    }
    
    public void setDrawOrder(final DrawOrder[] order) {
        if (order == null || order.length <= 0) {
            return;
        }
        this.mDrawOrder = order;
    }
    
    public enum DrawOrder
    {
        BAR("BAR", 0), 
        BUBBLE("BUBBLE", 1), 
        LINE("LINE", 2), 
        CANDLE("CANDLE", 3), 
        SCATTER("SCATTER", 4);
        
        private DrawOrder(final String s, final int n) {
        }
    }
}
