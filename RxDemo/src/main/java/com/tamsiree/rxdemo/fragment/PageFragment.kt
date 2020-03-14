package com.tamsiree.rxdemo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tamsiree.rxdemo.R

/**
 * @ClassName PageFragment
 * @Description TODO
 * @Author tamsiree
 * @Date 20-3-14 下午1:24
 * @Version 1.0
 */

class PageFragment : Fragment() {
    private var lblPage: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_page, container, false)
        lblPage = view.findViewById<View>(R.id.lbl_page) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val page = arguments!!.getInt("page", 0)
        if (arguments!!.containsKey("isLast")) {
            lblPage!!.setTextColor(Color.parseColor("#52BA97"))
            lblPage!!.text = "成功!"
        } else {
            lblPage!!.text = page.toString()
        }
    }

    companion object {
        fun newInstance(page: Int, isLast: Boolean): PageFragment {
            val args = Bundle()
            args.putInt("page", page)
            if (isLast) args.putBoolean("isLast", true)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}