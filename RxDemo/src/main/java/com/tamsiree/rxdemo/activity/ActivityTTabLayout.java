package com.tamsiree.rxdemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.fragment.FragmentSimpleCard;
import com.tamsiree.rxdemo.model.TabEntity;
import com.tamsiree.rxdemo.tools.ViewFindTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.view.tablayout.TMsgView;
import com.tamsiree.rxui.view.tablayout.TTabLayout;
import com.tamsiree.rxui.view.tablayout.listener.CustomTabEntity;
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener;
import com.tamsiree.rxui.view.tablayout.tool.UnreadMsgTool;

import java.util.ArrayList;
import java.util.Random;

public class ActivityTTabLayout extends ActivityBase {
    Random mRandom = new Random();
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private TTabLayout mTabLayout_1;
    private TTabLayout mTabLayout_2;
    private TTabLayout mTabLayout_3;
    private TTabLayout mTabLayout_4;
    private TTabLayout mTabLayout_5;
    private TTabLayout mTabLayout_6;
    private TTabLayout mTabLayout_7;
    private TTabLayout mTabLayout_8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttab_layout);

        for (String title : mTitles) {
            mFragments.add(FragmentSimpleCard.getInstance("Switch ViewPager " + title));
            mFragments2.add(FragmentSimpleCard.getInstance("Switch Fragment " + title));
        }


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindTool.find(mDecorView, R.id.vp_2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        /** with nothing */
        mTabLayout_1 = ViewFindTool.find(mDecorView, R.id.tl_1);
        /** with ViewPager */
        mTabLayout_2 = ViewFindTool.find(mDecorView, R.id.tl_2);
        /** with Fragments */
        mTabLayout_3 = ViewFindTool.find(mDecorView, R.id.tl_3);
        /** indicator固定宽度 */
        mTabLayout_4 = ViewFindTool.find(mDecorView, R.id.tl_4);
        /** indicator固定宽度 */
        mTabLayout_5 = ViewFindTool.find(mDecorView, R.id.tl_5);
        /** indicator矩形圆角 */
        mTabLayout_6 = ViewFindTool.find(mDecorView, R.id.tl_6);
        /** indicator三角形 */
        mTabLayout_7 = ViewFindTool.find(mDecorView, R.id.tl_7);
        /** indicator圆角色块 */
        mTabLayout_8 = ViewFindTool.find(mDecorView, R.id.tl_8);

        mTabLayout_1.setTabData(mTabEntities);
        tl_2();
        mTabLayout_3.setTabData(mTabEntities, this, R.id.fl_change, mFragments2);
        mTabLayout_4.setTabData(mTabEntities);
        mTabLayout_5.setTabData(mTabEntities);
        mTabLayout_6.setTabData(mTabEntities);
        mTabLayout_7.setTabData(mTabEntities);
        mTabLayout_8.setTabData(mTabEntities);

        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout_1.setCurrentTab(position);
                mTabLayout_2.setCurrentTab(position);
                mTabLayout_4.setCurrentTab(position);
                mTabLayout_5.setCurrentTab(position);
                mTabLayout_6.setCurrentTab(position);
                mTabLayout_7.setCurrentTab(position);
                mTabLayout_8.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout_8.setCurrentTab(2);
        mTabLayout_3.setCurrentTab(1);

        //显示未读红点
        mTabLayout_1.showDot(2);
        mTabLayout_3.showDot(1);
        mTabLayout_4.showDot(1);

        //两位数
        mTabLayout_2.showMsg(0, 55);
        mTabLayout_2.setMsgMargin(0, -5, 5);

        //三位数
        mTabLayout_2.showMsg(1, 100);
        mTabLayout_2.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        mTabLayout_2.showDot(2);
        TMsgView rtv_2_2 = mTabLayout_2.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgTool.setSize(rtv_2_2, dp2px(7.5f));
        }

        //设置未读消息背景
        mTabLayout_2.showMsg(3, 5);
        mTabLayout_2.setMsgMargin(3, 0, 5);
        TMsgView rtv_2_3 = mTabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void tl_2() {
        mTabLayout_2.setTabData(mTabEntities);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mTabLayout_2.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgTool.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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
