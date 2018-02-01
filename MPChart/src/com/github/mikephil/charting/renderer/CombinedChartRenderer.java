// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.highlight.Highlight;
import android.graphics.Canvas;
//import java.util.Iterator;
//import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
//import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
//import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
//import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
//import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import java.util.List;

public class CombinedChartRenderer extends DataRenderer
{
    protected List<DataRenderer> mRenderers;
    
    public CombinedChartRenderer(final CombinedChart chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.createRenderers(chart, animator, viewPortHandler);
    }
    
    protected void createRenderers(final CombinedChart chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        this.mRenderers = new ArrayList<DataRenderer>();
        final CombinedChart.DrawOrder[] orders = chart.getDrawOrder();
        CombinedChart.DrawOrder[] array;
        for (int length = (array = orders).length, i = 0; i < length; ++i) {
            final CombinedChart.DrawOrder order = array[i];
            switch (order) {
                case BAR: {
                    if (chart.getBarData() != null) {
                        this.mRenderers.add(new BarChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                    break;
                }
                case BUBBLE: {
                    if (chart.getBubbleData() != null) {
                        this.mRenderers.add(new BubbleChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                    break;
                }
                case LINE: {
                    if (chart.getLineData() != null) {
                        this.mRenderers.add(new LineChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                    break;
                }
                case CANDLE: {
                    if (chart.getCandleData() != null) {
                        this.mRenderers.add(new CandleStickChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                    break;
                }
                case SCATTER: {
                    if (chart.getScatterData() != null) {
                        this.mRenderers.add(new ScatterChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void initBuffers() {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.initBuffers();
        }
    }
    
    @Override
    public void drawData(final Canvas c) {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.drawData(c);
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.drawValues(c);
        }
    }
    
    @Override
    public void drawExtras(final Canvas c) {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.drawExtras(c);
        }
    }
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.drawHighlighted(c, indices);
        }
    }
    
    @Override
    public void calcXBounds(final BarLineScatterCandleBubbleDataProvider chart, final int xAxisModulus) {
        for (final DataRenderer renderer : this.mRenderers) {
            renderer.calcXBounds(chart, xAxisModulus);
        }
    }
    
    public DataRenderer getSubRenderer(final int index) {
        if (index >= this.mRenderers.size() || index < 0) {
            return null;
        }
        return this.mRenderers.get(index);
    }
    
    public List<DataRenderer> getSubRenderers() {
        return this.mRenderers;
    }
    
    public void setSubRenderers(final List<DataRenderer> renderers) {
        this.mRenderers = renderers;
    }
}
