package com.tamsiree.rxui.fragment;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class TFragmentManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /**
     * Fragment切换数组
     */
    private List<Fragment> mFragments;
    /**
     * 当前选中的Tab
     */
    private int mCurrentTab;

    public TFragmentManager(FragmentManager fm, int containerViewId, List<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }

    /**
     * androidX 包下的使用
     * 动态的使用Fragment
     * <p>
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param fragmentActivity fragmentActivity
     * @param fragment         fragment
     * @param containerViewId  <FrameLayout android:id="@+id/containerViewId"/>
     */
    public static void showFragment(FragmentActivity fragmentActivity, Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * androidX 包下的使用
     * 动态的使用Fragment
     * <p>
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param fragmentActivity fragmentActivity
     * @param fragmentLazy     fragmentLazy
     * @param containerViewId  <FrameLayout android:id="@+id/containerViewId"/>
     */
    public static void showFragmentLazy(FragmentActivity fragmentActivity, FragmentLazy fragmentLazy, int containerViewId) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragmentLazy);
        fragmentTransaction.commit();
        fragmentLazy.onHiddenChanged(true);
        fragmentLazy.onHiddenChanged(false);
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }

    //----------------------------------------------------------------------------------------------Fragment的静态使用 start
    //在布局文件中直接使用标签
    /*    <fragment
            android:layout_below="@id/id_fragment_title"
            android:id="@+id/id_fragment_content"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent" />*/

    //==============================================================================================Fragment的静态使用 end

    //----------------------------------------------------------------------------------------------Fragment的动态使用 start

    /**
     * android.app.Activity下的使用
     * 动态的使用Fragment
     * <p>
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param activity        activity
     * @param fragment        fragment
     * @param containerViewId <FrameLayout android:id="@+id/containerViewId"/>
     */
    public static void showFragment(Activity activity, android.app.Fragment fragment, int containerViewId) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 初始化fragments
     */
    private void initFragments() {
        for (Fragment fragment : mFragments) {
            mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commit();
        }

        if (!mFragments.isEmpty()) {
            setFragments(0);
        }
    }

    /**
     * 界面切换控制
     */
    public void setFragments(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragments.get(i);
            if (i == index) {
                ft.replace(mContainerViewId, fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentTab = index;
    }

    //==============================================================================================Fragment的动态使用 end


}