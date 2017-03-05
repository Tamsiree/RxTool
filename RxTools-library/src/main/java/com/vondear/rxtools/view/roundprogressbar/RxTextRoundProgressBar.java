package com.vondear.rxtools.view.roundprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.view.roundprogressbar.common.RxBaseRoundProgressBar;


public class RxTextRoundProgressBar extends RxBaseRoundProgressBar implements ViewTreeObserver.OnGlobalLayoutListener {
    protected final static int DEFAULT_TEXT_SIZE = 16;
    protected final static int DEFAULT_TEXT_MARGIN = 10;

    private TextView tvProgress;
    private int colorTextProgress;
    private int textProgressSize;
    private int textProgressMargin;
    private String textProgress;

    public RxTextRoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxTextRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_text_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRoundCornerProgress);

        colorTextProgress = typedArray.getColor(R.styleable.TextRoundCornerProgress_rcTextProgressColor, Color.WHITE);

        textProgressSize = (int) typedArray.getDimension(R.styleable.TextRoundCornerProgress_rcTextProgressSize, dp2px(DEFAULT_TEXT_SIZE));
        textProgressMargin = (int) typedArray.getDimension(R.styleable.TextRoundCornerProgress_rcTextProgressMargin, dp2px(DEFAULT_TEXT_MARGIN));

        textProgress = typedArray.getString(R.styleable.TextRoundCornerProgress_rcTextProgress);

        typedArray.recycle();
    }

    @Override
    protected void initView() {
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvProgress.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                int radius, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {
        drawTextProgress();
        drawTextProgressSize();
        drawTextProgressMargin();
        drawTextProgressPosition();
        drawTextProgressColor();
    }

    private void drawTextProgress() {
        tvProgress.setText(textProgress);
    }

    private void drawTextProgressColor() {
        tvProgress.setTextColor(colorTextProgress);
    }

    private void drawTextProgressSize() {
        tvProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, textProgressSize);
    }

    private void drawTextProgressMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tvProgress.getLayoutParams();
        params.setMargins(textProgressMargin, 0, textProgressMargin, 0);
        tvProgress.setLayoutParams(params);
    }

    private void drawTextProgressPosition() {
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
        clearTextProgressAlign();
        // TODO Temporary
        int textProgressWidth = tvProgress.getMeasuredWidth() + (getTextProgressMargin() * 2);
        float ratio = getMax() / getProgress();
        int progressWidth = (int) ((getLayoutWidth() - (getPadding() * 2)) / ratio);
        if (textProgressWidth + textProgressMargin < progressWidth) {
            alignTextProgressInsideProgress();
        } else {
            alignTextProgressOutsideProgress();
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

    private void clearTextProgressAlign() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_RIGHT, 0);
        params.addRule(RelativeLayout.LEFT_OF, 0);
        params.addRule(RelativeLayout.RIGHT_OF, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.removeRule(RelativeLayout.START_OF);
            params.removeRule(RelativeLayout.END_OF);
            params.removeRule(RelativeLayout.ALIGN_START);
            params.removeRule(RelativeLayout.ALIGN_END);
        }
        tvProgress.setLayoutParams(params);
    }

    private void alignTextProgressInsideProgress() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        if (isReverse()) {
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                params.addRule(RelativeLayout.ALIGN_START, R.id.layout_progress);
        } else {
            params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                params.addRule(RelativeLayout.ALIGN_END, R.id.layout_progress);
        }
        tvProgress.setLayoutParams(params);
    }

    private void alignTextProgressOutsideProgress() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        if (isReverse()) {
            params.addRule(RelativeLayout.LEFT_OF, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                params.addRule(RelativeLayout.START_OF, R.id.layout_progress);
        } else {
            params.addRule(RelativeLayout.RIGHT_OF, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                params.addRule(RelativeLayout.END_OF, R.id.layout_progress);
        }
        tvProgress.setLayoutParams(params);
    }

    public String getProgressText() {
        return textProgress;
    }

    public void setProgressText(String text) {
        textProgress = text;
        drawTextProgress();
        drawTextProgressPosition();
    }

    @Override
    public void setProgress(float progress) {
        super.setProgress(progress);
        drawTextProgressPosition();
    }

    public int getTextProgressColor() {
        return colorTextProgress;
    }

    public void setTextProgressColor(int color) {
        this.colorTextProgress = color;
        drawTextProgressColor();
    }

    public int getTextProgressSize() {
        return textProgressSize;
    }

    public void setTextProgressSize(int size) {
        this.textProgressSize = size;
        drawTextProgressSize();
        drawTextProgressPosition();
    }

    public int getTextProgressMargin() {
        return textProgressMargin;
    }

    public void setTextProgressMargin(int margin) {
        this.textProgressMargin = margin;
        drawTextProgressMargin();
        drawTextProgressPosition();
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        else
            tvProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        drawTextProgressPosition();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.colorTextProgress = this.colorTextProgress;
        ss.textProgressSize = this.textProgressSize;
        ss.textProgressMargin = this.textProgressMargin;

        ss.textProgress = this.textProgress;
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

        this.colorTextProgress = ss.colorTextProgress;
        this.textProgressSize = ss.textProgressSize;
        this.textProgressMargin = ss.textProgressMargin;

        this.textProgress = ss.textProgress;
    }

    private static class SavedState extends View.BaseSavedState {
        int colorTextProgress;
        int textProgressSize;
        int textProgressMargin;

        String textProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);

            this.colorTextProgress = in.readInt();
            this.textProgressSize = in.readInt();
            this.textProgressMargin = in.readInt();

            this.textProgress = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(this.colorTextProgress);
            out.writeInt(this.textProgressSize);
            out.writeInt(this.textProgressMargin);

            out.writeString(this.textProgress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
