package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentLoadingDemo
import com.tamsiree.rxdemo.fragment.FragmentLoadingWay
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_loading.*

/**
 * @author tamsiree
 */
class ActivityLoading : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            var titles = arrayOf(
                    "加载的方式", "加载的例子"
            )

            override fun getItem(position: Int): Fragment {
                return if (position == 0) {
                    FragmentLoadingWay.newInstance()
                } else {
                    FragmentLoadingDemo.newInstance()
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }
        tabs.setupWithViewPager(viewpager)
    }

    override fun initData() {

    }
}