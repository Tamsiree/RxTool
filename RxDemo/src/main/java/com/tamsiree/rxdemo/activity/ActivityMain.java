package com.tamsiree.rxdemo.activity;

import android.Manifest;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.adapter.AdapterFragmentViewPager;
import com.tamsiree.rxdemo.fragment.FragmentDemoType;
import com.tamsiree.rxdemo.model.TabEntity;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxPermissionsTool;
import com.tamsiree.rxui.activity.ActivityBase;
import com.tamsiree.rxui.fragment.FragmentPlaceholder;
import com.tamsiree.rxui.view.RxTitle;
import com.tamsiree.rxui.view.tablayout.TTabLayout;
import com.tamsiree.rxui.view.tablayout.listener.OnTabSelectListener;
import com.tamsiree.rxui.view.tablayout.listener.TabLayoutModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tamsiree
 */
public class ActivityMain extends ActivityBase {

    //双击返回键 退出
    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout1)
    TTabLayout mTabLayout1;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;

    private long mBackPressed;

    private String[] mTitles = {"我的", "Demo", "设置"};

    private List<Fragment> mFragments = new ArrayList<>();

    private ArrayList<TabLayoutModel> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {
            R.drawable.ic_contact_gray, R.drawable.ic_home_unselect,
            R.drawable.ic_colleague_unselect};
    private int[] mIconSelectIds = {
            R.drawable.ic_contact_blue, R.drawable.ic_home,
            R.drawable.ic_colleague_select};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        mContext = this;
        initData();
        initView();

        RxPermissionsTool.
                with(mContext).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    private void initView() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mViewPager.setAdapter(new AdapterFragmentViewPager(getSupportFragmentManager(), mTitles, mFragments));
        //mTabLayout1.setViewPager(mViewPager);
        initTabLayout();
    }

    private void initTabLayout() {
        mTabLayout1.setTabData(mTabEntities);
        mTabLayout1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    //mTabLayout1.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout1.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
    }

    private void initData() {

        //---------------------------------------TabLayout设置---------------------------------------
        if (mFragments.isEmpty()) {
            mFragments.add(FragmentPlaceholder.newInstance());
            mFragments.add(FragmentDemoType.newInstance());
            mFragments.add(FragmentPlaceholder.newInstance());

        }
        //==========================================================================================

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
    //==============================================================================================

}
