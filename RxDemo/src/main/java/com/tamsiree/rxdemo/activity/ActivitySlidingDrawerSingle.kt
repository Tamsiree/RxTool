package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.SlidingDrawer.OnDrawerScrollListener
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool
import com.tamsiree.rxkit.RxConstants
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxWebViewTool
import com.tamsiree.rxkit.interfaces.OnWebViewLoad
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_sliding_drawer_single.*

/**
 * @author tamsiree
 */
class ActivitySlidingDrawerSingle : ActivityBase() {

    var pb_web_base: ProgressBar? = null
    var web_base: WebView? = null

    var webPath = ""
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        setContentView(R.layout.activity_sliding_drawer_single)
        RxBarTool.setTransparentStatusBar(this)
    }

    override fun initView() {

        pb_web_base = findViewById(R.id.pb_web_base)
        web_base = findViewById(R.id.web_base)

        setPortrait(this)
        rx_title.setLeftFinish(mContext)

        slidingdrawer.setOnDrawerOpenListener {
            flag = true
            iv_slide.setImageResource(R.drawable.slibe_down)
        }
        slidingdrawer.setOnDrawerCloseListener {
            flag = false
            iv_slide.setImageResource(R.drawable.slibe_up)
        }
        slidingdrawer.setOnDrawerScrollListener(object : OnDrawerScrollListener {
            override fun onScrollEnded() {}
            override fun onScrollStarted() {}
        })

        pb_web_base?.max = 100
        RxWebViewTool.initWebView(mContext, this.web_base!!, object : OnWebViewLoad {
            override fun onPageStarted() {
                pb_web_base?.visibility = View.VISIBLE
            }

            override fun onReceivedTitle(title: String?) {
//                rx_title.title = title
            }

            override fun onProgressChanged(newProgress: Int) {
                pb_web_base?.progress = newProgress
            }

            override fun shouldOverrideUrlLoading() {

            }

            override fun onPageFinished() {
                pb_web_base?.visibility = View.GONE
            }

        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        //webPath = getIntent().getStringExtra("URL");
        webPath = RxConstants.URL_RXTOOL
        if (webPath == "") {
            webPath = "http://www.baidu.com"
        }

        web_base?.loadUrl(webPath)
        Log.v("帮助类完整连接", webPath)
    }
}