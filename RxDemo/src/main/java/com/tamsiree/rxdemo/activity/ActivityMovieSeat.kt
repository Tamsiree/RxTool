package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxSeatMovie.SeatChecker
import kotlinx.android.synthetic.main.activity_movie_seat.*

/**
 * @author tamsiree
 */
class ActivityMovieSeat : ActivityBase() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_movie_seat)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        seatView.setScreenName("3号厅荧幕") //设置屏幕名称
        seatView.setMaxSelected(8) //设置最多选中
        seatView.setSeatChecker(object : SeatChecker {
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
        seatView.setData(10, 15)
    }

    override fun initData() {

    }
}