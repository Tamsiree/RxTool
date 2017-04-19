package com.vondear.rxtools.model.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.vondear.rxtools.interfaces.onRequestListener;

import java.util.Map;

public class AliPayTools {

    private static final int SDK_PAY_FLAG = 1;

    private static onRequestListener sOnRequestListener;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        sOnRequestListener.onSuccess(resultStatus);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        sOnRequestListener.onError(resultStatus);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public static void aliPay(final Activity activity, String appid, boolean isRsa2, String alipay_rsa_private, AliPayModel aliPayModel, onRequestListener onRxHttp1) {
        sOnRequestListener = onRxHttp1;
        Map<String, String> params = AliPayOrderInfoUtil.buildOrderParamMap(appid, isRsa2, aliPayModel.getOut_trade_no(), aliPayModel.getName(), aliPayModel.getMoney(), aliPayModel.getDetail());
        String orderParam = AliPayOrderInfoUtil.buildOrderParam(params);

        String privateKey = alipay_rsa_private;

        String sign = AliPayOrderInfoUtil.getSign(params, privateKey, isRsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
