package com.vondear.tools.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.bean.ActionItem;
import com.vondear.rxtools.view.RxPopupImply;
import com.vondear.rxtools.view.RxPopupView;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.tooltips.RxToolTip;
import com.vondear.rxtools.view.tooltips.RxToolTipsManager;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityPopupView extends ActivityBase implements RxToolTipsManager.TipListener {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.ll_include_title)
    LinearLayout mLlIncludeTitle;
    @BindView(R.id.tv_definition)
    TextView mTvDefinition;
    @BindView(R.id.activity_popup_view)
    LinearLayout mActivityPopupView;
    @BindView(R.id.tv_imply)
    TextView mTvImply;
    @BindView(R.id.text_input_edit_text)
    TextInputEditText mEditText;
    @BindView(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.text_view_buttons_label)
    TextView mTextViewButtonsLabel;
    @BindView(R.id.button_above)
    Button mAboveBtn;
    @BindView(R.id.button_below)
    Button mBelowBtn;
    @BindView(R.id.button_left_to)
    Button mLeftToBtn;
    @BindView(R.id.button_right_to)
    Button mRightToBtn;
    @BindView(R.id.linear_layout_buttons_above_below)
    LinearLayout mLinearLayoutButtonsAboveBelow;
    @BindView(R.id.button_align_left)
    RadioButton mAlignLeft;
    @BindView(R.id.button_align_center)
    RadioButton mAlignCenter;
    @BindView(R.id.button_align_right)
    RadioButton mAlignRight;
    @BindView(R.id.linear_layout_buttons_align)
    RadioGroup mLinearLayoutButtonsAlign;
    @BindView(R.id.text_view)
    TextView mTextView;
    @BindView(R.id.parent_layout)
    RelativeLayout mParentLayout;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;

    private RxPopupView titlePopup;

    private static final String TAG = ActivityPopupView.class.getSimpleName();
    public static final String TIP_TEXT = "Tip";

    RxToolTipsManager mRxToolTipsManager;

    @RxToolTip.Align
    int mAlign = RxToolTip.ALIGN_CENTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_view);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mTvTitle.setText("PopupView的使用");

        mRxToolTipsManager = new RxToolTipsManager(this);

        mAlignCenter.setChecked(true);

    }

    private void initPopupView() {
        titlePopup = new RxPopupView(mContext, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.popupwindow_definition_layout);
        titlePopup.addAction(new ActionItem("标清"));
        titlePopup.addAction(new ActionItem("高清"));
        titlePopup.addAction(new ActionItem("超清"));
        titlePopup.setItemOnClickListener(new RxPopupView.OnItemOnClickListener() {

            @Override
            public void onItemClick(ActionItem item, int position) {
                // TODO Auto-generated method stub
                if (titlePopup.getAction(position).mTitle.equals(mTvDefinition.getText())) {
                    RxToast.showToast(mContext, "当前已经为" + mTvDefinition.getText(), 500);
                } else {
                    if (position >= 0 && position < 3) {
                        mTvDefinition.setText(titlePopup.getAction(position).mTitle);
                    }
                }
            }
        });
    }

    @OnClick({R.id.tv_imply, R.id.iv_finish, R.id.tv_definition, R.id.button_above, R.id.button_below, R.id.button_left_to, R.id.button_right_to, R.id.button_align_center, R.id.button_align_left, R.id.button_align_right})
    public void onClick(View view) {
        String text = TextUtils.isEmpty(mEditText.getText()) ? TIP_TEXT : mEditText.getText().toString();
        RxToolTip.Builder builder;
        switch (view.getId()) {
            case R.id.tv_imply:
                if (popupImply == null) {
                    popupImply = new RxPopupImply(mContext);
                }
                popupImply.show(mTvImply);
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_definition:
                initPopupView();
                titlePopup.show(mTvDefinition, 0);
                break;
            case R.id.button_above:
                mRxToolTipsManager.findAndDismiss(mTextView);
                builder = new RxToolTip.Builder(this, mTextView, mRootLayout, text, RxToolTip.POSITION_ABOVE);
                builder.setAlign(mAlign);
                mRxToolTipsManager.show(builder.build());
                break;
            case R.id.button_below:
                mRxToolTipsManager.findAndDismiss(mTextView);
                builder = new RxToolTip.Builder(this, mTextView, mRootLayout, text, RxToolTip.POSITION_BELOW);
                builder.setAlign(mAlign);
                builder.setBackgroundColor(getResources().getColor(R.color.orange));
                mRxToolTipsManager.show(builder.build());
                break;
            case R.id.button_left_to:
                mRxToolTipsManager.findAndDismiss(mTextView);
                builder = new RxToolTip.Builder(this, mTextView, mRootLayout, text, RxToolTip.POSITION_LEFT_TO);
                builder.setBackgroundColor(getResources().getColor(R.color.greenyellow));
                builder.setTextColor(getResources().getColor(R.color.black));
                builder.setGravity(RxToolTip.GRAVITY_CENTER);
                builder.setTextSize(12);
                mRxToolTipsManager.show(builder.build());
                break;
            case R.id.button_right_to:
                mRxToolTipsManager.findAndDismiss(mTextView);
                builder = new RxToolTip.Builder(this, mTextView, mRootLayout, text, RxToolTip.POSITION_RIGHT_TO);
                builder.setBackgroundColor(getResources().getColor(R.color.paleturquoise));
                builder.setTextColor(getResources().getColor(android.R.color.white));
                mRxToolTipsManager.show(builder.build());
                break;
            case R.id.button_align_center:
                mAlign = RxToolTip.ALIGN_CENTER;
                break;
            case R.id.button_align_left:
                mAlign = RxToolTip.ALIGN_LEFT;
                break;
            case R.id.button_align_right:
                mAlign = RxToolTip.ALIGN_RIGHT;
                break;
        }
    }

    private RxPopupImply popupImply;//提示  一小时后有惊喜

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        RxToolTip.Builder builder = new RxToolTip.Builder(this, mTextView, mRootLayout, TIP_TEXT, RxToolTip.POSITION_ABOVE);
        builder.setAlign(mAlign);
        mRxToolTipsManager.show(builder.build());
    }


    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        Log.d(TAG, "tip near anchor view " + anchorViewId + " dismissed");

        if (anchorViewId == R.id.text_view) {
            // Do something when a tip near view with id "R.id.text_view" has been dismissed
        }
    }
}
