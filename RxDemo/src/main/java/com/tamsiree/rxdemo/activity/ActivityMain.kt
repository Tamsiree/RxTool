package com.tamsiree.rxdemo.activity

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterFragmentViewPager
import com.tamsiree.rxdemo.fragment.FragmentDemoType
import com.tamsiree.rxdemo.model.TabEntity
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxkit.RxPermissionsTool
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.fragment.FragmentPlaceholder
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityMain : ActivityBase() {

    private var mBackPressed: Long = 0
    private val mTitles = arrayOf("我的", "Demo", "设置")
    private val mFragments: MutableList<Fragment> = ArrayList()
    private val mTabEntities = ArrayList<TabLayoutModel>()
    private val mIconUnselectIds = intArrayOf(
            R.drawable.ic_contact_gray, R.drawable.ic_home_unselect,
            R.drawable.ic_colleague_unselect)
    private val mIconSelectIds = intArrayOf(
            R.drawable.ic_contact_blue, R.drawable.ic_home,
            R.drawable.ic_colleague_select)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        RxDeviceTool.setPortrait(this)
        initData()
        initView()

        RxPermissionsTool
                .with(mContext)
                .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.READ_PHONE_STATE)
                .initPermission()
    }

    override fun initView() {
        for (i in mTitles.indices) {
            mTabEntities.add(TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        view_pager!!.adapter = AdapterFragmentViewPager(supportFragmentManager, mTitles, mFragments)
        initTabLayout()
    }

    private fun initTabLayout() {
        tab_layout1!!.setTabData(mTabEntities)
        tab_layout1!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                view_pager!!.currentItem = position
            }

            override fun onTabReselect(position: Int) {
                if (position == 0) {
                    //mTabLayout1.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        })
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                tab_layout1!!.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        view_pager.currentItem = 1
    }

    override fun initData() {

        //---------------------------------------TabLayout设置---------------------------------------
        if (mFragments.isEmpty()) {
            mFragments.add(FragmentPlaceholder.newInstance())
            mFragments.add(FragmentDemoType.newInstance())
            mFragments.add(FragmentPlaceholder.newInstance())
        }
        //==========================================================================================
    }

    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            RxToast.info("再次点击返回键退出")
        }
        mBackPressed = System.currentTimeMillis()
    } //==============================================================================================

    companion object {
        //双击返回键 退出
        //----------------------------------------------------------------------------------------------
        private const val TIME_INTERVAL = 2000 // # milliseconds, desired time passed between two back presses.
    }
}