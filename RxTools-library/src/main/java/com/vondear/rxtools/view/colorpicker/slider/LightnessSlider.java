package com.vondear.rxtools.view.colorpicker.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.view.colorpicker.ColorPickerView;
import com.vondear.rxtools.view.colorpicker.builder.PaintBuilder;


public class LightnessSlider extends AbsCustomSlider {
    private int color;
    private Paint barPaint = PaintBuilder.newPaint().build();
    private Paint solid = PaintBuilder.newPaint().build();
    private Paint clearingStroke = PaintBuilder.newPaint().color(0xffffffff).xPerMode(PorterDuff.Mode.CLEAR).build();

    private ColorPickerView colorPicker;

    public LightnessSlider(Context context) {
        super(context);
    }

    public LightnessSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightnessSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawBar(Canvas barCanvas) {
        int width = barCanvas.getWidth();
        int height = barCanvas.getHeight();

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        int l = Math.max(2, width / 256);
        for (int x = 0; x <= width; x += l) {
            hsv[2] = (float) x / (width - 1);
            barPaint.setColor(Color.HSVToColor(hsv));
            barCanvas.drawRect(x, 0, x + l, height, barPaint);
        }
    }

    @Override
    protected void onValueChanged(float value) {
        if (colorPicker != null)
            colorPicker.setLightness(value);
    }

    @Override
    protected void drawHandle(Canvas canvas, float x, float y) {
        solid.setColor(RxImageTool.colorAtLightness(color, value));
        canvas.drawCircle(x, y, handleRadius, clearingStroke);
        canvas.drawCircle(x, y, handleRadius * 0.75f, solid);
    }

    public void setColorPicker(ColorPickerView colorPicker) {
        this.colorPicker = colorPicker;
    }

    public void setColor(int color) {
        this.color = color;
        this.value = RxImageTool.lightnessOfColor(color);
        if (bar != null) {
            updateBar();
            invalidate();
        }
    }
}