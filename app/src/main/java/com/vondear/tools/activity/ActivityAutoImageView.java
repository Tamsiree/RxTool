package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;

import com.vondear.tools.R;
import com.vondear.vontools.VonBarUtils;

public class ActivityAutoImageView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VonBarUtils.noTitle(this);
        VonBarUtils.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);
    }
}
