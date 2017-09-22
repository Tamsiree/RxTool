package com.vondear.rxtools.interfaces;

import com.google.zxing.Result;

/**
 * Created by Vondear on 2017/9/22.
 */

public interface OnRxScanerListener {
    void onSuccess(String type, Result result);

    void onFail(String type, String message);
}
