package com.vondear.rxtools;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by vondear on 2016/12/26.
 */

public class RxFragmentUtils {

    //----------------------------------------------------------------------------------------------Fragment的静态使用 start
    //在布局文件中直接使用标签
    /*    <fragment
            android:layout_below="@id/id_fragment_title"
            android:id="@+id/id_fragment_content"
            android:name="com.zhy.zhy_fragments.ContentFragment"
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
     * @param fragmentActivity
     * @param fragment
     * @param r_id_fragment    <FrameLayout android:id="@+id/r_id_fragment"/>
     */
    public static void initFragment(FragmentActivity fragmentActivity, Fragment fragment, int r_id_fragment) {
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
     * @param activity
     * @param fragment
     * @param r_id_fragment <FrameLayout android:id="@+id/r_id_fragment"/>
     */
    public static void initFragment(Activity activity, android.app.Fragment fragment, int r_id_fragment) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r_id_fragment, fragment);
        fragmentTransaction.commit();
    }

    //==============================================================================================Fragment的动态使用 start
}
