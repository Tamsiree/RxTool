/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tamsiree.rxui.view.heart

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.tamsiree.rxui.R
import com.tamsiree.rxui.animation.RxAbstractPathAnimator
import com.tamsiree.rxui.animation.RxAbstractPathAnimator.Config.Companion.fromTypeArray
import com.tamsiree.rxui.animation.RxPathAnimator

/**
 * @author tamsiree
 */
class RxHeartLayout : RelativeLayout {
    private var mAnimator: RxAbstractPathAnimator? = null

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.RxHeartLayout, defStyleAttr, 0)
        mAnimator = RxPathAnimator(fromTypeArray(a))
        a.recycle()
    }

    var animator: RxAbstractPathAnimator?
        get() = mAnimator
        set(animator) {
            clearAnimation()
            mAnimator = animator
        }

    override fun clearAnimation() {
        for (i in 0 until childCount) {
            getChildAt(i).clearAnimation()
        }
        removeAllViews()
    }

    fun addHeart(color: Int) {
        val rxHeartView = RxHeartView(context)
        rxHeartView.setColor(color)
        mAnimator!!.start(rxHeartView, this)
    }

    fun addHeart(color: Int, heartResId: Int, heartBorderResId: Int) {
        val rxHeartView = RxHeartView(context)
        rxHeartView.setColorAndDrawables(color, heartResId, heartBorderResId)
        mAnimator!!.start(rxHeartView, this)
    }
}