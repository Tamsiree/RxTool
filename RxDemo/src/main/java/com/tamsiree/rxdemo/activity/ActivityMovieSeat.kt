package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxSeatMovie
import com.tamsiree.rxui.view.RxSeatMovie.SeatChecker
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityMovieSeat : ActivityBase() {
    @JvmField
    @BindView(R.id.seatView)
    var mSeatView: RxSeatMovie? = null

    @JvmField
    @BindView(R.id.activity_movie_seat)
    var mActivityMovieSeat: LinearLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_movie_seat)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    protected fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        mSeatView!!.setScreenName("3号厅荧幕") //设置屏幕名称
        mSeatView!!.setMaxSelected(8) //设置最多选中
        mSeatView!!.setSeatChecker(object : SeatChecker {
            override fun isValidSeat(row: Int, column: Int): Boolean {
                return !(column == 2 || column == 12)
            }

            override fun isSold(row: Int, column: Int): Boolean {
                return row == 6 && column == 6
            }

            override fun checked(row: Int, column: Int) {}
            override fun unCheck(row: Int, column: Int) {}
            override fun checkedSeatTxt(row: Int, column: Int): Array<String>? {
                return null
            }
        })
        mSeatView!!.setData(10, 15)
    }
}