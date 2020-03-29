package com.tamsiree.rxui.view.cardstack.tools

import com.tamsiree.rxui.view.cardstack.RxCardStackView

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 */
class RxStackScrollDelegateImpl(private val mRxCardStackView: RxCardStackView) : RxScrollDelegate {

    private var mScrollY = 0
    private var mScrollX = 0
    private fun updateChildPos() {
        for (i in 0 until mRxCardStackView.childCount) {
            val view = mRxCardStackView.getChildAt(i)
            if (view.top - mScrollY < mRxCardStackView.getChildAt(0).y) {
                view.translationY = mRxCardStackView.getChildAt(0).y - view.top
            } else if (view.top - mScrollY > view.top) {
                view.translationY = 0f
            } else {
                view.translationY = -mScrollY.toFloat()
            }
        }
    }

    override fun scrollViewTo(x: Int, y: Int) {
        var x = x
        var y = y
        x = clamp(x, mRxCardStackView.width - mRxCardStackView.paddingRight - mRxCardStackView.paddingLeft, mRxCardStackView.width)
        y = clamp(y, mRxCardStackView.showHeight, mRxCardStackView.totalLength)
        mScrollY = y
        mScrollX = x
        updateChildPos()
    }

    override var viewScrollY: Int
        get() = mScrollY
        set(y) {
            scrollViewTo(mScrollX, y)
        }

    override var viewScrollX: Int
        get() = mScrollX
        set(x) {
            scrollViewTo(x, mScrollY)
        }

    companion object {
        private fun clamp(n: Int, my: Int, child: Int): Int {
            if (my >= child || n < 0) {
                return 0
            }
            return if (my + n > child) {
                child - my
            } else n
        }
    }

}