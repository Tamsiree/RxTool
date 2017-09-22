package com.vondear.tools.activity;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.RxTextTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vondear.rxtools.RxBarTool.setTransparentStatusBar;
import static com.vondear.rxtools.RxConstants.URL_VONTOOLS;

public class ActivityTextTool extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_about_spannable)
    TextView mTvAboutSpannable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_utils);
        ButterKnife.bind(this);
        setTransparentStatusBar(this);
        mContext = this;
        initView();
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                RxToast.showToast(mContext, "事件触发了", 500);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };


        // 响应点击事件的话必须设置以下属性
        mTvAboutSpannable.setMovementMethod(LinkMovementMethod.getInstance());

        RxTextTool.getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
                .append("测试").append("Url\n").setUrl(URL_VONTOOLS)
                .append("列表项\n").setBullet(60, getResources().getColor(R.color.baby_blue))
                .append("  测试引用\n").setQuoteColor(getResources().getColor(R.color.baby_blue))
                .append("首行缩进\n").setLeadingMargin(30, 50)
                .append("测试").append("上标").setSuperscript().append("下标\n").setSubscript()
                .append("测试").append("点击事件\n").setClickSpan(clickableSpan)
                .append("测试").append("serif 字体\n").setFontFamily("serif")
                .append("测试").append("图片\n").setResourceId(R.drawable.logo)
                .append("测试").append("前景色").setForegroundColor(Color.GREEN).append("背景色\n").setBackgroundColor(getResources().getColor(R.color.baby_blue))
                .append("测试").append("删除线").setStrikethrough().append("下划线\n").setUnderline()
                .append("测试").append("sans-serif 字体\n").setFontFamily("sans-serif")
                .append("测试").append("2倍字体\n").setProportion(2)
                .append("测试").append("monospace字体\n").setFontFamily("monospace")
                .append("测试").append("普通模糊效果字体\n").setBlur(3, BlurMaskFilter.Blur.NORMAL)
                .append("测试").append(" 粗体 ").setBold().append(" 斜体 ").setItalic().append(" 粗斜体 \n").setBoldItalic()
                .append("测试").append("横向2倍字体\n").setXProportion(2)

                .append("\n测试正常对齐\n").setAlign(Layout.Alignment.ALIGN_NORMAL)
                .append("测试居中对齐\n").setAlign(Layout.Alignment.ALIGN_CENTER)
                .append("测试相反对齐\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .into(mTvAboutSpannable);
    }
}
