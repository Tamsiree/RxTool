package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.fragment.FragmentLazy
import kotlinx.android.synthetic.main.fr_simple_card.*

class FragmentSimpleCard : FragmentLazy() {
    private var mTitle: String? = null

    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fr_simple_card, viewGroup, false)
        return view
    }

    override fun initView() {

    }

    override fun initData() {
        val card_title_tv = card_title_tv
        card_title_tv.text = mTitle
    }

    companion object {
        @JvmStatic
        fun getInstance(title: String?): FragmentSimpleCard {
            val sf = FragmentSimpleCard()
            sf.mTitle = title
            return sf
        }
    }
}