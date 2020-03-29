package com.tamsiree.rxfeature.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.tamsiree.rxfeature.R
import com.tamsiree.rxfeature.tool.RxBarCode
import com.tamsiree.rxfeature.tool.RxQRCode
import com.tamsiree.rxkit.*
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.ticker.RxTickerUtils
import kotlinx.android.synthetic.main.activity_code_tool.*

/**
 * @author tamsiree
 */
class ActivityCodeTool : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBarTool.noTitle(this)
        RxBarTool.setTransparentStatusBar(this)
        setContentView(R.layout.activity_code_tool)
        RxDeviceTool.setPortrait(this)
    }

    override fun onResume() {
        super.onResume()
        updateScanCodeCount()
    }

    override fun initView() {
        ticker_scan_count.setCharacterList(NUMBER_LIST)
        ticker_made_count.setCharacterList(NUMBER_LIST)
        updateMadeCodeCount()
    }

    override fun initData() {
        initEvent()
    }

    private fun updateScanCodeCount() {
        ticker_scan_count!!.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_SCAN_CODE)).toString() + "", true)
    }

    private fun updateMadeCodeCount() {
        ticker_made_count.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)).toString() + "", true)
    }

    private fun initEvent() {
        rx_title.setLeftIconVisibility(true)
        rx_title.setTitleColor(Color.WHITE)
        rx_title.setTitleSize(RxImageTool.dp2px(20f))
        rx_title.setLeftFinish(mContext)
        ticker_scan_count!!.animationDuration = 500
        iv_create_qr_code.setOnClickListener {
            val str = et_qr_code.text.toString()
            if (RxDataTool.isNullString(str)) {
                RxToast.error("二维码文字内容不能为空！")
            } else {
                ll_code!!.visibility = View.VISIBLE

                //二维码生成方式一  推荐此方法
                RxQRCode.builder(str).backColor(-0x1).codeColor(-0x1000000).codeSide(600).codeLogo(resources.getDrawable(R.drawable.faviconhandsome)).codeBorder(1).into(iv_qr_code)

                //二维码生成方式二 默认宽和高都为800 背景为白色 二维码为黑色
                // RxQRCode.createQRCode(str,iv_qr_code);
                iv_qr_code!!.visibility = View.VISIBLE
                RxToast.success("二维码已生成!")
                RxSPTool.putContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE, (RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)) + 1).toString())
                updateMadeCodeCount()
                nestedScrollView!!.computeScroll()
            }
        }
        iv_create_bar_code!!.setOnClickListener {
            val str1 = et_bar_code!!.text.toString()
            if (RxDataTool.isNullString(str1)) {
                RxToast.error("条形码文字内容不能为空！")
            } else {
                ll_bar_code!!.visibility = View.VISIBLE

                //条形码生成方式一  推荐此方法
                RxBarCode.builder(str1).backColor(0x00000000).codeColor(-0x1000000).codeWidth(1000).codeHeight(300).into(iv_bar_code)

                //条形码生成方式二  默认宽为1000 高为300 背景为白色 二维码为黑色
                //iv_bar_code.setImageBitmap(RxBarCode.createBarCode(str1, 1000, 300));
                iv_bar_code!!.visibility = View.VISIBLE
                RxToast.success("条形码已生成!")
                RxSPTool.putContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE, (RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)) + 1).toString())
                updateMadeCodeCount()
            }
        }
        ll_scaner!!.setOnClickListener { RxActivityTool.skipActivity(mContext, ActivityScanerCode::class.java) }
        ll_qr!!.setOnClickListener {
            ll_qr_root!!.visibility = View.VISIBLE
            ll_bar_root!!.visibility = View.GONE
        }
        ll_bar!!.setOnClickListener {
            ll_bar_root!!.visibility = View.VISIBLE
            ll_qr_root!!.visibility = View.GONE
        }
    }

    companion object {
        private val NUMBER_LIST = RxTickerUtils.defaultNumberList
    }
}