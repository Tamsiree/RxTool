package com.vondear.rxtools.view.popupwindows.tools;

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

import com.vondear.rxtools.RxAnimationTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vondear
 */
public class RxPopupViewManager {

    private static final String TAG = RxPopupViewManager.class.getSimpleName();

    private static final int DEFAULT_ANIM_DURATION = 400;

    // Parameter for managing tip creation or reuse
    private Map<Integer, View> mTipsMap = new HashMap<>();

    private int mAnimationDuration;
    private TipListener mListener;

    public RxPopupViewManager() {
        mAnimationDuration = DEFAULT_ANIM_DURATION;
    }

    public RxPopupViewManager(TipListener listener) {
        mAnimationDuration = DEFAULT_ANIM_DURATION;
        mListener = listener;
    }

    public View show(RxPopupView rxPopupView) {
        View tipView = create(rxPopupView);
        if (tipView == null) {
            return null;
        }

        // animate tip visibility
        RxAnimationTool.popup(tipView, mAnimationDuration).start();

        return tipView;
    }

    private View create(RxPopupView rxPopupView) {

        if (rxPopupView.getAnchorView() == null) {
            Log.e(TAG, "Unable to create a tip, anchor view is null");
            return null;
        }

        if (rxPopupView.getRootView() == null) {
            Log.e(TAG, "Unable to create a tip, root layout is null");
            return null;
        }

        // only one tip is allowed near an anchor view at the same time, thus
        // reuse tip if already exist
        if (mTipsMap.containsKey(rxPopupView.getAnchorView().getId())) {
            return mTipsMap.get(rxPopupView.getAnchorView().getId());
        }

        // init tip view parameters
        TextView tipView = createTipView(rxPopupView);

        // on RTL languages replace sides
        if (RxPopupViewTool.isRtl()) {
            switchToolTipSidePosition(rxPopupView);
        }

        // set tool tip background / shape
        RxPopupViewBackgroundConstructor.setBackground(tipView, rxPopupView);

        // add tip to root layout
        rxPopupView.getRootView().addView(tipView);

        // find where to position the tool tip
        Point p = RxPopupViewCoordinatesFinder.getCoordinates(tipView, rxPopupView);

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
        int anchorViewId = rxPopupView.getAnchorView().getId();
        tipView.setTag(anchorViewId);

        // enter tip to map by 'anchorView' id
        mTipsMap.put(anchorViewId, tipView);

        return tipView;

    }

    private void moveTipToCorrectPosition(TextView tipView, Point p) {
        RxCoordinates tipViewRxCoordinates = new RxCoordinates(tipView);
        int translationX = p.x - tipViewRxCoordinates.left;
        int translationY = p.y - tipViewRxCoordinates.top;
        if (!RxPopupViewTool.isRtl()) tipView.setTranslationX(translationX);
        else tipView.setTranslationX(-translationX);
        tipView.setTranslationY(translationY);
    }

    @NonNull
    private TextView createTipView(RxPopupView rxPopupView) {
        TextView tipView = new TextView(rxPopupView.getContext());
        tipView.setTextColor(rxPopupView.getTextColor());
        tipView.setTextSize(rxPopupView.getTextSize());
        tipView.setText(rxPopupView.getMessage() != null ? rxPopupView.getMessage() : rxPopupView.getSpannableMessage());
        tipView.setVisibility(View.INVISIBLE);
        tipView.setGravity(rxPopupView.getTextGravity());
        setTipViewElevation(tipView, rxPopupView);
        return tipView;
    }

    private void setTipViewElevation(TextView tipView, RxPopupView rxPopupView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (rxPopupView.getElevation() > 0) {
                ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                    @SuppressLint("NewApi")
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setEmpty();
                    }
                };
                tipView.setOutlineProvider(viewOutlineProvider);
                tipView.setElevation(rxPopupView.getElevation());
            }
        }
    }

    private void switchToolTipSidePosition(RxPopupView rxPopupView) {
        if (rxPopupView.positionedLeftTo()) {
            rxPopupView.setPosition(RxPopupView.POSITION_RIGHT_TO);
        } else if (rxPopupView.positionedRightTo()) {
            rxPopupView.setPosition(RxPopupView.POSITION_LEFT_TO);
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
        RxAnimationTool.popout(view, mAnimationDuration, new AnimatorListenerAdapter() {
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

    public interface TipListener {
        void onTipDismissed(View view, int anchorViewId, boolean byUser);
    }

}
