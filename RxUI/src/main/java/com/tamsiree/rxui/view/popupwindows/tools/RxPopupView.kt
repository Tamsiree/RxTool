package com.tamsiree.rxui.view.popupwindows.tools

import android.content.Context
import android.text.Spannable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.tamsiree.rxui.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author Tamsiree
 */
class RxPopupView(builder: Builder) {
    val context: Context
    val anchorView: View
    val rootView: ViewGroup
    val message: String?

    @Position
    var position: Int

    @Align
    val align: Int
    var offsetX: Int
    val offsetY: Int
    private val mArrow: Boolean
    val backgroundColor: Int
    val textColor: Int
    val elevation: Float

    @Gravity
    private val mTextGravity: Int
    val spannableMessage: Spannable?
    val textSize: Int

    fun hideArrow(): Boolean {
        return !mArrow
    }

    fun positionedLeftTo(): Boolean {
        return POSITION_LEFT_TO == position
    }

    fun positionedRightTo(): Boolean {
        return POSITION_RIGHT_TO == position
    }

    fun positionedAbove(): Boolean {
        return POSITION_ABOVE == position
    }

    fun positionedBelow(): Boolean {
        return POSITION_BELOW == position
    }

    fun alignedCenter(): Boolean {
        return ALIGN_CENTER == align
    }

    fun alignedLeft(): Boolean {
        return ALIGN_LEFT == align
    }

    fun alignedRight(): Boolean {
        return ALIGN_RIGHT == align
    }

    val textGravity: Int
        get() {
            val gravity: Int
            gravity = when (mTextGravity) {
                GRAVITY_CENTER -> android.view.Gravity.CENTER
                GRAVITY_LEFT -> android.view.Gravity.START
                GRAVITY_RIGHT -> android.view.Gravity.END
                else -> android.view.Gravity.CENTER
            }
            return gravity
        }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(POSITION_ABOVE, POSITION_BELOW, POSITION_LEFT_TO, POSITION_RIGHT_TO)
    annotation class Position

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT)
    annotation class Align

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(GRAVITY_CENTER, GRAVITY_LEFT, GRAVITY_RIGHT)
    annotation class Gravity
    class Builder {
        var mContext: Context
        var mAnchorView: View
        var mRootViewGroup: ViewGroup
        var mMessage: String?

        @Position
        var mPosition: Int

        @Align
        var mAlign: Int
        var mOffsetX: Int
        var mOffsetY: Int
        var mArrow: Boolean
        var mBackgroundColor: Int
        var mTextColor: Int
        var mElevation = 0f

        @Gravity
        var mTextGravity: Int
        var mSpannableMessage: Spannable?
        var mTextSize: Int

        /**
         *
         * @param context context
         * @param anchorView the view which near it we want to put the tip
         * @param root a class extends ViewGroup which the created tip view will be added to
         * @param message message to show
         * @param position  put the tip above / below / left to / right to
         */
        constructor(context: Context, anchorView: View, root: ViewGroup, message: String?, @Position position: Int) {
            mContext = context
            mAnchorView = anchorView
            mRootViewGroup = root
            mMessage = message
            mSpannableMessage = null
            mPosition = position
            mAlign = ALIGN_CENTER
            mOffsetX = 0
            mOffsetY = 0
            mArrow = true
            mBackgroundColor = context.resources.getColor(R.color.colorBackground)
            mTextColor = context.resources.getColor(R.color.white)
            mTextGravity = GRAVITY_LEFT
            mTextSize = 14
        }

        /**
         * @param context    context
         * @param anchorView the view which near it we want to put the tip
         * @param root       a class extends ViewGroup which the created tip view will be added to
         * @param message    spannable message to show
         * @param position   put the tip above / below / left to / right to
         */
        constructor(context: Context, anchorView: View, root: ViewGroup, message: Spannable?, @Position position: Int) {
            mContext = context
            mAnchorView = anchorView
            mRootViewGroup = root
            mMessage = null
            mSpannableMessage = message
            mPosition = position
            mAlign = ALIGN_CENTER
            mOffsetX = 0
            mOffsetY = 0
            mArrow = true
            mBackgroundColor = context.resources.getColor(R.color.colorBackground)
            mTextColor = context.resources.getColor(R.color.white)
            mTextGravity = GRAVITY_LEFT
            mTextSize = 14
        }

        fun setPosition(@Position position: Int): Builder {
            mPosition = position
            return this
        }

        fun setAlign(@Align align: Int): Builder {
            mAlign = align
            return this
        }

        /**
         * @param offset offset to move the tip on x axis after tip was positioned
         * @return offset
         */
        fun setOffsetX(offset: Int): Builder {
            mOffsetX = offset
            return this
        }

        /**
         * @param offset offset to move the tip on y axis after tip was positioned
         * @return offset
         */
        fun setOffsetY(offset: Int): Builder {
            mOffsetY = offset
            return this
        }

        fun withArrow(value: Boolean): Builder {
            mArrow = value
            return this
        }

        fun setBackgroundColor(color: Int): Builder {
            mBackgroundColor = color
            return this
        }

        fun setTextColor(color: Int): Builder {
            mTextColor = color
            return this
        }

        fun setElevation(elevation: Float): Builder {
            mElevation = elevation
            return this
        }

        fun setGravity(@Gravity gravity: Int): Builder {
            mTextGravity = gravity
            return this
        }

        fun setTextSize(sizeInSp: Int): Builder {
            mTextSize = sizeInSp
            return this
        }

        fun build(): RxPopupView {
            return RxPopupView(this)
        }
    }

    companion object {
        const val POSITION_ABOVE = 0
        const val POSITION_BELOW = 1
        const val POSITION_LEFT_TO = 3
        const val POSITION_RIGHT_TO = 4
        const val ALIGN_CENTER = 0
        const val ALIGN_LEFT = 1
        const val ALIGN_RIGHT = 2
        const val GRAVITY_CENTER = 0
        const val GRAVITY_LEFT = 1
        const val GRAVITY_RIGHT = 2
    }

    init {
        context = builder.mContext
        anchorView = builder.mAnchorView
        rootView = builder.mRootViewGroup
        message = builder.mMessage
        position = builder.mPosition
        align = builder.mAlign
        offsetX = builder.mOffsetX
        offsetX = builder.mOffsetX
        offsetY = builder.mOffsetY
        mArrow = builder.mArrow
        backgroundColor = builder.mBackgroundColor
        textColor = builder.mTextColor
        elevation = builder.mElevation
        mTextGravity = builder.mTextGravity
        spannableMessage = builder.mSpannableMessage
        textSize = builder.mTextSize
    }
}