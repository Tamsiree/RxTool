package com.tamsiree.rxkit

import android.R
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import java.util.*
import kotlin.system.exitProcess

/**
 * @author tamsiree
 * @date 2016/1/24
 *
 *
 * 封装Activity相关工具类
 */
object RxActivityTool {
    var activityStack: Stack<Activity?>? = null


    /**
     * 添加Activity 到栈
     *
     * @param activity Activity
     */
    @JvmStatic
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 从List中移除活动
     *
     * @param activity 活动
     */
    @JvmStatic
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            if (activityStack!!.contains(activity)) {
                activityStack?.remove(activity)
            }
        }
    }


    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    @JvmStatic
    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    @JvmStatic
    @JvmOverloads
    fun finishActivity(isTransition: Boolean = false) {
        val activity = activityStack?.lastElement()
        if (isTransition) {
            activity?.onBackPressed()
        } else {
            activity?.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    @JvmStatic
    fun finishActivity(cls: Class<out Activity>) {
        for (activity in activityStack!!) {
            if (activity!!.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        val size = activityStack!!.size
        for (i in 0 until size) {
            if (null != activityStack!![i]) {
                activityStack!![i]!!.finish()
            }
        }
        activityStack!!.clear()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(context.packageName)
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断是否存在指定Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isExistActivity(context: Context, packageName: String?, className: String?): Boolean {
        val intent = Intent()
        intent.setClassName(packageName!!, className!!)
        return !(context.packageManager.resolveActivity(intent, 0) == null || intent.resolveActivity(context.packageManager) == null || context.packageManager.queryIntentActivities(intent, 0).size == 0)
    }

    /**
     * 打开指定的Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     */
    @JvmStatic
    fun launchActivity(context: Context, packageName: String?, className: String?, bundle: Bundle? = null) {
        context.startActivity(RxIntentTool.getComponentNameIntent(packageName, className, bundle))
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context Context
     * @param goal    Activity
     */
    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    @JvmOverloads
    fun skipActivityAndFinishAll(context: Context, goal: Class<out Activity>?, bundle: Bundle? = null, isFade: Boolean = false) {
        val intent = Intent(context, goal)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        finishActivity(context, false)
        if (isFade) {
            fadeTransition(context)
        }
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, isFade: Boolean = false, isTransition: Boolean = false) {
        skipActivity(context, goal, null, isFade)
        finishActivity(context, isTransition)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    @JvmOverloads
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, bundle: Bundle? = null, isFade: Boolean = false, isTransition: Boolean = false) {
        skipActivity(context, goal, bundle, isFade)
        finishActivity(context, isTransition)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    @JvmOverloads
    fun skipActivity(context: Context, goal: Class<out Activity>?, bundle: Bundle? = null, isFade: Boolean = false) {
        val intent = Intent(context, goal)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
        if (isFade) {
            fadeTransition(context)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun skipActivityForResult(context: Activity, goal: Class<out Activity>?, bundle: Bundle? = null, requestCode: Int) {
        val intent = Intent(context, goal)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivityForResult(intent, requestCode)
    }

    @JvmStatic
    @JvmOverloads
    fun skipActivityOnTransitions(mContext: Context?, goal: Class<out Activity>?, bundle: Bundle? = null, vararg pairs: Pair<View, String>?) {
        val intent = Intent(mContext, goal)
        val bundle1 = ActivityOptions.makeSceneTransitionAnimation(mContext as Activity?, *pairs).toBundle()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        ActivityCompat.startActivity(mContext!!, intent, bundle1)
    }

    @JvmStatic
    @JvmOverloads
    fun skipActivityTransition(mContext: Context, goal: Class<out Activity>?, bundle: Bundle? = null, view: View?, elementName: String?) {
        val intent = Intent(mContext, goal)
        val bundle1 = ActivityOptionsCompat.makeSceneTransitionAnimation((mContext as Activity), view!!, elementName!!).toBundle()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent, bundle1)
    }

    /**
     * 获取launcher activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @return launcher activity
     */
    @JvmStatic
    fun getLauncherActivity(context: Context, packageName: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = context.packageManager
        val infos = pm.queryIntentActivities(intent, 0)
        for (info in infos) {
            if (info.activityInfo.packageName == packageName) {
                return info.activityInfo.name
            }
        }
        return "no $packageName"
    }

    @JvmStatic
    @JvmOverloads
    fun finishActivity(mContext: Context, isTransition: Boolean = false) {
        removeActivity((mContext as Activity))
        if (isTransition) {
            mContext.onBackPressed()
        } else {
            mContext.finish()
        }
    }

    @JvmStatic
    fun fadeTransition(mContext: Context) {
        (mContext as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}