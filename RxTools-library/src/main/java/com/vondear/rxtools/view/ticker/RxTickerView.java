package com.vondear.rxtools.view.ticker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.vondear.rxtools.R;

/**
 * The primary view for showing a ticker text view that handles smoothly scrolling from the
 * current text to a given text. The scrolling behavior is defined by
 * {@link #setCharacterList(char[])} which dictates what characters come in between the starting
 * and ending characters.
 *
 * <p>This class primarily handles the drawing customization of the ticker view, for example
 * setting animation duration, interpolator, colors, etc. It ensures that the canvas is properly
 * positioned, and then it delegates the drawing of each column of text to
 * {@link RxTickerColumnManager}.
 *
 * <p>This class's API should behave similarly to that of a {@link android.widget.TextView}.
 * However, I chose to extend from {@link View} instead of {@link android.widget.TextView}
 * because it allows me full flexibility in customizing the drawing and also support different
 * customization attributes as they are implemented.
 */
public class RxTickerView extends View {
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_ANIMATION_DURATION = 350;
    private static final Interpolator DEFAULT_ANIMATION_INTERPOLATOR =
            new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_GRAVITY = Gravity.START;

    protected final Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private final RxTickerDrawMetrics metrics = new RxTickerDrawMetrics(textPaint);
    private final RxTickerColumnManager columnManager = new RxTickerColumnManager(metrics);
    private final ValueAnimator animator = ValueAnimator.ofFloat(1f);

    // Minor optimizations for re-positioning the canvas for the composer.
    private final Rect viewBounds = new Rect();

    private int lastMeasuredDesiredWidth, lastMeasuredDesiredHeight;

    // View attributes, defaults are set in init().
    private float textSize;
    private int textColor;
    private long animationDurationInMillis;
    private Interpolator animationInterpolator;
    private int gravity;
    private boolean animateMeasurementChange;

    public RxTickerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public RxTickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public RxTickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RxTickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * We currently only support the following set of XML attributes:
     * <ul>
     *     <li>app:textColor
     *     <li>app:textSize
     * </ul>
     *
     * @param context context from constructor
     * @param attrs attrs from constructor
     * @param defStyleAttr defStyleAttr from constructor
     * @param defStyleRes defStyleRes from constructor
     */
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final Resources res = context.getResources();

