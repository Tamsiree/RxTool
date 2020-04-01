package com.tamsiree.rxdemo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.fragment.FragmentLazy
import kotlinx.android.synthetic.main.fragment_page.*

/**
 * @ClassName PageFragment
 * @Description TODO
 * @Author tamsiree
 * @Date 20-3-14 下午1:24
 * @Version 1.0
 */

class FragmentPage : FragmentLazy() {
    private var lblPage: TextView? = null

    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_page, viewGroup, false)
        return view
    }

    override fun initView() {

    }

    override fun initData() {
        lblPage = lbl_page
        val page = arguments!!.getInt("page", 0)
        if (arguments!!.containsKey("isLast")) {
            lblPage!!.setTextColor(Color.parseColor("#52BA97"))
            lblPage!!.text = "成功!"
        } else {
            lblPage!!.text = page.toString()
        }
    }

    companion object {
        fun newInstance(page: Int, isLast: Boolean): FragmentPage {
            val args = Bundle()
            args.putInt("page", page)
            if (isLast) args.putBoolean("isLast", true)
            val fragment = FragmentPage()
            fragment.arguments = args
            return fragment
        }
    }
}