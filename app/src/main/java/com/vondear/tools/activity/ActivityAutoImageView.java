package com.vondear.tools.activity;

import android.os.Bundle;

import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.tools.R;

public class ActivityAutoImageView extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);
    }
}
