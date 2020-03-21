package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.model.ActionItem
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.popupwindows.RxPopupImply
import com.tamsiree.rxui.view.popupwindows.RxPopupSingleView
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupView
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewManager
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewManager.TipListener

/**
 * @author tamsiree
 */
class ActivityPopupView : ActivityBase(), TipListener {
    var mRxPopupViewManager: RxPopupViewManager? = null

    @RxPopupView.Align
    var mAlign = RxPopupView.ALIGN_CENTER

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.tv_imply)
    var mTvImply: TextView? = null

    @JvmField
    @BindView(R.id.tv_definition)
    var mTvDefinition: TextView? = null

    @JvmField
    @BindView(R.id.text_input_edit_text)
    var mTextInputEditText: TextInputEditText? = null

    @JvmField
    @BindView(R.id.text_input_layout)
    var mTextInputLayout: TextInputLayout? = null

    @JvmField
    @BindView(R.id.text_view_buttons_label)
    var mTextViewButtonsLabel: TextView? = null

    @JvmField
    @BindView(R.id.button_above)
    var mButtonAbove: Button? = null

    @JvmField
    @BindView(R.id.button_below)
    var mButtonBelow: Button? = null

    @JvmField
    @BindView(R.id.button_left_to)
    var mButtonLeftTo: Button? = null

    @JvmField
    @BindView(R.id.button_right_to)
    var mButtonRightTo: Button? = null

    @JvmField
    @BindView(R.id.linear_layout_buttons_above_below)
    var mLinearLayoutButtonsAboveBelow: LinearLayout? = null

    @JvmField
    @BindView(R.id.button_align_left)
    var mButtonAlignLeft: RadioButton? = null

    @JvmField
    @BindView(R.id.button_align_center)
    var mButtonAlignCenter: RadioButton? = null

    @JvmField
    @BindView(R.id.button_align_right)
    var mButtonAlignRight: RadioButton? = null

    @JvmField
    @BindView(R.id.linear_layout_buttons_align)
    var mLinearLayoutButtonsAlign: RadioGroup? = null

    @JvmField
    @BindView(R.id.text_view)
    var mTextView: TextView? = null

    @JvmField
    @BindView(R.id.parent_layout)
    var mParentLayout: RelativeLayout? = null

    @JvmField
    @BindView(R.id.activity_popup_view)
    var mActivityPopupView: LinearLayout? = null

    @JvmField
    @BindView(R.id.root_layout_s)
    var mRootLayoutS: RelativeLayout? = null
    private var titlePopup: RxPopupSingleView? = null
    private var popupImply //提示  一小时后有惊喜
            : RxPopupImply? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_view)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    protected fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        mRxPopupViewManager = RxPopupViewManager(this)
        mButtonAlignCenter!!.isChecked = true
    }

    private fun initPopupView() {
        titlePopup = RxPopupSingleView(mContext, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.popupwindow_definition_layout)
        titlePopup!!.addAction(ActionItem("标清"))
        titlePopup!!.addAction(ActionItem("高清"))
        titlePopup!!.addAction(ActionItem("超清"))
        titlePopup!!.setItemOnClickListener { item, position -> // TODO Auto-generated method stub
            if (titlePopup!!.getAction(position).mTitle == mTvDefinition!!.text) {
                RxToast.showToast(mContext, "当前已经为" + mTvDefinition!!.text, 500)
            } else {
                if (position >= 0 && position < 3) {
                    mTvDefinition!!.text = titlePopup!!.getAction(position).mTitle
                }
            }
        }
    }

    @OnClick(R.id.tv_imply, R.id.tv_definition, R.id.button_above, R.id.button_below, R.id.button_left_to, R.id.button_right_to, R.id.button_align_center, R.id.button_align_left, R.id.button_align_right)
    fun onClick(view: View) {
        val text = if (TextUtils.isEmpty(mTextInputEditText!!.text)) TIP_TEXT else mTextInputEditText!!.text.toString()
        val builder: RxPopupView.Builder
        val tipvView: View
        when (view.id) {
            R.id.tv_imply -> {
                if (popupImply == null) {
                    popupImply = RxPopupImply(mContext)
                }
                popupImply!!.show(mTvImply)
            }
            R.id.tv_definition -> {
                initPopupView()
                titlePopup!!.show(mTvDefinition, 0)
            }
            R.id.button_above -> {
                mRxPopupViewManager!!.findAndDismiss(mTextView)
                builder = RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_ABOVE)
                builder.setAlign(mAlign)
                tipvView = mRxPopupViewManager!!.show(builder.build())
            }
            R.id.button_below -> {
                mRxPopupViewManager!!.findAndDismiss(mTextView)
                builder = RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_BELOW)
                builder.setAlign(mAlign)
                builder.setBackgroundColor(resources.getColor(R.color.orange))
                tipvView = mRxPopupViewManager!!.show(builder.build())
            }
            R.id.button_left_to -> {
                mRxPopupViewManager!!.findAndDismiss(mTextView)
                builder = RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_LEFT_TO)
                builder.setBackgroundColor(resources.getColor(R.color.greenyellow))
                builder.setTextColor(resources.getColor(R.color.black))
                builder.setGravity(RxPopupView.GRAVITY_CENTER)
                builder.setTextSize(12)
                tipvView = mRxPopupViewManager!!.show(builder.build())
            }
            R.id.button_right_to -> {
                mRxPopupViewManager!!.findAndDismiss(mTextView)
                builder = RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_RIGHT_TO)
                builder.setBackgroundColor(resources.getColor(R.color.paleturquoise))
                builder.setTextColor(resources.getColor(android.R.color.black))
                tipvView = mRxPopupViewManager!!.show(builder.build())
            }
            R.id.button_align_center -> mAlign = RxPopupView.ALIGN_CENTER
            R.id.button_align_left -> mAlign = RxPopupView.ALIGN_LEFT
            R.id.button_align_right -> mAlign = RxPopupView.ALIGN_RIGHT
            else -> {
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val builder = RxPopupView.Builder(this, mTextView, mRootLayoutS, TIP_TEXT, RxPopupView.POSITION_ABOVE)
        builder.setAlign(mAlign)
        mRxPopupViewManager!!.show(builder.build())
    }

    override fun onTipDismissed(view: View, anchorViewId: Int, byUser: Boolean) {
        Log.d(TAG, "tip near anchor view $anchorViewId dismissed")
        if (anchorViewId == R.id.text_view) {
            // Do something when a tip near view with id "R.id.text_view" has been dismissed
        }
    }

    companion object {
        const val TIP_TEXT = "Tip"
        private val TAG = ActivityPopupView::class.java.simpleName
    }
}