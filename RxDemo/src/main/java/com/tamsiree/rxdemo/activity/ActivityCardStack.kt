package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterStackTest
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxTool
import com.tamsiree.rxkit.interfaces.OnSimpleListener
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.cardstack.RxCardStackView.ItemExpendListener
import com.tamsiree.rxui.view.cardstack.tools.RxAdapterAllMoveDownAnimator
import com.tamsiree.rxui.view.cardstack.tools.RxAdapterUpDownAnimator
import com.tamsiree.rxui.view.cardstack.tools.RxAdapterUpDownStackAnimator
import kotlinx.android.synthetic.main.activity_card_stack.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityCardStack : ActivityBase(), ItemExpendListener {

    private var mTestStackAdapter: AdapterStackTest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_stack)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        stackview_main.itemExpendListener = this
        mTestStackAdapter = AdapterStackTest(this)
        stackview_main.setAdapter(mTestStackAdapter)
        RxTool.delayToDo(200, object : OnSimpleListener {
            override fun doSomething() {
                mTestStackAdapter!!.updateData(Arrays.asList(*TEST_DATAS))
            }
        })
    }

    override fun initData() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_down -> stackview_main.setRxAdapterAnimator(RxAdapterAllMoveDownAnimator(stackview_main))
            R.id.menu_up_down -> stackview_main.setRxAdapterAnimator(RxAdapterUpDownAnimator(stackview_main))
            R.id.menu_up_down_stack -> stackview_main.setRxAdapterAnimator(RxAdapterUpDownStackAnimator(stackview_main))
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPreClick(view: View?) {
        stackview_main.pre()
    }

    fun onNextClick(view: View?) {
        stackview_main.next()
    }

    override fun onItemExpend(expend: Boolean) {
        button_container.visibility = if (expend) View.VISIBLE else View.GONE
    }

    companion object {
        var TEST_DATAS = arrayOf(
                R.color.custom_progress_green_header,
                R.color.custom_progress_green_progress,
                R.color.background_content,
                R.color.custom_progress_orange_header,
                R.color.custom_progress_orange_progress,
                R.color.darkslategrey,
                R.color.forestgreen,
                R.color.custom_progress_blue_header,
                R.color.cadetblue,
                R.color.custom_progress_purple_header,
                R.color.mediumaquamarine,
                R.color.mediumseagreen,
                R.color.custom_progress_red_header,
                R.color.custom_progress_red_progress,
                R.color.coral,
                R.color.WARNING_COLOR,
                R.color.aqua,
                R.color.blue_shadow_50,
                R.color.cadetblue,
                R.color.custom_progress_red_progress_half,
                R.color.brown,
                R.color.brown1,
                R.color.brown2,
                R.color.brown3,
                R.color.orange,
                R.color.custom_progress_orange_progress_half
        )
    }
}