package com.tamsiree.rxdemo.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.activity.ActivityLoadingDetail
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxkit.RxRecyclerViewDividerTool
import com.tamsiree.rxui.fragment.FragmentLazy
import com.tamsiree.rxui.view.loadingview.SpinKitView
import com.tamsiree.rxui.view.loadingview.SpriteFactory
import com.tamsiree.rxui.view.loadingview.Style
import kotlinx.android.synthetic.main.fragment_page1.*

/**
 * Created by Tamsiree.
 * @author tamsiree
 */
class FragmentLoadingWay : FragmentLazy() {
    var colors = intArrayOf(
            Color.parseColor("#99CCFF"),
            Color.parseColor("#34A853"))

    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_page1, viewGroup, false)
        return view
    }

    override fun initView() {

    }

    override fun initData() {
        val recyclerView: RecyclerView = list
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)))
        recyclerView.adapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                @SuppressLint("InflateParams") val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, null)
                return Holder(view)
            }

            override fun onBindViewHolder(holder: Holder, position: Int) {
                holder.bind(position)
            }

            override fun getItemCount(): Int {
                return Style.values().size
            }
        }
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var spinKitView: SpinKitView = itemView.findViewById(R.id.spin_kit)
        fun bind(position: Int) {
            var position = position
            itemView.setBackgroundColor(colors[position % colors.size])
            val finalPosition = position
            itemView.setOnClickListener { v -> ActivityLoadingDetail.start(v.context, finalPosition) }
            position %= 15
            val style = Style.values()[position]
            val drawable = SpriteFactory.create(style)
            spinKitView.setIndeterminateDrawable(drawable!!)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentLoadingWay {
            return FragmentLoadingWay()
        }
    }
}