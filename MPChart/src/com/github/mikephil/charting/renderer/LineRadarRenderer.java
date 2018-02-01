// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.renderer;

import android.graphics.Paint;
import com.github.mikephil.charting.utils.Utils;
import android.graphics.drawable.Drawable;
import android.graphics.Path;
import android.graphics.Canvas;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.animation.ChartAnimator;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer
{
    public LineRadarRenderer(final ChartAnimator animator, final ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }
    
    protected void drawFilledPath(final Canvas c, final Path filledPath, final Drawable drawable) {
        if (this.clipPathSupported()) {
            c.save();
            c.clipPath(filledPath);
            drawable.setBounds((int)this.mViewPortHandler.contentLeft(), (int)this.mViewPortHandler.contentTop(), (int)this.mViewPortHandler.contentRight(), (int)this.mViewPortHandler.contentBottom());
            drawable.draw(c);
            c.restore();
            return;
        }
        throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, this code was run on API level " + Utils.getSDKInt() + ".");
    }
    
    protected void drawFilledPath(final Canvas c, final Path filledPath, final int fillColor, final int fillAlpha) {
        final int color = fillAlpha << 24 | (fillColor & 0xFFFFFF);
        if (this.clipPathSupported()) {
            c.save();
            c.clipPath(filledPath);
            c.drawColor(color);
            c.restore();
        }
        else {
            final Paint.Style previous = this.mRenderPaint.getStyle();
            final int previousColor = this.mRenderPaint.getColor();
            this.mRenderPaint.setStyle(Paint.Style.FILL);
            this.mRenderPaint.setColor(color);
            c.drawPath(filledPath, this.mRenderPaint);
            this.mRenderPaint.setColor(previousColor);
            this.mRenderPaint.setStyle(previous);
        }
    }
    
    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
}
