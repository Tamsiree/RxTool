package com.vondear.rxtools.view.colorpicker.renderer;

import android.graphics.Color;
import android.graphics.Paint;

import com.vondear.rxtools.view.colorpicker.ColorCircle;
import com.vondear.rxtools.view.colorpicker.builder.PaintBuilder;

public class SimpleColorWheelRenderer extends AbsColorWheelRenderer {
    private Paint selectorFill = PaintBuilder.newPaint().build();
    private float[] hsv = new float[3];

    @Override
    public void draw() {
        final int setSize = colorCircleList.size();
        int currentCount = 0;
        float half = colorWheelRenderOption.targetCanvas.getWidth() / 2f;
        int density = colorWheelRenderOption.density;
        float maxRadius = colorWheelRenderOption.maxRadius;

        for (int i = 0; i < density; i++) {
            float p = (float) i / (density - 1); // 0~1
            float radius = maxRadius * p;
            float size = colorWheelRenderOption.cSize;
            int total = calcTotalCount(radius, size);

            for (int j = 0; j < total; j++) {
                double angle = Math.PI * 2 * j / total + (Math.PI / total) * ((i + 1) % 2);
                float x = half + (float) (radius * Math.cos(angle));
                float y = half + (float) (radius * Math.sin(angle));
                hsv[0] = (float) (angle * 180 / Math.PI);
                hsv[1] = radius / maxRadius;
                hsv[2] = colorWheelRenderOption.lightness;
                selectorFill.setColor(Color.HSVToColor(hsv));
                selectorFill.setAlpha(getAlphaValueAsInt());

                colorWheelRenderOption.targetCanvas.drawCircle(x, y, size - colorWheelRenderOption.strokeWidth, selectorFill);

                if (currentCount >= setSize)
                    colorCircleList.add(new ColorCircle(x, y, hsv));
                else colorCircleList.get(currentCount).set(x, y, hsv);
                currentCount++;
            }
        }
    }
}