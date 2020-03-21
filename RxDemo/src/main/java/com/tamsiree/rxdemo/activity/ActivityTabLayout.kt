package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*

class ActivityTabLayout : ActivityBase() {
    private val mItems = arrayOf("TGlideTabLayout", "CommonTabLayout", "TSectionTabLayout")

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.recyclerview)
    var mRecyclerview: RecyclerView? = null

    //    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class, SegmentTabActivity.class};
    private val mColumnCount = 2
    private var mData: MutableList<ModelDemo?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
        ButterKnife.bind(this)
        initData()
        initView()
    }

    private fun initData() {
        mData = ArrayList()
        mData?.add(ModelDemo("TTabLayout", R.drawable.circle_elves_ball, ActivityTTabLayout::class.java))
        mData?.add(ModelDemo("TGlideTabLayout", R.drawable.circle_elves_ball, ActivityTGlideTabLayout::class.java))
        mData?.add(ModelDemo("TSectionTabLayout", R.drawable.circle_elves_ball, ActivityTSectionTabLayout::class.java))
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(this)
        if (mColumnCount <= 1) {
            mRecyclerview!!.layoutManager = LinearLayoutManager(mContext)
        } else {
            mRecyclerview!!.layoutManager = GridLayoutManager(mContext, mColumnCount)
        }
        mRecyclerview!!.addItemDecoration(RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)))
        val recyclerViewMain = AdapterRecyclerViewMain(mData, object : ContentListener {
            override fun setListener(position: Int) {
                RxActivityTool.skipActivity(mContext!!, mData!![position]!!.activity)
            }
        })
        mRecyclerview!!.adapter = recyclerViewMain
    }
}