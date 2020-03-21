package com.tamsiree.rxdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterFragmentViewPager(fm: FragmentManager?, var mTitles: Array<String>, var mFragments: List<Fragment>) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

}