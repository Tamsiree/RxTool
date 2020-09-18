package com.tamsiree.rxui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

/**
 * @author tamsiree
 * @date 2018/2/2
 *
 *
 * 解决没有输入内容时也能展示筛选内容的需求
 */
class RxAutoCompleteTextView : AppCompatAutoCompleteTextView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    override fun enoughToFilter(): Boolean {
        return true
    }
}