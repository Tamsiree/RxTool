package com.tamsiree.rxui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tamsiree.rxui.model.ModelFVP

class AdapterFVP(fm: FragmentManager?, var modelFVP: List<ModelFVP>) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return modelFVP.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return modelFVP[position].name
    }

    override fun getItem(position: Int): Fragment {
        return modelFVP[position].fragment
    }

}