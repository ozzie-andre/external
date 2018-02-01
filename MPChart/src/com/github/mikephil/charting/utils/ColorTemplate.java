// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import android.graphics.Color;

public class ColorTemplate
{
    public static final int COLOR_NONE = 1122867;
    public static final int COLOR_SKIP = 1122868;
    public static final int[] LIBERTY_COLORS;
    public static final int[] JOYFUL_COLORS;
    public static final int[] PASTEL_COLORS;
    public static final int[] COLORFUL_COLORS;
    public static final int[] VORDIPLOM_COLORS;
    
    static {
        LIBERTY_COLORS = new int[] { Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130) };
        JOYFUL_COLORS = new int[] { Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209) };
        PASTEL_COLORS = new int[] { Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80) };
        COLORFUL_COLORS = new int[] { Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53) };
        VORDIPLOM_COLORS = new int[] { Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157) };
    }
    
    public static int rgb(final String hex) {
        final int color = (int)Long.parseLong(hex.replace("#", ""), 16);
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color >> 0 & 0xFF;
        return Color.rgb(r, g, b);
    }
    
    public static int getHoloBlue() {
        return Color.rgb(51, 181, 229);
    }
    
    public static int getColorWithAlphaComponent(final int color, final int alpha) {
        return (color & 0xFFFFFF) | (alpha & 0xFF) << 24;
    }
    
    public static List<Integer> createColors(final Resources r, final int[] colors) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final int i : colors) {
            result.add(r.getColor(i));
        }
        return result;
    }
    
    public static List<Integer> createColors(final int[] colors) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final int i : colors) {
            result.add(i);
        }
        return result;
    }
}
