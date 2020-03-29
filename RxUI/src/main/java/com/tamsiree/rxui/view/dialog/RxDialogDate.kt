package com.tamsiree.rxui.view.dialog

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.tamsiree.rxkit.RxTimeTool.getDaysByYearMonth
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.dialog.wheel.NumericWheelAdapter
import com.tamsiree.rxui.view.dialog.wheel.OnWheelChangedListener
import com.tamsiree.rxui.view.dialog.wheel.WheelView
import java.util.*
import kotlin.math.min

/**
 * @author tamsiree
 */
class RxDialogDate : RxDialog {
    private var mDateFormat = DateFormat.年月日

    lateinit var yearView: WheelView
        private set
    lateinit var monthView: WheelView
        private set
    lateinit var dayView: WheelView
        private set
    var curYear = 0
        private set
    var curMonth = 0
        private set
    var curDay = 0
        private set
    lateinit var sureView: TextView
        private set
    lateinit var cancleView: TextView
        private set
    lateinit var checkBoxDay: CheckBox
        private set
    private lateinit var mCalendar: Calendar
    private var llType: LinearLayout? = null
    private val months = arrayOf("01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12")
    private val days = arrayOf("01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    var beginYear = 0
        private set
    var endYear = 0
        private set
    val divideYear = endYear - beginYear

    constructor(mContext: Context?) : super(mContext!!) {
        this.mContext = mContext
        build()
    }

    constructor(mContext: Context?, beginYear: Int) : super(mContext!!) {
        this.mContext = mContext
        this.beginYear = beginYear
        build()
    }

    constructor(mContext: Context?, beginYear: Int, endYear: Int) : super(mContext!!) {
        this.mContext = mContext
        this.beginYear = beginYear
        this.endYear = endYear
        build()
    }

    constructor(mContext: Context?, dateFormat: DateFormat) : super(mContext!!) {
        this.mContext = mContext
        build()
        mDateFormat = dateFormat
        setDateFormat(mDateFormat)
    }

    val dateCN: String
        get() {
            val dateCN: String
            dateCN = when (mDateFormat) {
                DateFormat.年月 -> selectorYear.toString() + "年" + selectorMonth + "月"
                DateFormat.年月日 -> selectorYear.toString() + "年" + selectorMonth + "月" + selectorDay + "日"
                else -> selectorYear.toString() + "年" + selectorMonth + "月" + selectorDay + "日"
            }
            return dateCN
        }

    val dateEN: String
        get() {
            val dateCN: String
            dateCN = when (mDateFormat) {
                DateFormat.年月 -> curYear.toString() + "-" + months[curMonth]
                DateFormat.年月日 -> curYear.toString() + "-" + months[curMonth] + "-" + days[curDay]
                else -> curYear.toString() + "-" + months[curMonth] + "-" + days[curDay]
            }
            return dateCN
        }

    fun setDateFormat(dateFormat: DateFormat?) {
        when (dateFormat) {
            DateFormat.年月 -> {
                llType!!.visibility = View.VISIBLE
                checkBoxDay.isChecked = false
            }
            DateFormat.年月日 -> {
                llType!!.visibility = View.INVISIBLE
                checkBoxDay.isChecked = true
            }
            else -> {
                llType!!.visibility = View.INVISIBLE
                checkBoxDay.isChecked = true
            }
        }
    }

    private fun build() {
        mCalendar = Calendar.getInstance()
        val dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_year_month_day, null)
        val listener: OnWheelChangedListener = object : OnWheelChangedListener {
            override fun onChanged(wheel: WheelView, oldValue: Int, newValue: Int) {
                updateDays(yearView, monthView, dayView)
            }
        }
        curYear = mCalendar.get(Calendar.YEAR)
        if (beginYear == 0) {
            beginYear = curYear - 150
        }
        if (endYear == 0) {
            endYear = curYear
        }
        if (beginYear > endYear) {
            endYear = beginYear
        }

        //mYearView
        yearView = dialogView1.findViewById(R.id.wheelView_year)
        yearView.setBackgroundResource(R.drawable.transparent_bg)
        yearView.setWheelBackground(R.drawable.transparent_bg)
        yearView.setWheelForeground(R.drawable.wheel_val_holo)
        yearView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)
        yearView.setViewAdapter0(DateNumericAdapter(mContext, beginYear, endYear, endYear - beginYear))
        yearView.currentItem = endYear - beginYear
        yearView.addChangingListener(listener)


