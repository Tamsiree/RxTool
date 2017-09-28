package com.vondear.rxtools.view.colorpicker.builder;

import android.content.DialogInterface;

/**
 * Created by Vondear on 4/17/15.
 */
public interface ColorPickerClickListener {
    void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors);
}
