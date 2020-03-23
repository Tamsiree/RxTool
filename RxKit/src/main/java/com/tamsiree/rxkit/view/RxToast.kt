package com.tamsiree.rxkit.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.tamsiree.rxkit.R
import com.tamsiree.rxkit.RxTool

/**
 * @author tamsiree
 * @date 2018/06/11 14:20:10
 * 在系统的Toast基础上封装
 */
object RxToast {
    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#FD4C5B")

    @ColorInt
    private val INFO_COLOR = Color.parseColor("#2196F3")

    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#52BA97")

    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")
    private const val TOAST_TYPEFACE = "sans-serif-condensed"
    private var currentToast: Toast? = null
    //*******************************************普通 使用ApplicationContext 方法*********************
    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private var mToast: Toast? = null
    private var mExitTime: Long = 0

    @JvmStatic
    fun normal(message: String) {
        normal(RxTool.getContext(), message, Toast.LENGTH_SHORT, null, false)?.show()
    }

    @JvmStatic
    fun normal(message: String, icon: Drawable?) {
        normal(RxTool.getContext(), message, Toast.LENGTH_SHORT, icon, true)?.show()
    }

    @JvmStatic
    fun normal(message: String, duration: Int) {
        normal(RxTool.getContext(), message, duration, null, false)?.show()
    }

    @JvmStatic
    fun normal(message: String, duration: Int, icon: Drawable?) {
        normal(RxTool.getContext(), message, duration, icon, true)?.show()
    }

