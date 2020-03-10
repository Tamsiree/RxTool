package com.tamsiree.rxui.view.roundprogressbar.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tamsiree.rxkit.RxImageTool;
import com.tamsiree.rxui.R;


/**
 * @author tamsiree
 */
public abstract class RxBaseRoundProgress extends LinearLayout {
    protected final static int DEFAULT_MAX_PROGRESS = 100;
    protected final static int DEFAULT_PROGRESS = 0;
    protected final static int DEFAULT_SECONDARY_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS = 30;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;

    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;
    private LinearLayout layoutSecondaryProgress;

    private int radius;
    private int padding;
    private int totalWidth;

    private float max;
    private float progress;
    private float secondaryProgress;

    private int colorBackground;
    private int colorProgress;
    private int colorSecondaryProgress;

    private boolean isReverse;

    private OnProgressChangedListener progressChangedListener;

    public RxBaseRoundProgress(Context context) {
        super(context);
        setup(context, null);
    }

    public RxBaseRoundProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RxBaseRoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    @TargetApi(21)
    public RxBaseRoundProgress(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    // Setup a progress bar layout by sub class
    protected abstract int initLayout();

    // Setup an attribute parameter on sub class
    protected abstract void initStyleable(Context context, AttributeSet attrs);

    // Initial any view on sub class
    protected abstract void initView();

    // Draw a progress by sub class
    protected abstract void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                         int radius, int padding, int colorProgress, boolean isReverse);

    // Draw all view on sub class
    protected abstract void onViewDraw();

    public void setup(Context context, AttributeSet attrs) {
        setupStyleable(context, attrs);

        removeAllViews();
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(initLayout(), this);
        // Initial default view
        layoutBackground = findViewById(R.id.layout_background);
        layoutProgress = findViewById(R.id.layout_progress);
        layoutSecondaryProgress = findViewById(R.id.layout_secondary_progress);

        initView();
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {

        //获得这个控件对应的属性。
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxBaseRoundProgress);

        radius = (int) typedArray.getDimension(R.styleable.RxBaseRoundProgress_rcRadius, RxImageTool.dp2px(context, DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.RxBaseRoundProgress_rcBackgroundPadding, RxImageTool.dp2px(context, DEFAULT_BACKGROUND_PADDING));

        isReverse = typedArray.getBoolean(R.styleable.RxBaseRoundProgress_rcReverse, false);

        max = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.RxBaseRoundProgress_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorBackground = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcBackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_progress_default);
        colorProgress = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcProgressColor, colorProgressDefault);
        int colorSecondaryProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
        colorSecondaryProgress = typedArray.getColor(R.styleable.RxBaseRoundProgress_rcSecondaryProgressColor, colorSecondaryProgressDefault);

        typedArray.recycle();

        initStyleable(context, attrs);
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        totalWidth = newWidth;
        drawAll();
        postDelayed(() -> {
            drawPrimaryProgress();
            drawSecondaryProgress();
        }, 5);
    }

    // Redraw all view
    protected void drawAll() {
        drawBackgroundProgress();
        drawPadding();
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
        onViewDraw();
    }

    // Draw progress background
    private void drawBackgroundProgress() {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorBackground);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        layoutBackground.setBackground(backgroundDrawable);
    }

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private void drawPrimaryProgress() {
        drawProgress(layoutProgress, max, progress, totalWidth, radius, padding, colorProgress, isReverse);
    }

    private void drawSecondaryProgress() {
        drawProgress(layoutSecondaryProgress, max, secondaryProgress, totalWidth, radius, padding, colorSecondaryProgress, isReverse);
    }

    private void drawProgressReverse() {
        setupReverse(layoutProgress);
        setupReverse(layoutSecondaryProgress);
    }

    // Change progress position by depending on isReverse flag
    private void setupReverse(LinearLayout layoutProgress) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // For support with RTL on API 17 or more
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            // For support with RTL on API 17 or more
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    private void drawPadding() {
        layoutBackground.setPadding(padding, padding, padding, padding);
    }

    // Remove all of relative align rule
    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
    }

/*    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / (DisplayMetrics.DENSITY_DEFAULT * 1.0f)));
    }*/

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius >= 0) {
            this.radius = radius;
        }
        drawBackgroundProgress();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        if (padding >= 0) {
            this.padding = padding;
        }
        drawPadding();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0) {
            this.max = max;
        }
        if (this.progress > max) {
            this.progress = max;
        }
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getLayoutWidth() {
        return totalWidth;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            this.progress = 0;
        } else this.progress = Math.min(progress, max);
        drawPrimaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(getId(), this.progress, true, false);
        }
    }

    public float getSecondaryProgressWidth() {
        if (layoutSecondaryProgress != null) {
            return layoutSecondaryProgress.getWidth();
        }
        return 0;
    }

    public float getSecondaryProgress() {
        return secondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0) {
            this.secondaryProgress = 0;
        } else if (secondaryProgress > max) {
            this.secondaryProgress = max;
        } else {
            this.secondaryProgress = secondaryProgress;
        }
        drawSecondaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(getId(), this.secondaryProgress, false, true);
        }
    }

    public int getProgressBackgroundColor() {
        return colorBackground;
    }

    public void setProgressBackgroundColor(int colorBackground) {
        this.colorBackground = colorBackground;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return colorProgress;
    }

    public void setProgressColor(int colorProgress) {
        this.colorProgress = colorProgress;
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(int colorSecondaryProgress) {
        this.colorSecondaryProgress = colorSecondaryProgress;
        drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        progressChangedListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawAll();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.radius = this.radius;
        ss.padding = this.padding;

        ss.colorBackground = this.colorBackground;
        ss.colorProgress = this.colorProgress;
        ss.colorSecondaryProgress = this.colorSecondaryProgress;

        ss.max = this.max;
        ss.progress = this.progress;
        ss.secondaryProgress = this.secondaryProgress;

        ss.isReverse = this.isReverse;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.radius = ss.radius;
        this.padding = ss.padding;

        this.colorBackground = ss.colorBackground;
        this.colorProgress = ss.colorProgress;
        this.colorSecondaryProgress = ss.colorSecondaryProgress;

        this.max = ss.max;
        this.progress = ss.progress;
        this.secondaryProgress = ss.secondaryProgress;

        this.isReverse = ss.isReverse;
    }

    private static class SavedState extends BaseSavedState {
        float max;
        float progress;
        float secondaryProgress;

        int radius;
        int padding;

        int colorBackground;
        int colorProgress;
        int colorSecondaryProgress;

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.progress = in.readFloat();
            this.secondaryProgress = in.readFloat();

            this.radius = in.readInt();
            this.padding = in.readInt();

            this.colorBackground = in.readInt();
            this.colorProgress = in.readInt();
            this.colorSecondaryProgress = in.readInt();

            this.isReverse = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.progress);
            out.writeFloat(this.secondaryProgress);

            out.writeInt(this.radius);
            out.writeInt(this.padding);

            out.writeInt(this.colorBackground);
            out.writeInt(this.colorProgress);
            out.writeInt(this.colorSecondaryProgress);

            out.writeByte((byte) (this.isReverse ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress);
    }

}
