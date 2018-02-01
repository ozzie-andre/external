// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

import android.text.Layout;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.Canvas;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.VelocityTracker;
import android.view.MotionEvent;
import android.graphics.PointF;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.components.YAxis;
import java.util.List;
import android.content.res.Resources;
import android.util.Log;
import android.view.ViewConfiguration;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

@SuppressWarnings("deprecation")

public abstract class Utils
{
    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity;
    private static int mMaximumFlingVelocity;
    public static final double DEG2RAD = 0.017453292519943295;
    public static final float FDEG2RAD = 0.017453292f;
    private static final int[] POW_10;
    private static Rect mDrawTextRectBuffer;
    private static Paint.FontMetrics mFontMetricsBuffer;
    
    static {
        Utils.mMinimumFlingVelocity = 50;
        Utils.mMaximumFlingVelocity = 8000;
        POW_10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
        Utils.mDrawTextRectBuffer = new Rect();
        Utils.mFontMetricsBuffer = new Paint.FontMetrics();
    }
    
	public static void init(final Context context) {
        if (context == null) {
            Utils.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            Utils.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            Log.e("MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");
        }
        else {
            final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            Utils.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            Utils.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            final Resources res = context.getResources();
            Utils.mMetrics = res.getDisplayMetrics();
        }
    }
    
    @Deprecated
    public static void init(final Resources res) {
        Utils.mMetrics = res.getDisplayMetrics();
        Utils.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        Utils.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }
    
    public static float convertDpToPixel(final float dp) {
        if (Utils.mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
            return dp;
        }
        final DisplayMetrics metrics = Utils.mMetrics;
        final float px = dp * (metrics.densityDpi / 160.0f);
        return px;
    }
    
    public static float convertPixelsToDp(final float px) {
        if (Utils.mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
            return px;
        }
        final DisplayMetrics metrics = Utils.mMetrics;
        final float dp = px / (metrics.densityDpi / 160.0f);
        return dp;
    }
    
    public static int calcTextWidth(final Paint paint, final String demoText) {
        return (int)paint.measureText(demoText);
    }
    
    public static int calcTextHeight(final Paint paint, final String demoText) {
        final Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }
    
    public static float getLineHeight(final Paint paint) {
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        return metrics.descent - metrics.ascent;
    }
    
    public static float getLineSpacing(final Paint paint) {
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        return metrics.ascent - metrics.top + metrics.bottom;
    }
    
    public static FSize calcTextSize(final Paint paint, final String demoText) {
        final Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return new FSize(r.width(), r.height());
    }
    
    public static String formatNumber(final float number, final int digitCount, final boolean separateThousands) {
        return formatNumber(number, digitCount, separateThousands, '.');
    }
    
    public static String formatNumber(float number, int digitCount, final boolean separateThousands, final char separateChar) {
        final char[] out = new char[35];
        boolean neg = false;
        if (number == 0.0f) {
            return "0";
        }
        boolean zero = false;
        if (number < 1.0f && number > -1.0f) {
            zero = true;
        }
        if (number < 0.0f) {
            neg = true;
            number = -number;
        }
        if (digitCount > Utils.POW_10.length) {
            digitCount = Utils.POW_10.length - 1;
        }
        number *= Utils.POW_10[digitCount];
        long lval = Math.round(number);
        int ind = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;
        while (lval != 0L || charCount < digitCount + 1) {
            final int digit = (int)(lval % 10L);
            lval /= 10L;
            out[ind--] = (char)(digit + 48);
            if (++charCount == digitCount) {
                out[ind--] = ',';
                ++charCount;
                decimalPointAdded = true;
            }
            else {
                if (!separateThousands || lval == 0L || charCount <= digitCount) {
                    continue;
                }
                if (decimalPointAdded) {
                    if ((charCount - digitCount) % 4 != 0) {
                        continue;
                    }
                    out[ind--] = separateChar;
                    ++charCount;
                }
                else {
                    if ((charCount - digitCount) % 4 != 3) {
                        continue;
                    }
                    out[ind--] = separateChar;
                    ++charCount;
                }
            }
        }
        if (zero) {
            out[ind--] = '0';
            ++charCount;
        }
        if (neg) {
            out[ind--] = '-';
            ++charCount;
        }
        final int start = out.length - charCount;
        return String.valueOf(out, start, out.length - start);
    }
    
