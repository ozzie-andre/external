// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.charts;

import android.graphics.Paint;
import android.graphics.Typeface;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.components.XAxis;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import android.graphics.PointF;
import android.graphics.Canvas;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;
import com.github.mikephil.charting.data.PieData;

public class PieChart extends PieRadarChartBase<PieData>
{
    private RectF mCircleBox;
    private boolean mDrawXLabels;
    private float[] mDrawAngles;
    private float[] mAbsoluteAngles;
    private boolean mDrawHole;
    private boolean mDrawSlicesUnderHole;
    private boolean mUsePercentValues;
    private boolean mDrawRoundedSlices;
    private CharSequence mCenterText;
    private float mHoleRadiusPercent;
    protected float mTransparentCircleRadiusPercent;
    private boolean mDrawCenterText;
    private float mCenterTextRadiusPercent;
    protected float mMaxAngle;
    
    public PieChart(final Context context) {
        super(context);
        this.mCircleBox = new RectF();
        this.mDrawXLabels = true;
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    public PieChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mCircleBox = new RectF();
        this.mDrawXLabels = true;
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    public PieChart(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.mCircleBox = new RectF();
        this.mDrawXLabels = true;
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }
    
    @Override
    protected void init() {
        super.init();
        this.mRenderer = new PieChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis = null;
    }
    
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData == null) {
            return;
        }
        this.mRenderer.drawData(canvas);
        if (this.valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        this.mRenderer.drawExtras(canvas);
        this.mRenderer.drawValues(canvas);
        this.mLegendRenderer.renderLegend(canvas);
        this.drawDescription(canvas);
        this.drawMarkers(canvas);
    }
    
    @Override
    public void calculateOffsets() {
        super.calculateOffsets();
        if (this.mData == null) {
            return;
        }
        final float diameter = this.getDiameter();
        final float radius = diameter / 2.0f;
        final PointF c = this.getCenterOffsets();
        final float shift = ((PieData)this.mData).getDataSet().getSelectionShift();
        this.mCircleBox.set(c.x - radius + shift, c.y - radius + shift, c.x + radius - shift, c.y + radius - shift);
    }
    
    @Override
    protected void calcMinMax() {
        this.calcAngles();
    }
    
    @Override
    protected float[] getMarkerPosition(final Entry e, final Highlight highlight) {
        final PointF center = this.getCenterCircleBox();
        float r = this.getRadius();
        float off = r / 10.0f * 3.6f;
        if (this.isDrawHoleEnabled()) {
            off = (r - r / 100.0f * this.getHoleRadius()) / 2.0f;
        }
        r -= off;
        final float rotationAngle = this.getRotationAngle();
        final int i = e.getXIndex();
        final float offset = this.mDrawAngles[i] / 2.0f;
        final float x = (float)(r * Math.cos(Math.toRadians((rotationAngle + this.mAbsoluteAngles[i] - offset) * this.mAnimator.getPhaseY())) + center.x);
        final float y = (float)(r * Math.sin(Math.toRadians((rotationAngle + this.mAbsoluteAngles[i] - offset) * this.mAnimator.getPhaseY())) + center.y);
        return new float[] { x, y };
    }
    
    private void calcAngles() {
        this.mDrawAngles = new float[((PieData)this.mData).getYValCount()];
        this.mAbsoluteAngles = new float[((PieData)this.mData).getYValCount()];
        final float yValueSum = ((PieData)this.mData).getYValueSum();
        final List<IPieDataSet> dataSets = ((PieData)this.mData).getDataSets();
        int cnt = 0;
        for (int i = 0; i < ((PieData)this.mData).getDataSetCount(); ++i) {
            final IPieDataSet set = dataSets.get(i);
            for (int j = 0; j < set.getEntryCount(); ++j) {
                this.mDrawAngles[cnt] = this.calcAngle(Math.abs(set.getEntryForIndex(j).getVal()), yValueSum);
                if (cnt == 0) {
                    this.mAbsoluteAngles[cnt] = this.mDrawAngles[cnt];
                }
                else {
                    this.mAbsoluteAngles[cnt] = this.mAbsoluteAngles[cnt - 1] + this.mDrawAngles[cnt];
                }
                ++cnt;
            }
        }
    }
    
    public boolean needsHighlight(final int xIndex, final int dataSetIndex) {
        if (!this.valuesToHighlight() || dataSetIndex < 0) {
            return false;
        }
        for (int i = 0; i < this.mIndicesToHighlight.length; ++i) {
            if (this.mIndicesToHighlight[i].getXIndex() == xIndex && this.mIndicesToHighlight[i].getDataSetIndex() == dataSetIndex) {
                return true;
            }
        }
        return false;
    }
    
    private float calcAngle(final float value) {
        return this.calcAngle(value, ((PieData)this.mData).getYValueSum());
    }
    
