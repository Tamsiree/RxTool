package com.tamsiree.rxkit.interfaces

/**
 * Created by Tamsiree on 2017/8/14.
 */
interface OnWebViewLoad {
    fun onPageStarted()
    fun onReceivedTitle(title: String?)
    fun onProgressChanged(newProgress: Int)
    fun shouldOverrideUrlLoading()
    fun onPageFinished()
}