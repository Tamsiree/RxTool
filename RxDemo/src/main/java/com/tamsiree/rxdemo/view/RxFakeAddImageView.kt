package com.tamsiree.rxdemo.view

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author tamsiree
 */
class RxFakeAddImageView : AppCompatImageView {
    private val mPointF: PointF? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setMPointF(pointF: PointF) {
        x = pointF.x
        y = pointF.y
    }

    fun getmPointF(): PointF? {
        return mPointF
    }

    fun setmPointF(mPointF: PointF) {
        x = mPointF.x
        y = mPointF.y
    }
}