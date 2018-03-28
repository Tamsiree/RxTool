package com.vondear.rxtools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vondear.rxtools.RxActivityTool;

/**
 * @author vondear
 */
public class ActivityBase extends FragmentActivity {

    public ActivityBase mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityTool.addActivity(this);
//        DragAndDropPermissionsCompat
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
