package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.view.RxSeatAirplane;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityFlightSeat extends Activity {

    @BindView(R.id.fsv)
    RxSeatAirplane mFlightSeatView;
    @BindView(R.id.btn_clear)
    Button mBtnClear;
    @BindView(R.id.btn_zoom)
    Button mBtnZoom;
    @BindView(R.id.btn_goto)
    Button mBtnGoto;
    @BindView(R.id.activity_flight_seat)
    LinearLayout mActivityFlightSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        RxBarUtils.setTransparentStatusBar(this);
        setContentView(R.layout.activity_flight_seat);
        ButterKnife.bind(this);
        initView();

        setTestData();
    }

    private void initView() {
        mFlightSeatView.setMaxSelectStates(10);
    }

    public void zoom(View v) {
        mFlightSeatView.startAnim(true);
    }


    public void gotoposition(View v) {
        mFlightSeatView.goCabinPosition(RxSeatAirplane.CabinPosition.Middle);
    }


    public void clear(View v) {
        mFlightSeatView.setEmptySelecting();
    }


    private void setTestData() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j = j + 2) {
                mFlightSeatView.setSeatSelected(j, i);

            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j = j + 2) {
                mFlightSeatView.setSeatSelected(i + 20, j);

            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j = j + 3) {
                mFlightSeatView.setSeatSelected(i + 35, j);

            }
        }


        for (int i = 11; i < 20; i++) {
            for (int j = 0; j < 8; j = j + 4) {
                mFlightSeatView.setSeatSelected(i + 35, j);

            }
        }
        mFlightSeatView.invalidate();


    }


    @OnClick({R.id.btn_clear, R.id.btn_zoom, R.id.btn_goto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clear(view);
                break;
            case R.id.btn_zoom:
                zoom(view);
                break;
            case R.id.btn_goto:
                gotoposition(view);
                break;
        }
    }
}
