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
import com.tamsiree.rxdemo.model.TabModel
import com.tamsiree.rxdemo.tools.ViewFindTool.find
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.tablayout.TTabLayout
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel
import com.tamsiree.rxui.view.tablayout.tool.TLayoutMsgTool
import java.util.*

class ActivityTTabLayout : ActivityBase() {
    var mRandom = Random()

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

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
    private var mDecorView: View? = null
    private var mViewPager: ViewPager? = null
    private var mTabLayout_1: TTabLayout? = null
    private var mTabLayout_2: TTabLayout? = null
    private var mTabLayout_3: TTabLayout? = null
    private var mTabLayout_4: TTabLayout? = null
    private var mTabLayout_5: TTabLayout? = null
    private var mTabLayout_6: TTabLayout? = null
    private var mTabLayout_7: TTabLayout? = null
    private var mTabLayout_8: TTabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ttab_layout)
        ButterKnife.bind(this)

    }

    override fun initView() {
        mRxTitle!!.setLeftFinish(this)
        for (title in mTitles) {
            mFragments.add(getInstance("Switch ViewPager $title"))
            mFragments2.add(getInstance("Switch Fragment $title"))
        }
        for (i in mTitles.indices) {
            mTabEntities.add(TabModel(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        mDecorView = window.decorView
        mViewPager = find<ViewPager>(mDecorView!!, R.id.vp_2)
        mViewPager!!.adapter = MyPagerAdapter(supportFragmentManager)
        /** with nothing  */
        mTabLayout_1 = find<TTabLayout>(mDecorView!!, R.id.tl_1)
        /** with ViewPager  */
        mTabLayout_2 = find<TTabLayout>(mDecorView!!, R.id.tl_2)
        /** with Fragments  */
        mTabLayout_3 = find<TTabLayout>(mDecorView!!, R.id.tl_3)
        /** indicator固定宽度  */
        mTabLayout_4 = find<TTabLayout>(mDecorView!!, R.id.tl_4)
        /** indicator固定宽度  */
        mTabLayout_5 = find<TTabLayout>(mDecorView!!, R.id.tl_5)
        /** indicator矩形圆角  */
        mTabLayout_6 = find<TTabLayout>(mDecorView!!, R.id.tl_6)
        /** indicator三角形  */
        mTabLayout_7 = find<TTabLayout>(mDecorView!!, R.id.tl_7)
        /** indicator圆角色块  */
        mTabLayout_8 = find<TTabLayout>(mDecorView!!, R.id.tl_8)
        mTabLayout_1!!.setTabData(mTabEntities)
        tl_2()
        mTabLayout_3!!.setTabData(mTabEntities, this, R.id.fl_change, mFragments2)
        mTabLayout_4!!.setTabData(mTabEntities)
        mTabLayout_5!!.setTabData(mTabEntities)
        mTabLayout_6!!.setTabData(mTabEntities)
        mTabLayout_7!!.setTabData(mTabEntities)
        mTabLayout_8!!.setTabData(mTabEntities)
        mTabLayout_3!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                mTabLayout_1!!.currentTab = position
                mTabLayout_2!!.currentTab = position
                mTabLayout_4!!.currentTab = position
                mTabLayout_5!!.currentTab = position
                mTabLayout_6!!.currentTab = position
                mTabLayout_7!!.currentTab = position
                mTabLayout_8!!.currentTab = position
            }

            override fun onTabReselect(position: Int) {}
        })
        mTabLayout_8!!.currentTab = 2
        mTabLayout_3!!.currentTab = 1

        //显示未读红点
        mTabLayout_1!!.showDot(2)
        mTabLayout_3!!.showDot(1)
        mTabLayout_4!!.showDot(1)

        //两位数
        mTabLayout_2!!.showMsg(0, 55)
        mTabLayout_2!!.setMsgMargin(0, -5f, 5f)

        //三位数
        mTabLayout_2!!.showMsg(1, 100)
        mTabLayout_2!!.setMsgMargin(1, -5f, 5f)

        //设置未读消息红点
        mTabLayout_2!!.showDot(2)
        val rtv_2_2 = mTabLayout_2!!.getMsgView(2)
        if (rtv_2_2 != null) {
            TLayoutMsgTool.setSize(rtv_2_2, RxImageTool.dp2px(7.5f))
        }

        //设置未读消息背景
        mTabLayout_2!!.showMsg(3, 5)
        mTabLayout_2!!.setMsgMargin(3, 0f, 5f)
        val rtv_2_3 = mTabLayout_2!!.getMsgView(3)
        if (rtv_2_3 != null) {
            rtv_2_3.backgroundColor = Color.parseColor("#6D8FB0")
        }
    }

    override fun initData() {

    }

    private fun tl_2() {
        mTabLayout_2!!.setTabData(mTabEntities)
        mTabLayout_2!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                mViewPager!!.currentItem = position
            }

            override fun onTabReselect(position: Int) {
                if (position == 0) {
                    mTabLayout_2!!.showMsg(0, mRandom.nextInt(100) + 1)
                    //                    UnreadMsgTool.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        })
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                mTabLayout_2!!.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mViewPager!!.currentItem = 1
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