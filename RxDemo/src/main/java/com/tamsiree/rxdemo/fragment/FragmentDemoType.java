package com.tamsiree.rxdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.adapter.AdapterFragmentViewPager;
import com.tamsiree.rxui.fragment.FragmentLazy;
import com.tamsiree.rxui.view.tablayout.TGlideTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDemoType extends FragmentLazy {

    @BindView(R.id.tab_layout)
    TGlideTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<String> demoType = new ArrayList<>();

    private List<Fragment> mFragments = new ArrayList<>();

    public FragmentDemoType() {
    }

    public static FragmentDemoType newInstance() {
        return new FragmentDemoType();
    }

    @Override
    protected View initViews(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_demo_type, viewGroup, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        demoType.clear();

        /**
         * Demo的类别
         * 0：功能区
         * 1：特效区
         */
        demoType.add("功能区");
        demoType.add("特效区");

        mFragments.clear();

        for (int i = 0; i < demoType.size(); i++) {
            mFragments.add(FragmentDemo.newInstance(i));
        }

        mViewPager.setAdapter(new AdapterFragmentViewPager(getChildFragmentManager(), demoType.toArray(new String[demoType.size()]), mFragments));
        mTabLayout.setViewPager(mViewPager);

    }


}
