package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentDemoDataBankCard
import com.tamsiree.rxdemo.fragment.FragmentDemoDataPersonInfo
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.adapter.AdapterFVP
import com.tamsiree.rxui.model.ModelFVP
import kotlinx.android.synthetic.main.activity_demo_data.*
import java.util.*

class ActivityDemoData : ActivityBase() {

    private val modelFVPList: MutableList<ModelFVP> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_data)
    }

    override fun initView() {
        rxTitle.setLeftFinish(mContext)

        if (modelFVPList.isEmpty()) {
            modelFVPList.add(ModelFVP("银行卡", FragmentDemoDataBankCard.newInstance()))
            modelFVPList.add(ModelFVP("个人信息", FragmentDemoDataPersonInfo.newInstance()))
        }

        viewPager.adapter = AdapterFVP(supportFragmentManager, modelFVPList)
        tabLayout.setViewPager(viewPager)

    }

    override fun initData() {

    }

}
