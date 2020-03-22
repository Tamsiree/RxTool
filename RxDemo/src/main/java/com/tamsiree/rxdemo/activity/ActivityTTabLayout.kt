package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard.Companion.getInstance
import com.tamsiree.rxdemo.model.TabModel
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel
import com.tamsiree.rxui.view.tablayout.tool.TLayoutMsgTool
import kotlinx.android.synthetic.main.activity_ttab_layout.*
import java.util.*

class ActivityTTabLayout : ActivityBase() {
    var mRandom = Random()

    private val mFragments = ArrayList<Fragment>()
    private val mFragments2 = ArrayList<Fragment>()
    private val mTitles = arrayOf("首页", "消息", "联系人", "更多")
    private val mIconUnselectIds = intArrayOf(
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect)
    private val mIconSelectIds = intArrayOf(
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select)
    private val mTabEntities = ArrayList<TabLayoutModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ttab_layout)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        for (title in mTitles) {
            mFragments.add(getInstance("Switch ViewPager $title"))
            mFragments2.add(getInstance("Switch Fragment $title"))
        }
        for (i in mTitles.indices) {
            mTabEntities.add(TabModel(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }

        viewpage_2.adapter = MyPagerAdapter(supportFragmentManager)


        ttablayout_1.setTabData(mTabEntities)
        ttablayout_2()
        ttablayout_3.setTabData(mTabEntities, this, R.id.fl_change, mFragments2)
        ttablayout_4.setTabData(mTabEntities)
        ttablayout_5.setTabData(mTabEntities)
        ttablayout_6.setTabData(mTabEntities)
        ttablayout_7.setTabData(mTabEntities)
        ttablayout_8.setTabData(mTabEntities)
        ttablayout_3.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                ttablayout_1.currentTab = position
                ttablayout_2.currentTab = position
                ttablayout_4.currentTab = position
                ttablayout_5.currentTab = position
                ttablayout_6.currentTab = position
                ttablayout_7.currentTab = position
                ttablayout_8.currentTab = position
            }

            override fun onTabReselect(position: Int) {}
        })
        ttablayout_8.currentTab = 2
        ttablayout_3.currentTab = 1

        //显示未读红点
        ttablayout_1.showDot(2)
        ttablayout_3.showDot(1)
        ttablayout_4.showDot(1)

        //两位数
        ttablayout_2.showMsg(0, 55)
        ttablayout_2.setMsgMargin(0, -5f, 5f)

        //三位数
        ttablayout_2.showMsg(1, 100)
        ttablayout_2.setMsgMargin(1, -5f, 5f)

        //设置未读消息红点
        ttablayout_2.showDot(2)
        val rtv_2_2 = ttablayout_2.getMsgView(2)
        if (rtv_2_2 != null) {
            TLayoutMsgTool.setSize(rtv_2_2, RxImageTool.dp2px(7.5f))
        }

        //设置未读消息背景
        ttablayout_2.showMsg(3, 5)
        ttablayout_2.setMsgMargin(3, 0f, 5f)
        val rtv_2_3 = ttablayout_2.getMsgView(3)
        if (rtv_2_3 != null) {
            rtv_2_3.backgroundColor = Color.parseColor("#6D8FB0")
        }
    }

    override fun initData() {

    }

    private fun ttablayout_2() {
        ttablayout_2.setTabData(mTabEntities)
        ttablayout_2.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewpage_2.currentItem = position
            }

            override fun onTabReselect(position: Int) {
                if (position == 0) {
                    ttablayout_2.showMsg(0, mRandom.nextInt(100) + 1)
                    //                    UnreadMsgTool.show(ttablayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        })
        viewpage_2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                ttablayout_2.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewpage_2.currentItem = 1
    }

    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }
    }
}