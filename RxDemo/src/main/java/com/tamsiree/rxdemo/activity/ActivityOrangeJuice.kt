package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxAnimationTool.initRotateAnimation
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.progress.TOrangeJuice
import java.util.*

@SuppressLint("HandlerLeak")
class ActivityOrangeJuice : ActivityBase(), OnSeekBarChangeListener, View.OnClickListener {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    private var mLeafLoadingView: TOrangeJuice? = null
    private var mAmpireSeekBar: SeekBar? = null
    private var mDistanceSeekBar: SeekBar? = null
    private var mMplitudeText: TextView? = null
    private var mDisparityText: TextView? = null
    private var mFanView: View? = null
    private var mClearButton: Button? = null
    private var mProgress = 0
    private var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                REFRESH_PROGRESS -> if (mProgress < 40) {
                    mProgress += 1
                    // 随机800ms以内刷新一次
                    sendEmptyMessageDelayed(REFRESH_PROGRESS,
                            Random().nextInt(800).toLong())
                    mLeafLoadingView!!.setProgress(mProgress)
                } else {
                    mProgress += 1
                    // 随机1200ms以内刷新一次
                    sendEmptyMessageDelayed(REFRESH_PROGRESS,
                            Random().nextInt(1200).toLong())
                    mLeafLoadingView!!.setProgress(mProgress)
                }
                else -> {
                }
            }
        }
    }
    private var mProgressText: TextView? = null
    private var mAddProgress: View? = null
    private var mFloatTimeSeekBar: SeekBar? = null
    private var mRotateTimeSeekBar: SeekBar? = null
    private var mFloatTimeText: TextView? = null
    private var mRotateTimeText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orange_juice)
        ButterKnife.bind(this)
        initViews()
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000)
    }

    @SuppressLint("StringFormatMatches")
    private fun initViews() {
        mRxTitle!!.setLeftFinish(this)
        mFanView = findViewById(R.id.fan_pic)
        mProgressText = findViewById(R.id.text_progress)
        val rotateAnimation = initRotateAnimation(false, 1500, true,
                Animation.INFINITE)
        mFanView?.startAnimation(rotateAnimation)
        mClearButton = findViewById(R.id.clear_progress)
        mClearButton?.setOnClickListener(this)
        mLeafLoadingView = findViewById(R.id.leaf_loading)
        mMplitudeText = findViewById(R.id.text_ampair)
        mMplitudeText?.text = getString(R.string.current_mplitude,
                mLeafLoadingView?.middleAmplitude)
        mDisparityText = findViewById(R.id.text_disparity)
        mDisparityText?.text = getString(R.string.current_Disparity,
                mLeafLoadingView?.mplitudeDisparity)
        mAmpireSeekBar = findViewById(R.id.seekBar_ampair)
        mAmpireSeekBar?.setOnSeekBarChangeListener(this)
        mAmpireSeekBar?.progress = mLeafLoadingView?.middleAmplitude!!
        mAmpireSeekBar?.max = 50
        mDistanceSeekBar = findViewById(R.id.seekBar_distance)
        mDistanceSeekBar?.setOnSeekBarChangeListener(this)
        mDistanceSeekBar?.progress = mLeafLoadingView?.mplitudeDisparity!!
        mDistanceSeekBar?.max = 20
        mAddProgress = findViewById(R.id.add_progress)
        mAddProgress?.setOnClickListener(this)
        mFloatTimeText = findViewById(R.id.text_float_time)
        mFloatTimeSeekBar = findViewById(R.id.seekBar_float_time)
        mFloatTimeSeekBar?.setOnSeekBarChangeListener(this)
        mFloatTimeSeekBar?.max = 5000
        mFloatTimeSeekBar?.progress = mLeafLoadingView?.leafFloatTime!!.toInt()
        mFloatTimeText?.text = resources.getString(R.string.current_float_time,
                mLeafLoadingView?.leafFloatTime)
        mRotateTimeText = findViewById(R.id.text_rotate_time)
        mRotateTimeSeekBar = findViewById(R.id.seekBar_rotate_time)
        mRotateTimeSeekBar?.setOnSeekBarChangeListener(this)
        mRotateTimeSeekBar?.max = 5000
        mRotateTimeSeekBar?.progress = mLeafLoadingView?.leafRotateTime!!.toInt()
        mRotateTimeText?.text = resources.getString(R.string.current_float_time,
                mLeafLoadingView?.leafRotateTime)
    }

    @SuppressLint("StringFormatMatches")
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when {
            seekBar === mAmpireSeekBar -> {
                mLeafLoadingView!!.middleAmplitude = progress
                mMplitudeText!!.text = getString(R.string.current_mplitude, progress)
            }
            seekBar === mDistanceSeekBar -> {
                mLeafLoadingView!!.mplitudeDisparity = progress
                mDisparityText!!.text = getString(R.string.current_Disparity, progress)
            }
            seekBar === mFloatTimeSeekBar -> {
                mLeafLoadingView!!.leafFloatTime = progress.toLong()
                mFloatTimeText!!.text = resources.getString(R.string.current_float_time, progress)
            }
            seekBar === mRotateTimeSeekBar -> {
                mLeafLoadingView!!.leafRotateTime = progress.toLong()
                mRotateTimeText!!.text = resources.getString(R.string.current_rotate_time, progress)
            }
        }
        mProgressText!!.text = mProgress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onClick(v: View) {
        if (v === mClearButton) {
            mLeafLoadingView!!.setProgress(0)
            mHandler.removeCallbacksAndMessages(null)
            mProgress = 0
        } else if (v === mAddProgress) {
            mProgress++
            mLeafLoadingView!!.setProgress(mProgress)
            mProgressText!!.text = mProgress.toString()
        }
    }

    companion object {
        private const val REFRESH_PROGRESS = 0x10
    }
}