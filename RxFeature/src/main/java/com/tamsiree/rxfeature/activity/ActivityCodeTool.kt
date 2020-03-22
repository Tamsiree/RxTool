package com.tamsiree.rxfeature.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.tamsiree.rxfeature.R
import com.tamsiree.rxfeature.tool.RxBarCode
import com.tamsiree.rxfeature.tool.RxQRCode
import com.tamsiree.rxkit.*
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.ticker.RxTickerUtils
import com.tamsiree.rxui.view.ticker.RxTickerView

/**
 * @author tamsiree
 */
class ActivityCodeTool : ActivityBase() {
    private var mRxTitle: RxTitle? = null
    private var mEtQrCode: EditText? = null
    private var mIvCreateQrCode: ImageView? = null
    private var mIvQrCode: ImageView? = null
    private var mActivityCodeTool: LinearLayout? = null
    private var mLlCode: LinearLayout? = null
    private var mLlQrRoot: LinearLayout? = null
    private var mEtBarCode: EditText? = null
    private var mIvCreateBarCode: ImageView? = null
    private var mIvBarCode: ImageView? = null
    private var mLlBarCode: LinearLayout? = null
    private var mLlBarRoot: LinearLayout? = null
    private var mLlScaner: LinearLayout? = null
    private var mLlQr: LinearLayout? = null
    private var mLlBar: LinearLayout? = null
    private var mRxTickerViewMade: RxTickerView? = null
    private var mRxTickerViewScan: RxTickerView? = null
    private var nestedScrollView: NestedScrollView? = null
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
        mRxTitle = findViewById(R.id.rx_title)
        mEtQrCode = findViewById(R.id.et_qr_code)
        mIvCreateQrCode = findViewById(R.id.iv_create_qr_code)
        mIvQrCode = findViewById(R.id.iv_qr_code)
        mActivityCodeTool = findViewById(R.id.activity_code_tool)
        mLlCode = findViewById(R.id.ll_code)
        mLlQrRoot = findViewById(R.id.ll_qr_root)
        nestedScrollView = findViewById(R.id.nestedScrollView)
        mEtBarCode = findViewById(R.id.et_bar_code)
        mIvCreateBarCode = findViewById(R.id.iv_create_bar_code)
        mIvBarCode = findViewById(R.id.iv_bar_code)
        mLlBarCode = findViewById(R.id.ll_bar_code)
        mLlBarRoot = findViewById(R.id.ll_bar_root)
        mLlScaner = findViewById(R.id.ll_scaner)
        mLlQr = findViewById(R.id.ll_qr)
        mLlBar = findViewById(R.id.ll_bar)
        mRxTickerViewScan = findViewById(R.id.ticker_scan_count)
        mRxTickerViewScan?.setCharacterList(NUMBER_LIST)
        mRxTickerViewMade = findViewById(R.id.ticker_made_count)
        mRxTickerViewMade?.setCharacterList(NUMBER_LIST)
        updateMadeCodeCount()
    }

    override fun initData() {
        initEvent()
    }

    private fun updateScanCodeCount() {
        mRxTickerViewScan!!.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_SCAN_CODE)).toString() + "", true)
    }

    private fun updateMadeCodeCount() {
        mRxTickerViewMade!!.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)).toString() + "", true)
    }

    private fun initEvent() {
        mRxTitle!!.isLeftIconVisibility = true
        mRxTitle!!.titleColor = Color.WHITE
        mRxTitle!!.titleSize = RxImageTool.dp2px(20f)
        mRxTitle!!.setLeftFinish(mContext)
        mRxTickerViewScan!!.animationDuration = 500
        mIvCreateQrCode!!.setOnClickListener {
            val str = mEtQrCode!!.text.toString()
            if (RxDataTool.isNullString(str)) {
                RxToast.error("二维码文字内容不能为空！")
            } else {
                mLlCode!!.visibility = View.VISIBLE

                //二维码生成方式一  推荐此方法
                RxQRCode.builder(str).backColor(-0x1).codeColor(-0x1000000).codeSide(600).codeLogo(resources.getDrawable(R.drawable.faviconhandsome)).codeBorder(1).into(mIvQrCode)

                //二维码生成方式二 默认宽和高都为800 背景为白色 二维码为黑色
                // RxQRCode.createQRCode(str,mIvQrCode);
                mIvQrCode!!.visibility = View.VISIBLE
                RxToast.success("二维码已生成!")
                RxSPTool.putContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE, (RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)) + 1).toString())
                updateMadeCodeCount()
                nestedScrollView!!.computeScroll()
            }
        }
        mIvCreateBarCode!!.setOnClickListener {
            val str1 = mEtBarCode!!.text.toString()
            if (RxDataTool.isNullString(str1)) {
                RxToast.error("条形码文字内容不能为空！")
            } else {
                mLlBarCode!!.visibility = View.VISIBLE

                //条形码生成方式一  推荐此方法
                RxBarCode.builder(str1).backColor(0x00000000).codeColor(-0x1000000).codeWidth(1000).codeHeight(300).into(mIvBarCode)

                //条形码生成方式二  默认宽为1000 高为300 背景为白色 二维码为黑色
                //mIvBarCode.setImageBitmap(RxBarCode.createBarCode(str1, 1000, 300));
                mIvBarCode!!.visibility = View.VISIBLE
                RxToast.success("条形码已生成!")
                RxSPTool.putContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE, (RxDataTool.stringToInt(RxSPTool.getContent(mContext.baseContext!!, RxConstants.SP_MADE_CODE)) + 1).toString())
                updateMadeCodeCount()
            }
        }
        mLlScaner!!.setOnClickListener { RxActivityTool.skipActivity(mContext, ActivityScanerCode::class.java) }
        mLlQr!!.setOnClickListener {
            mLlQrRoot!!.visibility = View.VISIBLE
            mLlBarRoot!!.visibility = View.GONE
        }
        mLlBar!!.setOnClickListener {
            mLlBarRoot!!.visibility = View.VISIBLE
            mLlQrRoot!!.visibility = View.GONE
        }
    }

    companion object {
        private val NUMBER_LIST = RxTickerUtils.getDefaultNumberList()
    }
}