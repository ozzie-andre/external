// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.utils.Transformer;

public abstract class AxisRenderer extends Renderer
{
    protected Transformer mTrans;
    protected Paint mGridPaint;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;
    protected Paint mLimitLinePaint;
    
    public AxisRenderer(final ViewPortHandler viewPortHandler, final Transformer trans) {
        super(viewPortHandler);
        this.mTrans = trans;
        this.mAxisLabelPaint = new Paint(1);
        (this.mGridPaint = new Paint()).setColor(-7829368);
        this.mGridPaint.setStrokeWidth(1.0f);
        this.mGridPaint.setStyle(Paint.Style.STROKE);
        this.mGridPaint.setAlpha(90);
        (this.mAxisLinePaint = new Paint()).setColor(-16777216);
        this.mAxisLinePaint.setStrokeWidth(1.0f);
        this.mAxisLinePaint.setStyle(Paint.Style.STROKE);
        (this.mLimitLinePaint = new Paint(1)).setStyle(Paint.Style.STROKE);
    }
    
    public Paint getPaintAxisLabels() {
        return this.mAxisLabelPaint;
    }
    
    public Paint getPaintGrid() {
        return this.mGridPaint;
    }
    
    public Paint getPaintAxisLine() {
        return this.mAxisLinePaint;
    }
    
    public Transformer getTransformer() {
        return this.mTrans;
    }
    
    public abstract void renderAxisLabels(final Canvas p0);
    
    public abstract void renderGridLines(final Canvas p0);
    
    public abstract void renderAxisLine(final Canvas p0);
    
    public abstract void renderLimitLines(final Canvas p0);
}
