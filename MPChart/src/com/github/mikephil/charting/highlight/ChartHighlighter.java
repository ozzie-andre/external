// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.ArrayList;
import com.github.mikephil.charting.utils.SelectionDetail;
import java.util.List;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

@SuppressWarnings("rawtypes")
public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider>
{
    protected T mChart;
    
    public ChartHighlighter(final T chart) {
        this.mChart = chart;
    }
    
    public Highlight getHighlight(final float x, final float y) {
        final int xIndex = this.getXIndex(x);
        if (xIndex == -2147483647) {
            return null;
        }
        final int dataSetIndex = this.getDataSetIndex(xIndex, x, y);
        if (dataSetIndex == -2147483647) {
            return null;
        }
        return new Highlight(xIndex, dataSetIndex);
    }
    
    protected int getXIndex(final float x) {
        final float[] pts = { x, 0.0f };
        this.mChart.getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return Math.round(pts[0]);
    }
    
    protected int getDataSetIndex(final int xIndex, final float x, final float y) {
        final List<SelectionDetail> valsAtIndex = this.getSelectionDetailsAtIndex(xIndex);
        final float leftdist = Utils.getMinimumDistance(valsAtIndex, y, YAxis.AxisDependency.LEFT);
        final float rightdist = Utils.getMinimumDistance(valsAtIndex, y, YAxis.AxisDependency.RIGHT);
        final YAxis.AxisDependency axis = (leftdist < rightdist) ? YAxis.AxisDependency.LEFT : YAxis.AxisDependency.RIGHT;
        final int dataSetIndex = Utils.getClosestDataSetIndex(valsAtIndex, y, axis);
        return dataSetIndex;
    }
    
  
	protected List<SelectionDetail> getSelectionDetailsAtIndex(final int xIndex) {
        final List<SelectionDetail> vals = new ArrayList<SelectionDetail>();
        final float[] pts = new float[2];
        for (int i = 0; i < this.mChart.getData().getDataSetCount(); ++i) {
            final IDataSet dataSet = this.mChart.getData().getDataSetByIndex(i);
            if (dataSet.isHighlightEnabled()) {
                final float yVal = dataSet.getYValForXIndex(xIndex);
                if (yVal != Float.NaN) {
                    pts[1] = yVal;
                    this.mChart.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);
                    if (!Float.isNaN(pts[1])) {
                        vals.add(new SelectionDetail(pts[1], i, dataSet));
                    }
                }
            }
        }
        return vals;
    }
}
