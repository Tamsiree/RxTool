package com.tamsiree.rxdemo.activity

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxBarTool.setTransparentStatusBar
import com.tamsiree.rxkit.RxConstants.URL_RXTOOL
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxTextTool.getBuilder
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_text_utils.*

/**
 * @author tamsiree
 */
class ActivityTextTool : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_utils)
        ButterKnife.bind(this)
        setPortrait(this)
        setTransparentStatusBar(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                RxToast.showToast(mContext, "事件触发了", 500)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }

        // 响应点击事件的话必须设置以下属性
        tv_about_spannable.movementMethod = LinkMovementMethod.getInstance()
        getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
                .append("测试").append("Url\n").setUrl(URL_RXTOOL)
                .append("列表项\n").setBullet(60, resources.getColor(R.color.blue_baby))
                .append("  测试引用\n").setQuoteColor(resources.getColor(R.color.blue_baby))
                .append("首行缩进\n").setLeadingMargin(30, 50)
                .append("测试").append("上标").setSuperscript().append("下标\n").setSubscript()
                .append("测试").append("点击事件\n").setClickSpan(clickableSpan)
                .append("测试").append("serif 字体\n").setFontFamily("serif")
                .append("测试").append("图片\n").setResourceId(R.drawable.logo)
                .append("测试").append("前景色").setForegroundColor(Color.GREEN).append("背景色\n").setBackgroundColor(resources.getColor(R.color.blue_baby))
                .append("测试").append("删除线").setStrikethrough().append("下划线\n").setUnderline()
                .append("测试").append("sans-serif 字体\n").setFontFamily("sans-serif")
                .append("测试").append("2倍字体\n").setProportion(2f)
                .append("测试").append("monospace字体\n").setFontFamily("monospace")
                .append("测试").append("普通模糊效果字体\n").setBlur(3f, BlurMaskFilter.Blur.NORMAL)
                .append("测试").append(" 粗体 ").setBold().append(" 斜体 ").setItalic().append(" 粗斜体 \n").setBoldItalic()
                .append("测试").append("横向2倍字体\n").setXProportion(2f)
                .append("\n测试正常对齐\n").setAlign(Layout.Alignment.ALIGN_NORMAL)
                .append("测试居中对齐\n").setAlign(Layout.Alignment.ALIGN_CENTER)
                .append("测试相反对齐\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .into(tv_about_spannable)
    }

    override fun initData() {

    }
}