package com.vondear.tools.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vondear.rxtools.RxActivityUtils;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySeat extends AppCompatActivity {

    @BindView(R.id.btn_movie)
    Button mBtnMovie;
    @BindView(R.id.btn_flight)
    Button mBtnFlight;
    @BindView(R.id.activity_seat)
    LinearLayout mActivitySeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_movie, R.id.btn_flight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_movie:
                RxActivityUtils.skipActivity(ActivitySeat.this, ActivityMovieSeat.class);
                break;
            case R.id.btn_flight:
                RxActivityUtils.skipActivity(ActivitySeat.this, ActivityFlightSeat.class);
                break;
        }
    }
}
