// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

import android.view.View;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Matrix;

public class ViewPortHandler
{
    protected final Matrix mMatrixTouch;
    protected RectF mContentRect;
    protected float mChartWidth;
    protected float mChartHeight;
    private float mMinScaleY;
    private float mMaxScaleY;
    private float mMinScaleX;
    private float mMaxScaleX;
    private float mScaleX;
    private float mScaleY;
    private float mTransX;
    private float mTransY;
    private float mTransOffsetX;
    private float mTransOffsetY;
    protected final float[] matrixBuffer;
    
    public ViewPortHandler() {
        this.mMatrixTouch = new Matrix();
        this.mContentRect = new RectF();
        this.mChartWidth = 0.0f;
        this.mChartHeight = 0.0f;
        this.mMinScaleY = 1.0f;
        this.mMaxScaleY = Float.MAX_VALUE;
        this.mMinScaleX = 1.0f;
        this.mMaxScaleX = Float.MAX_VALUE;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mTransX = 0.0f;
        this.mTransY = 0.0f;
        this.mTransOffsetX = 0.0f;
        this.mTransOffsetY = 0.0f;
        this.matrixBuffer = new float[9];
    }
    
    public void setChartDimens(final float width, final float height) {
        final float offsetLeft = this.offsetLeft();
        final float offsetTop = this.offsetTop();
        final float offsetRight = this.offsetRight();
        final float offsetBottom = this.offsetBottom();
        this.mChartHeight = height;
        this.mChartWidth = width;
        this.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }
    
    public boolean hasChartDimens() {
        return this.mChartHeight > 0.0f && this.mChartWidth > 0.0f;
    }
    
    public void restrainViewPort(final float offsetLeft, final float offsetTop, final float offsetRight, final float offsetBottom) {
        this.mContentRect.set(offsetLeft, offsetTop, this.mChartWidth - offsetRight, this.mChartHeight - offsetBottom);
    }
    
    public float offsetLeft() {
        return this.mContentRect.left;
    }
    
    public float offsetRight() {
        return this.mChartWidth - this.mContentRect.right;
    }
    
    public float offsetTop() {
        return this.mContentRect.top;
    }
    
    public float offsetBottom() {
        return this.mChartHeight - this.mContentRect.bottom;
    }
    
    public float contentTop() {
        return this.mContentRect.top;
    }
    
    public float contentLeft() {
        return this.mContentRect.left;
    }
    
    public float contentRight() {
        return this.mContentRect.right;
    }
    
    public float contentBottom() {
        return this.mContentRect.bottom;
    }
    
    public float contentWidth() {
        return this.mContentRect.width();
    }
    
    public float contentHeight() {
        return this.mContentRect.height();
    }
    
    public RectF getContentRect() {
        return this.mContentRect;
    }
    
    public PointF getContentCenter() {
        return new PointF(this.mContentRect.centerX(), this.mContentRect.centerY());
    }
    
    public float getChartHeight() {
        return this.mChartHeight;
    }
    
    public float getChartWidth() {
        return this.mChartWidth;
    }
    
