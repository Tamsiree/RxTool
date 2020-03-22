package com.tamsiree.rxdemo.activity

import android.os.Bundle
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.SelfInfo
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxTimeTool.curTimeString
import com.tamsiree.rxkit.interfaces.OnSuccessAndErrorListener
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxpay.alipay.AliPayModel
import com.tamsiree.rxpay.alipay.AliPayTool
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_ali_pay.*

class ActivityAliPay : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ali_pay)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(this)
        payV2.setOnClickListener {                  //需要填写APPID 与 私钥
            AliPayTool.aliPay(mContext, SelfInfo.ALIPAY_APPID, true, SelfInfo.ALIPAY_RSA2_PRIVATE, AliPayModel(curTimeString, "0.01", "爱心", "一份爱心"), object : OnSuccessAndErrorListener {
                override fun onSuccess(s: String?) {
                    RxToast.success("支付成功")
                }

                override fun onError(s: String?) {
                    RxToast.error("支付失败$s")
                }
            })
        }
    }

    override fun initData() {

    }
}