package com.tamsiree.rxui.view.tcardgralleryview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * @ClassName RecyclerViewPageChangeListenerHelper
 * @Description 备注：RecyclerView 实现类似 ViewPager 的 PageChangeListener 监听
 * @Author Tamsiree
 * @Date 2020-3-13 下午1:11
 * @Version 1.0
 */
class RecyclerViewPageChangeListenerHelper(private val snapHelper: SnapHelper, private val onPageChangeListener: OnPageChangeListener) : RecyclerView.OnScrollListener() {
    private var oldPosition = -1 //防止同一Position多次触发
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        onPageChangeListener.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        var position = 0
        val layoutManager = recyclerView.layoutManager
        //获取当前选中的itemView
        val view = snapHelper.findSnapView(layoutManager)
        if (view != null) {
            //获取itemView的position
            position = layoutManager?.getPosition(view)!!
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onScrollStateChanged(recyclerView, newState)
            //newState == RecyclerView.SCROLL_STATE_IDLE 当滚动停止时触发防止在滚动过程中不停触发
            if (newState == RecyclerView.SCROLL_STATE_IDLE && oldPosition != position) {
                oldPosition = position
                onPageChangeListener.onPageSelected(position)
            }
        }
    }

    interface OnPageChangeListener {
        fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int)
        fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int)
        fun onPageSelected(position: Int)
    }

}