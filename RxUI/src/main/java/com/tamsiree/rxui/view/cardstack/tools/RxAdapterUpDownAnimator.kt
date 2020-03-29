package com.tamsiree.rxui.view.cardstack.tools

import android.animation.ObjectAnimator
import android.view.View
import com.tamsiree.rxui.view.cardstack.RxCardStackView

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class RxAdapterUpDownAnimator(rxCardStackView: RxCardStackView) : RxAdapterAnimator(rxCardStackView) {
    override fun itemExpandAnimatorSet(viewHolder: RxCardStackView.ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.clearAnimation()
        val oa = ObjectAnimator.ofFloat(itemView, View.Y, itemView.y, mRxCardStackView.scrollY + mRxCardStackView.paddingTop.toFloat())
        mSet!!.play(oa)
        var collapseShowItemCount = 0
        for (i in 0 until mRxCardStackView.childCount) {
            var childTop: Int
            if (i == mRxCardStackView.selectPosition) {
                continue
            }
            val child = mRxCardStackView.getChildAt(i)
            child.clearAnimation()
            if (i > mRxCardStackView.selectPosition && collapseShowItemCount < mRxCardStackView.numBottomShow) {
                childTop = mRxCardStackView.showHeight - getCollapseStartTop(collapseShowItemCount) + mRxCardStackView.scrollY
                val oAnim = ObjectAnimator.ofFloat(child, View.Y, child.y, childTop.toFloat())
                mSet!!.play(oAnim)
                collapseShowItemCount++
            } else if (i < mRxCardStackView.selectPosition) {
                val oAnim = ObjectAnimator.ofFloat(child, View.Y, child.y, mRxCardStackView.scrollY - child.height.toFloat())
                mSet!!.play(oAnim)
            } else {
                val oAnim = ObjectAnimator.ofFloat(child, View.Y, child.y, mRxCardStackView.showHeight + mRxCardStackView.scrollY.toFloat())
                mSet!!.play(oAnim)
            }
        }
    }

    override fun itemCollapseAnimatorSet(viewHolder: RxCardStackView.ViewHolder) {
        var childTop = mRxCardStackView.paddingTop
        for (i in 0 until mRxCardStackView.childCount) {
            val child = mRxCardStackView.getChildAt(i)
            child.clearAnimation()
            val lp = child.layoutParams as RxCardStackView.LayoutParams
            childTop += lp.topMargin
            if (i != 0) {
                childTop -= mRxCardStackView.overlapGaps * 2
                val oAnim = ObjectAnimator.ofFloat(child, View.Y, child.y, childTop.toFloat())
                mSet!!.play(oAnim)
            } else {
                val oAnim = ObjectAnimator.ofFloat(child, View.Y, child.y, childTop.toFloat())
                mSet!!.play(oAnim)
            }
            childTop += lp.mHeaderHeight
        }
    }
}