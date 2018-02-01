// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import android.graphics.PathEffect;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;
import android.graphics.Path;

public abstract class LineScatterCandleRadarRenderer extends DataRenderer
{
    private Path mHighlightLinePath;
    
    public LineScatterCandleRadarRenderer(final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mHighlightLinePath = new Path();
    }
    
    protected void drawHighlightLines(final Canvas c, final float[] pts, final ILineScatterCandleRadarDataSet set) {
        this.mHighlightPaint.setColor(set.getHighLightColor());
        this.mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
        this.mHighlightPaint.setPathEffect((PathEffect)set.getDashPathEffectHighlight());
        if (set.isVerticalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(pts[0], this.mViewPortHandler.contentTop());
            this.mHighlightLinePath.lineTo(pts[0], this.mViewPortHandler.contentBottom());
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(this.mViewPortHandler.contentLeft(), pts[1]);
            this.mHighlightLinePath.lineTo(this.mViewPortHandler.contentRight(), pts[1]);
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
    }
}
