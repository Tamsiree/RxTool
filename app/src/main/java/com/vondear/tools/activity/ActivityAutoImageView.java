package com.vondear.tools.activity;

import android.os.Bundle;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.tools.R;
import com.vondear.rxtools.RxBarUtils;

public class ActivityAutoImageView extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        RxBarUtils.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);

    }
}