    private float calcAngle(final float value, final float yValueSum) {
        return value / yValueSum * this.mMaxAngle;
    }
    
    @Deprecated
    @Override
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }
    
    @Override
    public int getIndexForAngle(final float angle) {
        final float a = Utils.getNormalizedAngle(angle - this.getRotationAngle());
        for (int i = 0; i < this.mAbsoluteAngles.length; ++i) {
            if (this.mAbsoluteAngles[i] > a) {
                return i;
            }
        }
        return -1;
    }
    
    public int getDataSetIndexForIndex(final int xIndex) {
        final List<IPieDataSet> dataSets = ((PieData)this.mData).getDataSets();
        for (int i = 0; i < dataSets.size(); ++i) {
            if (dataSets.get(i).getEntryForXIndex(xIndex) != null) {
                return i;
            }
        }
        return -1;
    }
    
    public float[] getDrawAngles() {
        return this.mDrawAngles;
    }
    
    public float[] getAbsoluteAngles() {
        return this.mAbsoluteAngles;
    }
    
    public void setHoleColor(final int color) {
        ((PieChartRenderer)this.mRenderer).getPaintHole().setColor(color);
    }
    
    public void setDrawSlicesUnderHole(final boolean enable) {
        this.mDrawSlicesUnderHole = enable;
    }
    
    public boolean isDrawSlicesUnderHoleEnabled() {
        return this.mDrawSlicesUnderHole;
    }
    
    public void setDrawHoleEnabled(final boolean enabled) {
        this.mDrawHole = enabled;
    }
    
    public boolean isDrawHoleEnabled() {
        return this.mDrawHole;
    }
    
    public void setCenterText(final CharSequence text) {
        if (text == null) {
            this.mCenterText = "";
        }
        else {
            this.mCenterText = text;
        }
    }
    
    public CharSequence getCenterText() {
        return this.mCenterText;
    }
    
    public void setDrawCenterText(final boolean enabled) {
        this.mDrawCenterText = enabled;
    }
    
    public boolean isDrawCenterTextEnabled() {
        return this.mDrawCenterText;
    }
    
    @Override
    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 2.0f;
    }
    
    @Override
    protected float getRequiredBaseOffset() {
        return 0.0f;
    }
    
    @Override
    public float getRadius() {
        if (this.mCircleBox == null) {
            return 0.0f;
        }
        return Math.min(this.mCircleBox.width() / 2.0f, this.mCircleBox.height() / 2.0f);
    }
    
    public RectF getCircleBox() {
        return this.mCircleBox;
    }
    
    public PointF getCenterCircleBox() {
        return new PointF(this.mCircleBox.centerX(), this.mCircleBox.centerY());
    }
    
    public void setCenterTextTypeface(final Typeface t) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTypeface(t);
    }
    
    public void setCenterTextSize(final float sizeDp) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(Utils.convertDpToPixel(sizeDp));
    }
    
    public void setCenterTextSizePixels(final float sizePixels) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(sizePixels);
    }
    
    public void setCenterTextColor(final int color) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setColor(color);
    }
    
    public void setHoleRadius(final float percent) {
        this.mHoleRadiusPercent = percent;
    }
    
    public float getHoleRadius() {
        return this.mHoleRadiusPercent;
    }
    
    public void setTransparentCircleColor(final int color) {
        final Paint p = ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle();
        final int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }
    
    public void setTransparentCircleRadius(final float percent) {
        this.mTransparentCircleRadiusPercent = percent;
    }
    
    public float getTransparentCircleRadius() {
        return this.mTransparentCircleRadiusPercent;
    }
    
    public void setTransparentCircleAlpha(final int alpha) {
        ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }
    
    public void setDrawSliceText(final boolean enabled) {
        this.mDrawXLabels = enabled;
    }
    
    public boolean isDrawSliceTextEnabled() {
        return this.mDrawXLabels;
    }
    
    public boolean isDrawRoundedSlicesEnabled() {
        return this.mDrawRoundedSlices;
    }
    
    public void setUsePercentValues(final boolean enabled) {
        this.mUsePercentValues = enabled;
    }
    
    public boolean isUsePercentValuesEnabled() {
        return this.mUsePercentValues;
    }
    
    public void setCenterTextRadiusPercent(final float percent) {
        this.mCenterTextRadiusPercent = percent;
    }
    
    public float getCenterTextRadiusPercent() {
        return this.mCenterTextRadiusPercent;
    }
    
    public float getMaxAngle() {
        return this.mMaxAngle;
    }
    
    public void setMaxAngle(float maxangle) {
        if (maxangle > 360.0f) {
            maxangle = 360.0f;
        }
        if (maxangle < 90.0f) {
            maxangle = 90.0f;
        }
        this.mMaxAngle = maxangle;
    }
    
    @Override
    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && this.mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer)this.mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
