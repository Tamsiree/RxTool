package com.vondear.vontools.view;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by varsovski on 29-Oct-15.
 */
public class AutoFitEditTextUtil {


    public static void setNormalization(final Activity a, View rootView, final AutoFitEditText aText) {

        // if the view is not instance of AutoFitEditText
        // i.e. if the user taps outside of the box
        if (!(rootView instanceof AutoFitEditText)) {

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
