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
        activityStack!!.add(activity)
    }

    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    @JvmStatic
    fun currentActivity(): Activity? {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    @JvmStatic
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        activity!!.finish()
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    @JvmStatic
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
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

    @JvmStatic
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(context.packageName)
            System.exit(0)
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
     * @param bundle      bundle
     */
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
    @JvmStatic
    fun skipActivityAndFinishAll(context: Context, goal: Class<out Activity>?, isFade: Boolean) {
        skipActivityAndFinishAll(context, goal, null, isFade)
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
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?) {
        skipActivity(context, goal, null, false)
        finishActivity(context, false)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, isFade: Boolean) {
        skipActivity(context, goal, null, isFade)
        finishActivity(context, false)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, bundle: Bundle?) {
        skipActivity(context, goal, bundle, false)
        finishActivity(context, false)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, isFade: Boolean, isTransition: Boolean) {
        skipActivity(context, goal, isFade)
        finishActivity(context, isTransition)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivityAndFinish(context: Context, goal: Class<out Activity>?, bundle: Bundle?, isFade: Boolean, isTransition: Boolean) {
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
    fun skipActivity(context: Context, goal: Class<out Activity>?, bundle: Bundle?) {
        val intent = Intent(context, goal)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }

    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivity(context: Context, goal: Class<out Activity>?, isFade: Boolean) {
        skipActivity(context, goal, null, isFade)
    }
    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    /**
     * Activity 跳转
     *
     * @param context Context
     * @param goal    Activity
     */
    @JvmStatic
    fun skipActivity(context: Context, goal: Class<out Activity>?, bundle: Bundle? = null, isFade: Boolean = false) {
        skipActivity(context, goal, bundle)
        if (isFade) {
            fadeTransition(context)
        }
    }

    @JvmStatic
    fun skipActivityForResult(context: Activity, goal: Class<out Activity>?, requestCode: Int) {
        skipActivityForResult(context, goal, null, requestCode)
    }

    @JvmStatic
    fun skipActivityForResult(context: Activity, goal: Class<out Activity>?, bundle: Bundle?, requestCode: Int) {
        val intent = Intent(context, goal)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivityForResult(intent, requestCode)
    }

    @JvmStatic
    fun skipActivityOnTransitions(mContext: Context?, goal: Class<out Activity>?, bundle: Bundle?, vararg pairs: Pair<View?, String?>?) {
        val intent = Intent(mContext, goal)
        val bundle1 = ActivityOptions.makeSceneTransitionAnimation(mContext as Activity?, *pairs).toBundle()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        ActivityCompat.startActivity(mContext!!, intent, bundle1)
    }

    @JvmStatic
    fun skipActivityTransition(mContext: Context, goal: Class<out Activity>?, bundle: Bundle?, view: View?, elementName: String?) {
        val intent = Intent(mContext, goal)
        val bundle1 = ActivityOptionsCompat.makeSceneTransitionAnimation((mContext as Activity), view!!, elementName!!).toBundle()
        intent.putExtras(bundle!!)
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
    fun finishActivity(mContext: Context, isTransition: Boolean) {
        if (isTransition) {
            (mContext as Activity).onBackPressed()
        } else {
            (mContext as Activity).finish()
        }
    }

    @JvmStatic
    fun fadeTransition(mContext: Context) {
        (mContext as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}