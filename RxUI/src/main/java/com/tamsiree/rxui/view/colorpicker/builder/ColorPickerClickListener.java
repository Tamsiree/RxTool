package com.tamsiree.rxui.view.colorpicker.builder;

import android.content.DialogInterface;

/**
 * @author Vondear
 * @date 2015/4/17 新增
 *       2018/6/11 11:36:40 整合修改
 */
public interface ColorPickerClickListener {
    void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors);
}
