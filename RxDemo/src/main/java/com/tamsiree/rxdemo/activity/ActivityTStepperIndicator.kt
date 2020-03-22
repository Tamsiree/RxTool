package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.PagerAdapter
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.indicator.TStepperIndicator
import kotlinx.android.synthetic.main.activity_tstepper_indicator.*

class ActivityTStepperIndicator : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tstepper_indicator)


    }

    override fun initView() {
        rx_title.setLeftFinish(this)

        pager.adapter = PagerAdapter(supportFragmentManager)

        val indicator: TStepperIndicator = findViewById(R.id.stepper_indicator)
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, true)

        indicator.addOnStepClickListener { step -> pager.setCurrentItem(step, true) }

    }

    override fun initData() {

    }

}
