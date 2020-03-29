package com.tamsiree.rxui.view.timeline.video

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TRecordDataExistTimeSection(val startTimeInMillisecond: Long, val endTimeInMillisecond: Long) {
    private val coverDateZeroOClockList: MutableList<Long> = ArrayList()

    fun getCoverDateZeroOClockList(): List<Long> {
        return coverDateZeroOClockList
    }

    companion object {
        private var mostLeftDayZeroTime = Long.MAX_VALUE
        private var mostRightDayZeroTime: Long = -1
    }

    init {
        if (startTimeInMillisecond < mostLeftDayZeroTime) {
            mostLeftDayZeroTime = startTimeInMillisecond
        }
        if (endTimeInMillisecond > mostRightDayZeroTime) {
            mostRightDayZeroTime = endTimeInMillisecond
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val zeroTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startTimeDateString = dateFormat.format(startTimeInMillisecond)
        val startTimeZeroTimeString = "$startTimeDateString 00:00:00"
        val endTimeDateString = dateFormat.format(endTimeInMillisecond)
        val endTimeZeroTimeString = "$endTimeDateString 00:00:00"
        try {
            val startTimeZeroDate = zeroTimeFormat.parse(startTimeZeroTimeString)
            val endTimeZeroDate = zeroTimeFormat.parse(endTimeZeroTimeString)
            var loopZeroDateInMilliseconds = startTimeZeroDate.time
            while (loopZeroDateInMilliseconds <= endTimeZeroDate.time) {
                coverDateZeroOClockList.add(loopZeroDateInMilliseconds)
                loopZeroDateInMilliseconds = loopZeroDateInMilliseconds + TVideoTimeline.SECONDS_PER_DAY * 1000
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}