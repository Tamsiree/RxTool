package com.vondear.rxtools.view.cardstack;

import android.animation.ObjectAnimator;
import android.view.View;

public class RxAdapterUpDownStackAnimator extends RxAdapterAnimator {

    public RxAdapterUpDownStackAnimator(RxCardStackView rxCardStackView) {
        super(rxCardStackView);
    }

    protected void itemExpandAnimatorSet(final RxCardStackView.ViewHolder viewHolder, int position) {
        final View itemView = viewHolder.itemView;
        itemView.clearAnimation();
        ObjectAnimator oa = ObjectAnimator.ofFloat(itemView, View.Y, itemView.getY(), mRxCardStackView.getChildAt(0).getY());
        mSet.play(oa);
        int collapseShowItemCount = 0;
        for (int i = 0; i < mRxCardStackView.getChildCount(); i++) {
            int childTop;
            if (i == mRxCardStackView.getSelectPosition()) continue;
            final View child = mRxCardStackView.getChildAt(i);
            child.clearAnimation();
            if (i > mRxCardStackView.getSelectPosition() && collapseShowItemCount < mRxCardStackView.getNumBottomShow()) {
                childTop = mRxCardStackView.getShowHeight() - getCollapseStartTop(collapseShowItemCount);
                ObjectAnimator oAnim = ObjectAnimator.ofFloat(child, View.Y, child.getY(), childTop);
                mSet.play(oAnim);
                collapseShowItemCount++;
            } else if (i < mRxCardStackView.getSelectPosition()) {
                ObjectAnimator oAnim = ObjectAnimator.ofFloat(child, View.Y, child.getY(), mRxCardStackView.getChildAt(0).getY());
                mSet.play(oAnim);
            } else {
                ObjectAnimator oAnim = ObjectAnimator.ofFloat(child, View.Y, child.getY(), mRxCardStackView.getShowHeight());
                mSet.play(oAnim);
            }
        }
    }

    @Override
    protected void itemCollapseAnimatorSet(RxCardStackView.ViewHolder viewHolder) {
        int childTop = mRxCardStackView.getPaddingTop();
        for (int i = 0; i < mRxCardStackView.getChildCount(); i++) {
            View child = mRxCardStackView.getChildAt(i);
            child.clearAnimation();
            final RxCardStackView.LayoutParams lp =
                    (RxCardStackView.LayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;
            if (i != 0) {
                childTop -= mRxCardStackView.getOverlapGaps() * 2;
            }
            ObjectAnimator oAnim = ObjectAnimator.ofFloat(child, View.Y, child.getY(),
                    childTop - mRxCardStackView.getRxScrollDelegate().getViewScrollY() < mRxCardStackView.getChildAt(0).getY()
                            ? mRxCardStackView.getChildAt(0).getY() : childTop - mRxCardStackView.getRxScrollDelegate().getViewScrollY());
            mSet.play(oAnim);
            childTop += lp.mHeaderHeight;
        }
    }

}
