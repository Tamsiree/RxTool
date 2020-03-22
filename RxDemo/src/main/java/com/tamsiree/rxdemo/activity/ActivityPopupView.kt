package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.model.ActionItem
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.popupwindows.RxPopupImply
import com.tamsiree.rxui.view.popupwindows.RxPopupSingleView
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupView
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewManager
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewManager.TipListener
import kotlinx.android.synthetic.main.activity_popup_view.*

/**
 * @author tamsiree
 */
class ActivityPopupView : ActivityBase(), TipListener {
    private var mRxPopupViewManager: RxPopupViewManager? = null

    @RxPopupView.Align
    var mAlign = RxPopupView.ALIGN_CENTER


    private var titlePopup: RxPopupSingleView? = null

    //提示  一小时后有惊喜
    private var popupImply: RxPopupImply? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_view)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        mRxPopupViewManager = RxPopupViewManager(this)
        button_align_center.isChecked = true

        val text = if (TextUtils.isEmpty(text_input_edit_text.text)) TIP_TEXT else text_input_edit_text.text.toString()
        var builder: RxPopupView.Builder
        var tipvView: View

        tv_imply.setOnClickListener {
            if (popupImply == null) {
                popupImply = RxPopupImply(mContext)
            }
            popupImply!!.show(tv_imply)
        }
        tv_definition.setOnClickListener {
            initPopupView()
            titlePopup!!.show(tv_definition, 0)
        }
        button_above.setOnClickListener {
            mRxPopupViewManager!!.findAndDismiss(text_view)
            builder = RxPopupView.Builder(this, text_view, parent_layout, text, RxPopupView.POSITION_ABOVE)
            builder.setAlign(mAlign)
            tipvView = mRxPopupViewManager!!.show(builder.build())
        }
        button_below.setOnClickListener {
            mRxPopupViewManager!!.findAndDismiss(text_view)
            builder = RxPopupView.Builder(this, text_view, parent_layout, text, RxPopupView.POSITION_BELOW)
            builder.setAlign(mAlign)
            builder.setBackgroundColor(resources.getColor(R.color.orange))
            tipvView = mRxPopupViewManager!!.show(builder.build())
        }
        button_left_to.setOnClickListener {
            mRxPopupViewManager!!.findAndDismiss(text_view)
            builder = RxPopupView.Builder(this, text_view, parent_layout, text, RxPopupView.POSITION_LEFT_TO)
            builder.setBackgroundColor(resources.getColor(R.color.greenyellow))
            builder.setTextColor(resources.getColor(R.color.black))
            builder.setGravity(RxPopupView.GRAVITY_CENTER)
            builder.setTextSize(12)
            tipvView = mRxPopupViewManager!!.show(builder.build())
        }
        button_right_to.setOnClickListener {
            mRxPopupViewManager!!.findAndDismiss(text_view)
            builder = RxPopupView.Builder(this, text_view, parent_layout, text, RxPopupView.POSITION_RIGHT_TO)
            builder.setBackgroundColor(resources.getColor(R.color.paleturquoise))
            builder.setTextColor(resources.getColor(android.R.color.black))
            tipvView = mRxPopupViewManager!!.show(builder.build())
        }
        button_align_center.setOnClickListener { mAlign = RxPopupView.ALIGN_CENTER }
        button_align_left.setOnClickListener { mAlign = RxPopupView.ALIGN_LEFT }
        button_align_right.setOnClickListener { mAlign = RxPopupView.ALIGN_RIGHT }
    }

    override fun initData() {

    }

    private fun initPopupView() {
        titlePopup = RxPopupSingleView(mContext, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.popupwindow_definition_layout)
        titlePopup!!.addAction(ActionItem("标清"))
        titlePopup!!.addAction(ActionItem("高清"))
        titlePopup!!.addAction(ActionItem("超清"))
        titlePopup!!.setItemOnClickListener { item, position ->
            if (titlePopup!!.getAction(position).mTitle == tv_definition.text) {
                RxToast.showToast(mContext, "当前已经为" + tv_definition.text, 500)
            } else {
                if (position in 0..2) {
                    tv_definition.text = titlePopup!!.getAction(position).mTitle
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val builder = RxPopupView.Builder(this, text_view, root_layout_s, TIP_TEXT, RxPopupView.POSITION_ABOVE)
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