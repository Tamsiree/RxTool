package com.vondear.rxtools.view.cardstack.tools;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.vondear.rxtools.view.cardstack.RxCardStackView;

/**
 * @author vondear
 */
public abstract class RxAdapterAnimator {
    public static final int ANIMATION_DURATION = 400;

    protected RxCardStackView mRxCardStackView;
    protected AnimatorSet mSet;

    public RxAdapterAnimator(RxCardStackView rxCardStackView) {
        mRxCardStackView = rxCardStackView;
    }

    protected void initAnimatorSet() {
        mSet = new AnimatorSet();
        mSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mSet.setDuration(getDuration());
    }

    public void itemClick(final RxCardStackView.ViewHolder viewHolder, int position) {
        if (mSet != null && mSet.isRunning()) return;
        initAnimatorSet();
        if (mRxCardStackView.getSelectPosition() == position) {
            onItemCollapse(viewHolder);
        } else {
            onItemExpand(viewHolder, position);
        }
        if (mRxCardStackView.getChildCount() == 1)
            mSet.end();
    }

    protected abstract void itemExpandAnimatorSet(RxCardStackView.ViewHolder viewHolder, int position);

    protected abstract void itemCollapseAnimatorSet(RxCardStackView.ViewHolder viewHolder);

    private void onItemExpand(final RxCardStackView.ViewHolder viewHolder, int position) {
        final int preSelectPosition = mRxCardStackView.getSelectPosition();
        final RxCardStackView.ViewHolder preSelectViewHolder = mRxCardStackView.getViewHolder(preSelectPosition);
        if (preSelectViewHolder != null) {
            preSelectViewHolder.onItemExpand(false);
        }
        mRxCardStackView.setSelectPosition(position);
        itemExpandAnimatorSet(viewHolder, position);
        mSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRxCardStackView.setScrollEnable(false);
                if (preSelectViewHolder != null) {
                    preSelectViewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, false);
                }
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewHolder.onItemExpand(true);
                if (preSelectViewHolder != null) {
                    preSelectViewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, false);
                }
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (preSelectViewHolder != null) {
                    preSelectViewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, false);
                }
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, true);
            }
        });
        mSet.start();
    }

    private void onItemCollapse(final RxCardStackView.ViewHolder viewHolder) {
        itemCollapseAnimatorSet(viewHolder);
        mSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                viewHolder.onItemExpand(false);
                mRxCardStackView.setScrollEnable(true);
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_START, false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRxCardStackView.setSelectPosition(RxCardStackView.DEFAULT_SELECT_POSITION);
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_END, false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                viewHolder.onAnimationStateChange(RxCardStackView.ANIMATION_STATE_CANCEL, false);
            }
        });
        mSet.start();
    }

    protected int getCollapseStartTop(int collapseShowItemCount) {
        return mRxCardStackView.getOverlapGapsCollapse()
                * (mRxCardStackView.getNumBottomShow() - collapseShowItemCount - (mRxCardStackView.getNumBottomShow() - (mRxCardStackView.getChildCount() - mRxCardStackView.getSelectPosition() > mRxCardStackView.getNumBottomShow()
                ? mRxCardStackView.getNumBottomShow()
                : mRxCardStackView.getChildCount() - mRxCardStackView.getSelectPosition() - 1)));
    }

    public int getDuration() {
        return mRxCardStackView.getDuration();
    }
}
