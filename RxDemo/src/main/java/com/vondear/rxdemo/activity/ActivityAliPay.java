package com.vondear.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vondear.rxdemo.R;
import com.vondear.rxdemo.SelfInfo;
import com.vondear.rxfeature.module.alipay.AliPayModel;
import com.vondear.rxfeature.module.alipay.AliPayTools;
import com.vondear.rxtool.RxTimeTool;
import com.vondear.rxtool.interfaces.OnSuccessAndErrorListener;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.activity.ActivityBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityAliPay extends ActivityBase {

    @BindView(R.id.iv_alipay)
    ImageView mIvAlipay;
    @BindView(R.id.payV2)
    Button mPayV2;
    @BindView(R.id.authV2)
    Button mAuthV2;
    @BindView(R.id.h5pay)
    Button mH5pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.payV2, R.id.authV2, R.id.h5pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.payV2:
                //需要填写APPID 与 私钥
                AliPayTools.aliPay(mContext, SelfInfo.ALIPAY_APPID, true, SelfInfo.ALIPAY_RSA2_PRIVATE, new AliPayModel(RxTimeTool.date2String(RxTimeTool.getCurTimeDate()), "0.01", "爱心", "一份爱心"), new OnSuccessAndErrorListener() {
                    @Override
                    public void onSuccess(String s) {
                        RxToast.success("支付成功");
                    }

                    @Override
                    public void onError(String s) {
                        RxToast.error("支付失败" + s);
                    }
                });
                break;
            case R.id.authV2:
                break;
            case R.id.h5pay:
                break;
            default:
                break;
        }
    }
}
