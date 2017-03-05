package com.vondear.rxtools.view.cardstack;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Observable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;

import com.vondear.rxtools.R;

import java.util.ArrayList;
import java.util.List;

public class RxCardStackView extends ViewGroup implements RxScrollDelegate {

    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    public static final int ANIMATION_STATE_START = 0;
    public static final int ANIMATION_STATE_END = 1;
    public static final int ANIMATION_STATE_CANCEL = 2;

    private static final String TAG = "CardStackView";

    public static final int ALL_DOWN = 0;
    public static final int UP_DOWN = 1;
    public static final int UP_DOWN_STACK = 2;

    static final int DEFAULT_SELECT_POSITION = -1;

    private int mTotalLength;
    private int mOverlapGaps;
    private int mOverlapGapsCollapse;
    private int mNumBottomShow;
    private RxAdapterStack mRxAdapterStack;
    private final ViewDataObserver mObserver = new ViewDataObserver();
    private int mSelectPosition = DEFAULT_SELECT_POSITION;
    private int mShowHeight;
    private List<ViewHolder> mViewHolders;

    private RxAdapterAnimator mRxAdapterAnimator;
    private int mDuration;

    private OverScroller mScroller;
    private int mLastMotionY;
    private boolean mIsBeingDragged = false;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mActivePointerId = INVALID_POINTER;
    private final int[] mScrollOffset = new int[2];
    private int mNestedYOffset;
    private boolean mScrollEnable = true;

    private RxScrollDelegate mRxScrollDelegate;
    private ItemExpendListener mItemExpendListener;

    public RxCardStackView(Context context) {
        this(context, null);
    }

