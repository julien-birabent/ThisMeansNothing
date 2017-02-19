package com.julienbirabent.thismeansnothing;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

import java.util.Random;

/**
 * Created by Julien on 2017-02-19.
 */

public class RandomColorGenerator {

    private Random rand = new Random();

    public RandomColorGenerator() {

    }

    public static int generate(){

        float[] TEMP_HSL = new float[]{0, 0, 0};
        float[] hsl = TEMP_HSL;
        hsl[0] = (float) (Math.random() * 360);
        hsl[1] = (float) (40 + (Math.random() * 60));
        hsl[2] = (float) (40 + (Math.random() * 60));
        return ColorUtils.HSLToColor(hsl);
    }
}
