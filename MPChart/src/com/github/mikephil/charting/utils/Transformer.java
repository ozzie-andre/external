// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

//import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import android.graphics.RectF;
import java.util.List;
import android.graphics.Path;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import android.graphics.Matrix;

public class Transformer
{
    protected Matrix mMatrixValueToPx;
    protected Matrix mMatrixOffset;
    protected ViewPortHandler mViewPortHandler;
    private Matrix mMBuffer1;
    private Matrix mMBuffer2;
    
    public Transformer(final ViewPortHandler viewPortHandler) {
        this.mMatrixValueToPx = new Matrix();
        this.mMatrixOffset = new Matrix();
        this.mMBuffer1 = new Matrix();
        this.mMBuffer2 = new Matrix();
        this.mViewPortHandler = viewPortHandler;
    }
    
    public void prepareMatrixValuePx(final float xChartMin, final float deltaX, final float deltaY, final float yChartMin) {
        float scaleX = this.mViewPortHandler.contentWidth() / deltaX;
        float scaleY = this.mViewPortHandler.contentHeight() / deltaY;
        if (Float.isInfinite(scaleX)) {
            scaleX = 0.0f;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0.0f;
        }
        this.mMatrixValueToPx.reset();
        this.mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        this.mMatrixValueToPx.postScale(scaleX, -scaleY);
    }
    
    public void prepareMatrixOffset(final boolean inverted) {
        this.mMatrixOffset.reset();
        if (!inverted) {
            this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
        }
        else {
            this.mMatrixOffset.setTranslate(this.mViewPortHandler.offsetLeft(), -this.mViewPortHandler.offsetTop());
            this.mMatrixOffset.postScale(1.0f, -1.0f);
        }
    }
    
    public float[] generateTransformedValuesScatter(final IScatterDataSet data, final float phaseY) {
        final float[] valuePoints = new float[data.getEntryCount() * 2];
        for (int j = 0; j < valuePoints.length; j += 2) {
            final Entry e = data.getEntryForIndex(j / 2);
            if (e != null) {
                valuePoints[j] = e.getXIndex();
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public float[] generateTransformedValuesBubble(final IBubbleDataSet data, final float phaseX, final float phaseY, final int from, final int to) {
        final int count = (int)Math.ceil(to - from) * 2;
        final float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            final Entry e = data.getEntryForIndex(j / 2 + from);
            if (e != null) {
                valuePoints[j] = (e.getXIndex() - from) * phaseX + from;
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public float[] generateTransformedValuesLine(final ILineDataSet data, final float phaseX, final float phaseY, final int from, final int to) {
        final int count = (int)Math.ceil((to - from) * phaseX) * 2;
        final float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            final Entry e = data.getEntryForIndex(j / 2 + from);
            if (e != null) {
                valuePoints[j] = e.getXIndex();
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public float[] generateTransformedValuesCandle(final ICandleDataSet data, final float phaseX, final float phaseY, final int from, final int to) {
        final int count = (int)Math.ceil((to - from) * phaseX) * 2;
        final float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            final CandleEntry e = data.getEntryForIndex(j / 2 + from);
            if (e != null) {
                valuePoints[j] = e.getXIndex();
                valuePoints[j + 1] = e.getHigh() * phaseY;
            }
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public float[] generateTransformedValuesBarChart(final IBarDataSet data, final int dataSetIndex, final BarData bd, final float phaseY) {
        final float[] valuePoints = new float[data.getEntryCount() * 2];
        final int setCount = bd.getDataSetCount();
        final float space = bd.getGroupSpace();
        for (int j = 0; j < valuePoints.length; j += 2) {
            //final Entry e = ((IDataSet<Entry>)data).getEntryForIndex(j / 2);
        	final Entry e = data.getEntryForIndex(j / 2);
            final int i = e.getXIndex();
            final float x = e.getXIndex() + i * (setCount - 1) + dataSetIndex + space * i + space / 2.0f;
            final float y = e.getVal();
            valuePoints[j] = x;
            valuePoints[j + 1] = y * phaseY;
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public float[] generateTransformedValuesHorizontalBarChart(final IBarDataSet data, final int dataSet, final BarData bd, final float phaseY) {
        final float[] valuePoints = new float[data.getEntryCount() * 2];
        final int setCount = bd.getDataSetCount();
        final float space = bd.getGroupSpace();
        for (int j = 0; j < valuePoints.length; j += 2) {
            //final Entry e = ((IDataSet<Entry>)data).getEntryForIndex(j / 2);
        	final Entry e = data.getEntryForIndex(j / 2);
            final int i = e.getXIndex();
            final float x = i + i * (setCount - 1) + dataSet + space * i + space / 2.0f;
            final float y = e.getVal();
            valuePoints[j] = y * phaseY;
            valuePoints[j + 1] = x;
        }
        this.getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }
    
    public void pathValueToPixel(final Path path) {
        path.transform(this.mMatrixValueToPx);
        path.transform(this.mViewPortHandler.getMatrixTouch());
        path.transform(this.mMatrixOffset);
    }
    
    public void pathValuesToPixel(final List<Path> paths) {
        for (int i = 0; i < paths.size(); ++i) {
            this.pathValueToPixel(paths.get(i));
        }
    }
    
    public void pointValuesToPixel(final float[] pts) {
        this.mMatrixValueToPx.mapPoints(pts);
        this.mViewPortHandler.getMatrixTouch().mapPoints(pts);
        this.mMatrixOffset.mapPoints(pts);
    }
    
    public void rectValueToPixel(final RectF r) {
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }
    
    public void rectValueToPixel(final RectF r, final float phaseY) {
        r.top *= phaseY;
        r.bottom *= phaseY;
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }
    
    public void rectValueToPixelHorizontal(final RectF r) {
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }
    
    public void rectValueToPixelHorizontal(final RectF r, final float phaseY) {
        r.left *= phaseY;
        r.right *= phaseY;
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }
    
    public void rectValuesToPixel(final List<RectF> rects) {
        final Matrix m = this.getValueToPixelMatrix();
        for (int i = 0; i < rects.size(); ++i) {
            m.mapRect((RectF)rects.get(i));
        }
    }
    
    public void pixelsToValue(final float[] pixels) {
        final Matrix tmp = new Matrix();
        this.mMatrixOffset.invert(tmp);
        tmp.mapPoints(pixels);
        this.mViewPortHandler.getMatrixTouch().invert(tmp);
        tmp.mapPoints(pixels);
        this.mMatrixValueToPx.invert(tmp);
        tmp.mapPoints(pixels);
    }
    
    public PointD getValuesByTouchPoint(final float x, final float y) {
        final float[] pts = { x, y };
        this.pixelsToValue(pts);
        final double xTouchVal = pts[0];
        final double yTouchVal = pts[1];
        return new PointD(xTouchVal, yTouchVal);
    }
    
    public Matrix getValueMatrix() {
        return this.mMatrixValueToPx;
    }
    
    public Matrix getOffsetMatrix() {
        return this.mMatrixOffset;
    }
    
    public Matrix getValueToPixelMatrix() {
        this.mMBuffer1.set(this.mMatrixValueToPx);
        this.mMBuffer1.postConcat(this.mViewPortHandler.mMatrixTouch);
        this.mMBuffer1.postConcat(this.mMatrixOffset);
        return this.mMBuffer1;
    }
    
    public Matrix getPixelToValueMatrix() {
        this.getValueToPixelMatrix().invert(this.mMBuffer2);
        return this.mMBuffer2;
    }
}
