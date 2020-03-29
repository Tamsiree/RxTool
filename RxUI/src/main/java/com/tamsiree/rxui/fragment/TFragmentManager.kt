package com.tamsiree.rxui.fragment

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class TFragmentManager(private val mFragmentManager: FragmentManager, private val mContainerViewId: Int,
                       /**
                        * Fragment切换数组
                        */
                       private val mFragments: MutableList<Fragment>) {

    /**
     * 当前选中的Tab
     */
    var currentTab = 0
        private set

    val currentFragment: Fragment
        get() = mFragments[currentTab]

    /**
     * 界面切换控制
     */
    fun setFragments(index: Int) {
        val fragment = mFragments[index]
        showFragment(mFragmentManager, fragment, mContainerViewId)
        currentTab = index
    }

    companion object {
        /**
         * android.app.Activity下的使用
         * 动态的使用Fragment
         *
         *
         * 在布局文件中使用 FrameLayout 标签
         *
         * @param activity        activity
         * @param fragment        fragment
         * @param containerViewId <FrameLayout android:id="@+id/containerViewId"></FrameLayout>
         */
        fun showFragment(activity: Activity, fragment: android.app.Fragment?, containerViewId: Int) {
            val fragmentManager = activity.fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(containerViewId, fragment)
            fragmentTransaction.commit()
        }

        /**
         * androidX 包下的使用
         * 动态的使用Fragment
         *
         *
         * 在布局文件中使用 FrameLayout 标签
         *
         * @param fragmentLazy    fragmentLazy
         * @param containerViewId <FrameLayout android:id="@+id/containerViewId"></FrameLayout>
         */
        fun showFragmentLazy(fragmentManager: FragmentManager, fragmentLazy: FragmentLazy, containerViewId: Int) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(containerViewId, fragmentLazy)
            fragmentTransaction.commit()
            fragmentLazy.onHiddenChanged(true)
            fragmentLazy.onHiddenChanged(false)
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
         * androidX 包下的使用
         * 动态的使用Fragment
         *
         *
         * 在布局文件中使用 FrameLayout 标签
         *
         * @param fragment        fragment
         * @param containerViewId <FrameLayout android:id="@+id/containerViewId"></FrameLayout>
         */
        fun showFragment(fragmentManager: FragmentManager, fragment: Fragment, containerViewId: Int) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(containerViewId, fragment)
            fragmentTransaction.commit()
            fragment.onHiddenChanged(true)
            fragment.onHiddenChanged(false)
        }
        //==============================================================================================Fragment的动态使用 end
        /**
         * androidX 包下的使用
         * 动态的使用Fragment
         *
         *
         * 在布局文件中使用 FrameLayout 标签
         *
         * @param fragmentActivity fragmentActivity
         * @param fragment         fragment
         * @param containerViewId  <FrameLayout android:id="@+id/containerViewId"></FrameLayout>
         */
        fun showFragment(fragmentActivity: FragmentActivity, fragment: Fragment?, containerViewId: Int) {
            val fragmentManager = fragmentActivity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(containerViewId, fragment!!)
            fragmentTransaction.commit()
        }

        /**
         * androidX 包下的使用
         * 动态的使用Fragment
         *
         *
         * 在布局文件中使用 FrameLayout 标签
         *
         * @param fragmentActivity fragmentActivity
         * @param fragmentLazy     fragmentLazy
         * @param containerViewId  <FrameLayout android:id="@+id/containerViewId"></FrameLayout>
         */
        fun showFragmentLazy(fragmentActivity: FragmentActivity, fragmentLazy: FragmentLazy, containerViewId: Int) {
            val fragmentManager = fragmentActivity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(containerViewId, fragmentLazy)
            fragmentTransaction.commit()
            fragmentLazy.onHiddenChanged(true)
            fragmentLazy.onHiddenChanged(false)
        }
    }

    init {
        if (mFragments.isNotEmpty()) {
            setFragments(0)
        }
    }
}