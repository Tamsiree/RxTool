package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.util.Log
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxRulerWheelView
import com.tamsiree.rxui.view.RxRulerWheelView.OnWheelItemSelectedListener
import com.tamsiree.rxui.view.wheelhorizontal.AbstractWheel
import com.tamsiree.rxui.view.wheelhorizontal.ArrayWheelAdapter
import com.tamsiree.rxui.view.wheelhorizontal.OnWheelScrollListener
import kotlinx.android.synthetic.main.activity_wheel_horizontal.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityWheelHorizontal : ActivityBase() {


    private val listYearMonth: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_wheel_horizontal)
        setPortrait(this)
    }

    private fun initRulerView() {

        val items: MutableList<String> = ArrayList()
        for (i in 1..40) {
            items.add((i * 1000).toString())
        }
        wheelview.items = items
        wheelview.selectIndex(8)
        wheelview.setAdditionCenterMark("元")
        val items2: MutableList<String> = ArrayList()
        items2.add("一月")
        items2.add("二月")
        items2.add("三月")
        items2.add("四月")
        items2.add("五月")
        items2.add("六月")
        items2.add("七月")
        items2.add("八月")
        items2.add("九月")
        items2.add("十月")
        items2.add("十一月")
        items2.add("十二月")
        wheelview2.items = items2
        val items3: MutableList<String> = ArrayList()
        items3.add("1")
        items3.add("2")
        items3.add("3")
        items3.add("5")
        items3.add("7")
        items3.add("11")
        items3.add("13")
        items3.add("17")
        items3.add("19")
        items3.add("23")
        items3.add("29")
        items3.add("31")
        wheelview3.items = items3
        wheelview3.setAdditionCenterMark("m")

//		mWheelview4.setItems(items);
//		mWheelview4.setEnabled(false);
        wheelview5.items = items
        wheelview5.minSelectableIndex = 3
        wheelview5.maxSelectableIndex = items.size - 3
        items.removeAt(items.size - 1)
        items.removeAt(items.size - 2)
        items.removeAt(items.size - 3)
        items.removeAt(items.size - 4)
        selected_tv.text = String.format("onWheelItemSelected：%1\$s", "")
        changed_tv.text = String.format("onWheelItemChanged：%1\$s", "")
        wheelview4.setOnWheelItemSelectedListener(object : OnWheelItemSelectedListener {
            override fun onWheelItemSelected(wheelView: RxRulerWheelView, position: Int) {
                selected_tv.text = String.format("onWheelItemSelected：%1\$s", wheelView.items[position])
            }

            override fun onWheelItemChanged(wheelView: RxRulerWheelView, position: Int) {
                changed_tv.text = String.format("onWheelItemChanged：%1\$s", wheelView.items[position])
            }
        })
        wheelview4.postDelayed(Runnable { wheelview4.items = items }, 3000)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        initRulerView()
    }

    override fun initData() {
        // TODO Auto-generated method stub
        listYearMonth.clear()
        val calendar = Calendar.getInstance(Locale.CHINA)
        for (i in calendar[Calendar.YEAR] - 3..calendar[Calendar.YEAR] + 2) {
            for (j in 1..12) {
                listYearMonth.add(i.toString() + "年" + j + "月")
            }
        }
        val arr = listYearMonth.toTypedArray()
        var CurrentIndex = 0
        for (i in arr.indices) {
            if (arr[i] == calendar[Calendar.YEAR].toString() + "年" + (calendar[Calendar.MONTH] + 1) + "月") {
                CurrentIndex = i
                break
            }
        }
        val ampmAdapter = ArrayWheelAdapter(
                this, arr)
        ampmAdapter.itemResource = R.layout.item_wheel_year_month
        ampmAdapter.itemTextResource = R.id.tv_year
        wheelView_year_month.viewAdapter = ampmAdapter
        // set current time
        wheelView_year_month.currentItem = CurrentIndex
        wheelView_year_month.addScrollingListener(object : OnWheelScrollListener {
            var before: String? = null
            var behind: String? = null
            override fun onScrollingStarted(wheel: AbstractWheel) {
                before = listYearMonth[wheel.currentItem]
            }

            override fun onScrollingFinished(wheel: AbstractWheel) {
                behind = listYearMonth[wheel.currentItem]
                Log.v("addScrollingListener", "listYearMonth:" + listYearMonth[wheel.currentItem])
                if (before != behind) {
                    val year = stringToInt(listYearMonth[wheel.currentItem].substring(0, 4))
                    val month = stringToInt(listYearMonth[wheel.currentItem].substring(5, 6))
                    //initBarChart(VonUtil.getDaysByYearMonth(year, month));
                }
            }
        })
        wheelView_year_month.addClickingListener { wheel, itemIndex ->
            Log.v("addScrollingListener", "listYearMonth:" + listYearMonth[itemIndex])
            wheelView_year_month.setCurrentItem(itemIndex, true)
            /*
                 * int year =
				 * VonUtil.StringToInt(listYearMonth.get(itemIndex)
				 * .substring(0, 4)); int month =
				 * VonUtil.StringToInt(listYearMonth
				 * .get(itemIndex).substring(5, 6));
				 * initBarChart(VonUtil.getDaysByYearMonth(year, month));
				 */
        }
    }
}