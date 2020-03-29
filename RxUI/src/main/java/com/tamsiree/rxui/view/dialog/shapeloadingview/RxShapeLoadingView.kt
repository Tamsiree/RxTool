package com.tamsiree.rxui.view.dialog.shapeloadingview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tamsiree.rxui.R
import com.tamsiree.rxui.view.dialog.shapeloadingview.RxShapeView

/**
 * @author Tamsiree
 * @date 2015/4/6
 * 2018/6/11 11:36:40 整合修改
 */
class RxShapeLoadingView : FrameLayout {
    private var mRxShapeView: RxShapeView? = null
    private var mIndicationIm: ImageView? = null
    private var mLoadTextView: TextView? = null
    private var mTextAppearance = 0
    private var mLoadText: String? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxShapeLoadingView)
        mLoadText = typedArray.getString(R.styleable.RxShapeLoadingView_loadingText)
        mTextAppearance = typedArray.getResourceId(R.styleable.RxShapeLoadingView_loadingTextAppearance, -1)
        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /*
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DialogShapeLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }*/
    fun dip2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_shape_loading_view1, null)
        mDistance = dip2px(54f).toFloat()
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        mRxShapeView = view.findViewById(R.id.shapeLoadingView)
        mIndicationIm = view.findViewById(R.id.indication)
        mLoadTextView = view.findViewById(R.id.promptTV)
        if (mTextAppearance != -1) {
            mLoadTextView?.setTextAppearance(context, mTextAppearance)
        }
        setLoadingText(mLoadText)
        addView(view, layoutParams)
        startLoading(200)
    }

    private var mAnimatorSet: AnimatorSet? = null
    private val mFreeFallRunnable = Runnable { freeFall() }

    private fun startLoading(delay: Long) {
        if (mAnimatorSet != null && mAnimatorSet!!.isRunning) {
            return
        }
        removeCallbacks(mFreeFallRunnable)
        if (delay > 0) {
            postDelayed(mFreeFallRunnable, delay)
        } else {
            post(mFreeFallRunnable)
        }
    }

    private fun stopLoading() {
        if (mAnimatorSet != null) {
            if (mAnimatorSet!!.isRunning) {
                mAnimatorSet!!.cancel()
            }
            mAnimatorSet = null
        }
        removeCallbacks(mFreeFallRunnable)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            startLoading(200)
        } else {
            stopLoading()
        }
    }

    fun setLoadingText(loadingText: CharSequence?) {
        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView!!.visibility = View.GONE
        } else {
            mLoadTextView!!.visibility = View.VISIBLE
        }
        mLoadTextView!!.text = loadingText
    }

    /**
     * 上抛
     */
    @SuppressLint("ObjectAnimatorBinding")
    fun upThrow() {
        val objectAnimator = ObjectAnimator.ofFloat(mRxShapeView, "translationY", mDistance, 0f)
        val scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1f)
        var objectAnimator1: ObjectAnimator? = null
        objectAnimator1 = when (mRxShapeView!!.shape) {
            RxShapeView.Shape.SHAPE_RECT -> ObjectAnimator.ofFloat(mRxShapeView, "rotation", 0f, -120f)
            RxShapeView.Shape.SHAPE_CIRCLE -> ObjectAnimator.ofFloat(mRxShapeView, "rotation", 0f, 180f)
            RxShapeView.Shape.SHAPE_TRIANGLE -> ObjectAnimator.ofFloat(mRxShapeView, "rotation", 0f, 180f)
        }
        objectAnimator.duration = ANIMATION_DURATION.toLong()
        objectAnimator1.duration = ANIMATION_DURATION.toLong()
        objectAnimator.interpolator = DecelerateInterpolator(factor)
        objectAnimator1.interpolator = DecelerateInterpolator(factor)
        val animatorSet = AnimatorSet()
        animatorSet.duration = ANIMATION_DURATION.toLong()
        animatorSet.playTogether(objectAnimator, objectAnimator1, scaleIndication)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                freeFall()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.start()
    }

    var factor = 1.2f

    /**
     * 下落
     */
    @SuppressLint("ObjectAnimatorBinding")
    fun freeFall() {
        val objectAnimator = ObjectAnimator.ofFloat(mRxShapeView, "translationY", 0f, mDistance)
        val scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1f, 0.2f)
        objectAnimator.duration = ANIMATION_DURATION.toLong()
        objectAnimator.interpolator = AccelerateInterpolator(factor)
        val animatorSet = AnimatorSet()
        animatorSet.duration = ANIMATION_DURATION.toLong()
        animatorSet.playTogether(objectAnimator, scaleIndication)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                mRxShapeView!!.changeShape()
                upThrow()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.start()
    }

    companion object {
        private const val ANIMATION_DURATION = 500
        private var mDistance = 200f
    }
}