package com.vondear.rxtools.view.tooltips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class RxToolTipsManager {

    private static final String TAG = RxToolTipsManager.class.getSimpleName();

    private static final int DEFAULT_ANIM_DURATION = 400;

    // Parameter for managing tip creation or reuse
    private Map<Integer, View> mTipsMap = new HashMap<>();

    private int mAnimationDuration;
    private TipListener mListener;

    public interface TipListener {
        void onTipDismissed(View view, int anchorViewId, boolean byUser);
    }

    public RxToolTipsManager() {
        mAnimationDuration = DEFAULT_ANIM_DURATION;
    }

    public RxToolTipsManager(TipListener listener) {
        mAnimationDuration = DEFAULT_ANIM_DURATION;
        mListener = listener;
    }

    public View show(RxToolTip rxToolTip) {
        View tipView = create(rxToolTip);
        if (tipView == null) {
            return null;
        }

        // animate tip visibility
        RxAnimationUtils.popup(tipView, mAnimationDuration).start();

        return tipView;
    }

    private View create(RxToolTip rxToolTip) {

        if (rxToolTip.getAnchorView() == null) {
            Log.e(TAG, "Unable to create a tip, anchor view is null");
            return null;
        }

        if (rxToolTip.getRootView() == null) {
            Log.e(TAG, "Unable to create a tip, root layout is null");
            return null;
        }

        // only one tip is allowed near an anchor view at the same time, thus
        // reuse tip if already exist
        if (mTipsMap.containsKey(rxToolTip.getAnchorView().getId())) {
            return mTipsMap.get(rxToolTip.getAnchorView().getId());
        }

        // init tip view parameters
        TextView tipView = createTipView(rxToolTip);

        // on RTL languages replace sides
        if (RxToolTipUtils.isRtl()) {
            switchToolTipSidePosition(rxToolTip);
        }

        // set tool tip background / shape
        RxToolTipBackgroundConstructor.setBackground(tipView, rxToolTip);

        // add tip to root layout
        rxToolTip.getRootView().addView(tipView);

        // find where to position the tool tip
        Point p = RxToolTipCoordinatesFinder.getCoordinates(tipView, rxToolTip);

        // move tip view to correct position
        moveTipToCorrectPosition(tipView, p);

        // set dismiss on click
        tipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(view, true);
            }
        });

        // bind tipView with anchorView id
        int anchorViewId = rxToolTip.getAnchorView().getId();
        tipView.setTag(anchorViewId);

        // enter tip to map by 'anchorView' id
        mTipsMap.put(anchorViewId, tipView);

        return tipView;

    }

    private void moveTipToCorrectPosition(TextView tipView, Point p) {
        RxCoordinates tipViewRxCoordinates = new RxCoordinates(tipView);
        int translationX = p.x - tipViewRxCoordinates.left;
        int translationY = p.y - tipViewRxCoordinates.top;
        if (!RxToolTipUtils.isRtl()) tipView.setTranslationX(translationX);
        else tipView.setTranslationX(-translationX);
        tipView.setTranslationY(translationY);
    }

    @NonNull
    private TextView createTipView(RxToolTip rxToolTip) {
        TextView tipView = new TextView(rxToolTip.getContext());
        tipView.setTextColor(rxToolTip.getTextColor());
        tipView.setTextSize(rxToolTip.getTextSize());
        tipView.setText(rxToolTip.getMessage() != null ? rxToolTip.getMessage() : rxToolTip.getSpannableMessage());
        tipView.setVisibility(View.INVISIBLE);
        tipView.setGravity(rxToolTip.getTextGravity());
        setTipViewElevation(tipView, rxToolTip);
        return tipView;
    }

    private void setTipViewElevation(TextView tipView, RxToolTip rxToolTip) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (rxToolTip.getElevation() > 0) {
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                    @SuppressLint("NewApi")
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setEmpty();
                    }
                };
                tipView.setOutlineProvider(viewOutlineProvider);
                tipView.setElevation(rxToolTip.getElevation());
            }
        }
    }

    private void switchToolTipSidePosition(RxToolTip rxToolTip) {
        if (rxToolTip.positionedLeftTo()) {
            rxToolTip.setPosition(RxToolTip.POSITION_RIGHT_TO);
        } else if (rxToolTip.positionedRightTo()) {
            rxToolTip.setPosition(RxToolTip.POSITION_LEFT_TO);
        }
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public boolean dismiss(View tipView, boolean byUser) {
        if (tipView != null && isVisible(tipView)) {
            int key = (int) tipView.getTag();
            mTipsMap.remove(key);
            animateDismiss(tipView, byUser);
            return true;
        }
        return false;
    }

    public boolean dismiss(Integer key) {
        return mTipsMap.containsKey(key) && dismiss(mTipsMap.get(key), false);
    }

    public View find(Integer key) {
        if (mTipsMap.containsKey(key)) {
            return mTipsMap.get(key);
        }
        return null;
    }

    public boolean findAndDismiss(final View anchorView) {
        View view = find(anchorView.getId());
        return view != null && dismiss(view, false);
    }

    public void clear() {
        if (!mTipsMap.isEmpty()) {
            for (Map.Entry<Integer, View> entry : mTipsMap.entrySet()) {
                dismiss(entry.getValue(), false);
            }
        }
        mTipsMap.clear();
    }

    private void animateDismiss(final View view, final boolean byUser) {
        RxAnimationUtils.popout(view, mAnimationDuration, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mListener != null) {
                    mListener.onTipDismissed(view, (Integer) view.getTag(), byUser);
                }
            }
        }).start();
    }

    public boolean isVisible(View tipView) {
        return tipView.getVisibility() == View.VISIBLE;
    }

}
