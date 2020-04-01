package com.tamsiree.rxdemo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxui.fragment.FragmentLazy
import com.tamsiree.rxui.view.loadingview.style.Circle
import com.tamsiree.rxui.view.loadingview.style.CubeGrid
import com.tamsiree.rxui.view.loadingview.style.DoubleBounce
import com.tamsiree.rxui.view.loadingview.style.Wave
import kotlinx.android.synthetic.main.fragment_page2.*

/**
 * Created by Tamsiree.
 * @author tamsiree
 */
class FragmentLoadingDemo : FragmentLazy() {

    var colors = intArrayOf(
            Color.parseColor("#89CFF0"),
            Color.parseColor("#2B3E51"))

    private var mWaveDrawable: Wave? = null
    private var mCircleDrawable: Circle? = null
    private var mChasingDotsDrawable: CubeGrid? = null


    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_page2, viewGroup, false)
        return view
    }

    override fun initView() {

    }

    override fun initData() {
        //ProgressBar
        val progressBar = progress
        val doubleBounce = DoubleBounce()
        doubleBounce.setBounds(0, 0, 100, 100)
        doubleBounce.color = colors[0]
        progressBar.indeterminateDrawable = doubleBounce

        //Button
        val button = button
        mWaveDrawable = Wave()
        mWaveDrawable!!.setBounds(0, 0, 100, 100)
        mWaveDrawable!!.color = resources.getColor(R.color.colorAccent)
        button.setCompoundDrawables(mWaveDrawable, null, null, null)

        //TextView
        val textView = text
        mCircleDrawable = Circle()
        mCircleDrawable!!.setBounds(0, 0, 100, 100)
        mCircleDrawable!!.color = Color.WHITE
        textView.setCompoundDrawables(null, null, mCircleDrawable, null)
        textView.setBackgroundColor(colors[0])

        //ImageView
        val imageView = image
        mChasingDotsDrawable = CubeGrid()
        mChasingDotsDrawable!!.color = Color.WHITE
        imageView.setImageDrawable(mChasingDotsDrawable)
        imageView.setBackgroundColor(colors[0])
    }

    override fun onResume() {
        super.onResume()
        mWaveDrawable!!.start()
        mCircleDrawable!!.start()
        mChasingDotsDrawable!!.start()
    }

    override fun onStop() {
        super.onStop()
        mWaveDrawable!!.stop()
        mCircleDrawable!!.stop()
        mChasingDotsDrawable!!.stop()
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentLoadingDemo {
            return FragmentLoadingDemo()
        }
    }
}