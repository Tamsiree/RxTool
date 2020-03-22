package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.timeline.video.TRecordDataExistTimeSection
import com.tamsiree.rxui.view.timeline.video.TVideoTimeline.OnBarMoveListener
import com.tamsiree.rxui.view.timeline.video.TVideoTimeline.OnBarScaledListener
import kotlinx.android.synthetic.main.activity_tvideo_timeline.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityTVideoTimeline : ActivityBase(), View.OnClickListener {


    private var mDayBt: Button? = null
    private var mHourBt: Button? = null
    private var mMinuteBt: Button? = null
    private val recordDays = 7
    private val currentRealDateTime = System.currentTimeMillis()
    private var calendar: Calendar? = null

    @SuppressLint("SimpleDateFormat")
    private val zeroTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvideo_timeline)
        ButterKnife.bind(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(this)



        mDayBt = findViewById(R.id.day)
        mHourBt = findViewById(R.id.hour)
        mMinuteBt = findViewById(R.id.minute)
        timebar_zoom_in_btn.setOnClickListener(this)
        timebar_zoom_out_btn.setOnClickListener(this)
        mDayBt?.setOnClickListener(this)
        mHourBt?.setOnClickListener(this)
        mMinuteBt?.setOnClickListener(this)
        calendar = Calendar.getInstance()
        calendar?.set(Calendar.HOUR_OF_DAY, 0)
        calendar?.set(Calendar.MINUTE, 0)
        calendar?.set(Calendar.SECOND, 0)
        //long timebarLeftEndPointTime = currentRealDateTime - 7 * 24 * 3600 * 1000;
        val timebarLeftEndPointTime = calendar?.timeInMillis
        println("calendar:" + calendar?.time + "  currentRealDateTime:" + currentRealDateTime)
        calendar = Calendar.getInstance()
        calendar?.set(Calendar.HOUR_OF_DAY, 0)
        calendar?.set(Calendar.MINUTE, 0)
        calendar?.set(Calendar.SECOND, 0)
        calendar?.add(Calendar.DAY_OF_MONTH, 1)
        val timebarRightEndPointTime = calendar?.timeInMillis
        //long timebarRightEndPointTime = currentRealDateTime + 3 * 3600 * 1000;
        my_timebar_view.initTimebarLengthAndPosition(timebarLeftEndPointTime!!,
                timebarRightEndPointTime!! - 1000, currentRealDateTime)
        val recordDataList: MutableList<TRecordDataExistTimeSection> = ArrayList()
        recordDataList.add(TRecordDataExistTimeSection(timebarLeftEndPointTime - ONE_HOUR_IN_MS * 1, timebarLeftEndPointTime + ONE_HOUR_IN_MS * 3))
        recordDataList.add(TRecordDataExistTimeSection(timebarLeftEndPointTime + ONE_HOUR_IN_MS * 4, timebarLeftEndPointTime + ONE_HOUR_IN_MS * 8))
        recordDataList.add(TRecordDataExistTimeSection(timebarLeftEndPointTime + ONE_HOUR_IN_MS * 12, timebarLeftEndPointTime + ONE_HOUR_IN_MS * 19))
        recordDataList.add(TRecordDataExistTimeSection(timebarLeftEndPointTime + ONE_HOUR_IN_MS * 20, timebarRightEndPointTime))
        my_timebar_view.recordDataExistTimeClipsList = recordDataList
        my_timebar_view.openMove()
        my_timebar_view.checkVideo(true)
        my_timebar_view.setOnBarMoveListener(object : OnBarMoveListener {
            override fun onBarMove(screenLeftTime: Long, screenRightTime: Long, currentTime: Long) {
                if (currentTime == -1L) {
                    Toast.makeText(mContext, "当前时刻没有录像", Toast.LENGTH_SHORT).show()
                }
                current_time_tv.text = zeroTimeFormat.format(currentTime)
            }

            override fun OnBarMoveFinish(screenLeftTime: Long, screenRightTime: Long, currentTime: Long) {
                current_time_tv.text = zeroTimeFormat.format(currentTime)
            }
        })
        my_timebar_view.setOnBarScaledListener(object : OnBarScaledListener {
            override fun onOnBarScaledMode(mode: Int) {
                Log.d(TAG, "onOnBarScaledMode()$mode")
            }

            override fun onBarScaled(screenLeftTime: Long, screenRightTime: Long, currentTime: Long) {
                current_time_tv.text = zeroTimeFormat.format(currentTime)
                Log.d(TAG, "onBarScaled()")
            }

            override fun onBarScaleFinish(screenLeftTime: Long, screenRightTime: Long, currentTime: Long) {
                Log.d(TAG, "onBarScaleFinish()")
            }
        })
    }

    override fun initData() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.timebar_zoom_in_btn -> my_timebar_view.scaleByPressingButton(true)
            R.id.timebar_zoom_out_btn -> my_timebar_view.scaleByPressingButton(false)
            R.id.day -> my_timebar_view.setMode(3)
            R.id.hour -> my_timebar_view.setMode(2)
            R.id.minute -> my_timebar_view.setMode(1)
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        my_timebar_view.recycle()
    }

    companion object {
        private const val ONE_MINUTE_IN_MS = 60 * 1000.toLong()
        private const val ONE_HOUR_IN_MS = 60 * ONE_MINUTE_IN_MS
        private const val ONE_DAY_IN_MS = 24 * ONE_HOUR_IN_MS
    }
}