package com.tamsiree.rxdemo.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxCaptcha
import com.tamsiree.rxui.view.RxCaptcha.TYPE
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.swipecaptcha.RxSwipeCaptcha
import com.tamsiree.rxui.view.swipecaptcha.RxSwipeCaptcha.OnCaptchaMatchCallback

/**
 * @author tamsiree
 */
class ActivityRxCaptcha : ActivityBase() {
    @JvmField
    @BindView(R.id.tv_code)
    var tvCode: TextView? = null

    @JvmField
    @BindView(R.id.iv_code)
    var ivCode: ImageView? = null

    @JvmField
    @BindView(R.id.btn_get_code)
    var btnGetCode: Button? = null

    @JvmField
    @BindView(R.id.swipeCaptchaView)
    var mRxSwipeCaptcha: RxSwipeCaptcha? = null

    @JvmField
    @BindView(R.id.dragBar)
    var mSeekBar: SeekBar? = null

    @JvmField
    @BindView(R.id.btnChange)
    var mBtnChange: Button? = null

    @JvmField
    @BindView(R.id.activity_rx_captcha)
    var mActivityRxCaptcha: LinearLayout? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_captcha)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        mRxSwipeCaptcha!!.onCaptchaMatchCallback = object : OnCaptchaMatchCallback {
            override fun matchSuccess(rxSwipeCaptcha: RxSwipeCaptcha) {
                RxToast.success(mContext!!, "验证通过！", Toast.LENGTH_SHORT).show()
                //swipeCaptcha.createCaptcha();
                mSeekBar!!.isEnabled = false
            }

            override fun matchFailed(rxSwipeCaptcha: RxSwipeCaptcha) {
                Log.d("zxt", "matchFailed() called with: rxSwipeCaptcha = [$rxSwipeCaptcha]")
                RxToast.error(mContext!!, "验证失败:拖动滑块将悬浮头像正确拼合", Toast.LENGTH_SHORT).show()
                rxSwipeCaptcha.resetCaptcha()
                mSeekBar!!.progress = 0
            }
        }
        mSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mRxSwipeCaptcha!!.setCurrentSwipeValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //随便放这里是因为控件
                mSeekBar!!.max = mRxSwipeCaptcha!!.maxSwipeValue
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [$seekBar]")
                mRxSwipeCaptcha!!.matchCaptcha()
            }
        })
        val simpleTarget: SimpleTarget<Drawable?> = object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                mRxSwipeCaptcha!!.setImageDrawable(resource)
                mRxSwipeCaptcha!!.createCaptcha()
            }
        }

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load(R.drawable.douyu)
                .into(simpleTarget)
    }

    @OnClick(R.id.btn_get_code, R.id.btnChange)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_get_code -> {
                RxCaptcha.build()
                        .backColor(0xffffff)
                        .codeLength(6)
                        .fontSize(60)
                        .lineNumber(0)
                        .size(200, 70)
                        .type(TYPE.CHARS)
                        .into(ivCode)
                tvCode!!.text = RxCaptcha.build().code
            }
            R.id.btnChange -> {
                mRxSwipeCaptcha!!.createCaptcha()
                mSeekBar!!.isEnabled = true
                mSeekBar!!.progress = 0
            }
            else -> {
            }
        }
    }
}