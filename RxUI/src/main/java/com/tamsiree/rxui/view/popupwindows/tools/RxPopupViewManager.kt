package com.tamsiree.rxui.view.popupwindows.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.Outline
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.TextView
import com.tamsiree.rxkit.RxAnimationTool.popout
import com.tamsiree.rxkit.RxAnimationTool.popup
import com.tamsiree.rxkit.TLog.e
import com.tamsiree.rxui.view.popupwindows.tools.RxPopupViewTool.isRtl
import java.util.*

/**
 * @author tamsiree
 */
class RxPopupViewManager {
    // Parameter for managing tip creation or reuse
    private val mTipsMap: MutableMap<Int, View> = HashMap()
    private var mAnimationDuration: Int
    private var mListener: TipListener? = null

    constructor() {
        mAnimationDuration = DEFAULT_ANIM_DURATION
    }

    constructor(listener: TipListener?) {
        mAnimationDuration = DEFAULT_ANIM_DURATION
        mListener = listener
    }

    fun show(rxPopupView: RxPopupView): View? {
        val tipView = create(rxPopupView) ?: return null

        // animate tip visibility
        popup(tipView, mAnimationDuration.toLong()).start()
        return tipView
    }

    private fun create(rxPopupView: RxPopupView): View? {
        if (rxPopupView.anchorView == null) {
            e(TAG, "Unable to create a tip, anchor view is null")
            return null
        }
        if (rxPopupView.rootView == null) {
            e(TAG, "Unable to create a tip, root layout is null")
            return null
        }

        // only one tip is allowed near an anchor view at the same time, thus
        // reuse tip if already exist
        if (mTipsMap.containsKey(rxPopupView.anchorView.id)) {
            return mTipsMap[rxPopupView.anchorView.id]
        }

        // init tip view parameters
        val tipView = createTipView(rxPopupView)

        // on RTL languages replace sides
        if (isRtl) {
            switchToolTipSidePosition(rxPopupView)
        }

        // set tool tip background / shape
        RxPopupViewBackgroundConstructor.setBackground(tipView, rxPopupView)

        // add tip to root layout
        rxPopupView.rootView.addView(tipView)

        // find where to position the tool tip
        val p = RxPopupViewCoordinatesFinder.getCoordinates(tipView, rxPopupView)

        // move tip view to correct position
        moveTipToCorrectPosition(tipView, p)

        // set dismiss on click
        tipView.setOnClickListener { view -> dismiss(view, true) }

        // bind tipView with anchorView id
        val anchorViewId = rxPopupView.anchorView.id
        tipView.tag = anchorViewId

        // enter tip to map by 'anchorView' id
        mTipsMap[anchorViewId] = tipView
        return tipView
    }

    private fun moveTipToCorrectPosition(tipView: TextView, p: Point) {
        val tipViewRxCoordinates = RxCoordinates(tipView)
        val translationX = p.x - tipViewRxCoordinates.left
        val translationY = p.y - tipViewRxCoordinates.top
        if (!isRtl) tipView.translationX = translationX.toFloat() else tipView.translationX = -translationX.toFloat()
        tipView.translationY = translationY.toFloat()
    }

    private fun createTipView(rxPopupView: RxPopupView): TextView {
        val tipView = TextView(rxPopupView.context)
        tipView.setTextColor(rxPopupView.textColor)
        tipView.textSize = rxPopupView.textSize.toFloat()
        tipView.text = if (rxPopupView.message != null) rxPopupView.message else rxPopupView.spannableMessage
        tipView.visibility = View.INVISIBLE
        tipView.gravity = rxPopupView.textGravity
        setTipViewElevation(tipView, rxPopupView)
        return tipView
    }

    private fun setTipViewElevation(tipView: TextView, rxPopupView: RxPopupView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (rxPopupView.elevation > 0) {
                val viewOutlineProvider: ViewOutlineProvider = object : ViewOutlineProvider() {
                    @SuppressLint("NewApi")
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setEmpty()
                    }
                }
                tipView.outlineProvider = viewOutlineProvider
                tipView.elevation = rxPopupView.elevation
            }
        }
    }

    private fun switchToolTipSidePosition(rxPopupView: RxPopupView) {
        if (rxPopupView.positionedLeftTo()) {
            rxPopupView.position = RxPopupView.POSITION_RIGHT_TO
        } else if (rxPopupView.positionedRightTo()) {
            rxPopupView.position = RxPopupView.POSITION_LEFT_TO
        }
    }

    fun setAnimationDuration(duration: Int) {
        mAnimationDuration = duration
    }

    fun dismiss(tipView: View?, byUser: Boolean): Boolean {
        if (tipView != null && isVisible(tipView)) {
            val key = tipView.tag as Int
            mTipsMap.remove(key)
            animateDismiss(tipView, byUser)
            return true
        }
        return false
    }

    fun dismiss(key: Int): Boolean {
        return mTipsMap.containsKey(key) && dismiss(mTipsMap[key], false)
    }

    fun find(key: Int): View? {
        return if (mTipsMap.containsKey(key)) {
            mTipsMap[key]
        } else null
    }

    fun findAndDismiss(anchorView: View): Boolean {
        val view = find(anchorView.id)
        return view != null && dismiss(view, false)
    }

    fun clear() {
        if (!mTipsMap.isEmpty()) {
            for ((_, value) in mTipsMap) {
                dismiss(value, false)
            }
        }
        mTipsMap.clear()
    }

    private fun animateDismiss(view: View, byUser: Boolean) {
        popout(view, mAnimationDuration.toLong(), object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (mListener != null) {
                    mListener!!.onTipDismissed(view, view.tag as Int, byUser)
                }
            }
        }).start()
    }

    fun isVisible(tipView: View): Boolean {
        return tipView.visibility == View.VISIBLE
    }

    interface TipListener {
        fun onTipDismissed(view: View, anchorViewId: Int, byUser: Boolean)
    }

    companion object {
        private val TAG = RxPopupViewManager::class.java.simpleName
        private const val DEFAULT_ANIM_DURATION = 400
    }
}