    public static float roundToNextSignificant(final double number) {
        final float d = (float)Math.ceil((float)Math.log10((number < 0.0) ? (-number) : number));
        final int pw = 1 - (int)d;
        final float magnitude = (float)Math.pow(10.0, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }
    
    public static int getDecimals(final float number) {
        final float i = roundToNextSignificant(number);
        return (int)Math.ceil(-Math.log10(i)) + 2;
    }
    
    public static int[] convertIntegers(final List<Integer> integers) {
        final int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = integers.get(i);
        }
        return ret;
    }
    
    public static String[] convertStrings(final List<String> strings) {
        final String[] ret = new String[strings.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = strings.get(i);
        }
        return ret;
    }
    
    public static double nextUp(double d) {
        if (d == Double.POSITIVE_INFINITY) {
            return d;
        }
        d += 0.0;
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d) + ((d >= 0.0) ? 1L : -1L));
    }
    
    public static int getClosestDataSetIndex(final List<SelectionDetail> valsAtIndex, final float val, final YAxis.AxisDependency axis) {
        int index = -2147483647;
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < valsAtIndex.size(); ++i) {
            final SelectionDetail sel = valsAtIndex.get(i);
            if (axis == null || sel.dataSet.getAxisDependency() == axis) {
                final float cdistance = Math.abs(sel.val - val);
                if (cdistance < distance) {
                    index = valsAtIndex.get(i).dataSetIndex;
                    distance = cdistance;
                }
            }
        }
        return index;
    }
    
    public static float getMinimumDistance(final List<SelectionDetail> valsAtIndex, final float val, final YAxis.AxisDependency axis) {
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < valsAtIndex.size(); ++i) {
            final SelectionDetail sel = valsAtIndex.get(i);
            if (sel.dataSet.getAxisDependency() == axis) {
                final float cdistance = Math.abs(sel.val - val);
                if (cdistance < distance) {
                    distance = cdistance;
                }
            }
        }
        return distance;
    }
    
    public static boolean needsDefaultFormatter(final ValueFormatter formatter) {
        return formatter == null || formatter instanceof DefaultValueFormatter;
    }
    
    public static PointF getPosition(final PointF center, final float dist, final float angle) {
        final PointF p = new PointF((float)(center.x + dist * Math.cos(Math.toRadians(angle))), (float)(center.y + dist * Math.sin(Math.toRadians(angle))));
        return p;
    }
    
    public static void velocityTrackerPointerUpCleanUpIfNecessary(final MotionEvent ev, final VelocityTracker tracker) {
        tracker.computeCurrentVelocity(1000, (float)Utils.mMaximumFlingVelocity);
        final int upIndex = ev.getActionIndex();
        final int id1 = ev.getPointerId(upIndex);
        final float x1 = tracker.getXVelocity(id1);
        final float y1 = tracker.getYVelocity(id1);
        for (int i = 0, count = ev.getPointerCount(); i < count; ++i) {
            if (i != upIndex) {
                final int id2 = ev.getPointerId(i);
                final float x2 = x1 * tracker.getXVelocity(id2);
                final float y2 = y1 * tracker.getYVelocity(id2);
                final float dot = x2 + y2;
                if (dot < 0.0f) {
                    tracker.clear();
                    break;
                }
            }
        }
    }
    
    @SuppressLint({ "NewApi" })
    public static void postInvalidateOnAnimation(final View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        }
        else {
            view.postInvalidateDelayed(10L);
        }
    }
    
    public static int getMinimumFlingVelocity() {
        return Utils.mMinimumFlingVelocity;
    }
    
    public static int getMaximumFlingVelocity() {
        return Utils.mMaximumFlingVelocity;
    }
    
    public static float getNormalizedAngle(float angle) {
        while (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle % 360.0f;
    }
    
    public static void drawText(final Canvas c, final String text, final float x, final float y, final Paint paint, final PointF anchor, final float angleDegrees) {
        float drawOffsetX = 0.0f;
        float drawOffsetY = 0.0f;
        paint.getTextBounds(text, 0, text.length(), Utils.mDrawTextRectBuffer);
        final float lineHeight = Utils.mDrawTextRectBuffer.height();
        drawOffsetX -= Utils.mDrawTextRectBuffer.left;
        drawOffsetY += lineHeight;
        final Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0f) {
            drawOffsetX -= Utils.mDrawTextRectBuffer.width() * 0.5f;
            drawOffsetY -= lineHeight * 0.5f;
            float translateX = x;
            float translateY = y;
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                final FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(Utils.mDrawTextRectBuffer.width(), lineHeight, angleDegrees);
                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
            }
            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
            c.drawText(text, drawOffsetX, drawOffsetY, paint);
            c.restore();
        }
        else {
            if (anchor.x != 0.0f || anchor.y != 0.0f) {
                drawOffsetX -= Utils.mDrawTextRectBuffer.width() * anchor.x;
                drawOffsetY -= lineHeight * anchor.y;
            }
            drawOffsetX += x;
            drawOffsetY += y;
            c.drawText(text, drawOffsetX, drawOffsetY, paint);
        }
        paint.setTextAlign(originalTextAlign);
    }
    
    public static void drawMultilineText(final Canvas c, final StaticLayout textLayout, final float x, final float y, final TextPaint paint, final PointF anchor, final float angleDegrees) {
        float drawOffsetX = 0.0f;
        float drawOffsetY = 0.0f;
        final float lineHeight = paint.getFontMetrics(Utils.mFontMetricsBuffer);
        final float drawWidth = textLayout.getWidth();
        final float drawHeight = textLayout.getLineCount() * lineHeight;
        drawOffsetX -= Utils.mDrawTextRectBuffer.left;
        drawOffsetY += drawHeight;
        final Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0f) {
            drawOffsetX -= drawWidth * 0.5f;
            drawOffsetY -= drawHeight * 0.5f;
            float translateX = x;
            float translateY = y;
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                final FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(drawWidth, drawHeight, angleDegrees);
                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
            }
            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
            c.translate(drawOffsetX, drawOffsetY);
            textLayout.draw(c);
            c.restore();
        }
        else {
            if (anchor.x != 0.0f || anchor.y != 0.0f) {
                drawOffsetX -= drawWidth * anchor.x;
                drawOffsetY -= drawHeight * anchor.y;
            }
            drawOffsetX += x;
            drawOffsetY += y;
            c.save();
            c.translate(drawOffsetX, drawOffsetY);
            textLayout.draw(c);
            c.restore();
        }
        paint.setTextAlign(originalTextAlign);
    }
    
    public static void drawMultilineText(final Canvas c, final String text, final float x, final float y, final TextPaint paint, final FSize constrainedToSize, final PointF anchor, final float angleDegrees) {
        final StaticLayout textLayout = new StaticLayout((CharSequence)text, 0, text.length(), paint, (int)Math.max(Math.ceil(constrainedToSize.width), 1.0), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        drawMultilineText(c, textLayout, x, y, paint, anchor, angleDegrees);
    }
    
    public static FSize getSizeOfRotatedRectangleByDegrees(final FSize rectangleSize, final float degrees) {
        final float radians = degrees * 0.017453292f;
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, radians);
    }
    
    public static FSize getSizeOfRotatedRectangleByRadians(final FSize rectangleSize, final float radians) {
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, radians);
    }
    
    public static FSize getSizeOfRotatedRectangleByDegrees(final float rectangleWidth, final float rectangleHeight, final float degrees) {
        final float radians = degrees * 0.017453292f;
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, radians);
    }
    
    public static FSize getSizeOfRotatedRectangleByRadians(final float rectangleWidth, final float rectangleHeight, final float radians) {
        return new FSize(Math.abs(rectangleWidth * (float)Math.cos(radians)) + Math.abs(rectangleHeight * (float)Math.sin(radians)), Math.abs(rectangleWidth * (float)Math.sin(radians)) + Math.abs(rectangleHeight * (float)Math.cos(radians)));
    }
    
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }
}
