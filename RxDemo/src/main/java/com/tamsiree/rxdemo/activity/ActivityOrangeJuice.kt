package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxAnimationTool.initRotateAnimation
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_orange_juice.*
import java.util.*

@SuppressLint("HandlerLeak")
class ActivityOrangeJuice : ActivityBase(), OnSeekBarChangeListener, View.OnClickListener {

    private var mProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orange_juice)
    }

    @SuppressLint("StringFormatMatches")
    override fun initView() {
        rx_title.setLeftFinish(this)

        val rotateAnimation = initRotateAnimation(false, 1500, true,
                Animation.INFINITE)
        fan_pic.startAnimation(rotateAnimation)

        clear_progress.setOnClickListener(this)

        text_ampair.text = getString(R.string.current_mplitude,
                leaf_loading.middleAmplitude)
        text_disparity.text = getString(R.string.current_Disparity,
                leaf_loading.mplitudeDisparity)

        seekBar_ampair.setOnSeekBarChangeListener(this)
        seekBar_ampair.progress = leaf_loading.middleAmplitude
        seekBar_ampair.max = 50

        seekBar_distance.setOnSeekBarChangeListener(this)
        seekBar_distance.progress = leaf_loading.mplitudeDisparity
        seekBar_distance.max = 20

        add_progress.setOnClickListener(this)


        seekBar_float_time.setOnSeekBarChangeListener(this)
        seekBar_float_time.max = 5000
        seekBar_float_time.progress = leaf_loading.leafFloatTime.toInt()
        text_float_time.text = resources.getString(R.string.current_float_time,
                leaf_loading.leafFloatTime)


        seekBar_rotate_time.setOnSeekBarChangeListener(this)
        seekBar_rotate_time.max = 5000
        seekBar_rotate_time.progress = leaf_loading.leafRotateTime.toInt()
        text_rotate_time.text = resources.getString(R.string.current_float_time, leaf_loading.leafRotateTime)
    }

    override fun initData() {
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000)
    }

    @SuppressLint("StringFormatMatches")
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when {
            seekBar === seekBar_ampair -> {
                leaf_loading.middleAmplitude = progress
                text_ampair.text = getString(R.string.current_mplitude, progress)
            }
            seekBar === seekBar_distance -> {
                leaf_loading.mplitudeDisparity = progress
                text_disparity.text = getString(R.string.current_Disparity, progress)
            }
            seekBar === seekBar_float_time -> {
                leaf_loading.leafFloatTime = progress.toLong()
                text_float_time.text = resources.getString(R.string.current_float_time, progress)
            }
            seekBar === seekBar_rotate_time -> {
                leaf_loading.leafRotateTime = progress.toLong()
                text_rotate_time.text = resources.getString(R.string.current_rotate_time, progress)
            }
        }
        text_progress.text = mProgress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onClick(v: View) {
        if (v === clear_progress) {
            leaf_loading.setProgress(0)
            mHandler.removeCallbacksAndMessages(null)
            mProgress = 0
        } else if (v === add_progress) {
            mProgress++
            leaf_loading.setProgress(mProgress)
            text_progress.text = mProgress.toString()
        }
    }

    companion object {
        private const val REFRESH_PROGRESS = 0x10
    }

    private var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                REFRESH_PROGRESS -> if (mProgress < 40) {
                    mProgress += 1
                    // 随机800ms以内刷新一次
                    sendEmptyMessageDelayed(REFRESH_PROGRESS,
                            Random().nextInt(800).toLong())
                    leaf_loading.setProgress(mProgress)
                } else {
                    mProgress += 1
                    // 随机1200ms以内刷新一次
                    sendEmptyMessageDelayed(REFRESH_PROGRESS,
                            Random().nextInt(1200).toLong())
                    leaf_loading.setProgress(mProgress)
                }
                else -> {
                }
            }
        }
    }
}