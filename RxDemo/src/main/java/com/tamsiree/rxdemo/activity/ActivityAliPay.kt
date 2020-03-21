package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.SelfInfo
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxTimeTool.curTimeString
import com.tamsiree.rxkit.interfaces.OnSuccessAndErrorListener
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxpay.alipay.AliPayModel
import com.tamsiree.rxpay.alipay.AliPayTool
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

class ActivityAliPay : ActivityBase() {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.payV2)
    var mPayV2: Button? = null

    @JvmField
    @BindView(R.id.authV2)
    var mAuthV2: Button? = null

    @JvmField
    @BindView(R.id.h5pay)
    var mH5pay: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ali_pay)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(this)
    }

    @OnClick(R.id.payV2, R.id.authV2, R.id.h5pay)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.payV2 ->                 //需要填写APPID 与 私钥
                AliPayTool.aliPay(mContext, SelfInfo.ALIPAY_APPID, true, SelfInfo.ALIPAY_RSA2_PRIVATE, AliPayModel(curTimeString, "0.01", "爱心", "一份爱心"), object : OnSuccessAndErrorListener {
                    override fun onSuccess(s: String?) {
                        RxToast.success("支付成功")
                    }

                    override fun onError(s: String?) {
                        RxToast.error("支付失败$s")
                    }
                })
            R.id.authV2 -> {
            }
            R.id.h5pay -> {
            }
            else -> {
            }
        }
    }
}