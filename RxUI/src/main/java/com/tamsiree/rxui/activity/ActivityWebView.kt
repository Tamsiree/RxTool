package com.tamsiree.rxui.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxConstants
import com.tamsiree.rxkit.RxWebViewTool.initWebView
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxkit.interfaces.OnWebViewLoad
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.R
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.include_webview.*

/**
 * @author tamsiree
 */
class ActivityWebView : ActivityBase() {

    private var webPath = ""
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar(this)
        setContentView(R.layout.activity_webview)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    // 初始化控件 - FindViewById之类的操作
    override fun initView() {
        web_rx_title.setLeftOnClickListener(View.OnClickListener {
            if (web_base.canGoBack()) {
                web_base.goBack()
            } else {
                finish()
            }
        })
    }

    // 初始化控件的数据及监听事件
    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        //设置加载进度最大值
        pb_web_base.max = 100

//        webPath = getIntent().getStringExtra("URL");

        //加载的URL
        webPath = RxConstants.URL_BAIDU_SEARCH
        if (webPath == "") {
            webPath = "http://www.baidu.com"
        }
        initWebView(mContext, web_base, object : OnWebViewLoad {
            override fun onPageStarted() {
                pb_web_base.visibility = View.VISIBLE
            }

            override fun onReceivedTitle(title: String) {
                web_rx_title.setTitle(title)
            }

            override fun onProgressChanged(newProgress: Int) {
                pb_web_base.progress = newProgress
            }

            override fun shouldOverrideUrlLoading() {}
            override fun onPageFinished() {
                pb_web_base.visibility = View.GONE
            }
        })
        web_base.loadUrl(webPath)
        TLog.v("帮助类完整连接", webPath)
        //        webBase.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,webBase.getHeight()));
    }

    fun setWebPath(url: String) {
        webPath = url
        web_base.loadUrl(webPath)
        TLog.v("设置新的URL：", webPath)
    }

    override fun onSaveInstanceState(paramBundle: Bundle) {
        super.onSaveInstanceState(paramBundle)
        paramBundle.putString("url", web_base.url)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        try {
            super.onConfigurationChanged(newConfig)
            if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                TLog.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE")
            } else if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TLog.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (web_base.canGoBack()) {
            web_base.goBack()
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
//                Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
                RxToast.normal("再次点击返回键退出")
            }
            mBackPressed = System.currentTimeMillis()
        }
    }

    companion object {
        /**
         * milliseconds, desired time passed between two back presses.
         */
        private const val TIME_INTERVAL = 2000
    }
}