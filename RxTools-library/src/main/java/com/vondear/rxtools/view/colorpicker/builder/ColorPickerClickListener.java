package com.vondear.rxtools.view.colorpicker.builder;

import android.content.DialogInterface;

/**
 * @author Vondear
 * @date 4/17/2015
 */
public interface ColorPickerClickListener {
    void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors);
}
