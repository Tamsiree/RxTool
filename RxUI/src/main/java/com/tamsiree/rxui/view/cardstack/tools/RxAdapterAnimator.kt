package com.tamsiree.rxui.view.cardstack.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.tamsiree.rxui.view.cardstack.RxCardStackView

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
abstract class RxAdapterAnimator(protected var mRxCardStackView: RxCardStackView) {
    @JvmField
    protected var mSet: AnimatorSet? = null
    protected fun initAnimatorSet() {
        mSet = AnimatorSet()
        mSet!!.interpolator = AccelerateDecelerateInterpolator()
        mSet!!.duration = duration.toLong()
    }

    fun itemClick(viewHolder: RxCardStackView.ViewHolder, position: Int) {
        if (mSet != null && mSet!!.isRunning) {
            return
        }
        initAnimatorSet()
        if (mRxCardStackView.selectPosition == position) {
            onItemCollapse(viewHolder)
        } else {
            onItemExpand(viewHolder, position)
        }
        if (mRxCardStackView.childCount == 1) {
            mSet!!.end()
        }
    }

    protected abstract fun itemExpandAnimatorSet(viewHolder: RxCardStackView.ViewHolder, position: Int)

    protected abstract fun itemCollapseAnimatorSet(viewHolder: RxCardStackView.ViewHolder)

    private fun onItemExpand(viewHolder: RxCardStackView.ViewHolder, position: Int) {
        val preSelectPosition = mRxCardStackView.selectPosition
        val preSelectViewHolder = mRxCardStackView.getViewHolder(preSelectPosition)
        preSelectViewHolder?.onItemExpand(false)
        mRxCardStackView.selectPosition = position
        itemExpandAnimatorSet(viewHolder, position)
        mSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                mRxCardStackView.setScrollEnable(false)
                preSelectViewHolder?.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, false)
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, true)
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewHolder.onItemExpand(true)
                preSelectViewHolder?.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, false)
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, true)
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                preSelectViewHolder?.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, false)
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, true)
            }
        })
        mSet!!.start()
    }

    private fun onItemCollapse(viewHolder: RxCardStackView.ViewHolder) {
        itemCollapseAnimatorSet(viewHolder)
        mSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                viewHolder.onItemExpand(false)
                mRxCardStackView.setScrollEnable(true)
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, false)
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mRxCardStackView.selectPosition = RxCardStackView.DEFAULT_SELECT_POSITION
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, false)
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, false)
            }
        })
        mSet!!.start()
    }

    protected fun getCollapseStartTop(collapseShowItemCount: Int): Int {
        return (mRxCardStackView.overlapGapsCollapse
                * (mRxCardStackView.numBottomShow - collapseShowItemCount - (mRxCardStackView.numBottomShow - if (mRxCardStackView.childCount - mRxCardStackView.selectPosition > mRxCardStackView.numBottomShow) mRxCardStackView.numBottomShow else mRxCardStackView.childCount - mRxCardStackView.selectPosition - 1)))
    }

    val duration: Int
        get() = mRxCardStackView.duration

    companion object {
        const val ANIMATION_DURATION = 400
    }

}