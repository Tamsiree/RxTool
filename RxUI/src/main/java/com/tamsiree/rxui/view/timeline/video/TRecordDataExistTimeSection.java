
package com.tamsiree.rxui.view.timeline.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TRecordDataExistTimeSection {

    private static long mostLeftDayZeroTime = Long.MAX_VALUE;

    private static long mostRightDayZeroTime = -1;

    private long startTimeInMillisecond;

    private long endTimeInMillisecond;

    private List<Long> coverDateZeroOClockList = new ArrayList<>();

    public TRecordDataExistTimeSection(long startTimeInMillisecond, long endTimeInMillisecond) {
        this.startTimeInMillisecond = startTimeInMillisecond;
        this.endTimeInMillisecond = endTimeInMillisecond;

        if (startTimeInMillisecond < mostLeftDayZeroTime) {
            mostLeftDayZeroTime = startTimeInMillisecond;
        }

        if (endTimeInMillisecond > mostRightDayZeroTime) {
            mostRightDayZeroTime = endTimeInMillisecond;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat zeroTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startTimeDateString = dateFormat.format(startTimeInMillisecond);
        String startTimeZeroTimeString = startTimeDateString + " 00:00:00";

        String endTimeDateString = dateFormat.format(endTimeInMillisecond);
        String endTimeZeroTimeString = endTimeDateString + " 00:00:00";

        try {
            Date startTimeZeroDate = zeroTimeFormat.parse(startTimeZeroTimeString);
            Date endTimeZeroDate = zeroTimeFormat.parse(endTimeZeroTimeString);

            long loopZeroDateInMilliseconds = startTimeZeroDate.getTime();
            while (loopZeroDateInMilliseconds <= endTimeZeroDate.getTime()) {
                coverDateZeroOClockList.add(loopZeroDateInMilliseconds);
                loopZeroDateInMilliseconds = loopZeroDateInMilliseconds + TVideoTimeline.SECONDS_PER_DAY * 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getStartTimeInMillisecond() {
        return startTimeInMillisecond;
    }

    public long getEndTimeInMillisecond() {
        return endTimeInMillisecond;
    }

    public List<Long> getCoverDateZeroOClockList() {
        return coverDateZeroOClockList;
    }
}