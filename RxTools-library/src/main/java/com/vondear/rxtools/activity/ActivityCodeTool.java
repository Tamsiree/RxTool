package com.vondear.rxtools.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxBarCode;
import com.vondear.rxtools.view.RxQRCode;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.ticker.RxTickerUtils;
import com.vondear.rxtools.view.ticker.RxTickerView;

import static com.vondear.rxtools.RxConstants.SP_MADE_CODE;
import static com.vondear.rxtools.RxConstants.SP_SCAN_CODE;

/**
 * @author vondear
 */
public class ActivityCodeTool extends ActivityBase {

    private static final char[] NUMBER_LIST = RxTickerUtils.getDefaultNumberList();
    private RxTitle mRxTitle;
    private EditText mEtQrCode;
    private ImageView mIvCreateQrCode;
    private ImageView mIvQrCode;
    private LinearLayout mActivityCodeTool;
    private LinearLayout mLlCode;
    private LinearLayout mLlQrRoot;
    private EditText mEtBarCode;
    private ImageView mIvCreateBarCode;
    private ImageView mIvBarCode;
    private LinearLayout mLlBarCode;
    private LinearLayout mLlBarRoot;
    private LinearLayout mLlScaner;
    private LinearLayout mLlQr;
    private LinearLayout mLlBar;
    private RxTickerView mRxTickerViewMade;
    private RxTickerView mRxTickerViewScan;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_code_tool);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScanCodeCount();
    }

    protected void initView() {
        mRxTitle =  findViewById(R.id.rx_title);
        mEtQrCode =  findViewById(R.id.et_qr_code);
        mIvCreateQrCode = findViewById(R.id.iv_create_qr_code);
        mIvQrCode =  findViewById(R.id.iv_qr_code);
        mActivityCodeTool =  findViewById(R.id.activity_code_tool);
        mLlCode =  findViewById(R.id.ll_code);
        mLlQrRoot =  findViewById(R.id.ll_qr_root);

        nestedScrollView =  findViewById(R.id.nestedScrollView);

        mEtBarCode =  findViewById(R.id.et_bar_code);
        mIvCreateBarCode =  findViewById(R.id.iv_create_bar_code);
        mIvBarCode = findViewById(R.id.iv_bar_code);
        mLlBarCode =  findViewById(R.id.ll_bar_code);
        mLlBarRoot = findViewById(R.id.ll_bar_root);
        mLlScaner = findViewById(R.id.ll_scaner);
        mLlQr = findViewById(R.id.ll_qr);
        mLlBar = findViewById(R.id.ll_bar);

        mRxTickerViewScan =  findViewById(R.id.ticker_scan_count);
        mRxTickerViewScan.setCharacterList(NUMBER_LIST);

        mRxTickerViewMade =findViewById(R.id.ticker_made_count);
        mRxTickerViewMade.setCharacterList(NUMBER_LIST);
        updateMadeCodeCount();
    }

    private void updateScanCodeCount() {
        mRxTickerViewScan.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext, SP_SCAN_CODE)) + "", true);
    }

    private void updateMadeCodeCount() {
        mRxTickerViewMade.setText(RxDataTool.stringToInt(RxSPTool.getContent(mContext, SP_MADE_CODE)) + "", true);
    }

    private void initEvent() {
        mRxTitle.setLeftFinish(mContext);

        mIvCreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtQrCode.getText().toString();
                if (RxDataTool.isNullString(str)) {
                    RxToast.error("二维码文字内容不能为空！");
                } else {
                    mLlCode.setVisibility(View.VISIBLE);

                    //二维码生成方式一  推荐此方法
                    RxQRCode.builder(str).
                            backColor(getResources().getColor(R.color.white)).
                            codeColor(getResources().getColor(R.color.black)).
                            codeSide(600).
                            into(mIvQrCode);

                    //二维码生成方式二 默认宽和高都为800 背景为白色 二维码为黑色
                    // RxQRCode.createQRCode(str,mIvQrCode);

                    mIvQrCode.setVisibility(View.VISIBLE);

                    RxToast.success("二维码已生成!");

                    RxSPTool.putContent(mContext, SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(mContext, SP_MADE_CODE)) + 1 + "");

                    updateMadeCodeCount();

                    nestedScrollView.computeScroll();
                }
            }
        });

        mIvCreateBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = mEtBarCode.getText().toString();
                if (RxDataTool.isNullString(str1)) {
                    RxToast.error("条形码文字内容不能为空！");
                } else {
                    mLlBarCode.setVisibility(View.VISIBLE);

                    //条形码生成方式一  推荐此方法
                    RxBarCode.builder(str1).
                            backColor(getResources().getColor(R.color.transparent)).
                            codeColor(getResources().getColor(R.color.black)).
                            codeWidth(1000).
                            codeHeight(300).
                            into(mIvBarCode);

                    //条形码生成方式二  默认宽为1000 高为300 背景为白色 二维码为黑色
                    //mIvBarCode.setImageBitmap(RxBarCode.createBarCode(str1, 1000, 300));

                    mIvBarCode.setVisibility(View.VISIBLE);

                    RxToast.success("条形码已生成!");

                    RxSPTool.putContent(mContext, SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(mContext, SP_MADE_CODE)) + 1 + "");

                    updateMadeCodeCount();
                }
            }
        });

        mLlScaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, ActivityScanerCode.class);
            }
        });

        mLlQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlQrRoot.setVisibility(View.VISIBLE);
                mLlBarRoot.setVisibility(View.GONE);
            }
        });

        mLlBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlBarRoot.setVisibility(View.VISIBLE);
                mLlQrRoot.setVisibility(View.GONE);
            }
        });
    }
}
