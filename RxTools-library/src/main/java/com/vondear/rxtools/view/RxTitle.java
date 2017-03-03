package com.vondear.rxtools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDataUtils;

/*
 * Created by vondear on 2017/1/2.
 */

public class RxTitle extends FrameLayout {
    //*******************************************控件start******************************************
    private RelativeLayout mRootLayout;

    private TextView mTvTitle;

    private LinearLayout mLlLeft;

    private ImageView mIvLeft;

    private LinearLayout mLlRight;

    private ImageView mIvRight;
    //===========================================控件end=============================================

    //*******************************************属性start*******************************************
    private int mRxBackground;

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
        mLlLeft = (LinearLayout) findViewById(R.id.ll_left);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mLlRight = (LinearLayout) findViewById(R.id.ll_right);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RxTitle);

        try {
            //获得属性值
            mRxBackground = a.getColor(R.styleable.RxTitle_RxBackground, getResources().getColor(R.color.transparent));//背景颜色
            mTitle = a.getString(R.styleable.RxTitle_title);//标题
            mTitleColor = a.getColor(R.styleable.RxTitle_titleColor, getResources().getColor(R.color.white));//标题颜色

            mTitleSize = a.getDimensionPixelSize(R.styleable.RxTitle_titleSize, 20);//标题字体大小
            //TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())

            mLeftIcon = a.getResourceId(R.styleable.RxTitle_leftIcon, R.drawable.back);//左边图标
            mRightIcon = a.getResourceId(R.styleable.RxTitle_RightIcon, R.drawable.set);//右边图标
            mLeftIconVisibility = a.getBoolean(R.styleable.RxTitle_leftIconVisibility, true);//左边图标是否显示
            mRightIconVisibility = a.getBoolean(R.styleable.RxTitle_RightIconVisibility, true);//右边图标是否显示
        } finally {
            //回收这个对象
            a.recycle();
        }

        //******************************************************************************************以下属性初始化
        if (mRxBackground != 0) {
            setRxBackground(mRxBackground);
        }

        if (!RxDataUtils.isNullString(mTitle)) {
            setTitle(mTitle);
        }

        if (mTitleColor != 0) {
            setTitleColor(mTitleColor);
        }

        if (mTitleSize != 0) {
            setTitleSize(mTitleSize);
        }

        if (mLeftIcon != 0) {
            setLeftIcon(mLeftIcon);
        }

        if (mRightIcon != 0) {
            setRightIcon(mRightIcon);
        }

        setLeftIconVisibility(mLeftIconVisibility);

        setRightIconVisibility(mRightIconVisibility);
        //==========================================================================================以上为属性初始化
    }

    //**********************************************************************************************以下为get方法

    public RelativeLayout getRootLayout() {
        return mRootLayout;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public LinearLayout getLlLeft() {
        return mLlLeft;
    }

    public ImageView getIvLeft() {
        return mIvLeft;
    }

    public LinearLayout getLlRight() {
        return mLlRight;
    }

    public ImageView getIvRight() {
        return mIvRight;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getRxBackground() {
        return mRxBackground;
    }

    public int getTitleColor() {
        return mTitleColor;
    }

    public int getTitleSize() {
        return mTitleSize;
    }

    public int getLeftIcon() {
        return mLeftIcon;
    }

    public int getRightIcon() {
        return mRightIcon;
    }

    public boolean isLeftIconVisibility() {
        return mLeftIconVisibility;
    }

    public boolean isRightIconVisibility() {
        return mRightIconVisibility;
    }

    //==============================================================================================以上为get方法

    //**********************************************************************************************以下为set方法

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        mLlLeft.setOnClickListener(onClickListener);
    }

    public void setRightOnClickListener(OnClickListener onClickListener) {
        mLlRight.setOnClickListener(onClickListener);
    }

    public void setRxBackground(int rxBackground) {
        mRxBackground = rxBackground;
        mRootLayout.setBackgroundColor(mRxBackground);

    }

    public void setTitle(String title) {
        mTitle = title;
        mTvTitle.setText(mTitle);

    }

    public void setTitleColor(int titleColor) {
        mTitleColor = titleColor;
        mTvTitle.setTextColor(mTitleColor);
    }

    public void setTitleSize(int titleSize) {
        mTitleSize = titleSize;
        mTvTitle.setTextSize(mTitleSize);
    }

    public void setLeftIcon(int leftIcon) {
        mLeftIcon = leftIcon;
        mIvLeft.setImageResource(mLeftIcon);
    }

    public void setRightIcon(int rightIcon) {
        mRightIcon = rightIcon;
        mIvRight.setImageResource(mRightIcon);
    }

    public void setLeftIconVisibility(boolean leftIconVisibility) {
        mLeftIconVisibility = leftIconVisibility;
        if (mLeftIconVisibility) {
            mIvLeft.setVisibility(VISIBLE);
        } else {
            mIvLeft.setVisibility(GONE);
        }
    }

    public void setRightIconVisibility(boolean rightIconVisibility) {
        mRightIconVisibility = rightIconVisibility;
        if (mRightIconVisibility) {
            mIvRight.setVisibility(VISIBLE);
        } else {
            mIvRight.setVisibility(GONE);
        }
    }
    //==============================================================================================以上为set方法

}
