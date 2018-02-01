// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.Utils;
import android.graphics.PointF;
import android.graphics.Canvas;
//import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.charts.RadarChart;

public class XAxisRendererRadarChart extends XAxisRenderer
{
    private RadarChart mChart;
    
    public XAxisRendererRadarChart(final ViewPortHandler viewPortHandler, final XAxis xAxis, final RadarChart chart) {
        super(viewPortHandler, xAxis, null);
        this.mChart = chart;
    }
    
    @Override
    public void renderAxisLabels(final Canvas c) {
        if (!this.mXAxis.isEnabled() || !this.mXAxis.isDrawLabelsEnabled()) {
            return;
        }
        final float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        final PointF drawLabelAnchor = new PointF(0.5f, 0.0f);
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
        final float sliceangle = this.mChart.getSliceAngle();
        final float factor = this.mChart.getFactor();
        final PointF center = this.mChart.getCenterOffsets();
        for (int mod = this.mXAxis.mAxisLabelModulus, i = 0; i < this.mXAxis.getValues().size(); i += mod) {
            final String label = this.mXAxis.getValues().get(i);
            final float angle = (sliceangle * i + this.mChart.getRotationAngle()) % 360.0f;
            final PointF p = Utils.getPosition(center, this.mChart.getYRange() * factor + this.mXAxis.mLabelRotatedWidth / 2.0f, angle);
            this.drawLabel(c, label, i, p.x, p.y - this.mXAxis.mLabelRotatedHeight / 2.0f, drawLabelAnchor, labelRotationAngleDegrees);
        }
    }
    
    @Override
    public void renderLimitLines(final Canvas c) {
    }
}
