package com.vondear.rxtools.view.colorpicker.builder;


import com.vondear.rxtools.view.colorpicker.ColorPickerView;
import com.vondear.rxtools.view.colorpicker.renderer.ColorWheelRenderer;
import com.vondear.rxtools.view.colorpicker.renderer.FlowerColorWheelRenderer;
import com.vondear.rxtools.view.colorpicker.renderer.SimpleColorWheelRenderer;

/**
 * @author vondear
 */
public class ColorWheelRendererBuilder {
    public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
        switch (wheelType) {
            case CIRCLE:
                return new SimpleColorWheelRenderer();
            case FLOWER:
                return new FlowerColorWheelRenderer();
                default:
                    break;
        }
        throw new IllegalArgumentException("wrong WHEEL_TYPE");
    }
}