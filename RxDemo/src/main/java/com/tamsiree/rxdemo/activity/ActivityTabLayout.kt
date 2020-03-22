package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain
import com.tamsiree.rxdemo.adapter.AdapterRecyclerViewMain.ContentListener
import com.tamsiree.rxdemo.model.ModelDemo
import com.tamsiree.rxkit.RxActivityTool
import com.tamsiree.rxkit.RxImageTool
import com.tamsiree.rxkit.RxRecyclerViewDividerTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import kotlinx.android.synthetic.main.activity_tablayout.*
import java.util.*

class ActivityTabLayout : ActivityBase() {
    private val mItems = arrayOf("TGlideTabLayout", "CommonTabLayout", "TSectionTabLayout")

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    private val mColumnCount = 2
    private var mData: MutableList<ModelDemo?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
        ButterKnife.bind(this)
    }

    override fun initView() {
        mRxTitle!!.setLeftFinish(this)
        if (mColumnCount <= 1) {
            recyclerview.layoutManager = LinearLayoutManager(mContext)
        } else {
            recyclerview.layoutManager = GridLayoutManager(mContext, mColumnCount)
        }
        recyclerview.addItemDecoration(RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)))
        val recyclerViewMain = AdapterRecyclerViewMain(mData, object : ContentListener {
            override fun setListener(position: Int) {
                RxActivityTool.skipActivity(mContext, mData!![position]!!.activity)
            }
        })
        recyclerview.adapter = recyclerViewMain
    }

    override fun initData() {
        mData = ArrayList()
        mData?.add(ModelDemo("TTabLayout", R.drawable.circle_elves_ball, ActivityTTabLayout::class.java))
        mData?.add(ModelDemo("TGlideTabLayout", R.drawable.circle_elves_ball, ActivityTGlideTabLayout::class.java))
        mData?.add(ModelDemo("TSectionTabLayout", R.drawable.circle_elves_ball, ActivityTSectionTabLayout::class.java))
        recyclerview.adapter?.notifyDataSetChanged()
    }
}