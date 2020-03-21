package com.tamsiree.rxkit

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.tamsiree.rxkit.RxFileTool.Companion.getUriForFile
import java.io.File

/**
 *
 * @author tamsiree
 * @date 2016/1/24
 */
object RxIntentTool {
    /**
     * 获取安装App(支持7.0)的意图
     *
     * @param context
     * @param filePath
     * @return
     */
    @JvmStatic
    fun getInstallAppIntent(context: Context?, filePath: String?): Intent? {
        //apk文件的本地路径
        val apkfile = File(filePath)
        if (!apkfile.exists()) {
            return null
        }
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri = getUriForFile(context!!, apkfile)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        return intent
    }

    /**
     * 获取卸载App的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    @JvmStatic
    fun getUninstallAppIntent(packageName: String): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    @JvmStatic
    fun getLaunchAppIntent(context: Context, packageName: String): Intent? {
        return getIntentByPackageName(context, packageName)
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private fun getIntentByPackageName(context: Context, packageName: String): Intent? {
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }

    /**
     * 获取App信息的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    @JvmStatic
    fun getAppInfoIntent(packageName: String): Intent {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        return intent.setData(Uri.parse("package:$packageName"))
    }

    /**
     * 获取App信息分享的意图
     *
     * @param info 分享信息
     * @return 意图
     */
    @JvmStatic
    fun getShareInfoIntent(info: String?): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        return intent.putExtra(Intent.EXTRA_TEXT, info)
    }

    /**
     * 获取其他应用的Intent
     *
     * @param packageName 包名
     * @param className   全类名
     * @return 意图
     */
    @JvmStatic
    fun getComponentNameIntent(packageName: String?, className: String?): Intent {
        return getComponentNameIntent(packageName, className, null)
    }

    /**
     * 获取其他应用的Intent
     *
     * @param packageName 包名
     * @param className   全类名
     * @return 意图
     */
    @JvmStatic
    fun getComponentNameIntent(packageName: String?, className: String?, bundle: Bundle?): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        if (bundle != null) intent.putExtras(bundle)
        val cn = ComponentName(packageName!!, className!!)
        intent.component = cn
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取应用详情页面具体设置 intent
     *
     * @return
     */
    @JvmStatic
    fun getAppDetailsSettingsIntent(mContext: Context): Intent {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", mContext.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.packageName)
        }
        return localIntent
    }

    /**
     * 获取应用详情页面具体设置 intent
     *
     * @param packageName 包名
     * @return intent
     */
    @JvmStatic
    fun getAppDetailsSettingsIntent(packageName: String?): Intent {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName)
        }
        return localIntent
    }
}