package com.tamsiree.rxdemo.activity

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterCardGallery
import com.tamsiree.rxkit.RxAnimationTool
import com.tamsiree.rxkit.TBlurTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.tcardgralleryview.CardScaleHelper
import kotlinx.android.synthetic.main.activity_tcard_grallery.*
import java.util.*

class ActivityTCardGallery : ActivityBase() {

    private val mList: MutableList<Int> = ArrayList()
    private var mCardScaleHelper: CardScaleHelper? = null
    private var mBlurRunnable: Runnable? = null
    private var mLastPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_tcard_grallery)
        init()
    }

    private fun init() {
        for (i in 0..4) {
            mList.add(R.drawable.bg_friend)
            mList.add(R.drawable.ova)
            mList.add(R.drawable.bg_family)
            mList.add(R.drawable.bg_splash)
        }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = AdapterCardGallery(mList)
        // mRecyclerView绑定scale效果
        // mRecyclerView绑定scale效果
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.currentItemPos = 2
        mCardScaleHelper!!.attachToRecyclerView(recyclerView)

        initBlurBackground()
    }

    private fun initBlurBackground() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange()
                }
            }
        })

        notifyBackgroundChange()
    }

    private fun notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper!!.currentItemPos) return
        mLastPos = mCardScaleHelper!!.currentItemPos
        val resId = mList[mCardScaleHelper!!.currentItemPos]
        blurView?.removeCallbacks(mBlurRunnable)
        mBlurRunnable = Runnable {
            val bitmap = BitmapFactory.decodeResource(resources, resId)
            RxAnimationTool.startSwitchBackgroundAnim(blurView, TBlurTool.getBlurBitmap(blurView?.context, bitmap, 15))
        }
        blurView?.postDelayed(mBlurRunnable, 500)

    }

}
