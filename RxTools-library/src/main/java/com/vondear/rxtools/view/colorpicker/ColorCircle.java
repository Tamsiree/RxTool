package com.vondear.rxtools.view.colorpicker;

import android.graphics.Color;

/**
 * @author vondear
 */
public class ColorCircle {
    private float x, y;
    private float[] hsv = new float[3];
    private float[] hsvClone;
    private int color;

    public ColorCircle(float x, float y, float[] hsv) {
        set(x, y, hsv);
    }

    public double sqDist(float x, float y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float[] getHsv() {
        return hsv;
    }

    public float[] getHsvWithLightness(float lightness) {
        if (hsvClone == null)
            hsvClone = hsv.clone();
        hsvClone[0] = hsv[0];
        hsvClone[1] = hsv[1];
        hsvClone[2] = lightness;
        return hsvClone;
    }

    public void set(float x, float y, float[] hsv) {
        this.x = x;
        this.y = y;
        this.hsv[0] = hsv[0];
        this.hsv[1] = hsv[1];
        this.hsv[2] = hsv[2];
        this.color = Color.HSVToColor(this.hsv);
    }

    public int getColor() {
        return color;
    }
}