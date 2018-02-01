// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.ChartData;
import java.util.ArrayList;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.utils.SelectionDetail;
import java.util.List;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public class CombinedHighlighter extends ChartHighlighter<BarLineScatterCandleBubbleDataProvider>
{
    public CombinedHighlighter(final BarLineScatterCandleBubbleDataProvider chart) {
        super(chart);
    }
    
    @Override
    protected List<SelectionDetail> getSelectionDetailsAtIndex(final int xIndex) {
        final CombinedData data = (CombinedData)this.mChart.getData();
        final List<ChartData> dataObjects = data.getAllData();
        final List<SelectionDetail> vals = new ArrayList<SelectionDetail>();
        final float[] pts = new float[2];
        for (int i = 0; i < dataObjects.size(); ++i) {
            for (int j = 0; j < dataObjects.get(i).getDataSetCount(); ++j) {
                final IDataSet dataSet = dataObjects.get(i).getDataSetByIndex(j);
                if (dataSet.isHighlightEnabled()) {
                    final float yVal = dataSet.getYValForXIndex(xIndex);
                    if (yVal != Float.NaN) {
                        pts[1] = yVal;
                        this.mChart.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(pts);
                        if (!Float.isNaN(pts[1])) {
                            vals.add(new SelectionDetail(pts[1], j, dataSet));
                        }
                    }
                }
            }
        }
        return vals;
    }
}
