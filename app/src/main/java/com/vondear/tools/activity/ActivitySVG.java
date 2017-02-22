package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jrummyapps.android.widget.AnimatedSvgView;
import com.vondear.rxtools.RxActivityUtils;
import com.vondear.rxtools.RxBarUtils;
import com.vondear.tools.R;
import com.vondear.tools.bean.SVG;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySVG extends Activity {

    @BindView(R.id.animated_svg_view)
    AnimatedSvgView mSvgView;
    @BindView(R.id.activity_svg)
    RelativeLayout mActivitySvg;
    @BindView(R.id.app_name)
    ImageView mAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.hideStatusBar(this);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        setSvg(SVG.values()[3]);
        CheckUpdate();
    }

    private void setSvg(SVG svg) {
        mSvgView.setGlyphStrings(svg.glyphs);
        mSvgView.setFillColors(svg.colors);
        mSvgView.setViewportSize(svg.width, svg.height);
        mSvgView.setTraceResidueColor(0x32000000);
        mSvgView.setTraceColors(svg.colors);
        mSvgView.rebuildGlyphData();
        mSvgView.start();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void CheckUpdate() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    Message msg = checkhandler.obtainMessage();
                    checkhandler.sendMessage(msg);
                    Thread.sleep(2000);
                    toMain();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler checkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mAppName.setVisibility(View.VISIBLE);
        }
    };

    public void toMain() {
        RxActivityUtils.skipActivityAndFinish(this, ActivityMain.class);
    }
}
