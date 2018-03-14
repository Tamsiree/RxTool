package com.vondear.rxtools.view.colorpicker.renderer;


import com.vondear.rxtools.view.colorpicker.ColorCircle;

import java.util.List;

/**
 * @author vondear
 */
public interface ColorWheelRenderer {
    float GAP_PERCENTAGE = 0.025f;

    void draw();

    ColorWheelRenderOption getRenderOption();

    void initWith(ColorWheelRenderOption colorWheelRenderOption);

    List<ColorCircle> getColorCircleList();
}
