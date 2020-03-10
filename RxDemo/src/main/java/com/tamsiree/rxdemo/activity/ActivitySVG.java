package com.tamsiree.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.android.widget.AnimatedSvgView;
import com.tamsiree.rxdemo.R;
import com.tamsiree.rxdemo.model.ModelSVG;
import com.tamsiree.rxkit.RxActivityTool;
import com.tamsiree.rxkit.RxBarTool;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxTool;
import com.tamsiree.rxui.activity.ActivityBase;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jaredrummler.android.widget.AnimatedSvgView.STATE_FINISHED;

/**
 * @author tamsiree
 */
public class ActivitySVG extends ActivityBase {


    @BindView(R.id.animated_svg_view)
    AnimatedSvgView mSvgView;
    @BindView(R.id.app_name)
    ImageView mAppName;
    @BindView(R.id.tv_app_name)
    TextView mTvAppName;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.activity_svg)
    RelativeLayout mActivitySvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.hideStatusBar(this);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        RxDeviceTool.setPortrait(this);
        setSvg(ModelSVG.values()[0]);
        mTvVersion.setText(String.format("VERSION %s", RxDeviceTool.getAppVersionName(mContext)));
    }

    private void setSvg(ModelSVG modelSvg) {
        mSvgView.setGlyphStrings(modelSvg.glyphs);
        mSvgView.setFillColors(modelSvg.colors);
        mSvgView.setViewportSize(modelSvg.width, modelSvg.height);
        mSvgView.setTraceResidueColor(0x32000000);
        mSvgView.setTraceColors(modelSvg.colors);
        mSvgView.rebuildGlyphData();
        mSvgView.setOnStateChangeListener(state -> {
            switch (state) {
                case STATE_FINISHED:
                    mTvAppName.setVisibility(View.VISIBLE);
                    mTvVersion.setVisibility(View.VISIBLE);
                    RxTool.delayToDo(2000, () -> {
                        RxActivityTool.skipActivityAndFinish(mContext, ActivityMain.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    });
                    break;
                default:
                    break;
            }
        });

        mSvgView.start();
    }
}
