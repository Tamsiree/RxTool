package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard.Companion.getInstance
import com.tamsiree.rxdemo.tools.ViewFindTool.find
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.tablayout.TSectionTabLayout
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import java.util.*

class ActivityTSectionTabLayout : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    private val mFragments = ArrayList<Fragment>()
    private val mFragments2 = ArrayList<Fragment>()
    private val mTitles = arrayOf("首页", "消息")
    private val mTitles_2 = arrayOf("首页", "消息", "联系人")
    private val mTitles_3 = arrayOf("首页", "消息", "联系人", "更多")
    private var mDecorView: View? = null
    private var mTabLayout_3: TSectionTabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tsection_tablayout)
        ButterKnife.bind(this)
        mRxTitle!!.setLeftFinish(this)
        for (title in mTitles_3) {
            mFragments.add(getInstance("Switch ViewPager $title"))
        }
        for (title in mTitles_2) {
            mFragments2.add(getInstance("Switch Fragment $title"))
        }
        mDecorView = window.decorView
        val tabLayout_1 = find<TSectionTabLayout>(mDecorView!!, R.id.tl_1)
        val tabLayout_2 = find<TSectionTabLayout>(mDecorView!!, R.id.tl_2)
        mTabLayout_3 = find<TSectionTabLayout>(mDecorView!!, R.id.tl_3)
        val tabLayout_4 = find<TSectionTabLayout>(mDecorView!!, R.id.tl_4)
        val tabLayout_5 = find<TSectionTabLayout>(mDecorView!!, R.id.tl_5)
        tabLayout_1.setTabData(mTitles)
        tabLayout_2.setTabData(mTitles_2)
        tl_3()
        tabLayout_4.setTabData(mTitles_2, this, R.id.fl_change, mFragments2)
        tabLayout_5.setTabData(mTitles_3)

        //显示未读红点
        tabLayout_1.showDot(2)
        tabLayout_2.showDot(2)
        mTabLayout_3!!.showDot(1)
        tabLayout_4.showDot(1)

        //设置未读消息红点
        mTabLayout_3!!.showDot(2)
        val rtv_3_2 = mTabLayout_3!!.getMsgView(2)
        if (rtv_3_2 != null) {
            rtv_3_2.backgroundColor = Color.parseColor("#6D8FB0")
        }
    }

    private fun tl_3() {
        val vp_3 = find<ViewPager>(mDecorView!!, R.id.vp_2)
        vp_3.adapter = MyPagerAdapter(supportFragmentManager)
        mTabLayout_3!!.setTabData(mTitles_3)
        mTabLayout_3!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                vp_3.currentItem = position
            }

            override fun onTabReselect(position: Int) {}
        })
        vp_3.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                mTabLayout_3!!.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        vp_3.currentItem = 1
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