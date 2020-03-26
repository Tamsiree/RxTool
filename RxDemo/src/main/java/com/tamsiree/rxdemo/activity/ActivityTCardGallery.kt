package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterCardGallery
import com.tamsiree.rxkit.interfaces.OnDoIntListener
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.tcardgralleryview.CardScaleHelper
import kotlinx.android.synthetic.main.activity_tcard_grallery.*
import java.util.*

class ActivityTCardGallery : ActivityBase() {

    private val mList: MutableList<Int> = ArrayList()
    private var mCardScaleHelper: CardScaleHelper? = null
    private var mBlurRunnable: Runnable? = null
    private var mLastPos = -1
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }*/
        setContentView(R.layout.activity_tcard_grallery)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = AdapterCardGallery(mList)
//        val linearSnapHelper = LinearSnapHelper()
        //将SnapHelper attach 到RecyclrView
//        linearSnapHelper.attachToRecyclerView(recyclerView)
        tIndicator.attachToRecyclerView(recyclerView)
        // mRecyclerView绑定scale效果
        // mRecyclerView绑定scale效果
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.currentItemPos = 0
        mCardScaleHelper!!.attachToRecyclerView(recyclerView, object : OnDoIntListener {
            override fun doSomething(intValue: Int) {
                RxToast.normal("选中$intValue")
                if (mLastPos == intValue) {
                    return
                }
                mLastPos = intValue
                blurView.notifyChange(mList[mLastPos])
            }
        })
    }

    override fun initData() {
        mList.add(R.drawable.bg_friend)
        mList.add(R.drawable.ova)
        mList.add(R.drawable.bg_family)
        mList.add(R.drawable.bg_splash)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}
