package com.vondear.tools.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.model.ActionItem;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.popupwindows.RxPopupImply;
import com.vondear.rxtools.view.popupwindows.RxPopupSingleView;
import com.vondear.rxtools.view.popupwindows.tools.RxPopupView;
import com.vondear.rxtools.view.popupwindows.tools.RxPopupViewManager;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author vondear
 */
public class ActivityPopupView extends ActivityBase implements RxPopupViewManager.TipListener {


    public static final String TIP_TEXT = "Tip";
    private static final String TAG = ActivityPopupView.class.getSimpleName();
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_imply)
    TextView mTvImply;
    @BindView(R.id.tv_definition)
    TextView mTvDefinition;
    @BindView(R.id.text_input_edit_text)
    TextInputEditText mTextInputEditText;
    @BindView(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.text_view_buttons_label)
    TextView mTextViewButtonsLabel;
    @BindView(R.id.button_above)
    Button mButtonAbove;
    @BindView(R.id.button_below)
    Button mButtonBelow;
    @BindView(R.id.button_left_to)
    Button mButtonLeftTo;
    @BindView(R.id.button_right_to)
    Button mButtonRightTo;
    @BindView(R.id.linear_layout_buttons_above_below)
    LinearLayout mLinearLayoutButtonsAboveBelow;
    @BindView(R.id.button_align_left)
    RadioButton mButtonAlignLeft;
    @BindView(R.id.button_align_center)
    RadioButton mButtonAlignCenter;
    @BindView(R.id.button_align_right)
    RadioButton mButtonAlignRight;
    @BindView(R.id.linear_layout_buttons_align)
    RadioGroup mLinearLayoutButtonsAlign;
    @BindView(R.id.text_view)
    TextView mTextView;
    @BindView(R.id.parent_layout)
    RelativeLayout mParentLayout;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;
    @BindView(R.id.activity_popup_view)
    LinearLayout mActivityPopupView;
    RxPopupViewManager mRxPopupViewManager;
    @RxPopupView.Align
    int mAlign = RxPopupView.ALIGN_CENTER;
    private RxPopupSingleView titlePopup;
    private RxPopupImply popupImply;//提示  一小时后有惊喜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_view);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mRxTitle.setLeftFinish(mContext);

        mRxPopupViewManager = new RxPopupViewManager(this);

        mButtonAlignCenter.setChecked(true);

    }

    private void initPopupView() {
        titlePopup = new RxPopupSingleView(mContext, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.popupwindow_definition_layout);
        titlePopup.addAction(new ActionItem("标清"));
        titlePopup.addAction(new ActionItem("高清"));
        titlePopup.addAction(new ActionItem("超清"));
        titlePopup.setItemOnClickListener(new RxPopupSingleView.OnItemOnClickListener() {

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

    @OnClick({R.id.tv_imply, R.id.tv_definition, R.id.button_above, R.id.button_below, R.id.button_left_to, R.id.button_right_to, R.id.button_align_center, R.id.button_align_left, R.id.button_align_right})
    public void onClick(View view) {
        String text = TextUtils.isEmpty(mTextInputEditText.getText()) ? TIP_TEXT : mTextInputEditText.getText().toString();
        RxPopupView.Builder builder;
        View tipvView;
        switch (view.getId()) {
            case R.id.tv_imply:
                if (popupImply == null) {
                    popupImply = new RxPopupImply(mContext);
                }
                popupImply.show(mTvImply);
                break;
            case R.id.tv_definition:
                initPopupView();
                titlePopup.show(mTvDefinition, 0);
                break;
            case R.id.button_above:
                mRxPopupViewManager.findAndDismiss(mTextView);
                builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_ABOVE);
                builder.setAlign(mAlign);
                tipvView =  mRxPopupViewManager.show(builder.build());
                break;
            case R.id.button_below:
                mRxPopupViewManager.findAndDismiss(mTextView);
                builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_BELOW);
                builder.setAlign(mAlign);
                builder.setBackgroundColor(getResources().getColor(R.color.orange));
                tipvView = mRxPopupViewManager.show(builder.build());
                break;
            case R.id.button_left_to:
                mRxPopupViewManager.findAndDismiss(mTextView);
                builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_LEFT_TO);
                builder.setBackgroundColor(getResources().getColor(R.color.greenyellow));
                builder.setTextColor(getResources().getColor(R.color.black));
                builder.setGravity(RxPopupView.GRAVITY_CENTER);
                builder.setTextSize(12);
                tipvView = mRxPopupViewManager.show(builder.build());
                break;
            case R.id.button_right_to:
                mRxPopupViewManager.findAndDismiss(mTextView);
                builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_RIGHT_TO);
                builder.setBackgroundColor(getResources().getColor(R.color.paleturquoise));
                builder.setTextColor(getResources().getColor(android.R.color.black));
                tipvView =  mRxPopupViewManager.show(builder.build());
                break;
            case R.id.button_align_center:
                mAlign = RxPopupView.ALIGN_CENTER;
                break;
            case R.id.button_align_left:
                mAlign = RxPopupView.ALIGN_LEFT;
                break;
            case R.id.button_align_right:
                mAlign = RxPopupView.ALIGN_RIGHT;
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        RxPopupView.Builder builder = new RxPopupView.Builder(this, mTextView, mRootLayout, TIP_TEXT, RxPopupView.POSITION_ABOVE);
        builder.setAlign(mAlign);
        mRxPopupViewManager.show(builder.build());
    }


    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        Log.d(TAG, "tip near anchor view " + anchorViewId + " dismissed");

        if (anchorViewId == R.id.text_view) {
            // Do something when a tip near view with id "R.id.text_view" has been dismissed
        }
    }
}
