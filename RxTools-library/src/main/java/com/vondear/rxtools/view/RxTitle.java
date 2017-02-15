package com.vondear.rxtools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDataUtils;

/**
 * Created by vonde on 2017/1/2.
 */

public class RxTitle extends FrameLayout {
    //-------------------------------------------控件start------------------------------------------
    private RelativeLayout mRootLayout;

    private TextView mTvTitle;

    private ImageView mIvLeft;

    private ImageView mIvRight;
    //===========================================控件end=============================================

    //-------------------------------------------属性start-------------------------------------------
    private int mBackground;

    private String mTitle;

    private int mTitleColor;

    private int mTitleSize;

    private int mLeftIcon;

    private int mRightIcon;

    private boolean mLeftIconVisibility;

    private boolean mRightIconVisibility;
    //===========================================属性end=============================================

    public RxTitle(Context context) {
        super(context);
    }

    public RxTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        //导入布局
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.include_rx_title, this);

        mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        mTvTitle = (TextView) findViewById(R.id.tv_rx_title);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvRight = (ImageView) findViewById(R.id.iv_right);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RxTitle);

        try {
            //获得属性值
            mBackground = a.getResourceId(R.styleable.RxTitle_background, R.color.blue1);
            mTitle = a.getString(R.styleable.RxTitle_title);
            mTitleColor = a.getColor(R.styleable.RxTitle_titleColor, getResources().getColor(R.color.white));
            mTitleSize = a.getDimensionPixelSize(R.styleable.RxTitle_titleSize, 12);//TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
            mLeftIcon = a.getResourceId(R.styleable.RxTitle_leftIcon, R.drawable.back);
            mRightIcon = a.getResourceId(R.styleable.RxTitle_RightIcon, R.drawable.set);
            mLeftIconVisibility = a.getBoolean(R.styleable.RxTitle_leftIconVisibility, true);
            mRightIconVisibility = a.getBoolean(R.styleable.RxTitle_RightIconVisibility, true);
        } finally {
            //回收这个对象
            a.recycle();
        }
        if (mBackground != 0) {
            mRootLayout.setBackgroundResource(mBackground);
        }

        if (!RxDataUtils.isNullString(mTitle)) {
            mTvTitle.setText(mTitle);
        }

        if (mTitleSize != 0) {
            mTvTitle.setTextSize(mTitleSize);
        }

        if (mLeftIcon != 0) {
            mIvLeft.setImageResource(mLeftIcon);
        }

        if (mRightIcon != 0) {
            mIvRight.setImageResource(mRightIcon);
        }

        if (mLeftIconVisibility) {
            mIvLeft.setVisibility(VISIBLE);
        } else {
            mIvLeft.setVisibility(GONE);
        }

        if (mRightIconVisibility) {
            mIvRight.setVisibility(VISIBLE);
        } else {
            mIvRight.setVisibility(GONE);
        }
    }

    public void setLeftOnClickListener(OnClickListener onClickListener){
        mIvLeft.setOnClickListener(onClickListener);
    }
}
