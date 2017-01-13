package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.view.RxRunTextView;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRunTextView extends Activity {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.ll_include_title)
    LinearLayout mLlIncludeTitle;
    @BindView(R.id.tv_runtitle)
    RxRunTextView mTvRuntitle;
    @BindView(R.id.activity_run_text_view)
    RelativeLayout mActivityRunTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_text_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("RunTextView的使用");
    }

    @OnClick(R.id.iv_finish)
    public void onClick() {
        finish();
    }
}
