package com.vondear.rxui.fragment;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 *
 * @author vondear
 * @date 2016/12/26
 */

public class RxFragmentTool {

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
     * v4包下的使用
     * 动态的使用Fragment
     *
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param fragmentActivity fragmentActivity
     * @param fragment fragment
     * @param r_id_fragment    <FrameLayout android:id="@+id/r_id_fragment"/>
     */
    public static void showFragment(FragmentActivity fragmentActivity, Fragment fragment, int r_id_fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r_id_fragment, fragment);
        fragmentTransaction.commit();
    }

    /**
     * android.app.Activity下的使用
     * 动态的使用Fragment
     *
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param activity activity
     * @param fragment fragment
     * @param r_id_fragment <FrameLayout android:id="@+id/r_id_fragment"/>
     */
    public static void showFragment(Activity activity, android.app.Fragment fragment, int r_id_fragment) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r_id_fragment, fragment);
        fragmentTransaction.commit();
    }

    //==============================================================================================Fragment的动态使用 start

    /**
     * v4包下的使用
     * 动态的使用Fragment
     *
     * 在布局文件中使用 FrameLayout 标签
     *
     * @param fragmentActivity fragmentActivity
     * @param fragmentLazy fragmentLazy
     * @param r_id_fragment    <FrameLayout android:id="@+id/r_id_fragment"/>
     */
    public static void showFragmentLazy(FragmentActivity fragmentActivity, FragmentLazy fragmentLazy, int r_id_fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r_id_fragment, fragmentLazy);
        fragmentTransaction.commit();
        fragmentLazy.onHiddenChanged(true);
        fragmentLazy.onHiddenChanged(false);
    }

}
