// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.graphics.Canvas;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.Color;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.graphics.Paint;
import com.github.mikephil.charting.animation.ChartAnimator;

public abstract class DataRenderer extends Renderer
{
    protected ChartAnimator mAnimator;
    protected Paint mRenderPaint;
    protected Paint mHighlightPaint;
    protected Paint mDrawPaint;
    protected Paint mValuePaint;
    
    public DataRenderer(final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = animator;
        (this.mRenderPaint = new Paint(1)).setStyle(Paint.Style.FILL);
        this.mDrawPaint = new Paint(4);
        (this.mValuePaint = new Paint(1)).setColor(Color.rgb(63, 63, 63));
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0f));
        (this.mHighlightPaint = new Paint(1)).setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }
    
    public Paint getPaintValues() {
        return this.mValuePaint;
    }
    
    public Paint getPaintHighlight() {
        return this.mHighlightPaint;
    }
    
    public Paint getPaintRender() {
        return this.mRenderPaint;
    }
    
    protected void applyValueTextStyle(final IDataSet set) {
        this.mValuePaint.setTypeface(set.getValueTypeface());
        this.mValuePaint.setTextSize(set.getValueTextSize());
    }
    
    public abstract void initBuffers();
    
    public abstract void drawData(final Canvas p0);
    
    public abstract void drawValues(final Canvas p0);
    
    public void drawValue(final Canvas c, final ValueFormatter formatter, final float value, final Entry entry, final int dataSetIndex, final float x, final float y, final int color) {
        this.mValuePaint.setColor(color);
        c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, this.mViewPortHandler), x, y, this.mValuePaint);
    }
    
    public abstract void drawExtras(final Canvas p0);
    
    public abstract void drawHighlighted(final Canvas p0, final Highlight[] p1);
}
