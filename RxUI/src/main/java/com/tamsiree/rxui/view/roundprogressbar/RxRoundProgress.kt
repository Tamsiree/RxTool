/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/
package com.tamsiree.rxui.view.roundprogressbar

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.roundprogressbar.common.RxBaseRoundProgress

/**
 * @author tamsiree
 */
class RxRoundProgress : RxBaseRoundProgress {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    public override fun initLayout(): Int {
        return R.layout.layout_round_corner_progress_bar
    }

    override fun initStyleable(context: Context, attrs: AttributeSet?) {}
    override fun initView() {}
    override fun drawProgress(layoutProgress: LinearLayout?, max: Float, progress: Float, totalWidth: Float,
                              radius: Int, padding: Int, colorProgress: Int, isReverse: Boolean) {
        val backgroundDrawable = createGradientDrawable(colorProgress)
        val newRadius = radius - padding / 2
        backgroundDrawable.cornerRadii = floatArrayOf(newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat())
        layoutProgress?.background = backgroundDrawable
        val ratio = max / progress
        val progressWidth = ((totalWidth - padding * 2) / ratio).toInt()
        val progressParams = layoutProgress?.layoutParams
        progressParams?.width = progressWidth
        layoutProgress?.layoutParams = progressParams
    }

    override fun onViewDraw() {}

}