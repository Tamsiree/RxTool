package com.tamsiree.rxkit

import android.annotation.SuppressLint
import com.tamsiree.rxkit.RxConstTool.DAY
import com.tamsiree.rxkit.RxConstTool.HOUR
import com.tamsiree.rxkit.RxConstTool.MIN
import com.tamsiree.rxkit.RxConstTool.MSEC
import com.tamsiree.rxkit.RxConstTool.SEC
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author tamsiree
 * @date 2016/1/24
 * 时间相关工具类
 */
object RxTimeTool {
    /**
     *
     * 在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.
     * 格式的意义如下： 日期和时间模式 <br></br>
     *
     * 日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
     * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
     * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
     *
     * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br></br>
     * <table border="1" cellspacing="1" cellpadding="1" summary="Chart shows pattern letters, date/time component,
    presentation, and examples.">
     * <tr>
     * <th align="left">字母</th>
     * <th align="left">日期或时间元素</th>
     * <th align="left">表示</th>
     * <th align="left">示例</th>
    </tr> *
     * <tr>
     * <td>`G`</td>
     * <td>Era 标志符</td>
     * <td>Text</td>
     * <td>`AD`</td>
    </tr> *
     * <tr>
     * <td>`y` </td>
     * <td>年 </td>
     * <td>Year </td>
     * <td>`1996`; `96` </td>
    </tr> *
     * <tr>
     * <td>`M` </td>
     * <td>年中的月份 </td>
     * <td>Month </td>
     * <td>`July`; `Jul`; `07` </td>
    </tr> *
     * <tr>
     * <td>`w` </td>
     * <td>年中的周数 </td>
     * <td>Number </td>
     * <td>`27` </td>
    </tr> *
     * <tr>
     * <td>`W` </td>
     * <td>月份中的周数 </td>
     * <td>Number </td>
     * <td>`2` </td>
    </tr> *
     * <tr>
     * <td>`D` </td>
     * <td>年中的天数 </td>
     * <td>Number </td>
     * <td>`189` </td>
    </tr> *
     * <tr>
     * <td>`d` </td>
     * <td>月份中的天数 </td>
     * <td>Number </td>
     * <td>`10` </td>
    </tr> *
     * <tr>
     * <td>`F` </td>
     * <td>月份中的星期 </td>
     * <td>Number </td>
     * <td>`2` </td>
    </tr> *
     * <tr>
     * <td>`E` </td>
     * <td>星期中的天数 </td>
     * <td>Text </td>
     * <td>`Tuesday`; `Tue` </td>
    </tr> *
     * <tr>
     * <td>`a` </td>
     * <td>Am/pm 标记 </td>
     * <td>Text </td>
     * <td>`PM` </td>
    </tr> *
     * <tr>
     * <td>`H` </td>
     * <td>一天中的小时数（0-23） </td>
     * <td>Number </td>
     * <td>`0` </td>
    </tr> *
     * <tr>
     * <td>`k` </td>
     * <td>一天中的小时数（1-24） </td>
     * <td>Number </td>
     * <td>`24` </td>
    </tr> *
     * <tr>
     * <td>`K` </td>
     * <td>am/pm 中的小时数（0-11） </td>
     * <td>Number </td>
     * <td>`0` </td>
    </tr> *
     * <tr>
     * <td>`h` </td>
     * <td>am/pm 中的小时数（1-12） </td>
     * <td>Number </td>
     * <td>`12` </td>
    </tr> *
     * <tr>
     * <td>`m` </td>
     * <td>小时中的分钟数 </td>
     * <td>Number </td>
     * <td>`30` </td>
    </tr> *
     * <tr>
     * <td>`s` </td>
     * <td>分钟中的秒数 </td>
     * <td>Number </td>
     * <td>`55` </td>
    </tr> *
     * <tr>
     * <td>`S` </td>
     * <td>毫秒数 </td>
     * <td>Number </td>
     * <td>`978` </td>
    </tr> *
     * <tr>
     * <td>`z` </td>
     * <td>时区 </td>
     * <td>General time zone </td>
     * <td>`Pacific Standard Time`; `PST`; `GMT-08:00` </td>
    </tr> *
     * <tr>
     * <td>`Z` </td>
     * <td>时区 </td>
     * <td>RFC 822 time zone </td>
     * <td>`-0800` </td>
    </tr> *
    </table> *
     * <pre>
     * HH:mm    15:44
     * h:mm a    3:44 下午
     * HH:mm z    15:44 CST
     * HH:mm Z    15:44 +0800
     * HH:mm zzzz    15:44 中国标准时间
     * HH:mm:ss    15:44:40
     * yyyy-MM-dd    2016-08-12
     * yyyy-MM-dd HH:mm    2016-08-12 15:44
     * yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
     * yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
     * EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
     * yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     * yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
     * K:mm a    3:44 下午
     * EEE, MMM d, ''yy    星期五, 八月 12, '16
     * hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
     * yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
     * EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
     * yyMMddHHmmssZ    160812154440+0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
    </pre> *
     */
    val DEFAULT_SDF = SimpleDateFormat(RxConstants.DATE_FORMAT_DETACH, Locale.getDefault())
    /**
     * 将时间戳转为时间字符串
     *
     * 格式为用户自定义
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    /**
     * 将时间戳转为时间字符串
     *
     * 格式为yyyy-MM-dd HH:mm:ss
     *
     * @param milliseconds 毫秒时间戳
     * @return 时间字符串
     */
    @JvmOverloads
    @JvmStatic
    fun milliseconds2String(milliseconds: Long, format: SimpleDateFormat = DEFAULT_SDF): String {
        return format.format(Date(milliseconds))
    }
    /**
     * 将时间字符串转为时间戳
     *
     * 格式为用户自定义
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    /**
     * 将时间字符串转为时间戳
     *
     * 格式为yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    @JvmOverloads
    @JvmStatic
    fun string2Milliseconds(time: String?, format: SimpleDateFormat = DEFAULT_SDF): Long {
        try {
            return format.parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return -1
    }
    /**
     * 将时间字符串转为Date类型
     *
     * 格式为用户自定义
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    /**
     * 将时间字符串转为Date类型
     *
     * 格式为yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间字符串
     * @return Date类型
     */
    @JvmOverloads
    @JvmStatic
    fun string2Date(time: String?, format: SimpleDateFormat = DEFAULT_SDF): Date {
        return Date(string2Milliseconds(time, format))
    }
    /**
     * 将Date类型转为时间字符串
     *
     * 格式为用户自定义
     *
     * @param time   Date类型时间
     * @param format 时间格式
     * @return 时间字符串
     */
    /**
     * 将Date类型转为时间字符串
     *
     * 格式为yyyy-MM-dd HH:mm:ss
     *
     * @param time Date类型时间
     * @return 时间字符串
     */
    @JvmOverloads
    @JvmStatic
    fun date2String(time: Date?, format: SimpleDateFormat = DEFAULT_SDF): String {
        return format.format(time)
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param time Date类型时间
     * @return 毫秒时间戳
     */
    @JvmStatic
    fun date2Milliseconds(time: Date): Long {
        return time.time
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param milliseconds 毫秒时间戳
     * @return Date类型时间
     */
    @JvmStatic
    fun milliseconds2Date(milliseconds: Long): Date {
        return Date(milliseconds)
    }

    /**
     * 毫秒时间戳单位转换（单位：unit）
     *
     * @param milliseconds 毫秒时间戳
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @return unit时间戳
     */
    @JvmStatic
    fun milliseconds2Unit(milliseconds: Long, unit: RxConstTool.TimeUnit?): Long {
        when (unit) {
            RxConstTool.TimeUnit.MSEC -> return milliseconds / MSEC
            RxConstTool.TimeUnit.SEC -> return milliseconds / SEC
            RxConstTool.TimeUnit.MIN -> return milliseconds / MIN
            RxConstTool.TimeUnit.HOUR -> return milliseconds / HOUR
            RxConstTool.TimeUnit.DAY -> return milliseconds / DAY
            else -> {
            }
        }
        return -1
    }

    /**
     * 获取两个时间差（单位：unit）
     *
     * time1和time2格式都为yyyy-MM-dd HH:mm:ss
     *
     * @param time0 时间字符串1
     * @param time1 时间字符串2
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalTime(time0: String?, time1: String?, unit: RxConstTool.TimeUnit?): Long {
        return getIntervalTime(time0, time1, unit, DEFAULT_SDF)
    }

    /**
     * 获取两个时间差（单位：unit）
     *
     * time1和time2格式都为format
     *
     * @param time0  时间字符串1
     * @param time1  时间字符串2
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @param format 时间格式
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalTime(time0: String?, time1: String?, unit: RxConstTool.TimeUnit?, format: SimpleDateFormat): Long {
        return Math.abs(milliseconds2Unit(string2Milliseconds(time0, format)
                - string2Milliseconds(time1, format), unit))
    }

    /**
     * 获取两个时间差（单位：unit）
     *
     * time1和time2都为Date类型
     *
     * @param time1 Date类型时间1
     * @param time2 Date类型时间2
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalTime(time1: Date, time2: Date, unit: RxConstTool.TimeUnit?): Long {
        return Math.abs(milliseconds2Unit(date2Milliseconds(time2)
                - date2Milliseconds(time1), unit))
    }

    /**
     * 获取当前时间
     *
     * @return 毫秒时间戳
     */
    val curTimeMills: Long
        get() = System.currentTimeMillis()

    /**
     * 获取当前时间
     *
     * 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 时间字符串
     */
    @JvmStatic
    val curTimeString: String
        get() = date2String(Date())

    /**
     * 获取当前时间
     *
     * 格式为用户自定义
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    @JvmStatic
    fun getCurTimeString(format: SimpleDateFormat): String {
        return date2String(Date(), format)
    }

    /**
     * 获取当前时间
     *
     * Date类型
     *
     * @return Date类型时间
     */
    val curTimeDate: Date
        get() = Date()

    /**
     * 获取与当前时间的差（单位：unit）
     *
     * time格式为yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间字符串
     * @param unit
     *  * [TimeUnit.MSEC]:毫秒
     *  * [TimeUnit.SEC]:秒
     *  * [TimeUnit.MIN]:分
     *  * [TimeUnit.HOUR]:小时
     *  * [TimeUnit.DAY]:天
     *
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalByNow(time: String?, unit: RxConstTool.TimeUnit?): Long {
        return getIntervalByNow(time, unit, DEFAULT_SDF)
    }

    /**
     * 获取与当前时间的差（单位：unit）
     *
     * time格式为format
     *
     * @param time   时间字符串
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @param format 时间格式
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalByNow(time: String?, unit: RxConstTool.TimeUnit?, format: SimpleDateFormat): Long {
        return getIntervalTime(curTimeString, time, unit, format)
    }

    /**
     * 获取与当前时间的差（单位：unit）
     *
     * time为Date类型
     *
     * @param time Date类型时间
     * @param unit
     *  * [TimeUnit.MSEC]: 毫秒
     *  * [TimeUnit.SEC]: 秒
     *  * [TimeUnit.MIN]: 分
     *  * [TimeUnit.HOUR]: 小时
     *  * [TimeUnit.DAY]: 天
     *
     * @return unit时间戳
     */
    @JvmStatic
    fun getIntervalByNow(time: Date, unit: RxConstTool.TimeUnit?): Long {
        return getIntervalTime(curTimeDate, time, unit)
    }

    /**
     * 判断闰年
     *
     * @param year 年份
     * @return `true`: 闰年<br></br>
     * `false`: 平年
     */
    @JvmStatic
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 将date转换成format格式的日期
     *
     * @param format 格式
     * @param date   日期
     * @return
     */
    @JvmStatic
    fun simpleDateFormat(format: String?, date: Date?): String {
        var format = format
        if (isNullString(format)) {
            format = RxConstants.DATE_FORMAT_DETACH_SSS
        }
        return SimpleDateFormat(format).format(date)
    }
    //--------------------------------------------字符串转换成时间戳-----------------------------------
    /**
     * 将指定格式的日期转换成时间戳
     *
     * @param mDate
     * @return
     */
    @JvmStatic
    fun Date2Timestamp(mDate: Date?): String {
        return mDate!!.time.toString().substring(0, 10)
    }

    /**
     * 将日期字符串 按照 指定的格式 转换成 DATE
     * 转换失败时 return null;
     *
     * @param format
     * @param datess
     * @return
     */
    @JvmStatic
    fun string2Date(format: String?, datess: String?): Date? {
        val sdr = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = sdr.parse(datess)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 将 yyyy年MM月dd日 转换成 时间戳
     *
     * @param format
     * @param datess
     * @return
     */
    @JvmStatic
    fun string2Timestamp(format: String?, datess: String?): String {
        val date = string2Date(format, datess)
        return Date2Timestamp(date)
    }
    //===========================================字符串转换成时间戳====================================
    /**
     * 获取当前日期时间 / 得到今天的日期
     * str yyyy-MM-dd HH:mm:ss 之类的
     *
     * @return
     */
    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(format: String?): String {
        return simpleDateFormat(format, Date())
    }

    /**
     * 时间戳  转换成 指定格式的日期
     * 如果format为空，则默认格式为
     *
     * @param times  时间戳
     * @param format 日期格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getDate(times: String?, format: String?): String {
        return simpleDateFormat(format, Date(stringToInt(times!!) * 1000L))
    }

    /**
     * 得到昨天的日期
     *
     * @param format 日期格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    @JvmStatic
    fun getYestoryDate(format: String?): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return simpleDateFormat(format, calendar.time)
    }

    /**
     * 视频时间 转换成 "mm:ss"
     *
     * @param milliseconds
     * @return
     */
    @JvmStatic
    fun formatTime(milliseconds: Long): String {
        val format = RxConstants.DATE_FORMAT_MM_SS
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("GMT+0")
        return sdf.format(milliseconds)
    }

    /**
     * "mm:ss" 转换成 视频时间
     *
     * @param time
     * @return
     */
    @JvmStatic
    fun formatSeconds(time: String?): Long {
        val format = RxConstants.DATE_FORMAT_MM_SS
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("GMT+0")
        val date: Date
        var times: Long = 0
        try {
            date = sdf.parse(time)
            val l = date.time
            times = l
            TLog.d("时间戳", times.toString() + "")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return times
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    @JvmStatic
    fun getDaysByYearMonth(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a[Calendar.YEAR] = year
        a[Calendar.MONTH] = month - 1
        a[Calendar.DATE] = 1
        a.roll(Calendar.DATE, -1)
        return a[Calendar.DATE]
    }

    /**
     * 判断当前日期是星期几
     *
     * @param strDate 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常<br></br>
     */
    @Throws(Exception::class)
    @JvmStatic
    fun stringForWeek(strDate: String?): Int {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = format.parse(strDate)
        return if (c[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
            7
        } else {
            c[Calendar.DAY_OF_WEEK] - 1
        }
    }

    /**
     * 判断当前日期是星期几
     *
     * @param strDate 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常<br></br>
     */
    @Throws(Exception::class)
    @JvmStatic
    fun stringForWeek(strDate: String?, simpleDateFormat: SimpleDateFormat): Int {
        val c = Calendar.getInstance()
        c.time = simpleDateFormat.parse(strDate)
        return if (c[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
            7
        } else {
            c[Calendar.DAY_OF_WEEK] - 1
        }
    }
}