// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class DefaultFillFormatter implements FillFormatter
{
    @Override
    public float getFillLinePosition(final ILineDataSet dataSet, final LineDataProvider dataProvider) {
        float fillMin = 0.0f;
        final float chartMaxY = dataProvider.getYChartMax();
        final float chartMinY = dataProvider.getYChartMin();
        final LineData data = dataProvider.getLineData();
        if (dataSet.getYMax() > 0.0f && dataSet.getYMin() < 0.0f) {
            fillMin = 0.0f;
        }
        else {
            if (data.getYMax() > 0.0f) {
                final float max = 0.0f;
            }
            else {
                final float max = chartMaxY;
            }
            float min;
            if (data.getYMin() < 0.0f) {
                min = 0.0f;
            }
            else {
                min = chartMinY;
            }
            float max;
            if (data.getYMax() < 0.0f) {
                max = 0.0f;
            }
            else {
                max = chartMaxY; 
            }
            fillMin = ((dataSet.getYMin() >= 0.0f) ? min : max);
        }
        return fillMin;
    }
}
