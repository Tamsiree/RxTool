package com.tamsiree.rxui.view.roundprogressbar

import android.annotation.TargetApi
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.roundprogressbar.common.RxBaseRoundProgress

/**
 * @author tamsiree
 */
class RxIconRoundProgress : RxBaseRoundProgress, View.OnClickListener {
    private var ivProgressIcon: ImageView? = null
    private var llIcon: LinearLayout? = null
    private var iconResource = 0
    private var iconSize = 0
    private var iconWidth = 0
    private var iconHeight = 0
    private var iconPadding = 0
    private var iconPaddingLeft = 0
    private var iconPaddingRight = 0
    private var iconPaddingTop = 0
    private var iconPaddingBottom = 0
    var colorIconBackground = 0
        private set
    private var iconClickListener: OnIconClickListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    public override fun initLayout(): Int {
        return R.layout.layout_icon_round_corner_progress_bar
    }

    override fun initStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxIconRoundProgress)
        iconResource = typedArray.getResourceId(R.styleable.RxIconRoundProgress_rcIconSrc, R.drawable.clover)
        iconSize = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconSize, -1f).toInt()
        iconWidth = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconWidth, dp2px(context, DEFAULT_ICON_SIZE.toFloat()).toFloat()).toInt()
        iconHeight = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconHeight, dp2px(context, DEFAULT_ICON_SIZE.toFloat()).toFloat()).toInt()
        iconPadding = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconPadding, -1f).toInt()
        iconPaddingLeft = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconPaddingLeft, dp2px(context, DEFAULT_ICON_PADDING_LEFT.toFloat()).toFloat()).toInt()
        iconPaddingRight = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconPaddingRight, dp2px(context, DEFAULT_ICON_PADDING_RIGHT.toFloat()).toFloat()).toInt()
        iconPaddingTop = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconPaddingTop, dp2px(context, DEFAULT_ICON_PADDING_TOP.toFloat()).toFloat()).toInt()
        iconPaddingBottom = typedArray.getDimension(R.styleable.RxIconRoundProgress_rcIconPaddingBottom, dp2px(context, DEFAULT_ICON_PADDING_BOTTOM.toFloat()).toFloat()).toInt()
        val colorIconBackgroundDefault = context.resources.getColor(R.color.round_corner_progress_bar_background_default)
        colorIconBackground = typedArray.getColor(R.styleable.RxIconRoundProgress_rcIconBackgroundColor, colorIconBackgroundDefault)
        typedArray.recycle()
    }

    override fun initView() {
        ivProgressIcon = findViewById(R.id.iv_progress_icon)
        llIcon = findViewById(R.id.ll_icon)
        ivProgressIcon?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.iv_progress_icon && iconClickListener != null) {
            iconClickListener!!.onIconClick()
        }
    }

    fun setOnIconClickListener(listener: OnIconClickListener?) {
        iconClickListener = listener
    }

    override fun drawProgress(layoutProgress: LinearLayout?, max: Float, progress: Float, totalWidth: Float,
                              radius: Int, padding: Int, colorProgress: Int, isReverse: Boolean) {
        val backgroundDrawable = createGradientDrawable(colorProgress)
        val newRadius = radius - padding / 2
        if (isReverse && progress != max) {
            backgroundDrawable.cornerRadii = floatArrayOf(newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat())
        } else {
            backgroundDrawable.cornerRadii = floatArrayOf(0f, 0f, newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), newRadius.toFloat(), 0f, 0f)
        }
        layoutProgress?.background = backgroundDrawable
        val ratio = max / progress
        val progressWidth = ((totalWidth - (padding * 2 + ivProgressIcon!!.width)) / ratio).toInt()
        val progressParams = layoutProgress?.layoutParams
        progressParams?.width = progressWidth
        layoutProgress?.layoutParams = progressParams
    }

    override fun onViewDraw() {
        drawImageIcon()
        drawImageIconSize()
        drawImageIconPadding()
        drawIconBackgroundColor()
    }

    private fun drawImageIcon() {
        ivProgressIcon!!.setImageResource(iconResource)
    }

    private fun drawImageIconSize() {
        if (iconSize == -1) {
            ivProgressIcon!!.layoutParams = LayoutParams(iconWidth, iconHeight)
        } else {
            ivProgressIcon!!.layoutParams = LayoutParams(iconSize, iconSize)
        }
    }

    private fun drawImageIconPadding() {
        if (iconPadding == -1 || iconPadding == 0) {
            ivProgressIcon!!.setPadding(iconPaddingLeft, iconPaddingTop, iconPaddingRight, iconPaddingBottom)
        } else {
            ivProgressIcon!!.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
        }
        ivProgressIcon!!.invalidate()
    }

    private fun drawIconBackgroundColor() {
        val iconBackgroundDrawable = createGradientDrawable(colorIconBackground)
        val radius = radius - padding / 2
        iconBackgroundDrawable.cornerRadii = floatArrayOf(radius.toFloat(), radius.toFloat(), 0f, 0f, 0f, 0f, radius.toFloat(), radius.toFloat())
        llIcon!!.background = iconBackgroundDrawable
    }

    var iconImageResource: Int
        get() = iconResource
        set(resId) {
            iconResource = resId
            drawImageIcon()
        }

    fun getIconSize(): Int {
        return iconSize
    }

    fun setIconSize(size: Int) {
        if (size >= 0) {
            iconSize = size
        }
        drawImageIconSize()
    }

    fun getIconPadding(): Int {
        return iconPadding
    }

    fun setIconPadding(padding: Int) {
        if (padding >= 0) {
            iconPadding = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingLeft(): Int {
        return iconPaddingLeft
    }

    fun setIconPaddingLeft(padding: Int) {
        if (padding > 0) {
            iconPaddingLeft = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingRight(): Int {
        return iconPaddingRight
    }

    fun setIconPaddingRight(padding: Int) {
        if (padding > 0) {
            iconPaddingRight = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingTop(): Int {
        return iconPaddingTop
    }

    fun setIconPaddingTop(padding: Int) {
        if (padding > 0) {
            iconPaddingTop = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingBottom(): Int {
        return iconPaddingBottom
    }

    fun setIconPaddingBottom(padding: Int) {
        if (padding > 0) {
            iconPaddingBottom = padding
        }
        drawImageIconPadding()
    }

    fun setIconBackgroundColor(color: Int) {
        colorIconBackground = color
        drawIconBackgroundColor()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.iconResource = iconResource
        ss.iconSize = iconSize
        ss.iconWidth = iconWidth
        ss.iconHeight = iconHeight
        ss.iconPadding = iconPadding
        ss.iconPaddingLeft = iconPaddingLeft
        ss.iconPaddingRight = iconPaddingRight
        ss.iconPaddingTop = iconPaddingTop
        ss.iconPaddingBottom = iconPaddingBottom
        ss.colorIconBackground = colorIconBackground
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        iconResource = ss.iconResource
        iconSize = ss.iconSize
        iconWidth = ss.iconWidth
        iconHeight = ss.iconHeight
        iconPadding = ss.iconPadding
        iconPaddingLeft = ss.iconPaddingLeft
        iconPaddingRight = ss.iconPaddingRight
        iconPaddingTop = ss.iconPaddingTop
        iconPaddingBottom = ss.iconPaddingBottom
        colorIconBackground = ss.colorIconBackground
    }

    private class SavedState : BaseSavedState {
        var iconResource = 0
        var iconSize = 0
        var iconWidth = 0
        var iconHeight = 0
        var iconPadding = 0
        var iconPaddingLeft = 0
        var iconPaddingRight = 0
        var iconPaddingTop = 0
        var iconPaddingBottom = 0
        var colorIconBackground = 0

        internal constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            iconResource = `in`.readInt()
            iconSize = `in`.readInt()
            iconWidth = `in`.readInt()
            iconHeight = `in`.readInt()
            iconPadding = `in`.readInt()
            iconPaddingLeft = `in`.readInt()
            iconPaddingRight = `in`.readInt()
            iconPaddingTop = `in`.readInt()
            iconPaddingBottom = `in`.readInt()
            colorIconBackground = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(iconResource)
            out.writeInt(iconSize)
            out.writeInt(iconWidth)
            out.writeInt(iconHeight)
            out.writeInt(iconPadding)
            out.writeInt(iconPaddingLeft)
            out.writeInt(iconPaddingRight)
            out.writeInt(iconPaddingTop)
            out.writeInt(iconPaddingBottom)
            out.writeInt(colorIconBackground)
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

    interface OnIconClickListener {
        fun onIconClick()
    }

    companion object {
        protected const val DEFAULT_ICON_SIZE = 20
        protected const val DEFAULT_ICON_PADDING_LEFT = 0
        protected const val DEFAULT_ICON_PADDING_RIGHT = 0
        protected const val DEFAULT_ICON_PADDING_TOP = 0
        protected const val DEFAULT_ICON_PADDING_BOTTOM = 0
    }
}