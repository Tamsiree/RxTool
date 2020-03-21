package com.tamsiree.rxkit

import android.app.ActivityManager
import android.content.Context

/**
 * @author tamsiree
 * @date 2016/9/24
 */
object RxServiceTool {
    /**
     * 获取服务是否开启
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isRunningService(context: Context, className: String): Boolean {
        // 进程的管理者,活动的管理者
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取正在运行的服务，最多获取1000个
        val runningServices = activityManager.getRunningServices(1000)
        // 遍历集合
        for (runningServiceInfo in runningServices) {
            val service = runningServiceInfo.service
            if (className == service.className) {
                return true
            }
        }
        return false
    }
}