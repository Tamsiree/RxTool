package com.tamsiree.rxui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxui.R

class FragmentPlaceholder : FragmentLazy() {
    override fun initViews(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_placeholder, viewGroup, false)
    }

    override fun initData() {}

    companion object {
        fun newInstance(): FragmentPlaceholder {
            return FragmentPlaceholder()
        }
    }
}