package com.tamsiree.rxui.view.tcardgralleryview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * 控制fling速度的RecyclerView
 *
 * Created by Tamsiree on 2020/3/11.
 */
class TCardGalleryView : RecyclerView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        var velocityX = velocityX
        var velocityY = velocityY
        velocityX = solveVelocity(velocityX)
        velocityY = solveVelocity(velocityY)
        return super.fling(velocityX, velocityY)
    }

    private fun solveVelocity(velocity: Int): Int {
        return if (velocity > 0) {
            Math.min(velocity, FLING_MAX_VELOCITY)
        } else {
            Math.max(velocity, -FLING_MAX_VELOCITY)
        }
    }

    companion object {
        private const val FLING_SCALE_DOWN_FACTOR = 0.5f // 减速因子
        private const val FLING_MAX_VELOCITY = 8000 // 最大顺时滑动速度
    }
}