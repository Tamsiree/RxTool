package com.tamsiree.rxkit

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * @author tamsiree
 * @date 2016/1/24
 */
object RxBarTool {
    /**
     * 隐藏状态栏
     *
     * 也就是设置全屏，一定要在setContentView之前调用，否则报错
     *
     * 此方法Activity可以继承AppCompatActivity
     *
     * 启动的时候状态栏会显示一下再隐藏，比如QQ的欢迎界面
     *
     * 在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
     *
     * 如加了以上配置Activity不能继承AppCompatActivity，会报错
     *
     * @param activity activity
     */
    @JvmStatic
    fun hideStatusBar(activity: Activity) {
        noTitle(activity)
        FLAG_FULLSCREEN(activity)
    }

    /**
     * 设置透明状态栏(api大于19方可使用)
     *
     * 可在Activity的onCreat()中调用
     *
     * 需在顶部控件布局中加入以下属性让内容出现在状态栏之下
     *
     * android:clipToPadding="true"
     *
     * android:fitsSystemWindows="true"
     *
     * @param activity activity
     */
    @JvmStatic
    fun setTransparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //透明状态栏
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) //透明导航栏
        }
    }

    /**
     * 隐藏Title
     * 一定要在setContentView之前调用，否则报错
     *
     * @param activity
     */
    @JvmStatic
    fun setNoTitle(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    @JvmStatic
    fun noTitle(activity: Activity) {
        setNoTitle(activity)
    }

    /**
     * 全屏
     * 也就是设置全屏，一定要在setContentView之前调用，否则报错
     *
     * @param activity
     */
    @JvmStatic
    fun FLAG_FULLSCREEN(activity: Activity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 判断状态栏是否存在
     *
     * @param activity activity
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    @JvmStatic
    fun isStatusBarExists(activity: Activity): Boolean {
        val params = activity.window.attributes
        return params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity activity
     * @return ActionBar高度
     */
    @JvmStatic
    fun getActionBarHeight(activity: Activity): Int {
        val tv = TypedValue()
        return if (activity.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, activity.resources.displayMetrics)
        } else 0
    }

    /**
     * 显示通知栏
     *
     * 需添加权限 `<uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>`
     *
     * @param context        上下文
     * @param isSettingPanel `true`: 打开设置<br></br>`false`: 打开通知
     */
    @JvmStatic
    fun showNotificationBar(context: Context, isSettingPanel: Boolean) {
        val methodName = if (Build.VERSION.SDK_INT <= 16) "expand" else if (isSettingPanel) "expandSettingsPanel" else "expandNotificationsPanel"
        invokePanels(context, methodName)
    }

    /**
     * 隐藏通知栏
     *
     * 需添加权限 `<uses-permission android:name="android.permission.EXPAND_STATUS_BAR"/>`
     *
     * @param context 上下文
     */
    @JvmStatic
    fun hideNotificationBar(context: Context) {
        val methodName = if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
        invokePanels(context, methodName)
    }

    /**
     * 反射唤醒通知栏
     *
     * @param context    上下文
     * @param methodName 方法名
     */
    private fun invokePanels(context: Context, methodName: String) {
        try {
            val service = context.getSystemService("statusbar")
            val statusBarManager = Class.forName("android.app.StatusBarManager")
            val expand = statusBarManager.getMethod(methodName)
            expand.invoke(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //==============================================================================================以下设置状态栏相关
    /**
     * 需要在布局中加入
     * android:clipToPadding="true"
     * android:fitsSystemWindows="true"
     * 这两行属性
     */
    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @JvmStatic
    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    @JvmStatic
    fun setStatusBarColor(activity: Activity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            //      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = activity.resources.getColor(colorId)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity)
            val tintManager = SystemBarTintManager(activity)
            tintManager.isStatusBarTintEnabled = true
            tintManager.setStatusBarTintResource(colorId)
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    @JvmStatic
    fun StatusBarLightMode(activity: Activity): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.window, true)) {
                result = 1
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                result = 2
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                result = 3
            }
        }
        return result
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    @JvmStatic
    fun StatusBarLightMode(activity: Activity, type: Int) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.window, true)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.window, true)
        } else if (type == 3) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    @JvmStatic
    fun StatusBarDarkMode(activity: Activity, type: Int) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.window, false)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.window, false)
        } else if (type == 3) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @JvmStatic
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @JvmStatic
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    } //==============================================================================================以上为设置状态栏相关
}