    public Matrix zoomIn(final float x, final float y) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(1.4f, 1.4f, x, y);
        return save;
    }
    
    public Matrix zoomOut(final float x, final float y) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(0.7f, 0.7f, x, y);
        return save;
    }
    
    public Matrix zoom(final float scaleX, final float scaleY) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(scaleX, scaleY);
        return save;
    }
    
    public Matrix zoom(final float scaleX, final float scaleY, final float x, final float y) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.postScale(scaleX, scaleY, x, y);
        return save;
    }
    
    public Matrix setZoom(final float scaleX, final float scaleY) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.setScale(scaleX, scaleY);
        return save;
    }
    
    public Matrix setZoom(final float scaleX, final float scaleY, final float x, final float y) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        save.setScale(scaleX, scaleY, x, y);
        return save;
    }
    
    public Matrix fitScreen() {
        this.mMinScaleX = 1.0f;
        this.mMinScaleY = 1.0f;
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        final float[] vals = new float[9];
        save.getValues(vals);
        vals[5] = (vals[2] = 0.0f);
        vals[4] = (vals[0] = 1.0f);
        save.setValues(vals);
        return save;
    }
    
    public Matrix translate(final float[] transformedPts) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        final float x = transformedPts[0] - this.offsetLeft();
        final float y = transformedPts[1] - this.offsetTop();
        save.postTranslate(-x, -y);
        return save;
    }
    
    public void centerViewPort(final float[] transformedPts, final View view) {
        final Matrix save = new Matrix();
        save.set(this.mMatrixTouch);
        final float x = transformedPts[0] - this.offsetLeft();
        final float y = transformedPts[1] - this.offsetTop();
        save.postTranslate(-x, -y);
        this.refresh(save, view, true);
    }
    
    public Matrix refresh(final Matrix newMatrix, final View chart, final boolean invalidate) {
        this.mMatrixTouch.set(newMatrix);
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
        if (invalidate) {
            chart.invalidate();
        }
        newMatrix.set(this.mMatrixTouch);
        return newMatrix;
    }
    
    public void limitTransAndScale(final Matrix matrix, final RectF content) {
        matrix.getValues(this.matrixBuffer);
        final float curTransX = this.matrixBuffer[2];
        final float curScaleX = this.matrixBuffer[0];
        final float curTransY = this.matrixBuffer[5];
        final float curScaleY = this.matrixBuffer[4];
        this.mScaleX = Math.min(Math.max(this.mMinScaleX, curScaleX), this.mMaxScaleX);
        this.mScaleY = Math.min(Math.max(this.mMinScaleY, curScaleY), this.mMaxScaleY);
        float width = 0.0f;
        float height = 0.0f;
        if (content != null) {
            width = content.width();
            height = content.height();
        }
        final float maxTransX = -width * (this.mScaleX - 1.0f);
        this.mTransX = Math.min(Math.max(curTransX, maxTransX - this.mTransOffsetX), this.mTransOffsetX);
        final float maxTransY = height * (this.mScaleY - 1.0f);
        this.mTransY = Math.max(Math.min(curTransY, maxTransY + this.mTransOffsetY), -this.mTransOffsetY);
        this.matrixBuffer[2] = this.mTransX;
        this.matrixBuffer[0] = this.mScaleX;
        this.matrixBuffer[5] = this.mTransY;
        this.matrixBuffer[4] = this.mScaleY;
        matrix.setValues(this.matrixBuffer);
    }
    
    public void setMinimumScaleX(float xScale) {
        if (xScale < 1.0f) {
            xScale = 1.0f;
        }
        this.mMinScaleX = xScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMaximumScaleX(final float xScale) {
        this.mMaxScaleX = xScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinMaxScaleX(float minScaleX, final float maxScaleX) {
        if (minScaleX < 1.0f) {
            minScaleX = 1.0f;
        }
        this.mMinScaleX = minScaleX;
        this.mMaxScaleX = maxScaleX;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMinimumScaleY(float yScale) {
        if (yScale < 1.0f) {
            yScale = 1.0f;
        }
        this.mMinScaleY = yScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public void setMaximumScaleY(final float yScale) {
        this.mMaxScaleY = yScale;
        this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
    }
    
    public Matrix getMatrixTouch() {
        return this.mMatrixTouch;
    }
    
    public boolean isInBoundsX(final float x) {
        return this.isInBoundsLeft(x) && this.isInBoundsRight(x);
    }
    
    public boolean isInBoundsY(final float y) {
        return this.isInBoundsTop(y) && this.isInBoundsBottom(y);
    }
    
    public boolean isInBounds(final float x, final float y) {
        return this.isInBoundsX(x) && this.isInBoundsY(y);
    }
    
    public boolean isInBoundsLeft(final float x) {
        return this.mContentRect.left <= x;
    }
    
    public boolean isInBoundsRight(float x) {
        x = (int)(x * 100.0f) / 100.0f;
        return this.mContentRect.right >= x;
    }
    
    public boolean isInBoundsTop(final float y) {
        return this.mContentRect.top <= y;
    }
    
    public boolean isInBoundsBottom(float y) {
        y = (int)(y * 100.0f) / 100.0f;
        return this.mContentRect.bottom >= y;
    }
    
    public float getScaleX() {
        return this.mScaleX;
    }
    
    public float getScaleY() {
        return this.mScaleY;
    }
    
    public float getMinScaleX() {
        return this.mMinScaleX;
    }
    
    public float getMaxScaleX() {
        return this.mMaxScaleX;
    }
    
    public float getMinScaleY() {
        return this.mMinScaleY;
    }
    
    public float getMaxScaleY() {
        return this.mMaxScaleY;
    }
    
    public float getTransX() {
        return this.mTransX;
    }
    
    public float getTransY() {
        return this.mTransY;
    }
    
    public boolean isFullyZoomedOut() {
        return this.isFullyZoomedOutX() && this.isFullyZoomedOutY();
    }
    
    public boolean isFullyZoomedOutY() {
        return this.mScaleY <= this.mMinScaleY && this.mMinScaleY <= 1.0f;
    }
    
    public boolean isFullyZoomedOutX() {
        return this.mScaleX <= this.mMinScaleX && this.mMinScaleX <= 1.0f;
    }
    
    public void setDragOffsetX(final float offset) {
        this.mTransOffsetX = Utils.convertDpToPixel(offset);
    }
    
    public void setDragOffsetY(final float offset) {
        this.mTransOffsetY = Utils.convertDpToPixel(offset);
    }
    
    public boolean hasNoDragOffset() {
        return this.mTransOffsetX <= 0.0f && this.mTransOffsetY <= 0.0f;
    }
    
    public boolean canZoomOutMoreX() {
        return this.mScaleX > this.mMinScaleX;
    }
    
    public boolean canZoomInMoreX() {
        return this.mScaleX < this.mMaxScaleX;
    }
    
    public boolean canZoomOutMoreY() {
        return this.mScaleY > this.mMinScaleY;
    }
    
    public boolean canZoomInMoreY() {
        return this.mScaleY < this.mMaxScaleY;
    }
}
