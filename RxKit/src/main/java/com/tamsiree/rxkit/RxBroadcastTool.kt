package com.tamsiree.rxkit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

/**
 *
 * @author Tamsiree
 * @date 2017/3/15
 */
object RxBroadcastTool {
    /**
     * 注册监听网络状态的广播
     * @param context
     * @return
     */
    @JvmStatic
    fun initRegisterReceiverNetWork(context: Context): BroadcastReceiverNetWork {
        // 注册监听网络状态的服务
        val mReceiverNetWork = BroadcastReceiverNetWork()
        val mFilter = IntentFilter()
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(mReceiverNetWork, mFilter)
        return mReceiverNetWork
    }

    /**
     * 网络状态改变广播
     */
    class BroadcastReceiverNetWork : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            RxNetTool.getNetWorkType(context)
        }
    }
}