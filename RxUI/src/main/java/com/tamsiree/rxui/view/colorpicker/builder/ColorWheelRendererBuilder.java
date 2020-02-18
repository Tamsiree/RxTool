package com.tamsiree.rxui.view.colorpicker.builder;


import com.tamsiree.rxui.view.colorpicker.ColorPickerView;
import com.tamsiree.rxui.view.colorpicker.renderer.ColorWheelRenderer;
import com.tamsiree.rxui.view.colorpicker.renderer.FlowerColorWheelRenderer;
import com.tamsiree.rxui.view.colorpicker.renderer.SimpleColorWheelRenderer;

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
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