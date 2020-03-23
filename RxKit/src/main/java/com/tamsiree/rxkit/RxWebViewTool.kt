package com.tamsiree.rxkit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tamsiree.rxkit.interfaces.OnWebViewLoad

/**
 *
 * @author Tamsiree
 * @date 2017/4/1
 */
object RxWebViewTool {

    @SuppressLint("SetJavaScriptEnabled")
    @JvmStatic
    fun initWebView(context: Activity, webBase: WebView, onWebViewLoad: OnWebViewLoad) {
        val webSettings = webBase.settings
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //加载缓存否则网络
        webSettings.loadsImagesAutomatically = true  //图片自动缩放 打开
        webBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null) //软件解码
        webBase.setLayerType(View.LAYER_TYPE_HARDWARE, null) //硬件解码

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
        /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/


        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true
        // 设置支持javascript脚本
        webSettings.javaScriptEnabled = true

        //webSettings.setPluginState(WebSettings.PluginState.ON);

        // 设置可以支持缩放
        webSettings.setSupportZoom(true)

        // 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.builtInZoomControls = true

        //隐藏缩放工具
        webSettings.displayZoomControls = false

        // 扩大比例的缩放
        webSettings.useWideViewPort = true

        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        webSettings.loadWithOverviewMode = true

        webSettings.databaseEnabled = true

        //保存密码
        webSettings.savePassword = true

        //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSettings.domStorageEnabled = true

        webBase.isSaveEnabled = true

        webBase.keepScreenOn = true

        // 设置setWebChromeClient对象
        webBase.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                onWebViewLoad.onReceivedTitle(title)
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onWebViewLoad.onProgressChanged(newProgress)
            }
        }

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webBase.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onWebViewLoad.onPageStarted()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!webBase.settings.loadsImagesAutomatically) {
                    webBase.settings.loadsImagesAutomatically = true
                }
                onWebViewLoad.onPageFinished()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                onWebViewLoad.shouldOverrideUrlLoading()
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url)
                    return false
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.


                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }
        }
        webBase.setDownloadListener { paramAnonymousString1, paramAnonymousString2, paramAnonymousString3, paramAnonymousString4, paramAnonymousLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramAnonymousString1)
            context.startActivity(intent)
        }
    }

    @JvmStatic
    fun loadData(webView: WebView, content: String?) {
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null) //这种写法可以正确解码
    }
}