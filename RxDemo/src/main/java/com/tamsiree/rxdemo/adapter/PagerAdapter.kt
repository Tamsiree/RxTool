package com.tamsiree.rxdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tamsiree.rxdemo.fragment.PageFragment

/**
 * @ClassName PagerAdapter
 * @Description TODO
 * @Author tamsiree
 * @Date 20-3-14 下午1:23
 * @Version 1.0
 */

internal class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(position + 1, position == count - 1)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Page $position"
    }
}