package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.vontools.VonUtils;

public class ActivityCreateQRCode extends Activity implements View.OnClickListener {
    private LinearLayout ll_back, ll_menu;
    private TextView tv_title;
    private ImageView iv_code;
    private ImageView iv_linecode;
    private int time_second = 0;
    private TextView tv_time_second;
    private static android.os.Handler Handler = new Handler();
    private static Runnable mRunnable = null;
    private LinearLayout ll_refresh;
    private int second = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_qrcode);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 透明导航栏
        initView();
        initData();
        AuthCode(tv_time_second, second);
    }

    private void AuthCode(final TextView view, final int time_second) {

        mRunnable = new Runnable() {
            int mSumNum = time_second;

            @Override
            public void run() {
                Handler.postDelayed(mRunnable, 1000);
                view.setText(mSumNum + "");
                view.setEnabled(false);
                mSumNum--;
                if (mSumNum < 0) {
                    view.setText(0 + "");
                    view.setEnabled(true);
                    Message message = new Message();
                    message.what = 60000;
                    mHandler.sendMessage(message);
                    // 干掉这个定时器，下次减不会累加
                    Handler.removeCallbacks(mRunnable);
                    AuthCode(tv_time_second, second);
                }
            }

        };
        Handler.postDelayed(mRunnable, 1000);
    }

    private void initView() {
        ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh);
        tv_time_second = (TextView) findViewById(R.id.tv_time_second);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setVisibility(View.VISIBLE);
        ll_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("动态生成码");
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_menu.setVisibility(View.VISIBLE);
        iv_code = (ImageView) findViewById(R.id.iv_code);
        iv_linecode = (ImageView) findViewById(R.id.iv_linecode);
        ll_menu.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);
    }

    private void initData() {
        // TODO Auto-generated method stub
        VonUtils.createQRImage("时间戳:" + System.currentTimeMillis(), 800, 800, iv_code);
        iv_linecode.setImageBitmap(VonUtils.drawLinecode(this, "" + System.currentTimeMillis(), 1000, 300));
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_refresh:
                Handler.removeCallbacks(mRunnable);
                initData();
                tv_time_second.setText(second + "");
                AuthCode(tv_time_second, second);
                break;
            default:
                break;
        }
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 60000:
                    initData();
                    break;
                default:
                    break;
            }
        }
    };
}
