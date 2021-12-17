package com.tamsiree.rxui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewDebug.ExportedProperty
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author tamsiree
 * @date 2016/6/28
 */
class RxRunTextView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    /**
     * 当前并没有焦点，我只是欺骗了Android系统
     */
    @ExportedProperty(category = "focus")
    override fun isFocused(): Boolean {
        return true
    }
}