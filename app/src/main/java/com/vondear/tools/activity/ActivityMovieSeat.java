package com.vondear.tools.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxMovieSeatTable;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMovieSeat extends ActivityBase {

    @BindView(R.id.seatView)
    RxMovieSeatTable mSeatView;
    @BindView(R.id.activity_movie_seat)
    LinearLayout mActivityMovieSeat;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        RxBarUtils.setTransparentStatusBar(this);
        setContentView(R.layout.activity_movie_seat);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxToast.info(mContext,"退出成功").show();
            }
        });

        mSeatView.setScreenName("3号厅荧幕");//设置屏幕名称
        mSeatView.setMaxSelected(8);//设置最多选中

        mSeatView.setSeatChecker(new RxMovieSeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if (column == 2 || column == 12) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if (row == 6 && column == 6) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        mSeatView.setData(10, 15);
    }
}
