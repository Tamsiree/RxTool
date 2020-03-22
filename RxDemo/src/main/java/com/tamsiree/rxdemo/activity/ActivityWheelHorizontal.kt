package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxRulerWheelView
import com.tamsiree.rxui.view.RxRulerWheelView.OnWheelItemSelectedListener
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.wheelhorizontal.AbstractWheel
import com.tamsiree.rxui.view.wheelhorizontal.ArrayWheelAdapter
import com.tamsiree.rxui.view.wheelhorizontal.OnWheelScrollListener
import com.tamsiree.rxui.view.wheelhorizontal.WheelHorizontalView
import java.util.*

/**
 * @author tamsiree
 */
class ActivityWheelHorizontal : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.wheelView_year_month)
    var mWheelViewYearMonth: WheelHorizontalView? = null

    @JvmField
    @BindView(R.id.imageView1)
    var mImageView1: ImageView? = null

    @JvmField
    @BindView(R.id.LinearLayout2)
    var mLinearLayout2: LinearLayout? = null

    @JvmField
    @BindView(R.id.wheelview)
    var mWheelview: RxRulerWheelView? = null

    @JvmField
    @BindView(R.id.wheelview2)
    var mWheelview2: RxRulerWheelView? = null

    @JvmField
    @BindView(R.id.wheelview3)
    var mWheelview3: RxRulerWheelView? = null

    @JvmField
    @BindView(R.id.wheelview4)
    var mWheelview4: RxRulerWheelView? = null

    @JvmField
    @BindView(R.id.wheelview5)
    var mWheelview5: RxRulerWheelView? = null

    @JvmField
    @BindView(R.id.changed_tv)
    var mChangedTv: TextView? = null

    @JvmField
    @BindView(R.id.selected_tv)
    var mSelectedTv: TextView? = null

    @JvmField
    @BindView(R.id.LinearLayout1)
    var mLinearLayout1: LinearLayout? = null
    private val listYearMonth: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_wheel_horizontal)
        ButterKnife.bind(this)
        setPortrait(this)
    }

    private fun initRulerView() {
        mWheelview = findViewById(R.id.wheelview)
        mWheelview2 = findViewById(R.id.wheelview2)
        mWheelview3 = findViewById(R.id.wheelview3)
        mWheelview4 = findViewById(R.id.wheelview4)
        mWheelview5 = findViewById(R.id.wheelview5)
        mSelectedTv = findViewById(R.id.selected_tv)
        mChangedTv = findViewById(R.id.changed_tv)
        val items: MutableList<String> = ArrayList()
        for (i in 1..40) {
            items.add((i * 1000).toString())
        }
        mWheelview?.items = items
        mWheelview?.selectIndex(8)
        mWheelview?.setAdditionCenterMark("元")
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
        mWheelview2?.items = items2
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
        mWheelview3?.items = items3
        mWheelview3?.setAdditionCenterMark("m")

//		mWheelview4.setItems(items);
//		mWheelview4.setEnabled(false);
        mWheelview5?.items = items
        mWheelview5?.minSelectableIndex = 3
        mWheelview5?.maxSelectableIndex = items.size - 3
        items.removeAt(items.size - 1)
        items.removeAt(items.size - 2)
        items.removeAt(items.size - 3)
        items.removeAt(items.size - 4)
        mSelectedTv?.text = String.format("onWheelItemSelected：%1\$s", "")
        mChangedTv?.text = String.format("onWheelItemChanged：%1\$s", "")
        mWheelview5?.setOnWheelItemSelectedListener(object : OnWheelItemSelectedListener {
            override fun onWheelItemSelected(wheelView: RxRulerWheelView, position: Int) {
                mSelectedTv?.text = String.format("onWheelItemSelected：%1\$s", wheelView.items[position])
            }

            override fun onWheelItemChanged(wheelView: RxRulerWheelView, position: Int) {
                mChangedTv?.text = String.format("onWheelItemChanged：%1\$s", wheelView.items[position])
            }
        })
        mWheelview4?.postDelayed(Runnable { mWheelview4?.items = items }, 3000)
    }

    override fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
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
        mWheelViewYearMonth!!.viewAdapter = ampmAdapter
        // set current time
        mWheelViewYearMonth!!.currentItem = CurrentIndex
        mWheelViewYearMonth!!.addScrollingListener(object : OnWheelScrollListener {
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
        mWheelViewYearMonth!!.addClickingListener { wheel, itemIndex ->
            Log.v("addScrollingListener", "listYearMonth:" + listYearMonth[itemIndex])
            mWheelViewYearMonth!!.setCurrentItem(itemIndex, true)
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