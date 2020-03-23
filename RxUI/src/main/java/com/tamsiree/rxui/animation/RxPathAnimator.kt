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
package com.tamsiree.rxui.animation

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author tamsiree
 */
class RxPathAnimator(config: Config?) : RxAbstractPathAnimator(config!!) {
    private val mCounter = AtomicInteger(0)
    private val mHandler: Handler
    override fun start(child: View, parent: ViewGroup?) {
        parent!!.addView(child, ViewGroup.LayoutParams(mConfig.heartWidth, mConfig.heartHeight))
        val anim = FloatAnimation(createPath(mCounter, parent, 2), randomRotation(), parent, child)
        anim.duration = mConfig.animDuration.toLong()
        anim.interpolator = LinearInterpolator()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                mHandler.post { parent.removeView(child) }
                mCounter.decrementAndGet()
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {
                mCounter.incrementAndGet()
            }
        })
        anim.interpolator = LinearInterpolator()
        child.startAnimation(anim)
    }

    init {
        mHandler = Handler(Looper.getMainLooper())
    }
}