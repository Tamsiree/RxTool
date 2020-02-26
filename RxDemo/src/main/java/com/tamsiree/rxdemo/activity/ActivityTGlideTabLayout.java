package com.tamsiree.rxdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard;
import com.tamsiree.rxdemo.tools.ViewFindTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.tablayout.TGlideTabLayout;
import com.tamsiree.rxui.view.tablayout.TLayoutMsg;
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class ActivityTGlideTabLayout extends ActivityBase implements OnTabSelectListener {
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tglide_tablayout);

        for (String title : mTitles) {
            mFragments.add(FragmentSimpleCard.getInstance(title));
        }


        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindTool.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        /** 默认 */
        TGlideTabLayout tabLayout_1 = ViewFindTool.find(decorView, R.id.tl_1);
        /**自定义部分属性*/
        TGlideTabLayout tabLayout_2 = ViewFindTool.find(decorView, R.id.tl_2);
        /** 字体加粗,大写 */
        TGlideTabLayout tabLayout_3 = ViewFindTool.find(decorView, R.id.tl_3);
        /** tab固定宽度 */
        TGlideTabLayout tabLayout_4 = ViewFindTool.find(decorView, R.id.tl_4);
        /** indicator固定宽度 */
        TGlideTabLayout tabLayout_5 = ViewFindTool.find(decorView, R.id.tl_5);
        /** indicator圆 */
        TGlideTabLayout tabLayout_6 = ViewFindTool.find(decorView, R.id.tl_6);
        /** indicator矩形圆角 */
        final TGlideTabLayout tabLayout_7 = ViewFindTool.find(decorView, R.id.tl_7);
        /** indicator三角形 */
        TGlideTabLayout tabLayout_8 = ViewFindTool.find(decorView, R.id.tl_8);
        /** indicator圆角色块 */
        TGlideTabLayout tabLayout_9 = ViewFindTool.find(decorView, R.id.tl_9);
        /** indicator圆角色块 */
        TGlideTabLayout tabLayout_10 = ViewFindTool.find(decorView, R.id.tl_10);

        tabLayout_1.setViewPager(vp);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(this);
        tabLayout_3.setViewPager(vp);
        tabLayout_4.setViewPager(vp);
        tabLayout_5.setViewPager(vp);
        tabLayout_6.setViewPager(vp);
        tabLayout_7.setViewPager(vp, mTitles);
        tabLayout_8.setViewPager(vp, mTitles, this, mFragments);
        tabLayout_9.setViewPager(vp);
        tabLayout_10.setViewPager(vp);

        vp.setCurrentItem(4);

        tabLayout_1.showDot(4);
        tabLayout_3.showDot(4);
        tabLayout_2.showDot(4);

        tabLayout_2.showMsg(3, 5);
        tabLayout_2.setMsgMargin(3, 0, 10);
        TLayoutMsg rtv_2_3 = tabLayout_2.getTMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tabLayout_2.showMsg(5, 5);
        tabLayout_2.setMsgMargin(5, 0, 10);

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

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
