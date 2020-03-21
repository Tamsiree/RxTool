package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterFragmentViewPager
import com.tamsiree.rxui.fragment.FragmentLazy
import kotlinx.android.synthetic.main.fragment_demo_type.*
import java.util.*

class FragmentDemoType : FragmentLazy() {

    private val demoType: MutableList<String> = ArrayList()
    private val mFragments: MutableList<Fragment> = ArrayList()
    override fun initViews(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_demo_type, viewGroup, false)
//        ButterKnife.bind(this, view)
        return view
    }

    override fun initData() {
        demoType.clear()
        /**
         * Demo的类别
         * 0：功能区
         * 1：特效区
         */
        demoType.add("功能区")
        demoType.add("特效区")
        mFragments.clear()
        for (i in demoType.indices) {
            mFragments.add(FragmentDemo.newInstance(i))
        }
        view_pager!!.adapter = AdapterFragmentViewPager(childFragmentManager, demoType.toTypedArray(), mFragments)
        tab_layout!!.setViewPager(view_pager)
    }

    companion object {
        fun newInstance(): FragmentDemoType {
            return FragmentDemoType()
        }
    }
}