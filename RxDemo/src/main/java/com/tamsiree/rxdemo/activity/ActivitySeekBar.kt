package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_seek_bar.*
import java.text.DecimalFormat

/**
 * @author tamsiree
 */
class ActivitySeekBar : ActivityBase() {

    private val df = DecimalFormat("0.00")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        RxDeviceTool.setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        seekbar1.setValue(10f)
        seekbar2.setValue(-0.5f, 0.8f)
        seekbar1.setOnRangeChangedListener { view, min, max, isFromUser -> seekbar1.setProgressDescription(min.toInt().toString() + "%") }
        seekbar2.setOnRangeChangedListener { view, min, max, isFromUser ->
            if (isFromUser) {
                progress2_tv.text = "$min-$max"
                seekbar2.setLeftProgressDescription(df.format(min.toDouble()))
                seekbar2.setRightProgressDescription(df.format(max.toDouble()))
            }
        }
    }

    override fun initData() {

    }
}