        // mMonthView
        monthView = dialogView1.findViewById(R.id.wheelView_month)
        monthView.setBackgroundResource(R.drawable.transparent_bg)
        monthView.setWheelBackground(R.drawable.transparent_bg)
        monthView.setWheelForeground(R.drawable.wheel_val_holo)
        monthView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)
        curMonth = mCalendar.get(Calendar.MONTH)
        monthView.setViewAdapter0(DateNumericAdapter(mContext, months[0].toInt(), months[months.size - 1].toInt(), months[curMonth].toInt()))
        monthView.currentItem = curMonth
        monthView.addChangingListener(listener)


        //mDayView
        dayView = dialogView1.findViewById(R.id.wheelView_day)
        updateDays(yearView, monthView, dayView)
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        dayView.currentItem = curDay - 1
        dayView.setBackgroundResource(R.drawable.transparent_bg)
        dayView.setWheelBackground(R.drawable.transparent_bg)
        dayView.setWheelForeground(R.drawable.wheel_val_holo)
        dayView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)
        sureView = dialogView1.findViewById(R.id.tv_sure)
        cancleView = dialogView1.findViewById(R.id.tv_cancel)
        llType = dialogView1.findViewById(R.id.ll_month_type)
        checkBoxDay = dialogView1.findViewById(R.id.checkBox_day)
        checkBoxDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dayView.visibility = View.VISIBLE
            } else {
                dayView.visibility = View.GONE
            }
        }
        layoutParams!!.gravity = Gravity.CENTER
        setContentView(dialogView1)
    }

    enum class DateFormat {
        年月, 年月日
    }

    val selectorYear: Int
        get() = beginYear + yearView.currentItem

    val selectorMonth: String
        get() = months[monthView.currentItem]

    val selectorDay: String
        get() = days[dayView.currentItem]

    fun setOnSureClick(l: View.OnClickListener?) {
        sureView.setOnClickListener(l)
    }

    fun setOnCancelClick(l: View.OnClickListener?) {
        cancleView.setOnClickListener(l)
    }

    /**
     * Updates mDayView wheel. Sets max mDays according to selected mMonthView and mYearView
     */
    private fun updateDays(year: WheelView?, month: WheelView?, day: WheelView?) {
        mCalendar[Calendar.YEAR] = beginYear + year?.currentItem!!
        mCalendar[Calendar.MONTH] = month!!.currentItem
        val maxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val maxDays = getDaysByYearMonth(beginYear + year.currentItem, month.currentItem + 1)
        day?.setViewAdapter0(DateNumericAdapter(mContext, 1, maxDays, mCalendar[Calendar.DAY_OF_MONTH] - 1))
        val curDay = min(maxDays, day?.currentItem!! + 1)
        day.setCurrentItem(curDay - 1, true)
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private inner class DateNumericAdapter(mContext: Context?, minValue: Int, maxValue: Int, // Index of item to be highlighted
                                           var currentValue: Int) : NumericWheelAdapter(mContext!!, minValue, maxValue) {
        // Index of current item
        var currentItem = 0

        override fun configureTextView(view: TextView) {
            super.configureTextView(view)
            /*if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
			}*/view.typeface = Typeface.SANS_SERIF
        }

        override fun getItem(index: Int, convertView: View?, parent: ViewGroup?): View? {
            currentItem = index
            return super.getItem(index, convertView, parent)
        }

        /**
         * Constructor
         */
        init {
            textSize = 16
        }
    }
}