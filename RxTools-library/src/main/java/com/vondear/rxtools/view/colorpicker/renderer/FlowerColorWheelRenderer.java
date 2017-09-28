package com.vondear.rxtools.view.colorpicker.renderer;

import android.graphics.Color;
import android.graphics.Paint;

import com.vondear.rxtools.view.colorpicker.ColorCircle;
import com.vondear.rxtools.view.colorpicker.builder.PaintBuilder;

public class FlowerColorWheelRenderer extends AbsColorWheelRenderer {
    private Paint selectorFill = PaintBuilder.newPaint().build();
    private float[] hsv = new float[3];
    private float sizeJitter = 1.2f;

    @Override
    public void draw() {
        final int setSize = colorCircleList.size();
        int currentCount = 0;
        float half = colorWheelRenderOption.targetCanvas.getWidth() / 2f;
        int density = colorWheelRenderOption.density;
        float strokeWidth = colorWheelRenderOption.strokeWidth;
        float maxRadius = colorWheelRenderOption.maxRadius;
        float cSize = colorWheelRenderOption.cSize;

        for (int i = 0; i < density; i++) {
            float p = (float) i / (density - 1); // 0~1
            float jitter = (i - density / 2f) / density; // -0.5 ~ 0.5
            float radius = maxRadius * p;
            float size = Math.max(1.5f + strokeWidth, cSize + (i == 0 ? 0 : cSize * sizeJitter * jitter));
            int total = Math.min(calcTotalCount(radius, size), density * 2);

            for (int j = 0; j < total; j++) {
                double angle = Math.PI * 2 * j / total + (Math.PI / total) * ((i + 1) % 2);
                float x = half + (float) (radius * Math.cos(angle));
                float y = half + (float) (radius * Math.sin(angle));
                hsv[0] = (float) (angle * 180 / Math.PI);
                hsv[1] = radius / maxRadius;
                hsv[2] = colorWheelRenderOption.lightness;
                selectorFill.setColor(Color.HSVToColor(hsv));
                selectorFill.setAlpha(getAlphaValueAsInt());

                colorWheelRenderOption.targetCanvas.drawCircle(x, y, size - strokeWidth, selectorFill);

                if (currentCount >= setSize)
                    colorCircleList.add(new ColorCircle(x, y, hsv));
                else colorCircleList.get(currentCount).set(x, y, hsv);
                currentCount++;
            }
        }
    }
}