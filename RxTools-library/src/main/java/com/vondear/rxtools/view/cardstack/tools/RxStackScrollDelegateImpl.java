package com.vondear.rxtools.view.cardstack.tools;

import android.view.View;

import com.vondear.rxtools.view.cardstack.RxCardStackView;

/**
 * @author vondear
 */
public class RxStackScrollDelegateImpl implements RxScrollDelegate {

    private RxCardStackView mRxCardStackView;
    private int mScrollY;
    private int mScrollX;

    public RxStackScrollDelegateImpl(RxCardStackView rxCardStackView) {
        mRxCardStackView = rxCardStackView;
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

    private void updateChildPos() {
        for (int i = 0; i < mRxCardStackView.getChildCount(); i++) {
            View view = mRxCardStackView.getChildAt(i);
            if (view.getTop() - mScrollY < mRxCardStackView.getChildAt(0).getY()) {
                view.setTranslationY(mRxCardStackView.getChildAt(0).getY() - view.getTop());
            } else if (view.getTop() - mScrollY > view.getTop()) {
                view.setTranslationY(0);
            } else {
                view.setTranslationY(-mScrollY);
            }
        }
    }

    @Override
    public void scrollViewTo(int x, int y) {
        x = clamp(x, mRxCardStackView.getWidth() - mRxCardStackView.getPaddingRight() - mRxCardStackView.getPaddingLeft(), mRxCardStackView.getWidth());
        y = clamp(y, mRxCardStackView.getShowHeight(), mRxCardStackView.getTotalLength());
        mScrollY = y;
        mScrollX = x;
        updateChildPos();
    }

    @Override
    public int getViewScrollY() {
        return mScrollY;
    }

    @Override
    public void setViewScrollY(int y) {
        scrollViewTo(mScrollX, y);
    }

    @Override
    public int getViewScrollX() {
        return mScrollX;
    }

    @Override
    public void setViewScrollX(int x) {
        scrollViewTo(x, mScrollY);
    }
}
