package com.tamsiree.rxkit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * @author vonde
 * @date 2016/11/13
 */
class RxRecyclerViewDividerTool : ItemDecoration {
    private var space = 0
    private var leftSpace = 0
    private var rightSpace = 0
    private var topSpace = 0
    private var bottomSpace = 0
    private var isTop = false

    constructor(leftSpace: Int, rightSpace: Int, topSpace: Int, bottomSpace: Int) {
        this.leftSpace = leftSpace
        this.rightSpace = rightSpace
        this.topSpace = topSpace
        this.bottomSpace = bottomSpace
    }

    constructor(leftSpace: Int, rightSpace: Int, topSpace: Int, bottomSpace: Int, isTop: Boolean) {
        this.leftSpace = leftSpace
        this.rightSpace = rightSpace
        this.topSpace = topSpace
        this.bottomSpace = bottomSpace
        this.isTop = isTop
    }

    constructor(space: Int, isTop: Boolean) {
        this.space = space
        this.isTop = isTop
    }

    constructor(space: Int) {
        this.space = space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (space == 0) {
            outRect.left = leftSpace
            outRect.right = rightSpace
            outRect.bottom = topSpace
            outRect.top = bottomSpace
        } else {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
        if (isTop) {
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0
            }
            outRect.left = 0
            outRect.right = 0
        }
    }
}