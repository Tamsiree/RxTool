package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.fragment.FragmentLoadingDemo
import com.tamsiree.rxdemo.fragment.FragmentLoadingWay
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityLoading : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.tabs)
    var mTabs: TabLayout? = null

    @JvmField
    @BindView(R.id.viewpager)
    var mViewpager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        mViewpager!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
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
        mTabs!!.setupWithViewPager(mViewpager)
    }
}