    @JvmStatic
    fun normal(message: String, duration: Int, icon: Drawable?, withIcon: Boolean): Toast? {
        return custom(RxTool.getContext(), message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @JvmStatic
    fun warning(message: String) {
        warning(RxTool.getContext(), message, Toast.LENGTH_SHORT, true)?.show()
    }

    @JvmStatic
    fun warning(message: String, duration: Int) {
        warning(RxTool.getContext(), message, duration, true)?.show()
    }

    @JvmStatic
    fun warning(message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(RxTool.getContext(), message, getDrawable(RxTool.getContext(), R.drawable.ic_error_outline_white_48dp), DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true)
    }

    @JvmStatic
    fun info(message: String) {
        info(RxTool.getContext(), message, Toast.LENGTH_SHORT, true)?.show()
    }

    @JvmStatic
    fun info(message: String, duration: Int) {
        info(RxTool.getContext(), message, duration, true)?.show()
    }

    @JvmStatic
    fun info(message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(RxTool.getContext(), message, getDrawable(RxTool.getContext(), R.drawable.ic_info_outline_white_48dp), DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true)
    }

    @JvmStatic
    fun success(message: String) {
        success(RxTool.getContext(), message, Toast.LENGTH_SHORT, true)?.show()
    }

    @JvmStatic
    fun success(message: String, duration: Int) {
        success(RxTool.getContext(), message, duration, true)?.show()
    }

    @JvmStatic
    fun success(message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(RxTool.getContext(), message, getDrawable(RxTool.getContext(), R.drawable.ic_check_white_48dp), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true)
    }

    @JvmStatic
    fun error(message: String) {
        error(RxTool.getContext(), message, Toast.LENGTH_SHORT, true)?.show()
    }

    //===========================================使用ApplicationContext 方法=========================
    //*******************************************常规方法********************************************
    @JvmStatic
    fun error(message: String, duration: Int) {
        error(RxTool.getContext(), message, duration, true)?.show()
    }

    @JvmStatic
    fun error(message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(RxTool.getContext(), message, getDrawable(RxTool.getContext(), R.drawable.ic_clear_white_48dp), DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true)
    }

    @CheckResult
    @JvmStatic
    fun normal(context: Context, message: String): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    @JvmStatic
    fun normal(context: Context, message: String, icon: Drawable?): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    @JvmStatic
    fun normal(context: Context, message: String, duration: Int): Toast? {
        return normal(context, message, duration, null, false)
    }

    @CheckResult
    @JvmStatic
    fun normal(context: Context, message: String, duration: Int, icon: Drawable?): Toast? {
        return normal(context, message, duration, icon, true)
    }

    @CheckResult
    @JvmStatic
    fun normal(context: Context, message: String, duration: Int, icon: Drawable?, withIcon: Boolean): Toast? {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @CheckResult
    @JvmStatic
    fun warning(context: Context, message: String): Toast? {
        return warning(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    @JvmStatic
    fun warning(context: Context, message: String, duration: Int): Toast? {
        return warning(context, message, duration, true)
    }

    @CheckResult
    @JvmStatic
    fun warning(context: Context, message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, getDrawable(context, R.drawable.ic_error_outline_white_48dp), DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true)
    }

    @CheckResult
    @JvmStatic
    fun info(context: Context, message: String): Toast? {
        return info(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    @JvmStatic
    fun info(context: Context, message: String, duration: Int): Toast? {
        return info(context, message, duration, true)
    }

    @CheckResult
    @JvmStatic
    fun info(context: Context, message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, getDrawable(context, R.drawable.ic_info_outline_white_48dp), DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true)
    }

    @CheckResult
    @JvmStatic
    fun success(context: Context, message: String): Toast? {
        return success(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    @JvmStatic
    fun success(context: Context, message: String, duration: Int): Toast? {
        return success(context, message, duration, true)
    }

    @CheckResult
    @JvmStatic
    fun success(context: Context, message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, getDrawable(context, R.drawable.ic_check_white_48dp), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true)
    }

    @CheckResult
    @JvmStatic
    fun error(context: Context, message: String): Toast? {
        return error(context, message, Toast.LENGTH_SHORT, true)
    }

    //===========================================常规方法============================================
    @CheckResult
    @JvmStatic
    fun error(context: Context, message: String, duration: Int): Toast? {
        return error(context, message, duration, true)
    }

    @CheckResult
    @JvmStatic
    fun error(context: Context, message: String, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, getDrawable(context, R.drawable.ic_clear_white_48dp), DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true)
    }

    @CheckResult
    @JvmStatic
    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false)
    }

    //*******************************************内需方法********************************************
    @CheckResult
    @JvmStatic
    fun custom(context: Context, message: String, @DrawableRes iconRes: Int, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean): Toast? {
        return custom(context, message, getDrawable(context, iconRes), textColor, tintColor, duration, withIcon, shouldTint)
    }

    @CheckResult
    @JvmStatic
    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean): Toast? {
        if (currentToast == null) {
            currentToast = Toast(context)
        } else {
            currentToast?.cancel()
            currentToast = null
            currentToast = Toast(context)
        }
        val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.toast_layout, null)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.toast_text)
        val drawableFrame: Drawable?
        drawableFrame = if (shouldTint) {
            tint9PatchDrawableFrame(context, tintColor)
        } else {
            getDrawable(context, R.drawable.toast_frame)
        }
        setBackground(toastLayout, drawableFrame)
        if (withIcon) {
            requireNotNull(icon) { "Avoid passing 'icon' as null if 'withIcon' is set to true" }
            setBackground(toastIcon, icon)
        } else {
            toastIcon.visibility = View.GONE
        }
        toastTextView.setTextColor(textColor)
        toastTextView.text = message
        toastTextView.typeface = Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL)
        currentToast?.view = toastLayout
        currentToast?.duration = duration
        return currentToast
    }

    @JvmStatic
    fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable? {
        val toastDrawable = getDrawable(context, R.drawable.toast_frame) as NinePatchDrawable?
        toastDrawable?.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return toastDrawable
    }

    //===========================================内需方法============================================
    //******************************************系统 Toast 替代方法***************************************
    @JvmStatic
    fun setBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    @JvmStatic
    fun showToast(context: Context?, str: String?, isLong: Boolean) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastShort(str: String?) {
        Toast.makeText(RxTool.getContext(), str, Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastShort(resId: Int) {
        Toast.makeText(RxTool.getContext(), RxTool.getContext().getString(resId), Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastLong(str: String?) {
        Toast.makeText(RxTool.getContext(), str, Toast.LENGTH_LONG).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastLong(resId: Int) {
        Toast.makeText(RxTool.getContext(), RxTool.getContext().getString(resId), Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    @JvmStatic
    fun showToast(msg: String?) {
        if (mToast == null) {
            mToast = Toast.makeText(RxTool.getContext(), msg, Toast.LENGTH_LONG)
        } else {
            mToast?.setText(msg)
        }
        mToast?.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(resId: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(RxTool.getContext(), RxTool.getContext().getString(resId), Toast.LENGTH_LONG)
        } else {
            mToast?.setText(RxTool.getContext().getString(resId))
        }
        mToast?.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    @JvmStatic
    fun showToast(context: Context, resId: Int, duration: Int) {
        showToast(context, context.getString(resId), duration)
    }
    //===========================================Toast 替代方法======================================
    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(context: Context?, msg: String?, duration: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration)
        } else {
            mToast?.setText(msg)
        }
        mToast?.show()
    }

    @JvmStatic
    fun doubleClickExit(): Boolean {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            normal("再按一次退出")
            mExitTime = System.currentTimeMillis()
            return false
        }
        return true
    }
}