package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxSeekBar
import com.tamsiree.rxui.view.RxTitle
import java.text.DecimalFormat

/**
 * @author tamsiree
 */
class ActivitySeekBar : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.seekbar1)
    var mSeekbar1: RxSeekBar? = null

    @JvmField
    @BindView(R.id.progress2_tv)
    var mProgress2Tv: TextView? = null

    @JvmField
    @BindView(R.id.seekbar2)
    var mSeekbar2: RxSeekBar? = null

    @JvmField
    @BindView(R.id.seekbar3)
    var mSeekbar3: RxSeekBar? = null

    @JvmField
    @BindView(R.id.seekbar4)
    var mSeekbar4: RxSeekBar? = null

    @JvmField
    @BindView(R.id.activity_main)
    var mActivityMain: LinearLayout? = null
    private val df = DecimalFormat("0.00")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        mSeekbar1!!.setValue(10f)
        mSeekbar2!!.setValue(-0.5f, 0.8f)
        mSeekbar1!!.setOnRangeChangedListener { view, min, max, isFromUser -> mSeekbar1!!.setProgressDescription(min.toInt().toString() + "%") }
        mSeekbar2!!.setOnRangeChangedListener { view, min, max, isFromUser ->
            if (isFromUser) {
                mProgress2Tv!!.text = "$min-$max"
                mSeekbar2!!.setLeftProgressDescription(df.format(min.toDouble()))
                mSeekbar2!!.setRightProgressDescription(df.format(max.toDouble()))
            }
        }
    }
}