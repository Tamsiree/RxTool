package com.tamsiree.rxdemo.activity;

import android.os.Bundle;

import com.tamsiree.rxdemo.R;
import com.tamsiree.rxtool.RxBarTool;
import com.tamsiree.rxtool.RxDeviceTool;
import com.tamsiree.rxui.activity.ActivityBase;

/**
 * @author tamsiree
 */
public class ActivityAutoImageView extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_auto_image_view);
        RxDeviceTool.setPortrait(this);
    }
}
