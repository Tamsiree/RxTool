package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.adapter.AdapterFVP
import com.tamsiree.rxui.fragment.FragmentLazy
import com.tamsiree.rxui.model.ModelFVP
import kotlinx.android.synthetic.main.fragment_demo_type.*
import java.util.*

class FragmentDemoType : FragmentLazy() {

    private val modelFVPList: MutableList<ModelFVP> = ArrayList()

    override fun initViews(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_demo_type, viewGroup, false)
        return view
    }

    override fun initData() {

        if (modelFVPList.isEmpty()) {
            modelFVPList.add(ModelFVP("功能区", FragmentDemo.newInstance(0)))
            modelFVPList.add(ModelFVP("特效区", FragmentDemo.newInstance(1)))
        }

        view_pager.adapter = AdapterFVP(childFragmentManager, modelFVPList)
        tab_layout.setViewPager(view_pager)
    }

    companion object {
        fun newInstance(): FragmentDemoType {
            return FragmentDemoType()
        }
    }
}