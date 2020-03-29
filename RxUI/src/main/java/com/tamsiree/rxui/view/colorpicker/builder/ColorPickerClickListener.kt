package com.tamsiree.rxui.view.colorpicker.builder

import android.content.DialogInterface

/**
 * @author Tamsiree
 * @date 2015/4/17 新增
 * 2018/6/11 11:36:40 整合修改
 */
interface ColorPickerClickListener {
    fun onClick(d: DialogInterface?, lastSelectedColor: Int, allColors: Array<Int?>?)
}