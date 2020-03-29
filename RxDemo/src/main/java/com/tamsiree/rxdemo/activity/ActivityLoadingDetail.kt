package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.tools.EvaluatorARGB.Companion.instance
import com.tamsiree.rxkit.RxDeviceTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.progressing.SpinKitView
import com.tamsiree.rxui.view.progressing.SpriteFactory
import com.tamsiree.rxui.view.progressing.Style

/**
 * @author tamsiree
 */
class ActivityLoadingDetail : ActivityBase() {

    var colors = intArrayOf(
            Color.parseColor("#D55400"),
            Color.parseColor("#2B3E51"),
            Color.parseColor("#00BD9C"),
            Color.parseColor("#227FBB"),
            Color.parseColor("#7F8C8D"),
            Color.parseColor("#FFCC5C"),
            Color.parseColor("#D55400"),
            Color.parseColor("#1AAF5D"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_detail)
        RxDeviceTool.setPortrait(this)
    }

    override fun initView() {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.offscreenPageLimit = 0
        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return Style.values().size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                @SuppressLint("InflateParams") val view = LayoutInflater.from(container.context).inflate(R.layout.item_pager, null)
                val spinKitView: SpinKitView = view.findViewById(R.id.spin_kit)
                val name = view.findViewById<TextView>(R.id.name)
                val style = Style.values()[position]
                name.text = style.name
                val drawable = SpriteFactory.create(style)
                spinKitView.setIndeterminateDrawable(drawable!!)
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val color = instance.evaluate(positionOffset,
                        colors[position % colors.size],
                        colors[(position + 1) % colors.size]) as Int
                window.decorView.setBackgroundColor(color)
            }

            override fun onPageSelected(position: Int) {
                window.decorView.setBackgroundColor(colors[position % colors.size])
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager.currentItem = intent.getIntExtra("position", 0)
    }

    override fun initData() {

    }

    companion object {
        fun start(context: Context, position: Int) {
            val intent = Intent(context, ActivityLoadingDetail::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }
}