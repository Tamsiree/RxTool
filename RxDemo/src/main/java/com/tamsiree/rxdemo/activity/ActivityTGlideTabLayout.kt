package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard.Companion.getInstance
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_tglide_tablayout.*
import java.util.*

class ActivityTGlideTabLayout : ActivityBase(), OnTabSelectListener {

    private val mTitles = arrayOf(
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    )

    private val mFragments = ArrayList<Fragment>()
    private var mAdapter: MyPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tglide_tablayout)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        for (title in mTitles) {
            mFragments.add(getInstance(title))
        }
        val decorView = window.decorView

        mAdapter = MyPagerAdapter(supportFragmentManager)
        viewpage.adapter = mAdapter

        tglidetablayout_1.setViewPager(viewpage)
        tglidetablayout_2.setViewPager(viewpage)
        tglidetablayout_2.setOnTabSelectListener(this)
        tglidetablayout_3.setViewPager(viewpage)
        tglidetablayout_4.setViewPager(viewpage)
        tglidetablayout_5.setViewPager(viewpage)
        tglidetablayout_6.setViewPager(viewpage)
        tglidetablayout_7.setViewPager(viewpage, mTitles)
        tglidetablayout_8.setViewPager(viewpage, mTitles, this, mFragments)
        tglidetablayout_9.setViewPager(viewpage)
        tglidetablayout_10.setViewPager(viewpage)
        viewpage.currentItem = 4
        tglidetablayout_1.showDot(4)
        tglidetablayout_3.showDot(4)
        tglidetablayout_2.showDot(4)
        tglidetablayout_2.showMsg(3, 5)
        tglidetablayout_2.setMsgMargin(3, 0f, 10f)
        val rtv_2_3 = tglidetablayout_2.getTMsgView(3)
        if (rtv_2_3 != null) {
            rtv_2_3.backgroundColor = Color.parseColor("#6D8FB0")
        }
        tglidetablayout_2.showMsg(5, 5)
        tglidetablayout_2.setMsgMargin(5, 0f, 10f)

//        tabLayout_7.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//                mFragments.add(FragmentSimpleCard.getInstance("后端"));
//                mAdapter.notifyDataSetChanged();
//                tabLayout_7.addNewTab("后端");
//            }
//        });
    }

    override fun initData() {

    }

    override fun onTabSelect(position: Int) {
        Toast.makeText(mContext, "onTabSelect&position--->$position", Toast.LENGTH_SHORT).show()
    }

    override fun onTabReselect(position: Int) {
        Toast.makeText(mContext, "onTabReselect&position--->$position", Toast.LENGTH_SHORT).show()
    }

    private inner class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
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