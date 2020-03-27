package com.tamsiree.rxdemo.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxCaptcha
import com.tamsiree.rxui.view.swipecaptcha.RxSwipeCaptcha
import com.tamsiree.rxui.view.swipecaptcha.RxSwipeCaptcha.OnCaptchaMatchCallback
import kotlinx.android.synthetic.main.activity_rx_captcha.*

/**
 * @author tamsiree
 */
class ActivityRxCaptcha : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_captcha)
        setPortrait(this)
        initView()
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        swipeCaptchaView.onCaptchaMatchCallback = object : OnCaptchaMatchCallback {
            override fun matchSuccess(rxSwipeCaptcha: RxSwipeCaptcha) {
                RxToast.success(mContext, "验证通过！", Toast.LENGTH_SHORT)?.show()
                //swipeCaptcha.createCaptcha();
                dragBar.isEnabled = false
            }

            override fun matchFailed(rxSwipeCaptcha: RxSwipeCaptcha) {
                TLog.d("zxt", "matchFailed() called with: rxSwipeCaptcha = [$rxSwipeCaptcha]")
                RxToast.error(mContext, "验证失败:拖动滑块将悬浮头像正确拼合", Toast.LENGTH_SHORT)?.show()
                rxSwipeCaptcha.resetCaptcha()
                dragBar.progress = 0
            }
        }
        dragBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                swipeCaptchaView.setCurrentSwipeValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //随便放这里是因为控件
                dragBar.max = swipeCaptchaView.maxSwipeValue
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                TLog.d("zxt", "onStopTrackingTouch() called with: seekBar = [$seekBar]")
                swipeCaptchaView.matchCaptcha()
            }
        })
        val simpleTarget: SimpleTarget<Drawable?> = object : SimpleTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                swipeCaptchaView.setImageDrawable(resource)
                swipeCaptchaView.createCaptcha()
            }
        }

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load(R.drawable.douyu)
                .into(simpleTarget)

        btn_get_code.setOnClickListener {
            RxCaptcha.build()
                    .backColor(0xffffff)
                    .codeLength(6)
                    .fontSize(60)
                    .lineNumber(0)
                    .size(200, 70)
                    .type(RxCaptcha.TYPE.CHARS)
                    .into(iv_code)
            tv_code.text = RxCaptcha.build().code
        }
        btnChange.setOnClickListener {
            swipeCaptchaView.createCaptcha()
            dragBar.isEnabled = true
            dragBar.progress = 0
        }
    }

    override fun initData() {

    }

}