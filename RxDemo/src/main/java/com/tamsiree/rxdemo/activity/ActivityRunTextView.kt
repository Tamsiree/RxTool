package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTextViewVertical
import kotlinx.android.synthetic.main.activity_run_text_view.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityRunTextView : ActivityBase() {

    private val titleList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_text_view)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        titleList.add("你是天上最受宠的一架钢琴")
        titleList.add("我是丑人脸上的鼻涕")
        titleList.add("你发出完美的声音")
        titleList.add("我被默默揩去")
        titleList.add("你冷酷外表下藏着诗情画意")
        titleList.add("我已经够胖还吃东西")
        titleList.add("你踏着七彩祥云离去")
        titleList.add("我被留在这里")
        text.setTextList(titleList)
        text.setText(26f, 5, -0x899eaa) //设置属性
        text.setTextStillTime(3000) //设置停留时长间隔
        text.setAnimTime(300) //设置进入和退出的时间间隔

        text.setOnItemClickListener(object : RxTextViewVertical.OnItemClickListener {
            override fun onItemClick(position: Int) {
                RxToast.success(mContext, "点击了 : " + titleList[position], Toast.LENGTH_SHORT, true)?.show()
            }

        })

        val views: MutableList<View> = ArrayList()
        setUPMarqueeView(views, 11)
        upview1.setViews(views)
    }

    override fun initData() {

    }

    private fun setUPMarqueeView(views: MutableList<View>, size: Int) {
        var i = 0
        while (i < size) {
            val position = i
            //设置滚动的单个布局
            val moreView = LayoutInflater.from(mContext).inflate(R.layout.item_view, null) as LinearLayout
            //初始化布局的控件
            val tv1 = moreView.findViewById<TextView>(R.id.tv1)
            val tv2 = moreView.findViewById<TextView>(R.id.tv2)
            /**
             * 设置监听
             */
            moreView.findViewById<View>(R.id.rl).setOnClickListener { }
            /**
             * 设置监听
             */
            moreView.findViewById<View>(R.id.rl2).setOnClickListener { }
            //进行对控件赋值
            tv1.text = "五一欢乐与您共享，ＸＸ节能高清惊喜大促销。"
            if (size > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                tv2.text = "五一充值送机，你准备好了吗？"
            } else {
                moreView.findViewById<View>(R.id.rl2).visibility = View.GONE
            }

            //添加到循环滚动数组里面去
            views.add(moreView)
            i += 2
        }
    }

    override fun onResume() {
        super.onResume()
        text.startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        text.stopAutoScroll()
    }
}