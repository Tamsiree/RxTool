package com.vondear.rxtools.view.likeview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;

import com.vondear.rxtools.R;
import com.vondear.rxtools.view.likeview.tools.RxPorterShapeImageView;
import com.vondear.rxtools.view.likeview.tools.RxShineView;

/**
 * @author vondear
 * @date 2016/7/5 下午2:27
 **/
public class RxShineButton extends RxPorterShapeImageView {
    private static final String TAG = "RxShineButton";
    int DEFAULT_WIDTH = 50;
    int DEFAULT_HEIGHT = 50;
    DisplayMetrics metrics = new DisplayMetrics();
    Activity activity;
    RxShineView mRxShineView;
    ValueAnimator shakeAnimator;
    RxShineView.ShineParams shineParams = new RxShineView.ShineParams();
    OnCheckedChangeListener listener;
    OnButtonClickListener onButtonClickListener;
    private boolean isChecked = false;
    private int btnColor;
    private int btnFillColor;
    private int bottomHeight;

    public RxShineButton(Context context) {
        super(context);
        if (context instanceof Activity) {
            init((Activity) context);
        }
    }


    public RxShineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton(context, attrs);
    }

    public RxShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context, attrs);
    }

    private void initButton(Context context, AttributeSet attrs) {

        if (context instanceof Activity) {
            init((Activity) context);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RxShineButton);
        btnColor = a.getColor(R.styleable.RxShineButton_btn_color, Color.GRAY);
        btnFillColor = a.getColor(R.styleable.RxShineButton_btn_fill_color, Color.BLACK);
        shineParams.allowRandomColor = a.getBoolean(R.styleable.RxShineButton_allow_random_color, false);
        shineParams.animDuration = a.getInteger(R.styleable.RxShineButton_shine_animation_duration, (int) shineParams.animDuration);
        shineParams.bigShineColor = a.getColor(R.styleable.RxShineButton_big_shine_color, shineParams.bigShineColor);
        shineParams.clickAnimDuration = a.getInteger(R.styleable.RxShineButton_click_animation_duration, (int) shineParams.clickAnimDuration);
        shineParams.enableFlashing = a.getBoolean(R.styleable.RxShineButton_enable_flashing, false);
        shineParams.shineCount = a.getInteger(R.styleable.RxShineButton_shine_count, shineParams.shineCount);
        shineParams.shineDistanceMultiple = a.getFloat(R.styleable.RxShineButton_shine_distance_multiple, shineParams.shineDistanceMultiple);
        shineParams.shineTurnAngle = a.getFloat(R.styleable.RxShineButton_shine_turn_angle, shineParams.shineTurnAngle);
        shineParams.smallShineColor = a.getColor(R.styleable.RxShineButton_small_shine_color, shineParams.smallShineColor);
        shineParams.smallShineOffsetAngle = a.getFloat(R.styleable.RxShineButton_small_shine_offset_angle, shineParams.smallShineOffsetAngle);
        shineParams.shineSize = a.getDimensionPixelSize(R.styleable.RxShineButton_shine_size, shineParams.shineSize);
        a.recycle();
        setSrcColor(btnColor);
    }

    public int getBottomHeight() {
        return bottomHeight;
    }

    public int getColor() {
        return btnFillColor;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        setChecked(checked, false);
    }

    public void setBtnColor(int btnColor) {
        this.btnColor = btnColor;
        setSrcColor(this.btnColor);
    }

    public void setBtnFillColor(int btnFillColor) {
        this.btnFillColor = btnFillColor;
    }

    public void setChecked(boolean checked, boolean anim) {
        isChecked = checked;
        if (checked) {
            setSrcColor(btnFillColor);
            isChecked = true;
            if (anim) {
                showAnim();
            }
        } else {
            setSrcColor(btnColor);
            isChecked = false;
            if (anim) {
                setCancel();
            }
        }
        onListenerUpdate(checked);
    }

    private void onListenerUpdate(boolean checked) {
        if (listener != null) {
            listener.onCheckedChanged(this, checked);
        }
    }

    public void setCancel() {
        setSrcColor(btnColor);
        if (shakeAnimator != null) {
            shakeAnimator.end();
            shakeAnimator.cancel();
        }
    }

    public void setAllowRandomColor(boolean allowRandomColor) {
        shineParams.allowRandomColor = allowRandomColor;
    }

    public void setAnimDuration(int durationMs) {
        shineParams.animDuration = durationMs;
    }

    public void setBigShineColor(int color) {
        shineParams.bigShineColor = color;
    }

    public void setClickAnimDuration(int durationMs) {
        shineParams.clickAnimDuration = durationMs;
    }

    public void enableFlashing(boolean enable) {
        shineParams.enableFlashing = enable;
    }

    public void setShineCount(int count) {
        shineParams.shineCount = count;
    }

    public void setShineDistanceMultiple(float multiple) {
        shineParams.shineDistanceMultiple = multiple;
    }

    public void setShineTurnAngle(float angle) {
        shineParams.shineTurnAngle = angle;
    }

    public void setSmallShineColor(int color) {
        shineParams.smallShineColor = color;
    }

    public void setSmallShineOffAngle(float angle) {
        shineParams.smallShineOffsetAngle = angle;
    }

    public void setShineSize(int size) {
        shineParams.shineSize = size;
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        if (l instanceof OnButtonClickListener) {
            super.setOnClickListener(l);
        } else {
            if (onButtonClickListener != null) {
                onButtonClickListener.setListener(l);
            }
        }
    }

    public void setOnCheckStateChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void init(Activity activity) {
        this.activity = activity;
        onButtonClickListener = new OnButtonClickListener();
        setOnClickListener(onButtonClickListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calPixels();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void showAnim() {
        if (activity != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            mRxShineView = new RxShineView(activity, this, shineParams);
            rootView.addView(mRxShineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            doShareAnim();
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    public void removeView(View view) {
        if (activity != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(view);
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    public void setShapeResource(int raw) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setShape(getResources().getDrawable(raw, null));
        } else {
            setShape(getResources().getDrawable(raw));
        }
    }

    private void doShareAnim() {
        shakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f);
        shakeAnimator.setInterpolator(new LinearInterpolator());
        shakeAnimator.setDuration(500);
        shakeAnimator.setStartDelay(180);
        invalidate();
        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        shakeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setSrcColor(btnFillColor);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setSrcColor(isChecked ? btnFillColor : btnColor);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                setSrcColor(btnColor);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        shakeAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void calPixels() {
        if (activity != null && metrics != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int[] location = new int[2];
            getLocationInWindow(location);
            bottomHeight = metrics.heightPixels - location[1];
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean checked);
    }

    public class OnButtonClickListener implements View.OnClickListener {
        View.OnClickListener listener;

        public OnButtonClickListener() {
        }

        public OnButtonClickListener(View.OnClickListener l) {
            listener = l;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (!isChecked) {
                isChecked = true;
                showAnim();
            } else {
                isChecked = false;
                setCancel();
            }
            onListenerUpdate(isChecked);
            if (listener != null) {
                listener.onClick(view);
            }
        }
    }

}
