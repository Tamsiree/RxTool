package com.tamsiree.rxui.view.roundprogressbar

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.roundprogressbar.common.RxBaseRoundProgress

/**
 * @author tamsiree
 */
class RxTextRoundProgress : RxBaseRoundProgress, OnGlobalLayoutListener {
    private var tvProgress: TextView? = null
    private var colorTextProgress = 0
    private var textProgressSize = 0
    private var textProgressMargin = 0
    private var textProgress: String? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initLayout(): Int {
        return R.layout.layout_text_round_corner_progress_bar
    }

    override fun initStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxTextRoundProgress)
        colorTextProgress = typedArray.getColor(R.styleable.RxTextRoundProgress_rcTextProgressColor, Color.WHITE)
        textProgressSize = typedArray.getDimension(R.styleable.RxTextRoundProgress_rcTextProgressSize, dp2px(context, DEFAULT_TEXT_SIZE.toFloat()).toFloat()).toInt()
        textProgressMargin = typedArray.getDimension(R.styleable.RxTextRoundProgress_rcTextProgressMargin, dp2px(context, DEFAULT_TEXT_MARGIN.toFloat()).toFloat()).toInt()
        textProgress = typedArray.getString(R.styleable.RxTextRoundProgress_rcTextProgress)
        typedArray.recycle()
    }

    override fun initView() {
        tvProgress = findViewById(R.id.tv_progress)
        tvProgress?.viewTreeObserver?.addOnGlobalLayoutListener(this)
    }

    override fun drawProgress(layoutProgress: LinearLayout?, max: Float, progress: Float, totalWidth: Float,
                              radius: Int, padding: Int, colorProgress: Int, isReverse: Boolean) {
        val backgroundDrawable = createGradientDrawable(colorProgress)
        val newRadius = radius - padding / 2
        backgroundDrawable.cornerRadii = floatArrayOf(newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat())
        layoutProgress?.background = backgroundDrawable
        val ratio = max / progress
        val progressWidth = ((totalWidth - padding * 2) / ratio).toInt()
        val progressParams = layoutProgress?.layoutParams
        progressParams?.width = progressWidth
        layoutProgress?.layoutParams = progressParams
    }

    override fun onViewDraw() {
        drawTextProgress()
        drawTextProgressSize()
        drawTextProgressMargin()
        drawTextProgressPosition()
        drawTextProgressColor()
    }

    private fun drawTextProgress() {
        tvProgress!!.text = textProgress
    }

    private fun drawTextProgressColor() {
        tvProgress!!.setTextColor(colorTextProgress)
    }

    private fun drawTextProgressSize() {
        tvProgress!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textProgressSize.toFloat())
    }

    private fun drawTextProgressMargin() {
        val params = tvProgress!!.layoutParams as MarginLayoutParams
        params.setMargins(textProgressMargin, 0, textProgressMargin, 0)
        tvProgress!!.layoutParams = params
    }

    private fun drawTextProgressPosition() {
//        tvProgress.setVisibility(View.INVISIBLE);
//        tvProgress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                    tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                else
//                    tvProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                setTextProgressAlign();
//            }
//        });
        clearTextProgressAlign()
        // TODO Temporary
        val textProgressWidth = tvProgress!!.measuredWidth + getTextProgressMargin() * 2
        val ratio = getMax() / getProgress()
        val progressWidth = ((layoutWidth - getPadding() * 2) / ratio).toInt()
        if (textProgressWidth + textProgressMargin < progressWidth) {
            alignTextProgressInsideProgress()
        } else {
            alignTextProgressOutsideProgress()
        }
    }

    //    private void setTextProgressAlign() {
    //        tvProgress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    //            @SuppressWarnings("deprecation")
    //            @Override
    //            public void onGlobalLayout() {
    //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    //                    tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    //                else
    //                    tvProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    //                tvProgress.setVisibility(View.VISIBLE);
    //            }
    //        });
    //        int textProgressWidth = tvProgress.getMeasuredWidth() + (getTextProgressMargin() * 2);
    //        float ratio = getMax() / getProgress();
    //        int progressWidth = (int) ((getLayoutWidth() - (getPadding() * 2)) / ratio);
    //        if (textProgressWidth + textProgressMargin < progressWidth) {
    //            alignTextProgressInsideProgress();
    //        } else {
    //            alignTextProgressOutsideProgress();
    //        }
    //    }
    private fun clearTextProgressAlign() {
        val params = tvProgress!!.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_RIGHT, 0)
        params.addRule(RelativeLayout.LEFT_OF, 0)
        params.addRule(RelativeLayout.RIGHT_OF, 0)
        params.removeRule(RelativeLayout.START_OF)
        params.removeRule(RelativeLayout.END_OF)
        params.removeRule(RelativeLayout.ALIGN_START)
        params.removeRule(RelativeLayout.ALIGN_END)
        tvProgress!!.layoutParams = params
    }

    private fun alignTextProgressInsideProgress() {
        val params = tvProgress!!.layoutParams as RelativeLayout.LayoutParams
        if (getReverse()) {
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.layout_progress)
            params.addRule(RelativeLayout.ALIGN_START, R.id.layout_progress)
        } else {
            params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.layout_progress)
            params.addRule(RelativeLayout.ALIGN_END, R.id.layout_progress)
        }
        tvProgress!!.layoutParams = params
    }

    private fun alignTextProgressOutsideProgress() {
        val params = tvProgress!!.layoutParams as RelativeLayout.LayoutParams
        if (getReverse()) {
            params.addRule(RelativeLayout.LEFT_OF, R.id.layout_progress)
            params.addRule(RelativeLayout.START_OF, R.id.layout_progress)
        } else {
            params.addRule(RelativeLayout.RIGHT_OF, R.id.layout_progress)
            params.addRule(RelativeLayout.END_OF, R.id.layout_progress)
        }
        tvProgress!!.layoutParams = params
    }

    var progressText: String?
        get() = textProgress
        set(text) {
            textProgress = text
            drawTextProgress()
            drawTextProgressPosition()
        }

    override fun setProgress(progress: Float) {
        super.setProgress(progress)
        drawTextProgressPosition()
    }

    var textProgressColor: Int
        get() = colorTextProgress
        set(color) {
            colorTextProgress = color
            drawTextProgressColor()
        }

    fun getTextProgressSize(): Int {
        return textProgressSize
    }

    fun setTextProgressSize(size: Int) {
        textProgressSize = size
        drawTextProgressSize()
        drawTextProgressPosition()
    }

    fun getTextProgressMargin(): Int {
        return textProgressMargin
    }

    fun setTextProgressMargin(margin: Int) {
        textProgressMargin = margin
        drawTextProgressMargin()
        drawTextProgressPosition()
    }

    override fun onGlobalLayout() {
        tvProgress!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
        drawTextProgressPosition()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.colorTextProgress = colorTextProgress
        ss.textProgressSize = textProgressSize
        ss.textProgressMargin = textProgressMargin
        ss.textProgress = textProgress
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        colorTextProgress = ss.colorTextProgress
        textProgressSize = ss.textProgressSize
        textProgressMargin = ss.textProgressMargin
        textProgress = ss.textProgress
    }

    private class SavedState : BaseSavedState {
        var colorTextProgress = 0
        var textProgressSize = 0
        var textProgressMargin = 0
        var textProgress: String? = null

        internal constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            colorTextProgress = `in`.readInt()
            textProgressSize = `in`.readInt()
            textProgressMargin = `in`.readInt()
            textProgress = `in`.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(colorTextProgress)
            out.writeInt(textProgressSize)
            out.writeInt(textProgressMargin)
            out.writeString(textProgress)
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        protected const val DEFAULT_TEXT_SIZE = 16
        protected const val DEFAULT_TEXT_MARGIN = 10
    }
}