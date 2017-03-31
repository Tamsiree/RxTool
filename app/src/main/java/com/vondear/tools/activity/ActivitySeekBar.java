package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import com.vondear.rxtools.view.RxSeekBar;
import com.vondear.tools.R;

import java.text.DecimalFormat;

public class ActivitySeekBar extends Activity {

    private RxSeekBar seekbar1;
    private RxSeekBar seekbar2;
    private TextView tv2;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);

        seekbar1 = (RxSeekBar)findViewById(R.id.seekbar1);
        seekbar2 = (RxSeekBar)findViewById(R.id.seekbar2);
        tv2 = (TextView)findViewById(R.id.progress2_tv);

        seekbar1.setValue(10);
        seekbar2.setValue(-0.5f,0.8f);

        seekbar1.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                seekbar1.setProgressDescription((int)min+"%");
            }
        });

        seekbar2.setOnRangeChangedListener(new RxSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RxSeekBar view, float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    tv2.setText(min + "-" + max);
                    seekbar2.setLeftProgressDescription(df.format(min));
                    seekbar2.setRightProgressDescription(df.format(max));
                }
            }
        });

    }
}
