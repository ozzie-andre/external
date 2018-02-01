// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.data.BarData;
import android.graphics.PointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.charts.BarChart;

public class XAxisRendererBarChart extends XAxisRenderer
{
    protected BarChart mChart;
    
    public XAxisRendererBarChart(final ViewPortHandler viewPortHandler, final XAxis xAxis, final Transformer trans, final BarChart chart) {
        super(viewPortHandler, xAxis, trans);
        this.mChart = chart;
    }
    
    @Override
    protected void drawLabels(final Canvas c, final float pos, final PointF anchor) {
        final float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        final float[] position = { 0.0f, 0.0f };
        final BarData bd = this.mChart.getData();
        final int step = bd.getDataSetCount();
        for (int i = this.mMinX; i <= this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[0] = i * step + i * bd.getGroupSpace() + bd.getGroupSpace() / 2.0f;
            if (step > 1) {
                final float[] array = position;
                final int n = 0;
                array[n] += (step - 1.0f) / 2.0f;
            }
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsX(position[0]) && i >= 0 && i < this.mXAxis.getValues().size()) {
                final String label = this.mXAxis.getValues().get(i);
                if (this.mXAxis.isAvoidFirstLastClippingEnabled()) {
                    if (i == this.mXAxis.getValues().size() - 1) {
                        final float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        if (position[0] + width / 2.0f > this.mViewPortHandler.contentRight()) {
                            position[0] = this.mViewPortHandler.contentRight() - width / 2.0f;
                        }
                    }
                    else if (i == 0) {
                        final float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        if (position[0] - width / 2.0f < this.mViewPortHandler.contentLeft()) {
                            position[0] = this.mViewPortHandler.contentLeft() + width / 2.0f;
                        }
                    }
                }
                this.drawLabel(c, label, i, position[0], pos, anchor, labelRotationAngleDegrees);
            }
        }
    }
    
    @Override
    public void renderGridLines(final Canvas c) {
        if (!this.mXAxis.isDrawGridLinesEnabled() || !this.mXAxis.isEnabled()) {
            return;
        }
        final float[] position = { 0.0f, 0.0f };
        this.mGridPaint.setColor(this.mXAxis.getGridColor());
        this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
        final BarData bd = this.mChart.getData();
        final int step = bd.getDataSetCount();
        for (int i = this.mMinX; i < this.mMaxX; i += this.mXAxis.mAxisLabelModulus) {
            position[0] = i * step + i * bd.getGroupSpace() - 0.5f;
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsX(position[0])) {
                c.drawLine(position[0], this.mViewPortHandler.offsetTop(), position[0], this.mViewPortHandler.contentBottom(), this.mGridPaint);
            }
        }
    }
}
