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
import android.widget.*
import androidx.core.widget.NestedScrollView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxAnimationTool.zoomIn
import com.tamsiree.rxkit.RxAnimationTool.zoomOut
import com.tamsiree.rxkit.RxBarTool.StatusBarLightMode
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxKeyboardTool.hideSoftInput
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.activity.AndroidBug5497Workaround

/**
 * @author tamsiree
 */
class ActivityLoginAct : ActivityBase() {
    @JvmField
    @BindView(R.id.logo)
    var mLogo: ImageView? = null

    @JvmField
    @BindView(R.id.et_mobile)
    var mEtMobile: EditText? = null

    @JvmField
    @BindView(R.id.iv_clean_phone)
    var mIvCleanPhone: ImageView? = null

    @JvmField
    @BindView(R.id.et_password)
    var mEtPassword: EditText? = null

    @JvmField
    @BindView(R.id.clean_password)
    var mCleanPassword: ImageView? = null

    @JvmField
    @BindView(R.id.iv_show_pwd)
    var mIvShowPwd: ImageView? = null

    @JvmField
    @BindView(R.id.btn_login)
    var mBtnLogin: Button? = null

    @JvmField
    @BindView(R.id.regist)
    var mRegist: TextView? = null

    @JvmField
    @BindView(R.id.forget_password)
    var mForgetPassword: TextView? = null

    @JvmField
    @BindView(R.id.content)
    var mContent: LinearLayout? = null

    @JvmField
    @BindView(R.id.scrollView)
    var mScrollView: NestedScrollView? = null

    @JvmField
    @BindView(R.id.service)
    var mService: LinearLayout? = null

    @JvmField
    @BindView(R.id.root)
    var mRoot: RelativeLayout? = null
    private var screenHeight = 0 //屏幕高度
    private var keyHeight = 0 //软件盘弹起后所占高度
    private val scale = 0.6f //logo缩放比例
    private val height = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_act)
        setTransparentStatusBar(this) //状态栏透明化
        StatusBarLightMode(mContext!!)
        ButterKnife.bind(this)
        setPortrait(this)
        if (isFullScreen(this)) {
            AndroidBug5497Workaround.assistActivity(this)
        }
        initView()
        initEvent()
    }

    private fun initView() {
        screenHeight = this.resources.displayMetrics.heightPixels //获取屏幕高度
        keyHeight = screenHeight / 3 //弹起高度为屏幕高度的1/3
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun initEvent() {
        mEtMobile!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && mIvCleanPhone!!.visibility == View.GONE) {
                    mIvCleanPhone!!.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPhone!!.visibility = View.GONE
                }
            }
        })
        mEtPassword!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && mCleanPassword!!.visibility == View.GONE) {
                    mCleanPassword!!.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPassword!!.visibility = View.GONE
                }
                if (s.toString().isEmpty()) {
                    return
                }
                if (!s.toString().matches(Regex("[A-Za-z0-9]+"))) {
                    val temp = s.toString()
                    Toast.makeText(mContext, "请输入数字或字母", Toast.LENGTH_SHORT).show()
                    s.delete(temp.length - 1, temp.length)
                    mEtPassword!!.setSelection(s.length)
                }
            }
        })
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        mScrollView!!.setOnTouchListener { v, event -> true }
        mScrollView!!.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
                Log.e("wenzhihao", "up------>" + (oldBottom - bottom))
                val dist = mContent!!.bottom - bottom
                if (dist > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist.toFloat())
                    mAnimatorTranslateY.duration = 300
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    zoomIn(mLogo!!, 0.6f, dist.toFloat())
                }
                mService!!.visibility = View.INVISIBLE
            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
                Log.e("wenzhihao", "down------>" + (bottom - oldBottom))
                if (mContent!!.bottom - oldBottom > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent!!.translationY, 0f)
                    mAnimatorTranslateY.duration = 300
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    zoomOut(mLogo!!, 0.6f)
                }
                mService!!.visibility = View.VISIBLE
            }
        }
        mBtnLogin!!.setOnClickListener { hideSoftInput(mContext!!) }
    }

    fun isFullScreen(activity: Activity): Boolean {
        return activity.window.attributes.flags and
                WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    @OnClick(R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.iv_clean_phone -> mEtMobile!!.setText("")
            R.id.clean_password -> mEtPassword!!.setText("")
            R.id.iv_show_pwd -> {
                if (mEtPassword!!.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEtPassword!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    mIvShowPwd!!.setImageResource(R.drawable.pass_visuable)
                } else {
                    mEtPassword!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mIvShowPwd!!.setImageResource(R.drawable.pass_gone)
                }
                val pwd = mEtPassword!!.text.toString()
                if (!TextUtils.isEmpty(pwd)) mEtPassword!!.setSelection(pwd.length)
            }
        }
    }
}