package com.tamsiree.rxdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard;
import com.tamsiree.rxdemo.tools.ViewFindTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.RxTitle;
import com.tamsiree.rxui.view.tablayout.TLayoutMsg;
import com.tamsiree.rxui.view.tablayout.TSectionTabLayout;
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTSectionTabLayout extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息"};
    private String[] mTitles_2 = {"首页", "消息", "联系人"};
    private String[] mTitles_3 = {"首页", "消息", "联系人", "更多"};
    private View mDecorView;
    private TSectionTabLayout mTabLayout_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsection_tablayout);
        ButterKnife.bind(this);

        mRxTitle.setLeftFinish(this);

        for (String title : mTitles_3) {
            mFragments.add(FragmentSimpleCard.getInstance("Switch ViewPager " + title));
        }

        for (String title : mTitles_2) {
            mFragments2.add(FragmentSimpleCard.getInstance("Switch Fragment " + title));
        }

        mDecorView = getWindow().getDecorView();

        TSectionTabLayout tabLayout_1 = ViewFindTool.find(mDecorView, R.id.tl_1);
        TSectionTabLayout tabLayout_2 = ViewFindTool.find(mDecorView, R.id.tl_2);
        mTabLayout_3 = ViewFindTool.find(mDecorView, R.id.tl_3);
        TSectionTabLayout tabLayout_4 = ViewFindTool.find(mDecorView, R.id.tl_4);
        TSectionTabLayout tabLayout_5 = ViewFindTool.find(mDecorView, R.id.tl_5);

        tabLayout_1.setTabData(mTitles);
        tabLayout_2.setTabData(mTitles_2);
        tl_3();
        tabLayout_4.setTabData(mTitles_2, this, R.id.fl_change, mFragments2);
        tabLayout_5.setTabData(mTitles_3);

        //显示未读红点
        tabLayout_1.showDot(2);
        tabLayout_2.showDot(2);
        mTabLayout_3.showDot(1);
        tabLayout_4.showDot(1);

        //设置未读消息红点
        mTabLayout_3.showDot(2);
        TLayoutMsg rtv_3_2 = mTabLayout_3.getMsgView(2);
        if (rtv_3_2 != null) {
            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void tl_3() {
        final ViewPager vp_3 = ViewFindTool.find(mDecorView, R.id.vp_2);
        vp_3.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout_3.setTabData(mTitles_3);
        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_3.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_3.setCurrentItem(1);
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
            return mTitles_3[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
