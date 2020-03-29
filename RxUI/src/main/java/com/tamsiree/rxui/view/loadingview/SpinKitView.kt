package com.tamsiree.rxui.view.loadingview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.loadingview.SpriteFactory.create
import com.tamsiree.rxui.view.loadingview.sprite.Sprite

/**
 * @author tamsiree
 */
class SpinKitView @TargetApi(Build.VERSION_CODES.LOLLIPOP) constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : ProgressBar(context, attrs, defStyleAttr, defStyleRes) {
    private val mStyle: Style
    var color: Int
    private var mSprite: Sprite? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.SpinKitViewStyle) : this(context, attrs, defStyleAttr, R.style.SpinKitView)

    private fun init() {
        val sprite = create(mStyle)
        setIndeterminateDrawable(sprite!!)
    }

    override fun setIndeterminateDrawable(d: Drawable) {
        require(d is Sprite) { "this d must be instanceof Sprite" }
        setIndeterminateDrawable(d)
    }

    override fun getIndeterminateDrawable(): Sprite {
        return mSprite!!
    }

    fun setIndeterminateDrawable(d: Sprite) {
        super.setIndeterminateDrawable(d)
        mSprite = d
        if (mSprite!!.color == 0) {
            mSprite!!.color = color
        }
        onSizeChanged(width, height, width, height)
        if (visibility == View.VISIBLE) {
            mSprite!!.start()
        }
    }

    override fun unscheduleDrawable(who: Drawable) {
        super.unscheduleDrawable(who)
        if (who is Sprite) {
            who.stop()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            if (mSprite != null && visibility == View.VISIBLE) {
                mSprite!!.start()
            }
        }
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == View.SCREEN_STATE_OFF) {
            if (mSprite != null) {
                mSprite!!.stop()
            }
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SpinKitView, defStyleAttr,
                defStyleRes)
        mStyle = Style.values()[a.getInt(R.styleable.SpinKitView_SpinKit_Style, 0)]
        color = a.getColor(R.styleable.SpinKitView_SpinKit_Color, Color.WHITE)
        a.recycle()
        init()
        isIndeterminate = true
    }
}