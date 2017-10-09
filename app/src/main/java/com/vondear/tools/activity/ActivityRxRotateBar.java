package com.vondear.tools.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxRotateBar;
import com.vondear.rxtools.view.RxRotateBarBasic;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxRotateBar extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.ll_change)
    LinearLayout mLlChange;
    @BindView(R.id.rating_view)
    RxRotateBar mRatingView;
    @BindView(R.id.btn_change)
    Button mBtnChange;

    RxRotateBarBasic bar1;
    RxRotateBarBasic bar2;
    RxRotateBarBasic bar3;
    RxRotateBarBasic bar4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_rotate_bar);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);

        bar1 = new RxRotateBarBasic(5, "魅力");
        bar2 = new RxRotateBarBasic(8, "财力");
        bar3 = new RxRotateBarBasic(3, "精力");
        bar4 = new RxRotateBarBasic(4, "体力");
        mRatingView.setAnimatorListener(new RxRotateBar.AnimatorListener() {
            @Override
            public void onRotateStart() {

            }

            @Override
            public void onRotateEnd() {
                mBtnChange.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRatingStart() {

            }

            @Override
            public void onRatingEnd() {

            }
        });
        mRatingView.addRatingBar(bar1);
        mRatingView.addRatingBar(bar2);
        mRatingView.addRatingBar(bar3);
        mRatingView.addRatingBar(bar4);
        mRatingView.show();
    }

    @OnClick(R.id.btn_change)
    public void onViewClicked() {
        mBtnChange.setVisibility(View.GONE);
        mLlChange.setBackgroundColor(Color.WHITE);
        mRatingView.setCenterTextColor(R.color.navy);
        mRatingView.clear();
        bar1.setRatedColor(getResources().getColor(R.color.google_red));
        bar1.setOutlineColor(getResources().getColor(R.color.google_red));
        bar1.setRatingBarColor(RxImageTool.changeColorAlpha(getResources().getColor(R.color.google_red), 130));
        bar1.setRate(9);
        bar2.setRatedColor(getResources().getColor(R.color.google_yellow));
        bar2.setOutlineColor(getResources().getColor(R.color.google_yellow));
        bar2.setRatingBarColor(RxImageTool.changeColorAlpha(getResources().getColor(R.color.google_yellow), 130));
        bar2.setRate(9);
        bar3.setRatedColor(getResources().getColor(R.color.darkslateblue));
        bar3.setOutlineColor(getResources().getColor(R.color.darkslateblue));
        bar3.setRatingBarColor(RxImageTool.changeColorAlpha(getResources().getColor(R.color.darkslateblue), 130));
        bar3.setRate(9);
        bar4.setRatedColor(getResources().getColor(R.color.google_green));
        bar4.setOutlineColor(getResources().getColor(R.color.google_green));
        bar4.setRatingBarColor(RxImageTool.changeColorAlpha(getResources().getColor(R.color.google_green), 130));
        bar4.setRate(9);
        mRatingView.show();
    }
}
