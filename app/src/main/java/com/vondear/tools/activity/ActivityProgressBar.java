package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.RxDataUtils;
import com.vondear.rxtools.view.RxRoundProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityProgressBar extends Activity {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_include_title)
    LinearLayout mLlIncludeTitle;
    @BindView(R.id.roundProgressBar1)
    RxRoundProgress mRxRoundProgress1;
    @BindView(R.id.activity_round_progress_bar)
    LinearLayout mActivityRoundProgressBar;
    @BindView(R.id.pb_line_of_credit)
    ProgressBar mPbLineOfCredit;

    private double progress;
    private int progress1;
    private double max_money = 0;
    double money = 1000;
    private int money1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        setContentView(R.layout.activity_progress_bar);
        ButterKnife.bind(this);
        initView();
        initRoundProgress();
        initLineProgress();
    }

    private void initView() {
        mLlIncludeTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTvTitle.setText("进度条视图");
    }

    private void initRoundProgress() {
        // TODO Auto-generated method stub
        progress = 0;// 进度初始化

        if (max_money <= 0) {
            if (money < 100 && money > 0) {
                mRxRoundProgress1.setMax(100);
            } else if (money >= 100 && money < 1000) {
                mRxRoundProgress1.setMax(1000);
            } else if (money >= 1000 && money < 5000) {
                mRxRoundProgress1.setMax(5000);
            } else if (money >= 5000 && money < 20000) {
                mRxRoundProgress1.setMax(20000);
            } else if (money >= 20000 && money < 100000) {
                mRxRoundProgress1.setMax(100000);
            } else if (money >= 100000) {
                mRxRoundProgress1.setMax(RxDataUtils.stringToInt(money * 1.1 + ""));
            }
        } else {
            mRxRoundProgress1.setMax(max_money);
        }
        /**/
        /*
         * int i = (new Double(max_money)).intValue();
		 * mRoundProgressBar1.setMax((i));
		 */

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (progress < mRxRoundProgress1.getMax()) {
                        progress += mRxRoundProgress1.getMax() * 0.01;
                        if (progress < mRxRoundProgress1.getMax()) {
                            mRxRoundProgress1.setProgress(progress);
                        }
                        Thread.sleep(8);
                    }
                    while (progress > 0) {
                        progress -= mRxRoundProgress1.getMax() * 0.01;
                        if (progress > 0) {
                            mRxRoundProgress1.setProgress(progress);
                        }
                        Thread.sleep(8);
                    }

                    if (money != 0) {
                        while (progress < money) {
                            progress += money * 0.01;
                            mRxRoundProgress1.setProgress(progress);
                            Thread.sleep(10);
                        }
                    }

                    mRxRoundProgress1.setProgress(money);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initLineProgress() {
        // TODO Auto-generated method stub
        progress1 = 0;// 进度初始化

        money1 = 100001;
        if (money1 < 100 && money1 > 0) {
            mPbLineOfCredit.setMax(100);
        } else if (money1 > 100 && money1 < 1000) {
            mPbLineOfCredit.setMax(1000);
        } else if (money1 > 1000 && money1 < 5000) {
            mPbLineOfCredit.setMax(5000);
        } else if (money1 > 5000 && money1 < 20000) {
            mPbLineOfCredit.setMax(20000);
        } else if (money1 > 20000 && money1 < 100000) {
            mPbLineOfCredit.setMax(100000);
        } else if (money1 > 100000) {
            mPbLineOfCredit.setMax(RxDataUtils.stringToInt(money1 * 2 + ""));
        }
        /*int i =  (new Double(SysCtlUtil.StringToDouble(quota))).intValue();
        final int current = (new Double(SysCtlUtil.StringToDouble(quota_surplus))).intValue() ;
		mPbLineOfCredit.setMax((i));*/
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (progress1 < mPbLineOfCredit.getMax()) {
                        progress1 += mPbLineOfCredit.getMax() * 0.01;
                        if (progress1 < mPbLineOfCredit.getMax()) {
                            mPbLineOfCredit.setProgress(progress1);
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8);
                    }
                    while (progress1 > 0) {
                        progress1 -= mPbLineOfCredit.getMax() * 0.01;
                        if (progress1 > 0) {
                            mPbLineOfCredit.setProgress(progress1);
                            //tv_current.setText(progress1+"");
                        }
                        Thread.sleep(8);
                    }

                    while (progress1 < money1) {
                        progress1 += money1 * 0.01;
                        mPbLineOfCredit.setProgress(progress1);
                        //tv_current.setText(progress1+"");
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @OnClick(R.id.iv_finish)
    public void onClick() {
        finish();
    }
}