    public RxCardStackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxCardStackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RxCardStackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RxCardStackView, defStyleAttr, defStyleRes);
        setOverlapGaps(array.getDimensionPixelSize(R.styleable.RxCardStackView_stackOverlapGaps, dp2px(20)));
        setOverlapGapsCollapse(array.getDimensionPixelSize(R.styleable.RxCardStackView_stackOverlapGapsCollapse, dp2px(20)));
        setDuration(array.getInt(R.styleable.RxCardStackView_stackDuration, RxAdapterAnimator.ANIMATION_DURATION));
        setAnimationType(array.getInt(R.styleable.RxCardStackView_stackAnimationType, UP_DOWN_STACK));
        setNumBottomShow(array.getInt(R.styleable.RxCardStackView_stackNumBottomShow, 3));
        array.recycle();

        mViewHolders = new ArrayList<>();
        initScroller();
    }

    private void initScroller() {
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private int dp2px(int value) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkContentHeightByParent();
        measureChild(widthMeasureSpec, heightMeasureSpec);
    }

    private void checkContentHeightByParent() {
        View parentView = (View) getParent();
        mShowHeight = parentView.getMeasuredHeight() - parentView.getPaddingTop() - parentView.getPaddingBottom();
    }

    private void measureChild(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        mTotalLength = 0;
        mTotalLength += getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            final int totalLength = mTotalLength;
            final LayoutParams lp =
                    (LayoutParams) child.getLayoutParams();
            if (lp.mHeaderHeight == -1) lp.mHeaderHeight = child.getMeasuredHeight();
            final int childHeight = lp.mHeaderHeight;
            mTotalLength = Math.max(totalLength, totalLength + childHeight + lp.topMargin +
                    lp.bottomMargin);
            mTotalLength -= mOverlapGaps * 2;
            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            maxWidth = Math.max(maxWidth, measuredWidth);
        }

        mTotalLength += mOverlapGaps * 2;
        int heightSize = mTotalLength;
        heightSize = Math.max(heightSize, mShowHeight);
        int heightSizeAndState = resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                heightSizeAndState);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild();
    }

    private void layoutChild() {
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            final LayoutParams lp =
                    (LayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;
            if (i != 0) {
                childTop -= mOverlapGaps * 2;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            } else {
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }
            childTop += lp.mHeaderHeight;
        }
    }

    public void updateSelectPosition(final int selectPosition) {
        post(new Runnable() {
            @Override
            public void run() {
                doCardClickAnimation(mViewHolders.get(selectPosition), selectPosition);
            }
        });
    }

    public void clearSelectPosition() {
        updateSelectPosition(mSelectPosition);
    }

    public void clearScrollYAndTranslation() {
        if (mSelectPosition != DEFAULT_SELECT_POSITION) {
            clearSelectPosition();
        }
        if (mRxScrollDelegate != null) mRxScrollDelegate.setViewScrollY(0);
        requestLayout();
    }

    public void setAdapter(RxAdapterStack rxAdapterStack) {
        mRxAdapterStack = rxAdapterStack;
        mRxAdapterStack.registerObserver(mObserver);
        refreshView();
    }

    public void setAnimationType(int type) {
        RxAdapterAnimator rxAdapterAnimator;
        switch (type) {
            case ALL_DOWN:
                rxAdapterAnimator = new RxAdapterAllMoveDownAnimator(this);
                break;
            case UP_DOWN:
                rxAdapterAnimator = new RxAdapterUpDownAnimator(this);
                break;
            default:
                rxAdapterAnimator = new RxAdapterUpDownStackAnimator(this);
                break;
        }
        setRxAdapterAnimator(rxAdapterAnimator);
    }

    public void setRxAdapterAnimator(RxAdapterAnimator rxAdapterAnimator) {
        clearScrollYAndTranslation();
        mRxAdapterAnimator = rxAdapterAnimator;
        if (mRxAdapterAnimator instanceof RxAdapterUpDownStackAnimator) {
            mRxScrollDelegate = new RxStackScrollDelegateImpl(this);
        } else {
            mRxScrollDelegate = this;
        }
    }

    private void refreshView() {
        removeAllViews();
        mViewHolders.clear();
        for (int i = 0; i < mRxAdapterStack.getItemCount(); i++) {
            ViewHolder holder = getViewHolder(i);
            holder.position = i;
            holder.onItemExpand(i == mSelectPosition);
            addView(holder.itemView);
            setClickAnimator(holder, i);
            mRxAdapterStack.bindViewHolder(holder, i);
        }
        requestLayout();
    }

    ViewHolder getViewHolder(int i) {
        if (i == DEFAULT_SELECT_POSITION) return null;
        ViewHolder viewHolder;
        if (mViewHolders.size() <= i || mViewHolders.get(i).mItemViewType != mRxAdapterStack.getItemViewType(i)) {
            viewHolder = mRxAdapterStack.createView(this, mRxAdapterStack.getItemViewType(i));
            mViewHolders.add(viewHolder);
        } else {
            viewHolder = mViewHolders.get(i);
        }
        return viewHolder;
    }

    private void setClickAnimator(final ViewHolder holder, final int position) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition == DEFAULT_SELECT_POSITION) return;
                performItemClick(mViewHolders.get(mSelectPosition));
            }
        });
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performItemClick(holder);
            }
        });
    }

    public void next() {
        if (mSelectPosition == DEFAULT_SELECT_POSITION || mSelectPosition == mViewHolders.size() - 1) return;
        performItemClick(mViewHolders.get(mSelectPosition + 1));
    }

    public void pre() {
        if (mSelectPosition == DEFAULT_SELECT_POSITION || mSelectPosition == 0) return;
        performItemClick(mViewHolders.get(mSelectPosition - 1));
    }

    public boolean isExpending() {
        return mSelectPosition != DEFAULT_SELECT_POSITION;
    }

    public void performItemClick(ViewHolder viewHolder) {
        doCardClickAnimation(viewHolder, viewHolder.position);
    }

    private void doCardClickAnimation(final ViewHolder viewHolder, int position) {
        checkContentHeightByParent();
        mRxAdapterAnimator.itemClick(viewHolder, position);
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        if (getViewScrollY() == 0 && !canScrollVertically(1)) {
            return false;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);
                if (yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    mNestedYOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final int y = (int) ev.getY();
                mLastMotionY = y;
                mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mIsBeingDragged = !mScroller.isFinished();
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                if (mScroller.springBack(getViewScrollX(), getViewScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        if (!mScrollEnable) {
            mIsBeingDragged = false;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsBeingDragged) {
            super.onTouchEvent(ev);
        }
        if (!mScrollEnable) {
            return true;
        }
        initVelocityTrackerIfNotExists();

        MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = ev.getActionMasked();

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                final int y = (int) ev.getY(activePointerIndex);
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionY = y - mScrollOffset[1];
                    final int range = getScrollRange();
                    if (mRxScrollDelegate instanceof RxStackScrollDelegateImpl) {
                        mRxScrollDelegate.scrollViewTo(0, deltaY + mRxScrollDelegate.getViewScrollY());
                    } else {
                        if (overScrollBy(0, deltaY, 0, getViewScrollY(),
                                0, range, 0, 0, true)) {
                            mVelocityTracker.clear();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);
                    if (getChildCount() > 0) {
                        if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                            fling(-initialVelocity);
                        } else {
                            if (mScroller.springBack(getViewScrollX(), mRxScrollDelegate.getViewScrollY(), 0, 0, 0,
                                    getScrollRange())) {
                                postInvalidate();
                            }
                        }
                        mActivePointerId = INVALID_POINTER;
                    }
                }
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    if (mScroller.springBack(getViewScrollX(), mRxScrollDelegate.getViewScrollY(), 0, 0, 0, getScrollRange())) {
                        postInvalidate();
                    }
                    mActivePointerId = INVALID_POINTER;
                }
                endDrag();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionY = (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            scrollRange = Math.max(0,
                    mTotalLength - mShowHeight);
        }
        return scrollRange;
    }

    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = mShowHeight;
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = mTotalLength;
        final int scrollY = mRxScrollDelegate.getViewScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY,
                                  boolean clampedX, boolean clampedY) {
        if (!mScroller.isFinished()) {
            final int oldX = mRxScrollDelegate.getViewScrollX();
            final int oldY = mRxScrollDelegate.getViewScrollY();
            mRxScrollDelegate.setViewScrollX(scrollX);
            mRxScrollDelegate.setViewScrollY(scrollY);
            onScrollChanged(mRxScrollDelegate.getViewScrollX(), mRxScrollDelegate.getViewScrollY(), oldX, oldY);
            if (clampedY) {
                mScroller.springBack(mRxScrollDelegate.getViewScrollX(), mRxScrollDelegate.getViewScrollY(), 0, 0, 0, getScrollRange());
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mRxScrollDelegate.scrollViewTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = mShowHeight;
            int bottom = mTotalLength;
            mScroller.fling(mRxScrollDelegate.getViewScrollX(), mRxScrollDelegate.getViewScrollY(), 0, velocityY, 0, 0, 0,
                    Math.max(0, bottom - height), 0, 0);
            postInvalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), getWidth());
            y = clamp(y, mShowHeight, mTotalLength);
            if (x != mRxScrollDelegate.getViewScrollX() || y != mRxScrollDelegate.getViewScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    @Override
    public int getViewScrollX() {
        return getScrollX();
    }

    @Override
    public void scrollViewTo(int x, int y) {
        scrollTo(x, y);
    }

    @Override
    public void setViewScrollY(int y) {
        setScrollY(y);
    }

    @Override
    public void setViewScrollX(int x) {
        setScrollX(x);
    }

    @Override
    public int getViewScrollY() {
        return getScrollY();
    }

    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my + n) > child) {
            return child - my;
        }
        return n;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public int mHeaderHeight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.RxCardStackView);
            mHeaderHeight = array.getDimensionPixelSize(R.styleable.RxCardStackView_stackHeaderHeight, -1);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        VH createView(ViewGroup parent, int viewType) {
            VH holder = onCreateView(parent, viewType);
            holder.mItemViewType = viewType;
            return holder;
        }

        protected abstract VH onCreateView(ViewGroup parent, int viewType);

        public void bindViewHolder(VH holder, int position) {
            onBindViewHolder(holder, position);
        }

        protected abstract void onBindViewHolder(VH holder, int position);

        public abstract int getItemCount();

        public int getItemViewType(int position) {
            return 0;
        }

        public final void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }

        public void registerObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }
    }

    public static abstract class ViewHolder {

        public View itemView;
        int mItemViewType = INVALID_TYPE;
        int position;

        public ViewHolder(View view) {
            itemView = view;
        }

        public Context getContext() {
            return itemView.getContext();
        }

        public abstract void onItemExpand(boolean b);

        protected void onAnimationStateChange(int state, boolean willBeSelect) {

        }
    }

    public static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }
    }

    private class ViewDataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            refreshView();
        }
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
        mItemExpendListener.onItemExpend(mSelectPosition != DEFAULT_SELECT_POSITION);
    }

    public int getOverlapGaps() {
        return mOverlapGaps;
    }

    public void setOverlapGaps(int overlapGaps) {
        mOverlapGaps = overlapGaps;
    }

    public int getOverlapGapsCollapse() {
        return mOverlapGapsCollapse;
    }

    public void setOverlapGapsCollapse(int overlapGapsCollapse) {
        mOverlapGapsCollapse = overlapGapsCollapse;
    }

    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    public int getShowHeight() {
        return mShowHeight;
    }

    public int getTotalLength() {
        return mTotalLength;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        if (mRxAdapterAnimator != null) return mDuration;
        return 0;
    }

    public void setNumBottomShow(int numBottomShow) {
        mNumBottomShow = numBottomShow;
    }

    public int getNumBottomShow() {
        return mNumBottomShow;
    }

    public RxScrollDelegate getRxScrollDelegate() {
        return mRxScrollDelegate;
    }

    public ItemExpendListener getItemExpendListener() {
        return mItemExpendListener;
    }

    public void setItemExpendListener(ItemExpendListener itemExpendListener) {
        mItemExpendListener = itemExpendListener;
    }

    public interface ItemExpendListener{
        void onItemExpend(boolean expend);
    }
}
