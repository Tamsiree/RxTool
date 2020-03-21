package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
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
import com.tamsiree.rxui.view.tablayout.TGlideTabLayout
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener
import java.util.*

class ActivityTGlideTabLayout : ActivityBase(), OnTabSelectListener {
    private val mTitles = arrayOf(
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    )

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    private val mFragments = ArrayList<Fragment>()
    private var mAdapter: MyPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tglide_tablayout)
        ButterKnife.bind(this)
        mRxTitle!!.setLeftFinish(this)
        for (title in mTitles) {
            mFragments.add(getInstance(title))
        }
        val decorView = window.decorView
        val vp = find<ViewPager>(decorView, R.id.vp)
        mAdapter = MyPagerAdapter(supportFragmentManager)
        vp.adapter = mAdapter
        /** 默认  */
        val tabLayout_1 = find<TGlideTabLayout>(decorView, R.id.tl_1)

        /**自定义部分属性 */
        val tabLayout_2 = find<TGlideTabLayout>(decorView, R.id.tl_2)

        /** 字体加粗,大写  */
        val tabLayout_3 = find<TGlideTabLayout>(decorView, R.id.tl_3)

        /** tab固定宽度  */
        val tabLayout_4 = find<TGlideTabLayout>(decorView, R.id.tl_4)

        /** indicator固定宽度  */
        val tabLayout_5 = find<TGlideTabLayout>(decorView, R.id.tl_5)

        /** indicator圆  */
        val tabLayout_6 = find<TGlideTabLayout>(decorView, R.id.tl_6)

        /** indicator矩形圆角  */
        val tabLayout_7 = find<TGlideTabLayout>(decorView, R.id.tl_7)

        /** indicator三角形  */
        val tabLayout_8 = find<TGlideTabLayout>(decorView, R.id.tl_8)

        /** indicator圆角色块  */
        val tabLayout_9 = find<TGlideTabLayout>(decorView, R.id.tl_9)

        /** indicator圆角色块  */
        val tabLayout_10 = find<TGlideTabLayout>(decorView, R.id.tl_10)
        tabLayout_1.setViewPager(vp)
        tabLayout_2.setViewPager(vp)
        tabLayout_2.setOnTabSelectListener(this)
        tabLayout_3.setViewPager(vp)
        tabLayout_4.setViewPager(vp)
        tabLayout_5.setViewPager(vp)
        tabLayout_6.setViewPager(vp)
        tabLayout_7.setViewPager(vp, mTitles)
        tabLayout_8.setViewPager(vp, mTitles, this, mFragments)
        tabLayout_9.setViewPager(vp)
        tabLayout_10.setViewPager(vp)
        vp.currentItem = 4
        tabLayout_1.showDot(4)
        tabLayout_3.showDot(4)
        tabLayout_2.showDot(4)
        tabLayout_2.showMsg(3, 5)
        tabLayout_2.setMsgMargin(3, 0f, 10f)
        val rtv_2_3 = tabLayout_2.getTMsgView(3)
        if (rtv_2_3 != null) {
            rtv_2_3.backgroundColor = Color.parseColor("#6D8FB0")
        }
        tabLayout_2.showMsg(5, 5)
        tabLayout_2.setMsgMargin(5, 0f, 10f)

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