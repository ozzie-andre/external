// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

//import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import java.util.List;
//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.utils.Transformer;
import android.graphics.Path;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Utils;
//import java.util.Iterator;
import android.graphics.Canvas;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.ScatterBuffer;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer
{
    protected ScatterDataProvider mChart;
    protected ScatterBuffer[] mScatterBuffers;
    
    public ScatterChartRenderer(final ScatterDataProvider chart, final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
    }
    
    @Override
    public void initBuffers() {
        final ScatterData scatterData = this.mChart.getScatterData();
        this.mScatterBuffers = new ScatterBuffer[scatterData.getDataSetCount()];
        for (int i = 0; i < this.mScatterBuffers.length; ++i) {
            final IScatterDataSet set = scatterData.getDataSetByIndex(i);
            this.mScatterBuffers[i] = new ScatterBuffer(set.getEntryCount() * 2);
        }
    }
    
    @Override
    public void drawData(final Canvas c) {
        final ScatterData scatterData = this.mChart.getScatterData();
        for (final IScatterDataSet set : scatterData.getDataSets()) {
            if (set.isVisible()) {
                this.drawDataSet(c, set);
            }
        }
    }
    
    protected void drawDataSet(final Canvas c, final IScatterDataSet dataSet) {
        final Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        final float phaseX = this.mAnimator.getPhaseX();
        final float phaseY = this.mAnimator.getPhaseY();
        final float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());
        final float shapeHalf = shapeSize / 2.0f;
        final float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        final float shapeHoleSize = shapeHoleSizeHalf * 2.0f;
        final int shapeHoleColor = dataSet.getScatterShapeHoleColor();
        final float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.0f;
        final float shapeStrokeSizeHalf = shapeStrokeSize / 2.0f;
        final ScatterChart.ScatterShape shape = dataSet.getScatterShape();
        final ScatterBuffer buffer = this.mScatterBuffers[this.mChart.getScatterData().getIndexOfDataSet(dataSet)];
        buffer.setPhases(phaseX, phaseY);
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        switch (shape) {
            case SQUARE: {
                for (int i = 0; i < buffer.size(); i += 2) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[i])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i])) {
                        if (this.mViewPortHandler.isInBoundsY(buffer.buffer[i + 1])) {
                            this.mRenderPaint.setColor(dataSet.getColor(i / 2));
                            if (shapeHoleSize > 0.0) {
                                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                                this.mRenderPaint.setStrokeWidth(shapeStrokeSize);
                                c.drawRect(buffer.buffer[i] - shapeHoleSizeHalf - shapeStrokeSizeHalf, buffer.buffer[i + 1] - shapeHoleSizeHalf - shapeStrokeSizeHalf, buffer.buffer[i] + shapeHoleSizeHalf + shapeStrokeSizeHalf, buffer.buffer[i + 1] + shapeHoleSizeHalf + shapeStrokeSizeHalf, this.mRenderPaint);
                                if (shapeHoleColor != 1122867) {
                                    this.mRenderPaint.setStyle(Paint.Style.FILL);
                                    this.mRenderPaint.setColor(shapeHoleColor);
                                    c.drawRect(buffer.buffer[i] - shapeHoleSizeHalf, buffer.buffer[i + 1] - shapeHoleSizeHalf, buffer.buffer[i] + shapeHoleSizeHalf, buffer.buffer[i + 1] + shapeHoleSizeHalf, this.mRenderPaint);
                                }
                            }
                            else {
                                this.mRenderPaint.setStyle(Paint.Style.FILL);
                                c.drawRect(buffer.buffer[i] - shapeHalf, buffer.buffer[i + 1] - shapeHalf, buffer.buffer[i] + shapeHalf, buffer.buffer[i + 1] + shapeHalf, this.mRenderPaint);
                            }
                        }
                    }
                }
                break;
            }
            case CIRCLE: {
                for (int i = 0; i < buffer.size(); i += 2) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[i])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i])) {
                        if (this.mViewPortHandler.isInBoundsY(buffer.buffer[i + 1])) {
                            this.mRenderPaint.setColor(dataSet.getColor(i / 2));
                            if (shapeHoleSize > 0.0) {
                                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                                this.mRenderPaint.setStrokeWidth(shapeStrokeSize);
                                c.drawCircle(buffer.buffer[i], buffer.buffer[i + 1], shapeHoleSizeHalf + shapeStrokeSizeHalf, this.mRenderPaint);
                                if (shapeHoleColor != 1122867) {
                                    this.mRenderPaint.setStyle(Paint.Style.FILL);
                                    this.mRenderPaint.setColor(shapeHoleColor);
                                    c.drawCircle(buffer.buffer[i], buffer.buffer[i + 1], shapeHoleSizeHalf, this.mRenderPaint);
                                }
                            }
                            else {
                                this.mRenderPaint.setStyle(Paint.Style.FILL);
                                c.drawCircle(buffer.buffer[i], buffer.buffer[i + 1], shapeHalf, this.mRenderPaint);
                            }
                        }
                    }
                }
                break;
            }
            case TRIANGLE: {
                this.mRenderPaint.setStyle(Paint.Style.FILL);
                final Path tri = new Path();
                for (int j = 0; j < buffer.size(); j += 2) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j])) {
                        if (this.mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])) {
                            this.mRenderPaint.setColor(dataSet.getColor(j / 2));
                            tri.moveTo(buffer.buffer[j], buffer.buffer[j + 1] - shapeHalf);
                            tri.lineTo(buffer.buffer[j] + shapeHalf, buffer.buffer[j + 1] + shapeHalf);
                            tri.lineTo(buffer.buffer[j] - shapeHalf, buffer.buffer[j + 1] + shapeHalf);
                            if (shapeHoleSize > 0.0) {
                                tri.lineTo(buffer.buffer[j], buffer.buffer[j + 1] - shapeHalf);
                                tri.moveTo(buffer.buffer[j] - shapeHalf + shapeStrokeSize, buffer.buffer[j + 1] + shapeHalf - shapeStrokeSize);
                                tri.lineTo(buffer.buffer[j] + shapeHalf - shapeStrokeSize, buffer.buffer[j + 1] + shapeHalf - shapeStrokeSize);
                                tri.lineTo(buffer.buffer[j], buffer.buffer[j + 1] - shapeHalf + shapeStrokeSize);
                                tri.lineTo(buffer.buffer[j] - shapeHalf + shapeStrokeSize, buffer.buffer[j + 1] + shapeHalf - shapeStrokeSize);
                            }
                            tri.close();
                            c.drawPath(tri, this.mRenderPaint);
                            tri.reset();
                            if (shapeHoleSize > 0.0 && shapeHoleColor != 1122867) {
                                this.mRenderPaint.setColor(shapeHoleColor);
                                tri.moveTo(buffer.buffer[j], buffer.buffer[j + 1] - shapeHalf + shapeStrokeSize);
                                tri.lineTo(buffer.buffer[j] + shapeHalf - shapeStrokeSize, buffer.buffer[j + 1] + shapeHalf - shapeStrokeSize);
                                tri.lineTo(buffer.buffer[j] - shapeHalf + shapeStrokeSize, buffer.buffer[j + 1] + shapeHalf - shapeStrokeSize);
                                tri.close();
                                c.drawPath(tri, this.mRenderPaint);
                                tri.reset();
                            }
                        }
                    }
                }
                break;
            }
            case CROSS: {
                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                this.mRenderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
                for (int j = 0; j < buffer.size(); j += 2) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j])) {
                        if (this.mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])) {
                            this.mRenderPaint.setColor(dataSet.getColor(j / 2));
                            c.drawLine(buffer.buffer[j] - shapeHalf, buffer.buffer[j + 1], buffer.buffer[j] + shapeHalf, buffer.buffer[j + 1], this.mRenderPaint);
                            c.drawLine(buffer.buffer[j], buffer.buffer[j + 1] - shapeHalf, buffer.buffer[j], buffer.buffer[j + 1] + shapeHalf, this.mRenderPaint);
                        }
                    }
                }
                break;
            }
            case X: {
                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                this.mRenderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
                for (int j = 0; j < buffer.size(); j += 2) {
                    if (!this.mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[j])) {
                        if (this.mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])) {
                            this.mRenderPaint.setColor(dataSet.getColor(j / 2));
                            c.drawLine(buffer.buffer[j] - shapeHalf, buffer.buffer[j + 1] - shapeHalf, buffer.buffer[j] + shapeHalf, buffer.buffer[j + 1] + shapeHalf, this.mRenderPaint);
                            c.drawLine(buffer.buffer[j] + shapeHalf, buffer.buffer[j + 1] - shapeHalf, buffer.buffer[j] - shapeHalf, buffer.buffer[j + 1] + shapeHalf, this.mRenderPaint);
                        }
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public void drawValues(final Canvas c) {
        if (this.mChart.getScatterData().getYValCount() < this.mChart.getMaxVisibleCount() * this.mViewPortHandler.getScaleX()) {
            final List<IScatterDataSet> dataSets = this.mChart.getScatterData().getDataSets();
            for (int i = 0; i < this.mChart.getScatterData().getDataSetCount(); ++i) {
                final IScatterDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled()) {
                    if (dataSet.getEntryCount() != 0) {
                        this.applyValueTextStyle(dataSet);
                        final float[] positions = this.mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesScatter(dataSet, this.mAnimator.getPhaseY());
                        final float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());
                        for (int j = 0; j < positions.length * this.mAnimator.getPhaseX(); j += 2) {
                            if (!this.mViewPortHandler.isInBoundsRight(positions[j])) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsLeft(positions[j])) {
                                if (this.mViewPortHandler.isInBoundsY(positions[j + 1])) {
                                    final Entry entry = dataSet.getEntryForIndex(j / 2);
                                    this.drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, positions[j], positions[j + 1] - shapeSize, dataSet.getValueTextColor(j / 2));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void drawExtras(final Canvas c) {
    }
    
    @Override
    public void drawHighlighted(final Canvas c, final Highlight[] indices) {
        for (int i = 0; i < indices.length; ++i) {
            final IScatterDataSet set = this.mChart.getScatterData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null) {
                if (set.isHighlightEnabled()) {
                    final int xIndex = indices[i].getXIndex();
                    if (xIndex <= this.mChart.getXChartMax() * this.mAnimator.getPhaseX()) {
                        final float yVal = set.getYValForXIndex(xIndex);
                        if (yVal != Float.NaN) {
                            final float y = yVal * this.mAnimator.getPhaseY();
                            final float[] pts = { xIndex, y };
                            this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                            this.drawHighlightLines(c, pts, set);
                        }
                    }
                }
            }
        }
    }
}