        int textColor = DEFAULT_TEXT_COLOR;
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE,
                res.getDisplayMetrics());
        int gravity = DEFAULT_GRAVITY;

        // Set the view attributes from XML or from default values defined in this class
        final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ticker_TickerView,
                defStyleAttr, defStyleRes);

        final int textAppearanceResId = arr.getResourceId(
                R.styleable.ticker_TickerView_android_textAppearance, -1);

        // Check textAppearance first
        if (textAppearanceResId != -1) {
            final TypedArray textAppearanceArr = context.obtainStyledAttributes(
                    textAppearanceResId,
                    new int[] {
                            // TODO: having textColor first here does not work, why?
                            android.R.attr.textSize,
                            android.R.attr.textColor,
                    });

            textSize = textAppearanceArr.getDimension(0, textSize);
            textColor = textAppearanceArr.getColor(1, textColor);

            textAppearanceArr.recycle();
        }

        // Custom set attributes on the view should override textAppearance if applicable.
        gravity = arr.getInt(R.styleable.ticker_TickerView_android_gravity, gravity);
        textColor = arr.getColor(R.styleable.ticker_TickerView_android_textColor, textColor);
        textSize = arr.getDimension(R.styleable.ticker_TickerView_android_textSize, textSize);

        // After we've fetched the correct values for the attributes, set them on the view
        animationInterpolator = DEFAULT_ANIMATION_INTERPOLATOR;
        this.animationDurationInMillis = arr.getInt(
                R.styleable.ticker_TickerView_ticker_animationDuration, DEFAULT_ANIMATION_DURATION);
        this.animateMeasurementChange = arr.getBoolean(
                R.styleable.ticker_TickerView_ticker_animateMeasurementChange, false);
        this.gravity = gravity;
        setTextColor(textColor);
        setTextSize(textSize);

        arr.recycle();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                columnManager.setAnimationProgress(animation.getAnimatedFraction());
                checkForRelayout();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                columnManager.onAnimationEnd();
                checkForRelayout();
            }
        });
    }


    /********** BEGIN PUBLIC API **********/


    /**
     * This is the primary API that the view uses to determine how to animate from one character
     * to another. The provided character array dictates what characters will appear between
     * the start and end characters.
     *
     * <p>For example, given the list [a,b,c,d,e], if the view wants to animate from 'd' to 'a',
     * it will know that it has to go from 'd' to 'c' to 'b' to 'a', and these are the characters
     * that show up during the animation scroll.
     *
     * <p>You can find some helpful character list generators in {@link RxTickerUtils}.
     *
     * <p>Special note: the character list must contain {@link RxTickerUtils#EMPTY_CHAR} because the
     * ticker needs to know how to animate from empty to another character (e.g. when the length
     * of the string changes).
     *
     * @param characterList the character array that dictates character orderings.
     */
    public void setCharacterList(char[] characterList) {
        boolean foundEmpty = false;
        for (char character : characterList) {
            if (character == RxTickerUtils.EMPTY_CHAR) {
                foundEmpty = true;
                break;
            }
        }

        if (!foundEmpty) {
            throw new IllegalArgumentException("Missing RxTickerUtils#EMPTY_CHAR in character list");
        }

        columnManager.setCharacterList(characterList);
    }

    /**
     * Sets the string value to display. If the TickerView is currently empty, then this method
     * will immediately display the provided text. Otherwise, it will run the default animation
     * to reach the provided text.
     *
     * @param text the text to display.
     */
    public void setText(String text) {
        setText(text, columnManager.getCurrentWidth() > 0);
    }

    /**
     * Similar to {@link #setText(String)} but provides the optional argument of whether to
     * animate to the provided text or not.
     *
     * @param text the text to display.
     * @param animate whether to animate to text.
     */
    public synchronized void setText(String text, boolean animate) {
        final char[] targetText = text == null ? new char[0] : text.toCharArray();

        if (columnManager.shouldDebounceText(targetText)) {
            return;
        }

        columnManager.setText(targetText);
        setContentDescription(text);

        if (animate) {
            // Kick off the animator that draws the transition
            if (animator.isRunning()) {
                animator.cancel();
            }

            animator.setDuration(animationDurationInMillis);
            animator.setInterpolator(animationInterpolator);
            animator.start();
        } else {
            columnManager.setAnimationProgress(1f);
            columnManager.onAnimationEnd();
            checkForRelayout();
            invalidate();
        }
    }

    /**
     * @return the current text color that's being used to draw the text.
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * Sets the text color used by this view. The default text color is defined by
     * {@link #DEFAULT_TEXT_COLOR}.
     *
     * @param color the color to set the text to.
     */
    public void setTextColor(int color) {
        if (this.textColor != color) {
            textColor = color;
            textPaint.setColor(textColor);
            invalidate();
        }
    }

    /**
     * @return the current text size that's being used to draw the text.
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * Sets the text size used by this view. The default text size is defined by
     * {@link #DEFAULT_TEXT_SIZE}.
     *
     * @param textSize the text size to set the text to.
     */
    public void setTextSize(float textSize) {
        if (this.textSize != textSize) {
            this.textSize = textSize;
            textPaint.setTextSize(textSize);
            onTextPaintMeasurementChanged();
        }
    }

    /**
     * @return the current text typeface.
     */
    public Typeface getTypeface() {
        return textPaint.getTypeface();
    }

    /**
     * Sets the typeface size used by this view.
     *
     * @param typeface the typeface to use on the text.
     */
    public void setTypeface(Typeface typeface) {
        textPaint.setTypeface(typeface);
        onTextPaintMeasurementChanged();
    }

    /**
     * @return the duration in milliseconds that the transition animations run for.
     */
    public long getAnimationDuration() {
        return animationDurationInMillis;
    }

    /**
     * Sets the duration in milliseconds that this TickerView runs its transition animations. The
     * default animation duration is defined by {@link #DEFAULT_ANIMATION_DURATION}.
     *
     * @param animationDurationInMillis the duration in milliseconds.
     */
    public void setAnimationDuration(long animationDurationInMillis) {
        this.animationDurationInMillis = animationDurationInMillis;
    }

    /**
     * @return the interpolator used to interpolate the animated values.
     */
    public Interpolator getAnimationInterpolator() {
        return animationInterpolator;
    }

    /**
     * Sets the interpolator for the transition animation. The default interpolator is defined by
     * {@link #DEFAULT_ANIMATION_INTERPOLATOR}.
     *
     * @param animationInterpolator the interpolator for the animation.
     */
    public void setAnimationInterpolator(Interpolator animationInterpolator) {
        this.animationInterpolator = animationInterpolator;
    }

    /**
     * @return the current text gravity used to align the text. Should be one of the values defined
     *         in {@link Gravity}.
     */
    public int getGravity() {
        return gravity;
    }

    /**
     * Sets the gravity used to align the text.
     *
     * @param gravity the new gravity, should be one of the values defined in
     *                {@link Gravity}.
     */
    public void setGravity(int gravity) {
        if (this.gravity != gravity) {
            this.gravity = gravity;
            invalidate();
        }
    }

    /**
     * Enables/disables the flag to animate measurement changes. If this flag is enabled, any
     * animation that changes the content's text width (e.g. 9999 to 10000) will have the view's
     * measured width animated along with the text width. However, a side effect of this is that
     * the entering/exiting character might get truncated by the view's view bounds as the width
     * shrinks or expands.
     *
     * <p>Warning: using this feature may degrade performance as it will force a re-measure and
     * re-layout during each animation frame.
     *
     * <p>This flag is disabled by default.
     *
     * @param animateMeasurementChange whether or not to animate measurement changes.
     */
    public void setAnimateMeasurementChange(boolean animateMeasurementChange) {
        this.animateMeasurementChange = animateMeasurementChange;
    }

    /**
     * @return whether or not we are currently animating measurement changes.
     */
    public boolean getAnimateMeasurementChange() {
        return animateMeasurementChange;
    }

    /**
     * Adds a custom {@link Animator.AnimatorListener} to listen to animator
     * update events used by this view.
     *
     * @param animatorListener the custom animator listener.
     */
    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        animator.addListener(animatorListener);
    }

    /**
     * Removes the specified custom {@link Animator.AnimatorListener} from
     * this view.
     *
     * @param animatorListener the custom animator listener.
     */
    public void removeAnimatorListener(Animator.AnimatorListener animatorListener) {
        animator.removeListener(animatorListener);
    }


    /********** END PUBLIC API **********/


    /**
     * Force the view to call {@link #requestLayout()} if the new text doesn't match the old bounds
     * we set for the previous view state.
     */
    private void checkForRelayout() {
        final boolean widthChanged = lastMeasuredDesiredWidth != computeDesiredWidth();
        final boolean heightChanged = lastMeasuredDesiredHeight != computeDesiredHeight();

        if (widthChanged || heightChanged) {
            requestLayout();
        }
    }

    private int computeDesiredWidth() {
        final int contentWidth = (int) (animateMeasurementChange ?
                columnManager.getCurrentWidth() : columnManager.getMinimumRequiredWidth());
        return contentWidth + getPaddingLeft() + getPaddingRight();
    }

    private int computeDesiredHeight() {
        return (int) metrics.getCharHeight() + getPaddingTop() + getPaddingBottom();
    }

    /**
     * Re-initialize all of our variables that are dependent on the TextPaint measurements.
     */
    private void onTextPaintMeasurementChanged() {
        metrics.invalidate();
        checkForRelayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int desiredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = MeasureSpec.getSize(heightMeasureSpec);

        lastMeasuredDesiredWidth = computeDesiredWidth();
        lastMeasuredDesiredHeight = computeDesiredHeight();

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                desiredWidth = Math.min(desiredWidth, lastMeasuredDesiredWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                desiredWidth = lastMeasuredDesiredWidth;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                desiredHeight = Math.min(desiredHeight, lastMeasuredDesiredHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                desiredHeight = lastMeasuredDesiredHeight;
                break;
        }

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        viewBounds.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                height - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        realignAndClipCanvasForGravity(canvas);

        // canvas.drawText writes the text on the baseline so we need to translate beforehand.
        canvas.translate(0f, metrics.getCharBaseline());

        columnManager.draw(canvas, textPaint);

        canvas.restore();
    }

    private void realignAndClipCanvasForGravity(Canvas canvas) {
        final float currentWidth = columnManager.getCurrentWidth();
        final float currentHeight = metrics.getCharHeight();
        realignAndClipCanvasForGravity(canvas, gravity, viewBounds, currentWidth, currentHeight);
    }

    // VisibleForTesting
    static void realignAndClipCanvasForGravity(Canvas canvas, int gravity, Rect viewBounds,
            float currentWidth, float currentHeight) {
        final int availableWidth = viewBounds.width();
        final int availableHeight = viewBounds.height();

        float translationX = 0;
        float translationY = 0;
        if ((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            translationY = viewBounds.top + (availableHeight - currentHeight) / 2f;
        }
        if ((gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            translationX = viewBounds.left + (availableWidth - currentWidth) / 2f;
        }
        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            translationY = 0;
        }
        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            translationY = viewBounds.top + (availableHeight - currentHeight);
        }
        if ((gravity & Gravity.START) == Gravity.START) {
            translationX = 0;
        }
        if ((gravity & Gravity.END) == Gravity.END) {
            translationX = viewBounds.left + (availableWidth - currentWidth);
        }

        canvas.translate(translationX ,translationY);
        canvas.clipRect(0f, 0f, currentWidth, currentHeight);
    }
}
