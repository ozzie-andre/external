// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.RelativeLayout;

public abstract class MarkerView extends RelativeLayout
{
    public MarkerView(final Context context, final int layoutResource) {
        super(context);
        this.setupLayoutResource(layoutResource);
    }
    
    private void setupLayoutResource(final int layoutResource) {
        final View inflated = LayoutInflater.from(this.getContext()).inflate(layoutResource, (ViewGroup)this);
        inflated.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-2, -2));
        inflated.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }
    
    public void draw(final Canvas canvas, float posx, float posy) {
        posx += this.getXOffset(posx);
        posy += this.getYOffset(posy);
        canvas.translate(posx, posy);
        this.draw(canvas);
        canvas.translate(-posx, -posy);
    }
    
    public abstract void refreshContent(final Entry p0, final Highlight p1);
    
    public abstract int getXOffset(final float p0);
    
    public abstract int getYOffset(final float p0);
}
