package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard.Companion.getInstance
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_tsection_tablayout.*
import java.util.*

class ActivityTSectionTabLayout : ActivityBase() {

    private val mFragments = ArrayList<Fragment>()
    private val mFragments2 = ArrayList<Fragment>()
    private val mTitles = arrayOf("首页", "消息")
    private val mTitles_2 = arrayOf("首页", "消息", "联系人")
    private val mTitles_3 = arrayOf("首页", "消息", "联系人", "更多")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tsection_tablayout)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        for (title in mTitles_3) {
            mFragments.add(getInstance("Switch ViewPager $title"))
        }
        for (title in mTitles_2) {
            mFragments2.add(getInstance("Switch Fragment $title"))
        }

        tglidetablayout_1.setTabData(mTitles)
        tglidetablayout_2.setTabData(mTitles_2)
        tl_3()
        tglidetablayout_4.setTabData(mTitles_2, this, R.id.fl_change, mFragments2)
        tglidetablayout_5.setTabData(mTitles_3)

        //显示未读红点
        tglidetablayout_1.showDot(2)
        tglidetablayout_2.showDot(2)
        tglidetablayout_3!!.showDot(1)
        tglidetablayout_4.showDot(1)

        //设置未读消息红点
        tglidetablayout_3!!.showDot(2)
        val rtv_3_2 = tglidetablayout_3!!.getMsgView(2)
        if (rtv_3_2 != null) {
            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"))
        }
    }

    override fun initData() {

    }

    private fun tl_3() {
        viewpage_2.adapter = MyPagerAdapter(supportFragmentManager)
        tglidetablayout_3!!.setTabData(mTitles_3)
        tglidetablayout_3!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewpage_2.currentItem = position
            }

            override fun onTabReselect(position: Int) {}
        })
        viewpage_2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                tglidetablayout_3!!.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewpage_2.currentItem = 1
    }

    private inner class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles_3[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }
    }
}