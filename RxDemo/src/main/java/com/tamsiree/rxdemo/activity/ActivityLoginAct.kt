package com.tamsiree.rxdemo.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxAnimationTool.zoomIn
import com.tamsiree.rxkit.RxAnimationTool.zoomOut
import com.tamsiree.rxkit.RxBarTool.StatusBarLightMode
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxKeyboardTool.hideSoftInput
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.activity.AndroidBug5497Workaround
import kotlinx.android.synthetic.main.activity_login_act.*

/**
 * @author tamsiree
 */
class ActivityLoginAct : ActivityBase() {

    private var screenHeight = 0 //屏幕高度
    private var keyHeight = 0 //软件盘弹起后所占高度
    private val scale = 0.6f //logo缩放比例
    private val height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_act)
        setTransparentStatusBar(this) //状态栏透明化
        StatusBarLightMode(mContext)
        setPortrait(this)
        if (isFullScreen(this)) {
            AndroidBug5497Workaround.assistActivity(this)
        }
    }

    override fun initView() {
        screenHeight = this.resources.displayMetrics.heightPixels //获取屏幕高度
        keyHeight = screenHeight / 3 //弹起高度为屏幕高度的1/3
        initEvent()
    }

    override fun initData() {

    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun initEvent() {
        et_mobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.visibility == View.GONE) {
                    iv_clean_phone.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.visibility = View.GONE
                }
            }
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && clean_password.visibility == View.GONE) {
                    clean_password.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.visibility = View.GONE
                }
                if (s.toString().isEmpty()) {
                    return
                }
                if (!s.toString().matches(Regex("[A-Za-z0-9]+"))) {
                    val temp = s.toString()
                    Toast.makeText(mContext, "请输入数字或字母", Toast.LENGTH_SHORT).show()
                    s.delete(temp.length - 1, temp.length)
                    et_password.setSelection(s.length)
                }
            }
        })
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        scrollView.setOnTouchListener { v, event -> true }
        scrollView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
                Log.e("wenzhihao", "up------>" + (oldBottom - bottom))
                val dist = content.bottom - bottom
                if (dist > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -dist.toFloat())
                    mAnimatorTranslateY.duration = 300
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    zoomIn(logo, scale, dist.toFloat())
                }
                service.visibility = View.INVISIBLE
            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
                Log.e("wenzhihao", "down------>" + (bottom - oldBottom))
                if (content.bottom - oldBottom > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", content.translationY, 0f)
                    mAnimatorTranslateY.duration = 300
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    zoomOut(logo, scale)
                }
                service.visibility = View.VISIBLE
            }
        }
        btn_login.setOnClickListener { hideSoftInput(mContext) }

        iv_clean_phone.setOnClickListener { et_mobile.setText("") }
        clean_password.setOnClickListener { et_password.setText("") }
        iv_show_pwd.setOnClickListener {
            if (et_password.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                et_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                iv_show_pwd.setImageResource(R.drawable.pass_visuable)
            } else {
                et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                iv_show_pwd.setImageResource(R.drawable.pass_gone)
            }
            val pwd = et_password.text.toString()
            if (!TextUtils.isEmpty(pwd)) et_password.setSelection(pwd.length)
        }

    }

    fun isFullScreen(activity: Activity): Boolean {
        return activity.window.attributes.flags and
                WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }


}