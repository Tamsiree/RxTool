package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tamsiree.rxdemo.R

class FragmentSimpleCard : Fragment() {
    private var mTitle: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fr_simple_card, null)
        val card_title_tv = v.findViewById<TextView>(R.id.card_title_tv)
        card_title_tv.text = mTitle
        return v
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