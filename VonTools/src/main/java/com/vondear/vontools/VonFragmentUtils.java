package com.vondear.vontools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by vonde on 2016/12/26.
 */

public class VonFragmentUtils {

    public static void initFragment(FragmentActivity fragmentActivity,Fragment fragment,int r_id_fragment) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r_id_fragment, fragment);
        fragmentTransaction.commit();
    }
}
