package com.vondear.rxdemo.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.vondear.rxdemo.R;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxui.activity.ActivityBase;
import com.vondear.rxui.view.RxSeatMovie;
import com.vondear.rxui.view.RxTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author vondear
 */
public class ActivityMovieSeat extends ActivityBase {

    @BindView(R.id.seatView)
    RxSeatMovie mSeatView;
    @BindView(R.id.activity_movie_seat)
    LinearLayout mActivityMovieSeat;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_movie_seat);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        initView();
    }


    protected void initView() {
        mRxTitle.setLeftFinish(mContext);

        mSeatView.setScreenName("3号厅荧幕");//设置屏幕名称
        mSeatView.setMaxSelected(8);//设置最多选中

        mSeatView.setSeatChecker(new RxSeatMovie.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                return !(column == 2 || column == 12);
            }

            @Override
            public boolean isSold(int row, int column) {
                return row == 6 && column == 6;
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
