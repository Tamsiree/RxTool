package com.vondear.rxtools.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class RxTextAutoZoom extends android.support.v7.widget.AppCompatEditText {
    private static final int NO_LINE_LIMIT = -1;
    private final RectF _availableSpaceRect = new RectF();
    private final SparseIntArray _textCachedSizes = new SparseIntArray();
    private final SizeTester _sizeTester;
    private float _maxTextSize;
    private float _spacingMult = 1.0f;
    private float _spacingAdd = 0.0f;
    private Float _minTextSize;
    private int _widthLimit;
    private int _maxLines;
    private boolean _enableSizeCache = true;
    private boolean _initiallized = false;
    private TextPaint paint;

    private interface SizeTester {
        /**
         * AutoFitEditText
         *
         * @param suggestedSize  Size of text to be tested
         * @param availableSpace available space in which text must fit
         * @return an integer < 0 if after applying {@code suggestedSize} to
         * text, it takes less space than {@code availableSpace}, > 0
         * otherwise
         */
        public int onTestSize(int suggestedSize, RectF availableSpace);
    }

    public RxTextAutoZoom(final Context context) {
        this(context, null, 0);
    }

    public RxTextAutoZoom(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxTextAutoZoom(final Context context, final AttributeSet attrs,
                          final int defStyle) {
        super(context, attrs, defStyle);
        // using the minimal recommended font size
        _minTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                12, getResources().getDisplayMetrics());
        _maxTextSize = getTextSize();
        if (_maxLines == 0)
            // no value was assigned during construction
            _maxLines = NO_LINE_LIMIT;
        // prepare size tester:
        _sizeTester = new SizeTester() {
            final RectF textRect = new RectF();

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public int onTestSize(final int suggestedSize,
                                  final RectF availableSPace) {
                paint.setTextSize(suggestedSize);
                final String text = getText().toString();
                final boolean singleline = getMaxLines() == 1;
                if (singleline) {
                    textRect.bottom = paint.getFontSpacing();
                    textRect.right = paint.measureText(text);
                } else {
                    final StaticLayout layout = new StaticLayout(text, paint,
                            _widthLimit, Layout.Alignment.ALIGN_NORMAL, _spacingMult,
                            _spacingAdd, true);
                    if (getMaxLines() != NO_LINE_LIMIT
                            && layout.getLineCount() > getMaxLines())
                        return 1;
                    textRect.bottom = layout.getHeight();
                    int maxWidth = -1;
                    for (int i = 0; i < layout.getLineCount(); i++)
                        if (maxWidth < layout.getLineWidth(i))
                            maxWidth = (int) layout.getLineWidth(i);
                    textRect.right = maxWidth;
                }
                textRect.offsetTo(0, 0);
                if (availableSPace.contains(textRect))
                    // may be too small, don't worry we will find the best match
                    return -1;
                // else, too big
                return 1;
            }
        };
        _initiallized = true;
    }

    @Override
    public void setTypeface(final Typeface tf) {
        if (paint == null)
            paint = new TextPaint(getPaint());
        paint.setTypeface(tf);
        super.setTypeface(tf);
    }

    @Override
    public void setTextSize(final float size) {
        _maxTextSize = size;
        _textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setMaxLines(final int maxlines) {
        super.setMaxLines(maxlines);
        _maxLines = maxlines;
        reAdjust();
    }

    @Override
    public int getMaxLines() {
        return _maxLines;
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        _maxLines = 1;
        reAdjust();
    }

    @Override
    public void setSingleLine(final boolean singleLine) {
        super.setSingleLine(singleLine);
        if (singleLine)
            _maxLines = 1;
        else
            _maxLines = NO_LINE_LIMIT;
        reAdjust();
    }

    @Override
    public void setLines(final int lines) {
        super.setLines(lines);
        _maxLines = lines;
        reAdjust();
    }

    @Override
    public void setTextSize(final int unit, final float size) {
        final Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        _maxTextSize = TypedValue.applyDimension(unit, size,
                r.getDisplayMetrics());
        _textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(final float add, final float mult) {
        super.setLineSpacing(add, mult);
        _spacingMult = mult;
        _spacingAdd = add;
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param
     */
    public void setMinTextSize(final Float minTextSize) {
        _minTextSize = minTextSize;
        reAdjust();
    }

    public Float get_minTextSize() {
        return _minTextSize;
    }

    private void reAdjust() {
        adjustTextSize();
    }

    private void adjustTextSize() {
        if (!_initiallized)
            return;
        final int startSize = Math.round(_minTextSize);
        final int heightLimit = getMeasuredHeight()
                - getCompoundPaddingBottom() - getCompoundPaddingTop();
        _widthLimit = getMeasuredWidth() - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
        if (_widthLimit <= 0)
            return;
        _availableSpaceRect.right = _widthLimit;
        _availableSpaceRect.bottom = heightLimit;
        super.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                efficientTextSizeSearch(startSize, (int) _maxTextSize,
                        _sizeTester, _availableSpaceRect));
    }

    /**
     * Enables or disables size caching, enabling it will improve performance
     * where you are animating a value inside TextView. This stores the font
     * size against getText().length() Be careful though while enabling it as 0
     * takes more space than 1 on some fonts and so on.
     *
     * @param enable enable font size caching
     */
    public void setEnableSizeCache(final boolean enable) {
        _enableSizeCache = enable;
        _textCachedSizes.clear();
        adjustTextSize();
    }

    private int efficientTextSizeSearch(final int start, final int end,
                                        final SizeTester sizeTester, final RectF availableSpace) {
        if (!_enableSizeCache)
            return binarySearch(start, end, sizeTester, availableSpace);
        final String text = getText().toString();
        final int key = text == null ? 0 : text.length();
        int size = _textCachedSizes.get(key);
        if (size != 0)
            return size;
        size = binarySearch(start, end, sizeTester, availableSpace);
        _textCachedSizes.put(key, size);
        return size;
    }

    private int binarySearch(final int start, final int end,
                             final SizeTester sizeTester, final RectF availableSpace) {
        int lastBest = start;
        int lo = start;
        int hi = end - 1;
        int mid = 0;
        while (lo <= hi) {
            mid = lo + hi >>> 1;
            final int midValCmp = sizeTester.onTestSize(mid, availableSpace);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = mid + 1;
            } else if (midValCmp > 0) {
                hi = mid - 1;
                lastBest = hi;
            } else
                return mid;
        }
        // make sure to return last best
        // this is what should always be returned
        return lastBest;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        reAdjust();
    }

    @Override
    protected void onSizeChanged(final int width, final int height,
                                 final int oldwidth, final int oldheight) {
        _textCachedSizes.clear();
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight)
            reAdjust();
    }


    public static void setNormalization(final Activity a, View rootView, final RxTextAutoZoom aText) {

        // if the view is not instance of AutoFitEditText
        // i.e. if the user taps outside of the box
        if (!(rootView instanceof RxTextAutoZoom)) {

            rootView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(a);
                    if (aText.get_minTextSize() != null && aText.getTextSize() < aText.get_minTextSize()) {
                        // you can define your minSize, in this case is 50f
                        // trim all the new lines and set the text as it was
                        // before
                        aText.setText(aText.getText().toString().replace("\n", ""));


                    }
                    return false;
                }
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (rootView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
                View innerView = ((ViewGroup) rootView).getChildAt(i);
                setNormalization(a, innerView, aText);
            }
        }
    }

    public static void hideSoftKeyboard(Activity a) {
        InputMethodManager inputMethodManager = (InputMethodManager) a
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (a.getCurrentFocus() != null
                && a.getCurrentFocus().getWindowToken() != null)
            inputMethodManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), 0);
    }
}
