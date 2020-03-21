package com.tamsiree.rxdemo.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import android.widget.SlidingDrawer.OnDrawerScrollListener
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.noTitle
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxConstants
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivitySlidingDrawerSingle : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.textView1)
    var mTextView1: TextView? = null

    @JvmField
    @BindView(R.id.textView2)
    var mTextView2: TextView? = null

    @JvmField
    @BindView(R.id.textView3)
    var mTextView3: TextView? = null

    @JvmField
    @BindView(R.id.textView4)
    var mTextView4: TextView? = null

    @JvmField
    @BindView(R.id.iv_slide)
    var mIvSlide: ImageView? = null

    @JvmField
    @BindView(R.id.textView8)
    var mTextView8: TextView? = null

    @JvmField
    @BindView(R.id.handle)
    var mHandle: LinearLayout? = null

    @JvmField
    @BindView(R.id.textView14)
    var mTextView14: TextView? = null

    @JvmField
    @BindView(R.id.pb_web_base)
    var mPbWebBase: ProgressBar? = null

    @JvmField
    @BindView(R.id.web_base)
    var mWebBase: WebView? = null

    @JvmField
    @BindView(R.id.LinearLayout2)
    var mLinearLayout2: LinearLayout? = null

    @JvmField
    @BindView(R.id.content)
    var mContent: LinearLayout? = null

    @JvmField
    @BindView(R.id.slidingdrawer)
    var mSlidingdrawer: SlidingDrawer? = null
    var webPath = ""
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noTitle(this)
        setContentView(R.layout.activity_sliding_drawer_single)
        setTransparentStatusBar(this)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        initData()
    }

    private fun initData() {
        mSlidingdrawer!!.setOnDrawerOpenListener {
            flag = true
            mIvSlide!!.setImageResource(R.drawable.slibe_down)
        }
        mSlidingdrawer!!.setOnDrawerCloseListener {
            flag = false
            mIvSlide!!.setImageResource(R.drawable.slibe_up)
        }
        mSlidingdrawer!!.setOnDrawerScrollListener(object : OnDrawerScrollListener {
            override fun onScrollEnded() {}
            override fun onScrollStarted() {}
        })
        mPbWebBase!!.max = 100
        //        webPath = getIntent().getStringExtra("URL");
        webPath = RxConstants.URL_RXTOOL
        if (webPath == "") {
            webPath = "http://www.baidu.com"
        }
        val webSettings = mWebBase!!.settings
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.loadsImagesAutomatically = true
        mWebBase!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mWebBase!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        //        mWebBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
        /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/webSettings.javaScriptEnabled = true // 设置支持javascript脚本
        //        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true) // 设置可以支持缩放
        webSettings.builtInZoomControls = true // 设置出现缩放工具
        webSettings.displayZoomControls = false //隐藏缩放工具
        webSettings.useWideViewPort = true // 扩大比例的缩放
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //自适应屏幕
        webSettings.loadWithOverviewMode = true
        webSettings.databaseEnabled = true
        webSettings.savePassword = true
        webSettings.domStorageEnabled = true
        mWebBase!!.isSaveEnabled = true
        mWebBase!!.keepScreenOn = true


        // 设置setWebChromeClient对象
        mWebBase!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                // TODO Auto-generated method stub
                mPbWebBase!!.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }
        }
        mWebBase!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!mWebBase!!.settings.loadsImagesAutomatically) {
                    mWebBase!!.settings.loadsImagesAutomatically = true
                }
                mPbWebBase!!.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // TODO Auto-generated method stub
                mPbWebBase!!.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url)
                    return false
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }
        mWebBase!!.setDownloadListener { paramAnonymousString1, paramAnonymousString2, paramAnonymousString3, paramAnonymousString4, paramAnonymousLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramAnonymousString1)
            startActivity(intent)
        }
        mWebBase!!.loadUrl(webPath)
        Log.v("帮助类完整连接", webPath)
    }
}