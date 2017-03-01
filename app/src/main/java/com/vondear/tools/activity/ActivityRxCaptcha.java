package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.RxCaptcha;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxCaptcha extends Activity {

    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_captcha);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_code)
    public void onClick() {
        ivCode.setImageBitmap(RxCaptcha.getInstance().getBitmap());
        tvCode.setText(RxCaptcha.getInstance().getCode());
    }
}
